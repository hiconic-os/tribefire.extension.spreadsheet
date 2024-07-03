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


import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.data.EntityTypeMetaData;


public interface SpreadsheetEntityContextLinking extends EntityTypeMetaData {
	final EntityType<SpreadsheetEntityContextLinking> T = EntityTypes.T(SpreadsheetEntityContextLinking.class);
	
	static String type = "type";
	static String linkProperty = "linkProperty";
	static String hashProperty = "hashProperty";
	
	@Mandatory
	GmEntityType getType();
	void setType(GmEntityType type);
	
	@Mandatory
	GmProperty getLinkProperty();
	void setLinkProperty(GmProperty linkProperty);
	
	@Mandatory
	@Description("Property holding the hash that is being used for fast identity management. "+
			"It must come from the type that declares this metadata directly or indirectly (transitively).")
	GmProperty getHashProperty();
	void setHashProperty(GmProperty hashProperty);
}
