// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.extension.spreadsheet.model.exchange.metadata;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;

import tribefire.extension.scripting.model.deployment.Script;

@Description("Maps a script that filters entities read from the spreadsheet. "
		+ "A variable named 'entity' containing the entity to be filtered will be passed to the script to be filtered.")
public interface SpreadsheetEntityFilterScript extends EntityTypeMetaData {
	final EntityType<SpreadsheetEntityFilterScript> T = EntityTypes.T(SpreadsheetEntityFilterScript.class);

	static String script = "script";

	@Mandatory
	Script getScript();
	void setScript(Script script);
}
