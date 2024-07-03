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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.braintribe.model.meta.data.constraint.DateClipping;
import com.braintribe.model.time.DateOffsetUnit;

public class DateClipper {
	
	private DateClipping dateClipping;
	private ZoneId zoneId;
	
	public DateClipper(DateClipping dateClipping, ZoneId zoneId) {
		this.dateClipping = dateClipping;
		this.zoneId = zoneId;
	}
	
	public ZoneId getZoneId() {
		return zoneId;
	}
	
	public Date clip(Date date) {
		if (dateClipping == null)
			return date;
		
		class Clipper {
			DateOffsetUnit upperBound = Optional.ofNullable(dateClipping.getUpper()).orElse(DateOffsetUnit.year);
			DateOffsetUnit lowerBound = Optional.ofNullable(dateClipping.getLower()).orElse(DateOffsetUnit.millisecond);
			
			int clip(DateOffsetUnit unit, int value, int clippedValue) {
				return (unit.ordinal() >= lowerBound.ordinal() && unit.ordinal() <= upperBound.ordinal())? value: clippedValue;
			}
		}
		
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zoneId);
		
		Clipper clipper = new Clipper();
		
		LocalDateTime clippedLocalDateTime = LocalDateTime.of(
			clipper.clip(DateOffsetUnit.year, localDateTime.getYear(), 0),
			clipper.clip(DateOffsetUnit.month, localDateTime.getMonthValue(), 1),
			clipper.clip(DateOffsetUnit.day, localDateTime.getDayOfMonth(), 1),
			clipper.clip(DateOffsetUnit.hour, localDateTime.getHour(), 0),
			clipper.clip(DateOffsetUnit.minute, localDateTime.getMinute(), 0),
			clipper.clip(DateOffsetUnit.second, localDateTime.getSecond(), 0),
			clipper.clip(DateOffsetUnit.millisecond, localDateTime.getNano() / 1_000_000, 0) * 1_000_000
		);

		return Date.from(clippedLocalDateTime.atZone(zoneId).toInstant());
	}
}
