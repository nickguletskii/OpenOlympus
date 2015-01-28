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

import java.util.List;

import org.ng200.openolympus.customPageSupport.CustomPage;
import org.ng200.openolympus.customPageSupport.CustomPageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Configuration
@EnableWebMvc
@Import({
		ThymeleafConfig.class,
		CustomPageConfig.class
})
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		final PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
		final Resource[] resources = new ClassPathResource[] {
			new ClassPathResource("openolympus.properties")
		};
		propertySources.setLocations(resources);
		propertySources.setIgnoreUnresolvablePlaceholders(true);
		return propertySources;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(MvcConfig.class);

	@javax.annotation.Resource(name = "customPages")
	List<CustomPage> customPages;

	@Value("${customResourceLocation}")
	String customResourceLocation;

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		MvcConfig.logger.info("Custom resource location: {}",
				this.customResourceLocation);

		if (!StringUtils.isEmpty(this.customResourceLocation)) {
			registry.addResourceHandler("/customResources/**")
					.addResourceLocations(this.customResourceLocation);
		}

		registry.addResourceHandler("/resources/**").addResourceLocations(
				"classpath:resources/");

		registry.addResourceHandler("/favicon.png").addResourceLocations(
				"classpath:resources/");
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/errors/404").setViewName("errors/404");

		for (final CustomPage customPage : this.customPages) {
			MvcConfig.logger.info(
					"Registering custom page page {}, template path: {}",
					customPage.getURL(), customPage.getTemplatePath());
			registry.addViewController(customPage.getURL()).setViewName(
					customPage.getTemplatePath());
		}

		registry.addViewController("/**").setViewName("singlepage");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	}

	@Bean
	public FilterRegistrationBean charsetFilter() {
		final FilterRegistrationBean f = new FilterRegistrationBean();
		final CharacterEncodingFilter fl = new CharacterEncodingFilter();
		fl.setEncoding("UTF-8");
		fl.setForceEncoding(true);
		f.setFilter(fl);
		f.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return f;
	}

	@Bean(name = "simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		final SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		r.setDefaultErrorView("error");
		r.setExceptionAttribute("exception");
		return r;
	}
}