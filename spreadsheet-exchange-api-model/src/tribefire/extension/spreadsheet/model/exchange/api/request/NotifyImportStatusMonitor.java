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
package tribefire.extension.spreadsheet.model.exchange.api.request;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.DispatchableRequest;

public interface NotifyImportStatusMonitor extends DispatchableRequest {
	EntityType<NotifyImportStatusMonitor> T = EntityTypes.T(NotifyImportStatusMonitor.class);

	String totalRowCount = "totalRowCount";
	String rowCount = "rowCount";
	String entityCount = "entityCount";
	
	public int getTotalRowCount();
	public void setTotalRowCount(int totalRowCount);
	
	public int getRowCount();
	public void setRowCount(int rowCount);
	
	public int getEntityCount();
	public void setEntityCount(int entityCount);
}
