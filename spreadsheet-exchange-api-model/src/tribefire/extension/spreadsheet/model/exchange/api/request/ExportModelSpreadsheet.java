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
package tribefire.extension.spreadsheet.model.exchange.api.request;

import java.util.List;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.generic.typecondition.TypeCondition;
import com.braintribe.model.service.api.ServiceRequest;

import tribefire.extension.spreadsheet.model.exchange.api.data.ExportModelColumn;
import tribefire.extension.spreadsheet.model.exchange.api.data.ModelSpreadsheet;

public interface ExportModelSpreadsheet extends SpreadsheetExchangeRequest {
	EntityType<ExportModelSpreadsheet> T = EntityTypes.T(ExportModelSpreadsheet.class);
	
	String delimiter = "delimiter";
	String select = "select";
	String propertyTypeFilter = "propertyTypeFilter";
	String typeFilter = "typeFilter";
	
	@Initializer("';'")
	String getDelimiter();
	void setDelimiter(String delimiter);
	
	List<ExportModelColumn> getSelect();
	void setSelect(List<ExportModelColumn> select);
	
	TypeCondition getPropertyTypeFilter();
	void setPropertyTypeFilter(TypeCondition propertyTypeFilter);
	
	TypeCondition getTypeFilter();
	void setTypeFilter(TypeCondition typeFilter);
	
	@Override
	EvalContext<? extends ModelSpreadsheet> eval(Evaluator<ServiceRequest> evaluator);
}
