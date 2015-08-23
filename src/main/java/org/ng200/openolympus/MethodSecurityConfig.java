package org.ng200.openolympus;

import java.util.ArrayList;
import java.util.List;

import org.ng200.openolympus.security.OpenOlympusRootDecisionVoter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	private static final Logger logger = LoggerFactory
			.getLogger(MethodSecurityConfig.class);

	@Bean
	public OpenOlympusRootDecisionVoter rootDecisionVoter() {
		return new OpenOlympusRootDecisionVoter();
	}

	@Override
	protected AccessDecisionManager accessDecisionManager() {
		ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
		expressionAdvice.setExpressionHandler(getExpressionHandler());

		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(rootDecisionVoter());

		return new AffirmativeBased(decisionVoters);
	}

}