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
package tribefire.extension.spreadsheet.exchange.wire;

import static com.braintribe.wire.api.util.Lists.list;

import java.util.List;

import com.braintribe.wire.api.module.WireModule;

import tribefire.extension.scripting.module.wire.ScriptingModuleWireModule;
import tribefire.extension.spreadsheet.exchange.wire.space.SpreadsheetExchangeModuleSpace;
import tribefire.module.wire.contract.StandardTribefireModuleWireModule;
import tribefire.module.wire.contract.TribefireModuleContract;

public enum SpreadsheetExchangeModuleWireModule implements StandardTribefireModuleWireModule {

	INSTANCE;

	@Override
	public Class<? extends TribefireModuleContract> moduleSpaceClass() {
		return SpreadsheetExchangeModuleSpace.class;
	}

	@Override
	public List<WireModule> dependencies() {
		//@formatter:off
		return list(
				ScriptingModuleWireModule.INSTANCE);
		//@formatter:on
	}

}
