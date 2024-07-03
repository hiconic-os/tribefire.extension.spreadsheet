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
package tribefire.extension.spreadsheet.processing.importing.common;

import java.util.Map;
import java.util.function.Function;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;

import tribefire.extension.spreadsheet.model.exchange.api.request.ImportSpreadsheetRequest;

public abstract class SheetEntityStreamingContext<T extends ImportSpreadsheetRequest> {
	public abstract EntityType<GenericEntity> getImportTargetType();

	public abstract T getSpreadsheetImport();

	public abstract Function<String, String> getColumnNameAdapter();
	
	public abstract Map<String, Property> getProperties();

	public abstract ModelMdResolver getCmdrContextBuilder();

	public abstract Maybe<Object> convert(Object value, int rowNum, int cell, String columnName, Property property);
	
	public abstract void notifyRowCount(int rowCount);
	
	public abstract void notifyTotalRowCount(int rowCount);
}
