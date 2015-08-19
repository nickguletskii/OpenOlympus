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
package org.ng200.openolympus.resourceResolvers;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.config.StorageConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public final class OpenOlympusMessageSource extends
		ReloadableResourceBundleMessageSource {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenOlympusMessageSource.class);

	private StorageConfiguration storageConfig;
	private long lastModified;

	@Override
	public void clearCache() {
		this.lastModified = System.currentTimeMillis();
		super.clearCache();
	}

	public Properties getKeys(Locale locale) {
		return this.getMergedProperties(locale).getProperties();
	}

	public long getLastModified() {
		return this.lastModified;
	}

	@Override
	protected PropertiesHolder refreshProperties(String filename,
			PropertiesHolder propHolder) {
		this.lastModified = System.currentTimeMillis();
		return super.refreshProperties(filename, propHolder);
	}

	private void reportMissingLocalisationKey(final String code) {
		try {
			if (code.isEmpty() || Character.isUpperCase(code.charAt(0))) {
				return;
			}
			final Path file = FileSystems.getDefault().getPath(
					this.storageConfig.getStoragePath(),
					"missingLocalisation.txt");
			if (!FileAccess.exists(file)) {
				FileAccess.createDirectories(file.getParent());
				FileAccess.createFile(file);
			}
			final Set<String> s = new TreeSet<>(Arrays.asList(FileAccess
					.readUTF8String(file).split("\n")));
			s.add(code);
			FileAccess.writeUTF8StringToFile(file,
					s.stream().collect(Collectors.joining("\n")));
		} catch (final IOException e) {
			OpenOlympusMessageSource.logger.error(
					"Couldn't add to missing key repo: {}", e);
		}
	}

	@Override
	protected MessageFormat resolveCode(final String code,
			final Locale locale) {
		final MessageFormat format = super.resolveCode(code, locale);
		;
		if (format == null) {
			this.reportMissingLocalisationKey(code);
		}
		return format;
	}

	@Override
	protected String resolveCodeWithoutArguments(final String code,
			final Locale locale) {
		final String string = super.resolveCodeWithoutArguments(code, locale);
		if (string == null) {
			this.reportMissingLocalisationKey(code);
		}
		return string;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}