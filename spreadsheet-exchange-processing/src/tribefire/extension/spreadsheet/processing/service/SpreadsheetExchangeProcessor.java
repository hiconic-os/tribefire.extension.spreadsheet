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
package tribefire.extension.spreadsheet.processing.service;

import java.util.Optional;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.accessrequest.api.AbstractDispatchingAccessRequestProcessor;
import com.braintribe.model.processing.accessrequest.api.AccessRequestContext;
import com.braintribe.model.processing.accessrequest.api.DispatchConfiguration;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.FileTools;
import com.braintribe.utils.stream.api.StreamPipeFactory;
import com.braintribe.utils.stream.api.StreamPipes;

import tribefire.extension.scripting.api.ScriptingEngineResolver;
import tribefire.extension.spreadsheet.model.exchange.api.data.ImportReport;
import tribefire.extension.spreadsheet.model.exchange.api.request.ExportModelSpreadsheet;
import tribefire.extension.spreadsheet.model.exchange.api.request.ImportCsvSheet;
import tribefire.extension.spreadsheet.model.exchange.api.request.ImportExcelSheet;
import tribefire.extension.spreadsheet.model.exchange.api.request.ImportSpreadsheet;
import tribefire.extension.spreadsheet.model.exchange.api.request.ImportSpreadsheetRequest;
import tribefire.extension.spreadsheet.model.exchange.api.request.SpreadsheetExchangeRequest;
import tribefire.extension.spreadsheet.processing.importing.common.SpreadsheetImporter;
import tribefire.extension.spreadsheet.processing.importing.csv.CsvImporter;
import tribefire.extension.spreadsheet.processing.importing.excel.ExcelSheetImporter;

public class SpreadsheetExchangeProcessor extends AbstractDispatchingAccessRequestProcessor<SpreadsheetExchangeRequest, Object> {

	private StreamPipeFactory streamPipeFactory;
	private ScriptingEngineResolver scriptingEngineResolver;

	@Configurable
	public void setStreamPipeFactory(StreamPipeFactory streamPipeFactory) {
		this.streamPipeFactory = streamPipeFactory;
	}

	@Override
	protected void configureDispatching(DispatchConfiguration dispatching) {
		dispatching.register(ImportExcelSheet.T, this::importExcelSheet);
		dispatching.register(ImportCsvSheet.T, this::importCsvSheet);
		dispatching.register(ImportSpreadsheet.T, this::importSpreadsheet);
		dispatching.registerStatefulWithContext(ExportModelSpreadsheet.T, c -> new ModelSpreadsheetExportProcessor(streamPipeFactory));
	}

	private ImportReport importSpreadsheet(AccessRequestContext<ImportSpreadsheet> context) {
		ImportSpreadsheet originalRequest = context.getOriginalRequest();
		Resource sheet = originalRequest.getSheet();

		if (sheet == null)
			throw new IllegalArgumentException("ImportSpreadsheet.sheet must not be empty");

		String filename = sheet.getName();

		if (filename == null)
			throw new IllegalArgumentException("ImportSpreadsheet.sheet name must not be empty");

		final EntityType<? extends ImportSpreadsheetRequest> importRequestType;

		// detect specific import request type from filename extension
		String extension = FileTools.getExtension(filename).toLowerCase();

		switch (extension) {
			case "xlsx":
				importRequestType = ImportExcelSheet.T;
				break;
			case "csv":
				importRequestType = ImportCsvSheet.T;
				break;
			default:
				throw new IllegalArgumentException("Unsupported file extension for import type detection of filename: " + filename);
		}

		// create type specific import request and transfer common properties
		ImportSpreadsheetRequest importRequest = importRequestType.create();

		for (Property property : ImportSpreadsheetRequest.T.getProperties()) {
			property.setDirect(importRequest, property.getDirect(originalRequest));
		}

		// execute specific request
		return importRequest.eval(context).get();
	}
	private ImportReport importExcelSheet(AccessRequestContext<ImportExcelSheet> context) {
		return process(new ExcelSheetImporter(scriptingEngineResolver), context);
	}

	private ImportReport importCsvSheet(AccessRequestContext<ImportCsvSheet> context) {
		return process(new CsvImporter(scriptingEngineResolver), context);
	}

	private <T extends ImportSpreadsheetRequest> ImportReport process(SpreadsheetImporter<T> importer, AccessRequestContext<T> context) {
		importer.setStreamPipeFactory(Optional.ofNullable(streamPipeFactory).orElseGet(StreamPipes::simpleFactory));
		return importer.process(context);
	}

	public void setEngineResolver(ScriptingEngineResolver scriptingEngineResolver) {
		this.scriptingEngineResolver = scriptingEngineResolver;
	}

}
