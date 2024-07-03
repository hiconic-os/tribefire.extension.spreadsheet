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

import java.util.Iterator;

public class ParallelIterator<T> {
	private Iterator<T> iterator;
	
	public ParallelIterator(Iterator<T> iterator) {
		this.iterator = iterator;
	}
	
	public static <T> ParallelIterator<T> of(Iterator<T> it) {
		return new ParallelIterator<T>(it);
	}
	
	public synchronized T next() {
		if (iterator.hasNext())
			return iterator.next();
		else return null;
	}
}
