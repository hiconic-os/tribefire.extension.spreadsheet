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

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.function.Function;

import com.braintribe.model.generic.reflection.SimpleType;

public interface Conversions {
	static Boolean stringToBoolean(String s) {
		switch (s.toLowerCase()) {
		case "true":
		case "t":
		case "yes":
		case "y":
		case "on":
		case "active":
		case "enabled":
		case "1":
			return true;
		default:
			return false;
		}
	}
	
	static Function<String, Number> stringToNumberFunction(String decimalSeparator, String digitGroupingSymbol, SimpleType targetType) {
		return s -> {
			StringBuilder builder = new StringBuilder(s.length());
			StringTokenizer tokenizer = new StringTokenizer(s, decimalSeparator + digitGroupingSymbol, true);
			
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				
				if (token.equals(digitGroupingSymbol)) {
					// noop
				}
				else if (token.equals(decimalSeparator)) {
					builder.append('.');
				}
				else {
					builder.append(token);
				}
			}
			
			String normalized = builder.toString();
			
			switch (targetType.getTypeCode()) {
				case floatType:
					return Float.parseFloat(normalized);
				case doubleType:
					return Double.parseDouble(normalized);
				case integerType: 
					return stringToIntegerNumber(normalized).intValue();
				case longType:
					return stringToIntegerNumber(normalized).longValue();
				case decimalType:
					return new BigDecimal(s);
				default:
					throw new IllegalStateException("Target type [" + targetType + "] is not a number type");
			}
			
		};
	}
	
	static Integer stringToInteger(String s) {
		return stringToIntegerNumber(s).intValue();
	}
	
	static Long stringToLong(String s) {
		return stringToIntegerNumber(s).longValue();
	}
	
	static Double stringToIntegerNumber(String s) {
		double number = Double.parseDouble(s);
		
		if (Math.floor(number) == number)
			return number;
		
		throw new IllegalStateException("String [" + s + "] is not an integer.");
	}
}
