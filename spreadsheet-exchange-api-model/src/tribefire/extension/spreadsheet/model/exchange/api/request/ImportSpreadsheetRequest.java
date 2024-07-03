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

import java.util.Map;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.service.api.ServiceRequest;

import tribefire.extension.spreadsheet.model.exchange.api.data.ImportReport;
import tribefire.extension.spreadsheet.model.exchange.api.data.PushAddress;

@Abstract
public interface ImportSpreadsheetRequest extends SpreadsheetExchangeRequest {
	EntityType<ImportSpreadsheetRequest> T = EntityTypes.T(ImportSpreadsheetRequest.class);

	String sheet = "sheet";
	String useCase = "useCase";
	String targetType = "targetType";
	String lenient = "lenient";
	String enrichments = "enrichments";
	String statusMonitor = "statusMonitor";

	Resource getSheet();
	void setSheet(Resource sheet);

	@Description("Sets the explicit usecase to be used for metadata resolution.")
	String getUseCase();
	void setUseCase(String useCase);

	String getTargetType();
	void setTargetType(String targetType);

	boolean getLenient();
	void setLenient(boolean lenient);

	boolean getTransient();
	void setTransient(boolean lenient);

	Integer getStartRow();
	void setStartRow(Integer startRow);

	Integer getMaxRows();
	void setMaxRows(Integer maxRows);

	Object getBundleId();
	void setBundleId(Object id);

	Map<String, Object> getEnrichments();
	void setEnrichments(Map<String, Object> enrichments);

	PushAddress getStatusMonitor();
	void setStatusMonitor(PushAddress statusMonitor);

	// String updateExisting = "updateExisting";
	// boolean getUpdateExisting();
	// void setUpdateExisting(boolean updateExisting);

	@Override
	EvalContext<? extends ImportReport> eval(Evaluator<ServiceRequest> evaluator);
}
