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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.assertj.core.util.Arrays;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CsvNullLab {
	public static void main(String[] args) {
		try {
			InputStream in = new FileInputStream("res/test.csv");
			
			CSVParser csvParser = new CSVParserBuilder().withSeparator(',').withStrictQuotes(true).withQuoteChar('"').build();
			CSVReader reader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(in, "UTF-8"))) //
					.withCSVParser(csvParser).build();
			
			String line[];
			while ((line = reader.readNext()) != null) {
				System.out.println(Arrays.asList(line));
			}
			
		} catch (Exception e) {
			throw new RuntimeException("", e);
		}

	}
}
