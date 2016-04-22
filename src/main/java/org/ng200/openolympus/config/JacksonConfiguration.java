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
package org.ng200.openolympus.config;

import org.ng200.openolympus.DurationJacksonModule;
import org.ng200.openolympus.OffsetDateTimeModule;
import org.ng200.openolympus.SecurityClearanceJacksonFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonConfiguration {
	@Bean
	@Primary
	public ObjectMapper jacksonObjectMapper() {
		return this.objectMapperBuilder().build();
	}

	@JsonFilter("default")
	private static class DefaultFilterMixin {

	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		return Jackson2ObjectMapperBuilder
				.json()
				.failOnUnknownProperties(false)
				.modules(new OffsetDateTimeModule(),
						new DurationJacksonModule())
				.filters(new SimpleFilterProvider().addFilter(
						"default",
						this.securityClearanceJacksonFilter()))
				.mixIn(Object.class, DefaultFilterMixin.class)
				.annotationIntrospector(new JacksonAnnotationIntrospector());
	}

	@Bean
	@Profile("web")
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(
				this.jacksonObjectMapper());
	}

	@Bean
	public SecurityClearanceJacksonFilter securityClearanceJacksonFilter() {
		return new SecurityClearanceJacksonFilter();
	}
}
