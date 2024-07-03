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

import java.util.Comparator;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.SimpleType;

public class DistinctionComparator implements Comparator<GenericEntity> {
	private GenericModelTypeReflection typeReflection = GMF.getTypeReflection();
	@Override
	public int compare(GenericEntity o1, GenericEntity o2) {
		EntityType<GenericEntity> et1 = typeReflection.getType(o1);
		EntityType<GenericEntity> et2 = typeReflection.getType(o2);
		
		if (et1 != et2) {
			return et1.getTypeSignature().compareTo(et2.getTypeSignature());
		}
		else {
			for (Property property: et1.getProperties()) {
				if (property.getType() instanceof SimpleType) {
					Object v1 = property.get(o1);
					Object v2 = property.get(o2);
					
					int retValue;
					
					if (v1 == v2) 
						retValue = 0;
					else if (v1 == null)
						retValue = -1;
					else if (v2 == null)
						retValue = 1;
					else
						retValue = compareObjects(v1, v2);
					
					if (retValue != 0)
						return retValue;
				}
			}
			
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	private int compareObjects(Object v1, Object v2) {
		return ((Comparable<Object>)v1).compareTo(v2);
	}
}
