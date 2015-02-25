package org.ng200.openolympus;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

@Component
public class OlympusConverters {

	@Converter(autoApply = true)
	public static class DurationConverter implements
			AttributeConverter<Duration, Long> {

		@Override
		public Long convertToDatabaseColumn(Duration attribute) {
			if (attribute == null)
				return null;
			return attribute.getSeconds();
		}

		@Override
		public Duration convertToEntityAttribute(Long dbData) {
			if (dbData == null)
				return null;
			return Duration.ofMillis(dbData);
		}
	}

}
