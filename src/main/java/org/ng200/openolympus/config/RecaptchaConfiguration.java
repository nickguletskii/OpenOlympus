package org.ng200.openolympus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openolympus.recaptcha")
public class RecaptchaConfiguration {

	private boolean recaptchaEnabled = false;

	private String recaptchaPrivateKey;

	private String recaptchaPublicKey;

	public boolean isRecaptchaEnabled() {
		return recaptchaEnabled;
	}

	public void setRecaptchaEnabled(boolean recaptchaEnabled) {
		this.recaptchaEnabled = recaptchaEnabled;
	}

	public String getRecaptchaPrivateKey() {
		return recaptchaPrivateKey;
	}

	public void setRecaptchaPrivateKey(String recaptchaPrivateKey) {
		this.recaptchaPrivateKey = recaptchaPrivateKey;
	}

	public String getRecaptchaPublicKey() {
		return recaptchaPublicKey;
	}

	public void setRecaptchaPublicKey(String recaptchaPublicKey) {
		this.recaptchaPublicKey = recaptchaPublicKey;
	}

}
