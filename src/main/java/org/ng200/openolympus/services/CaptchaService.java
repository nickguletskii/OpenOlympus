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
package org.ng200.openolympus.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ng200.openolympus.config.RecaptchaConfiguration;
import org.ng200.openolympus.recaptcha.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CaptchaService {

	private final CloseableHttpClient httpclient = HttpClients.createDefault();

	@Autowired
	private RecaptchaConfiguration recaptchaConfiguration;

	public List<String> checkCaptcha(final String recaptchaResponse)
			throws URISyntaxException, IOException, JsonParseException,
			JsonMappingException, ClientProtocolException {
		if (!this.recaptchaConfiguration.isRecaptchaEnabled()) {
			return null;
		}

		final URI uri = new URIBuilder().setScheme("https")
				.setHost("www.google.com").setPath("/recaptcha/api/siteverify")
				.setParameter("secret",
						this.recaptchaConfiguration.getRecaptchaPrivateKey())
				.setParameter("response", recaptchaResponse).build();

		final HttpGet httpget = new HttpGet(uri);
		try (CloseableHttpResponse httpResponse = this.httpclient
				.execute(httpget)) {
			final ObjectMapper mapper = new ObjectMapper();

			final RecaptchaResponse response = mapper.readValue(httpResponse
					.getEntity().getContent(),
					new TypeReference<RecaptchaResponse>() {
					});
			if (response.isSuccess()) {
				return null;
			}
			return response.getErrorCodes();
		}
	}

}
