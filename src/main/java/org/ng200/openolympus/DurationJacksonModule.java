/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DurationJacksonModule extends SimpleModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5894603418971898080L;

	private static final Logger logger = LoggerFactory
			.getLogger(DurationJacksonModule.class);

	public DurationJacksonModule() {
		addSerializer(new JsonSerializer<Duration>() {
			@Override
			public void serialize(Duration duration, JsonGenerator gen,
					SerializerProvider serializerProvider) throws IOException,
					JsonProcessingException {
				gen.writeNumber(duration.toMillis());
			}

			@Override
			public Class<Duration> handledType() {
				return Duration.class;
			}
		});
		addDeserializer(Duration.class, new JsonDeserializer<Duration>() {
			@Override
			public Duration deserialize(JsonParser jp,
					DeserializationContext ctxt) throws IOException,
					JsonProcessingException {
				long l = jp.getValueAsLong();
				return Duration.ofMillis(l);
			}

			@Override
			public Class<Duration> handledType() {
				return Duration.class;
			}
		});
	}

	@Override
	public void setupModule(SetupContext context) {
		super.setupModule(context);
		context.setMixInAnnotations(Duration.class, DurationMixins.class);
	}

	public static class DurationMixins {
		@JsonCreator
		public static Duration ofMillis(long val) {
			return Duration.ofMillis(val);
		}

		@JsonCreator
		public static Duration ofMillis(String val) {
			return Duration.ofMillis(Long.valueOf(val));
		}
	}
}