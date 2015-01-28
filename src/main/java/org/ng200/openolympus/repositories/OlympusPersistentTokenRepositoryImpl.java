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
package org.ng200.openolympus.repositories;

import java.util.Date;
import java.util.List;

import org.ng200.openolympus.model.RememberMeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class OlympusPersistentTokenRepositoryImpl implements
		PersistentTokenRepository {

	@Autowired
	private OlympusPersistentTokenRepository olympusPersistentTokenRepository;

	private static final Logger logger = LoggerFactory
			.getLogger(OlympusPersistentTokenRepositoryImpl.class);

	@Override
	public void createNewToken(final PersistentRememberMeToken token) {

		final PersistentRememberMeToken current = this.getTokenForSeries(token
				.getSeries());

		if (current != null) {
			throw new DataIntegrityViolationException("Series Id '"
					+ token.getSeries() + "' already exists!");
		}

		final RememberMeToken newToken = new RememberMeToken(
				token.getUsername(), token.getSeries(), token.getTokenValue(),
				token.getDate());
		OlympusPersistentTokenRepositoryImpl.logger
				.info("Creating persistent login token for user "
						+ token.getUsername());
		this.olympusPersistentTokenRepository.save(newToken);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
		final RememberMeToken token = this.olympusPersistentTokenRepository
				.findBySeries(seriesId);
		if (token == null) {
			return null;
		}
		return new PersistentRememberMeToken(token.getUsername(),
				token.getSeries(), token.getTokenValue(), token.getDate());
	}

	@Override
	public void removeUserTokens(final String username) {
		final List<RememberMeToken> tokens = this.olympusPersistentTokenRepository
				.findByUsername(username);
		this.olympusPersistentTokenRepository.delete(tokens);
	}

	@Override
	public void updateToken(final String series, final String tokenValue,
			final Date lastUsed) {
		final RememberMeToken token = this.olympusPersistentTokenRepository
				.findBySeries(series);
		if (token != null) {
			token.setTokenValue(tokenValue);
			token.setDate(lastUsed);
			this.olympusPersistentTokenRepository.save(token);
		}

	}
}