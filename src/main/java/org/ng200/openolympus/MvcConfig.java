/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Import({ ThymeleafConfig.class })
@PropertySource("classpath:spring.properties")
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		final PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
		final Resource[] resources = new ClassPathResource[] { new ClassPathResource(
				"spring.properties") };
		propertySources.setLocations(resources);
		propertySources.setIgnoreUnresolvablePlaceholders(true);
		return propertySources;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"classpath:resources/");
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/errors/404").setViewName("errors/404");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");
		return source;
	}
}