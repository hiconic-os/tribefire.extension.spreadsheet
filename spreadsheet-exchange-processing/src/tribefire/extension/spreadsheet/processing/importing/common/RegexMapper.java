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

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMapper implements Function<String, String> {
	private Pattern pattern;
	private String template;
	
	public RegexMapper(String patternString, String template) {
		super();
		this.pattern = Pattern.compile(patternString);
		this.template = template;
	}
	
	@Override
	public String apply(String s) {
		Matcher matcher = pattern.matcher(s);
		
		if (matcher.find()) {
			return matcher.replaceFirst(template);
		}
		else {
			return s;
		}
	}
}
