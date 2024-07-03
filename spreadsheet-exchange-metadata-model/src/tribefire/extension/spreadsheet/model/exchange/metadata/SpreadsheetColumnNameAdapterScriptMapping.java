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
package tribefire.extension.spreadsheet.model.exchange.metadata;

import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;

import tribefire.extension.scripting.model.deployment.Script;

public interface SpreadsheetColumnNameAdapterScriptMapping extends EntityTypeMetaData {

	final EntityType<SpreadsheetColumnNameAdapterScriptMapping> T = EntityTypes.T(SpreadsheetColumnNameAdapterScriptMapping.class);

	String script = "script";

	@Mandatory
	Script getScript();
	void setScript(Script script);
}
