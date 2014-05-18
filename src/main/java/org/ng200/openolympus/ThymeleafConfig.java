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
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class ThymeleafConfig {

	@Bean
	public FileTemplateResolver customTemplateResolver() {
		final FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setPrefix(StorageSpace.STORAGE_PREFIX + "/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(2);
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addTemplateResolver(this.templateResolver());
		engine.addTemplateResolver(this.customTemplateResolver());
		engine.addDialect(new SpringSecurityDialect());
		return engine;
	}

	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(1);
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(this.templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

}