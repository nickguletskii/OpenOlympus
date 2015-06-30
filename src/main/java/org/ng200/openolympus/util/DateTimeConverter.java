package org.ng200.openolympus.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.Converter;

public class DateTimeConverter implements Converter<Timestamp, LocalDateTime> {

	@Override
	public LocalDateTime from(Timestamp databaseObject) {
		return databaseObject.toLocalDateTime();
	}

	@Override
	public Timestamp to(LocalDateTime userObject) {
		return Timestamp.valueOf(userObject);
	}

	@Override
	public Class<Timestamp> fromType() {
		return Timestamp.class;
	}

	@Override
	public Class<LocalDateTime> toType() {
		return LocalDateTime.class;
	}

}