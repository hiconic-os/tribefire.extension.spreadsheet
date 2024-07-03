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
package tribefire.extension.spreadsheet.exchange.wire.space;

import com.braintribe.cartridge.common.processing.accessrequest.InternalizingAccessRequestProcessor;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.scripting.module.wire.contract.ScriptingContract;
import tribefire.extension.spreadsheet.model.exchange.api.request.SpreadsheetExchangeRequest;
import tribefire.extension.spreadsheet.processing.service.SpreadsheetExchangeProcessor;
import tribefire.module.wire.contract.ModuleResourcesContract;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

@Managed
public class SpreadsheetExchangeModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	@Import
	private ModuleResourcesContract moduleResources;

	@Import
	private ScriptingContract scripting;

	@Override
	public void bindHardwired() {
		// Bind hardwired deployables here.
		tfPlatform.hardwiredDeployables().bindOnNewServiceDomain("domain.spreadsheet.exchange", "Spreadsheet Service Domain") //
				.model("tribefire.extension.spreadsheet:spreadsheet-exchange-api-model") //
				.serviceProcessor( //
						"processor.spreadsheet.exchange", //
						"Spreadsheet Exchange Processor", //
						SpreadsheetExchangeRequest.T, //
						this::spreadsheetExchangeServiceProcessor) //
				.please();
	}

	@Managed
	private InternalizingAccessRequestProcessor<SpreadsheetExchangeRequest, Object> spreadsheetExchangeServiceProcessor() {
		return new InternalizingAccessRequestProcessor<SpreadsheetExchangeRequest, Object>(spreadsheetExchangeProcessor(),
				tfPlatform.requestUserRelated().sessionFactory(), tfPlatform.systemUserRelated().sessionFactory());
	}

	@Managed
	private SpreadsheetExchangeProcessor spreadsheetExchangeProcessor() {
		SpreadsheetExchangeProcessor bean = new SpreadsheetExchangeProcessor();
		bean.setEngineResolver(scripting.scriptingEngineResolver());
		bean.setStreamPipeFactory(tfPlatform.resourceProcessing().streamPipeFactory());
		return bean;
	}

}
