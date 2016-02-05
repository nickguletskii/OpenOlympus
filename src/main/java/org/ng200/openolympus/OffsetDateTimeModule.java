/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ng200.openolympus;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class OffsetDateTimeModule extends SimpleModule {
	public static class OffsetDateTimeMixins {
		@JsonCreator
		public static OffsetDateTime fromString(String val) {
			return OffsetDateTime.parse(val,
					DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		}

	}

	/**
	 *
	 */
	private static final long serialVersionUID = 5894603418971898080L;

	public OffsetDateTimeModule() {
		this.addSerializer(new JsonSerializer<OffsetDateTime>() {
			@Override
			public Class<OffsetDateTime> handledType() {
				return OffsetDateTime.class;
			}

			@Override
			public void serialize(OffsetDateTime offsetDateTime,
					JsonGenerator gen,
					SerializerProvider serializerProvider) throws IOException,
							JsonProcessingException {
				gen.writeString(offsetDateTime.format(
						DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			}
		});
		this.addDeserializer(OffsetDateTime.class,
				new JsonDeserializer<OffsetDateTime>() {
					@Override
					public OffsetDateTime deserialize(JsonParser jp,
							DeserializationContext ctxt) throws IOException,
									JsonProcessingException {
						return OffsetDateTime.parse(jp.getValueAsString(),
								DateTimeFormatter.ISO_OFFSET_DATE_TIME);
					}

					@Override
					public Class<OffsetDateTime> handledType() {
						return OffsetDateTime.class;
					}
				});
		this.setMixInAnnotation(OffsetDateTime.class,
				OffsetDateTimeMixins.class);
	}

}