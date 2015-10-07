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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.security.OpenOlympusRootDecisionVoter;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.util.AnnotationExtraUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	public static class OlmpusConfigAttribute implements ConfigAttribute {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3135156204059260315L;

		@Override
		public String getAttribute() {
			return null;
		}

	}

	public static class MTD
			extends AbstractMethodSecurityMetadataSource {

		public MTD() {
		}

		@Override
		public Collection<ConfigAttribute> getAttributes(Method method,
				Class<?> targetClass) {
			if (method.getDeclaringClass() == Object.class) {
				return Collections.emptyList();
			}

			SecurityOr preAuthorize = AnnotationExtraUtils.findAnnotation(method, targetClass,
					SecurityOr.class);

			if (preAuthorize == null) {
				return Collections.emptyList();
			}
			return Lists.from(new OlmpusConfigAttribute());
		}

		@Override
		public Collection<ConfigAttribute> getAllConfigAttributes() {
			return null;
		}
	}

	@Bean
	public OpenOlympusRootDecisionVoter rootDecisionVoter() {
		return new OpenOlympusRootDecisionVoter();
	}

	@Override
	protected AccessDecisionManager accessDecisionManager() {

		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(rootDecisionVoter());

		return new AffirmativeBased(decisionVoters);
	}

	@Override
	protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
		return new DelegatingMethodSecurityMetadataSource(
				Lists.from(new MTD()));
	}
}