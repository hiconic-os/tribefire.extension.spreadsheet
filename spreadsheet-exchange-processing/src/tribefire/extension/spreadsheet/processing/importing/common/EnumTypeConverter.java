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
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.model.generic.reflection.EnumType;

import tribefire.extension.spreadsheet.model.reason.ConversionFailed;

/**
 * @author peter.gazdik
 */
public class EnumTypeConverter implements TypeConverter<String, Enum<?>> {
	private final EnumType<?> enumType;
	
	public EnumTypeConverter(EnumType<?> enumType) {
		super();
		this.enumType = enumType;
	}

	@Override
	public Maybe<Enum<?>> convert(String source) {
		Enum<?> enumValue = enumType.findEnumValue(source);
		
		if (enumValue == null)
			return Reasons.build(ConversionFailed.T) //
					.text("EnumType [" + enumType.getTypeSignature() + "] has no enum constant [" + source + "]") //
					.toMaybe();

		return Maybe.complete(enumValue);
	}

	@Override
	public Class<String> getFromClass() {
		return String.class;
	}

	@Override
	public Class<Enum<?>> getToClass() {
		return (Class<Enum<?>>) enumType.getJavaType();
	}

}
