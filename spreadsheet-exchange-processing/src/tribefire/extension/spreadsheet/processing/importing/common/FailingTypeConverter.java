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

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;

public class FailingTypeConverter<S, T> implements TypeConverter<S, T> {
	private final Reason reason;
	private final Class<S> fromClass;
	private final Class<T> toClass;
	
	public FailingTypeConverter(Class<S> fromClass, Class<T> toClass, Reason reason) {
		super();
		this.fromClass = fromClass;
		this.toClass = toClass;
		this.reason = reason;
	}

	@Override
	public Maybe<T> convert(S source) {
		return reason.asMaybe();
	}
	
	@Override
	public Class<S> getFromClass() {
		return fromClass;
	}
	
	@Override
	public Class<T> getToClass() {
		return toClass;
	}
}
