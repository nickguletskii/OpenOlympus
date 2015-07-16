package org.ng200.openolympus.config;

import org.ng200.openolympus.DurationJacksonModule;
import org.ng200.openolympus.SecurityClearanceJacksonFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

@SuppressWarnings("deprecation")
@Component
@Configuration
public class JacksonConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
		return Jackson2ObjectMapperBuilder
				.json()
				.failOnUnknownProperties(false)
				.modules(new DurationJacksonModule())
				.filters(new FilterProvider() {

					@Override
					public PropertyFilter findPropertyFilter(Object filterId,
							Object valueToFilter) {
						return new SecurityClearanceJacksonFilter();
					}

					@Override
					public BeanPropertyFilter findFilter(Object filterId) {
						return new SecurityClearanceJacksonFilter();
					}
				})
				.annotationIntrospector(new JacksonAnnotationIntrospector());
	}

	@Bean
	@Primary
	public ObjectMapper jacksonObjectMapper() {
		return jacksonObjectMapperBuilder().build();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(jacksonObjectMapper());
	}
}
