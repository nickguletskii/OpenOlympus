package org.ng200.openolympus.util;

import java.time.Duration;

import org.jooq.Converter;

public class DurationConverter implements Converter<Long, Duration> {

	@Override
	public Duration from(Long databaseObject) {
		return Duration.ofMillis(databaseObject);
	}

	@Override
	public Long to(Duration userObject) {
		return userObject.toMillis();
	}

	@Override
	public Class<Long> fromType() {
		return Long.class;
	}

	@Override
	public Class<Duration> toType() {
		return Duration.class;
	}

}