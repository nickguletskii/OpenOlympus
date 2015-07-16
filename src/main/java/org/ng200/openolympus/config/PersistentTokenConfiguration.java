package org.ng200.openolympus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openolympus.persistentLogin")
public class PersistentTokenConfiguration {
	private String persistentTokenKey;

	public String getPersistentTokenKey() {
		return persistentTokenKey;
	}

	public void setPersistentTokenKey(String persistentTokenKey) {
		this.persistentTokenKey = persistentTokenKey;
	}
}
