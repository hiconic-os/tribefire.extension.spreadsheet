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
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Format;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.ExcelNumberFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingCell;

public class SheetLab {

	static Field rawContentsField;
	static Method createDateFormatMethod;

	static {
		// rawContentsField= createDateFormat(String pFormatStr, double cellValue)
		try {

			rawContentsField = StreamingCell.class.getDeclaredField("rawContents");
			rawContentsField.setAccessible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {

			Field field = StreamingCell.class.getDeclaredField("rawContents");
			field.setAccessible(true);

			// Locale.setDefault(Locale.ENGLISH);
			// System.out.println("EN");
			// outputSheet(field);
			//
			// Locale.setDefault(Locale.GERMAN);
			// System.out.println("DE");
			// outputSheet(field);

			outputSheet();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void outputSheet() throws Exception {
		Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(65536).open(new FileInputStream("res/date-test2.xlsx"));

		for (Sheet sheet : workbook) {
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				if (row.getRowNum() == 0)
					continue;

				if (row.getRowNum() > 5)
					break;

				for (Cell cell : row) {
					System.out.print("row: " + row.getRowNum() + ", cell: " + cell.getColumnIndex() + ", ");
					System.out.print("cell type: " + cell.getCellType() + ", ");
					System.out.print("format: " + cell.getCellStyle().getDataFormat() + ", ");
					System.out.print("raw value: " + rawContentsField.get(cell) + ", ");
					System.out.println("string value: " + cell.getStringCellValue());

					CellStyle style = cell.getCellStyle();
					ExcelNumberFormat excelNumberFormat = ExcelNumberFormat.from(style);

					if (DateUtil.isADateFormat(excelNumberFormat)) {
						StreamingCell streamingCell = new StreamingCell(cell.getColumnIndex(), cell.getRowIndex(), false);
						streamingCell.setRawContents("0");
						streamingCell.setCellStyle(cell.getCellStyle());
						Format format = new DataFormatter().createFormat(streamingCell);

						Object parseObject = format.parseObject(cell.getStringCellValue());
						System.out.println(parseObject);
					}

				}
			}
		}
	}
}
