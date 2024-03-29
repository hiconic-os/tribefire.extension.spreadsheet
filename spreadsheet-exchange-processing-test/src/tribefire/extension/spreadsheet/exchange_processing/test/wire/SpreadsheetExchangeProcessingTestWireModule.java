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
package tribefire.extension.spreadsheet.exchange_processing.test.wire;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.List;

import tribefire.extension.spreadsheet.exchange_processing.test.wire.contract.SpreadsheetExchangeProcessingTestContract;
import com.braintribe.gm.service.access.wire.common.CommonAccessProcessingWireModule;
import com.braintribe.gm.service.wire.common.CommonServiceProcessingWireModule;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;

public enum SpreadsheetExchangeProcessingTestWireModule implements WireTerminalModule<SpreadsheetExchangeProcessingTestContract> {
	INSTANCE;
	
	@Override
	public List<WireModule> dependencies() {
		return asList(CommonServiceProcessingWireModule.INSTANCE, CommonAccessProcessingWireModule.INSTANCE);
	}
	
}
