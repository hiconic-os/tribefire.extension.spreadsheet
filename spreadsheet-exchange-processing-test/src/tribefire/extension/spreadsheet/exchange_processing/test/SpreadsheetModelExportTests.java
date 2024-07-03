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
package tribefire.extension.spreadsheet.exchange_processing.test;

import java.io.FileInputStream;
import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.braintribe.model.generic.typecondition.basic.IsTypeKind;
import com.braintribe.model.generic.typecondition.basic.TypeKind;
import com.braintribe.utils.lcd.CollectionTools;

import tribefire.extension.spreadsheet.model.exchange.api.data.ExportModelColumn;
import tribefire.extension.spreadsheet.model.exchange.api.data.ModelSpreadsheet;
import tribefire.extension.spreadsheet.model.exchange.api.request.ExportModelSpreadsheet;

public class SpreadsheetModelExportTests extends SpreadsheetExchangeProcessingTestBase implements TestConstants {
	
	@Test
	public void textStringPropertyExportWithSelect() throws IOException {
		IsTypeKind isStringType = IsTypeKind.T.create();
		isStringType.setKind(TypeKind.stringType);
		
		ExportModelSpreadsheet exportModelSpreadsheet = ExportModelSpreadsheet.T.create();
		exportModelSpreadsheet.setDomainId(ACCESS_IMPORT);
		exportModelSpreadsheet.setDelimiter(";");
		exportModelSpreadsheet.setSelect(CollectionTools.getList(
				ExportModelColumn.model, 
				ExportModelColumn.packageName, 
				ExportModelColumn.simpleTypeName, 
				ExportModelColumn.propertyName));
		exportModelSpreadsheet.setPropertyTypeFilter(isStringType);
		
		ModelSpreadsheet modelSpreadsheet = exportModelSpreadsheet.eval(evaluator).get();

		Assertions.assertThat(modelSpreadsheet.getSheet().openStream()).hasSameContentAs(new FileInputStream("res/export/model-export-access.import-filtered-selected.csv"));
	}
}
