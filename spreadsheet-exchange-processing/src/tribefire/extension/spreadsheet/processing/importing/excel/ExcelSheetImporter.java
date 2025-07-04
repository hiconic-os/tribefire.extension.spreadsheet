// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.extension.spreadsheet.processing.importing.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.braintribe.exception.Exceptions;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InternalError;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.utils.IOTools;
import com.monitorjbl.xlsx.StreamingReader;

import tribefire.extension.scripting.api.ScriptingEngineResolver;
import tribefire.extension.spreadsheet.model.exchange.api.request.ImportExcelSheet;
import tribefire.extension.spreadsheet.model.reason.ConversionFailed;
import tribefire.extension.spreadsheet.model.reason.IncompleteEntity;
import tribefire.extension.spreadsheet.processing.importing.common.SheetEntityStreamingContext;
import tribefire.extension.spreadsheet.processing.importing.common.SpreadsheetImporter;

public class ExcelSheetImporter extends SpreadsheetImporter<ImportExcelSheet> {

	public ExcelSheetImporter(ScriptingEngineResolver scriptingEngineResolver) {
		super(scriptingEngineResolver);
	}

	private class ExcelEntityStreamer implements EntityStreamer {

		private Workbook workbook;
		private Iterator<Row> iterator;
		private final SheetEntityStreamingContext<ImportExcelSheet> context;
		private List<ColumnInfo> columnInfos;
		private InputStream in;
		private int rowCount = 0;
		private final Integer startRow;
		private final int maxRows;

		private ExcelEntityStreamer(SheetEntityStreamingContext<ImportExcelSheet> context) {
			this.context = context;

			this.startRow = context.getSpreadsheetImport().getStartRow();
			this.maxRows = Optional.ofNullable(context.getSpreadsheetImport().getMaxRows()).orElse(-1);
		}

		private Iterator<Row> getIterator() {
			if (iterator == null)
				open();

			return iterator;
		}

		private void open() {
			ImportExcelSheet spreadsheetImport = context.getSpreadsheetImport();
			in = spreadsheetImport.getSheet().openStream();

			workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(65536).open(in);

			Function<String, String> columnNameAdapter = context.getColumnNameAdapter();
			Map<String, Property> properties = context.getProperties();

			Row firstRow = null;
			String sheetName = spreadsheetImport.getSheetName();

			Predicate<String> sheetFilter = sheetName != null ? n -> sheetName.equals(n) : n -> true;

			for (Sheet worksheet : workbook) {

				if (!sheetFilter.test(worksheet.getSheetName()))
					continue;

				Iterator<Row> rowIterator = worksheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					if (firstRow == null) {
						firstRow = row;
					}

					Set<String> curNames = new HashSet<String>(properties.keySet());

					Iterator<Cell> cellIterator = row.cellIterator();

					// Map<Integer, Property> indices = null;
					List<ColumnInfo> infos = null;
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();

						String originalColumnName = cell.getStringCellValue();

						if (!originalColumnName.isEmpty()) {
							String columnName = columnNameAdapter.apply(originalColumnName);
							if (curNames.remove(columnName)) {
								if (infos == null)
									infos = new ArrayList<>();

								ColumnInfo info = new ColumnInfo();
								info.cellIndex = cell.getColumnIndex();
								info.property = properties.get(columnName);
								info.columnName = originalColumnName;

								infos.add(info);
							}
						}
					}

					if (infos != null) {// if (curNames.isEmpty()) {
						this.columnInfos = infos;
						this.iterator = rowIterator;

						// wind to required start row
						if (startRow != null) {
							int c = startRow;
							for (int i = 0; i < c; i++) {
								if (this.iterator.hasNext()) {
									this.iterator.next();
								} else {
									break;
								}
							}
						}

						context.notifyTotalRowCount(worksheet.getLastRowNum() - firstRow.getRowNum() + 1);
						return;
					}
				}
			}

			this.columnInfos = Collections.emptyList();
			this.iterator = Collections.emptyIterator();
		}

		public synchronized Row nextRow() {
			if (rowCount == maxRows)
				return null;

			try {
				Iterator<Row> it = getIterator();
				if (it.hasNext()) {
					context.notifyRowCount(++rowCount);
					return it.next();
				}
			} catch (Exception e) {
				iterator = Collections.emptyIterator();
				throw Exceptions.unchecked(e);
			}

			return null;
		}

		private Reason extendReason(Row row, Reason umbrellaReason, Reason reason) {
			if (umbrellaReason == null) {
				umbrellaReason = Reasons.build(IncompleteEntity.T)
						.text("Entity in row [" + (row.getRowNum() + 1) + "] could not be successfully imported").toReason();
			}

			umbrellaReason.getReasons().add(reason);

			return umbrellaReason;
		}

		@Override
		public ParsedEntity next(Supplier<GenericEntity> factory) {
			Row row = null;
			while ((row = nextRow()) != null) {
				GenericEntity entity = null;
				Reason reason = null;

				for (ColumnInfo info : columnInfos) {
					Property property = info.property;
					Cell cell = row.getCell(info.cellIndex);
					if (cell == null)
						continue;

					Object value = null;

					try {
						value = CellValues.getCellValue(cell, property.getType());
					} catch (Exception e) {
						reason = extendReason(row, reason, Reasons.build(ConversionFailed.T) //
								.text("Could not convert cell value from column [" + info.columnName + "] for target property [" + property.getName()
										+ "]") //
								.cause(InternalError.from(e)).toReason());

						continue;
					}

					if (value == null)
						continue;

					try {
						Maybe<Object> convertedPotential = context.convert(value, row.getRowNum(), info.cellIndex, info.columnName, property);

						if (entity == null)
							entity = factory.get();

						if (convertedPotential.isSatisfied()) {
							property.set(entity, convertedPotential.get());
						} else {
							reason = extendReason(row, reason, convertedPotential.whyUnsatisfied());
						}

					} catch (Exception e) {
						throw new IllegalStateException("Could not convert value [" + value + "]  from column [" + info.columnName + "] in row ["
								+ (row.getRowNum() + 1) + "] for " + property.toString(), e);
					}
				}

				if (entity != null || reason != null) {
					return new ParsedEntity(row.getRowNum(), entity, reason);
				}
			}

			return null;
		}

		@Override
		public void close() {
			if (workbook != null)
				IOTools.closeCloseableUnchecked(workbook);

			if (in != null)
				IOTools.closeCloseableUnchecked(in);
		}

	}

	@Override
	protected EntityStreamer streamEntitiesFromSpreadsheet(SheetEntityStreamingContext<ImportExcelSheet> streamContext) throws Exception {
		return new ExcelEntityStreamer(streamContext);
	}

	@Override
	protected String getImportFormatUseCase() {
		return "excel-import";
	}
}
