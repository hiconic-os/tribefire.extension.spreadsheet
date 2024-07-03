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
package tribefire.extension.spreadsheet.processing.importing.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import com.braintribe.model.generic.reflection.EssentialTypes;
import com.braintribe.model.generic.reflection.GenericModelType;

public interface CellValues {
	static Object getCellValue(Cell cell, GenericModelType targetType) {
		if (targetType == EssentialTypes.TYPE_STRING && cell.getCellStyle().getDataFormat() == 0) {
			return getGeneralCellValue(cell);
		}
		
		switch (cell.getCellType()) {
		case BLANK: return null;
		case ERROR: return cell.getErrorCellValue();
		case STRING: return cell.getStringCellValue();
		case BOOLEAN: return cell.getBooleanCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				return cell.getDateCellValue();
			else
				return cell.getNumericCellValue();
		default:
			throw new UnsupportedOperationException("unsupported cell type " + cell.getCellType());
		}
	}
	
	static Object getGeneralCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case BLANK: return null;
		case ERROR: return cell.getErrorCellValue();
		case STRING: return cell.getStringCellValue();
		case BOOLEAN: 
			throw new IllegalStateException("'General' formatted BOOLEAN value [" + cell.getBooleanCellValue() + "] cannot be safely converted to string due to Excel locale sensitivity");
		case NUMERIC:
			double number = cell.getNumericCellValue();
			long integerPart = (long)number;
			double fraction = number - integerPart;
			if (fraction != 0)
				throw new IllegalStateException("'General' formatted NUMBER value [" +  number + "] that is not an integer cannot be safely converted to string due to Excel locale sensitivity");
			return Long.toString(integerPart);
		default:
			throw new UnsupportedOperationException("unsupported cell type " + cell.getCellType());
		}
	}
}
