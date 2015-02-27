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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.ng200.openolympus.FileAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

public class OlympusCustomThymeleafResourceResolver implements
		IResourceResolver {
	public static final String DESCRIPTION_SUFFIX = "</div>\n</body>\n\n</html>";
	public static final String DESCRIPTION_PREFIX = "<!DOCTYPE html>\n<html\n\txmlns=\"http://www.w3.org/1999/xhtml\"\n\txmlns:th=\"http://www.thymeleaf.org\" xmlns:oo=\"http://openolympus\"\n>\n<head>\n</head>\n\n<body><div th:fragment=\"content\">";
	public static final Whitelist HTML_WHITELIST = Whitelist.relaxed()
			.addTags("locale").addAttributes("locale", "name")
			.addAttributes(":all", "class", "style", "th:fragment");

	private static final Logger logger = LoggerFactory
			.getLogger(OlympusCustomThymeleafResourceResolver.class);

	@Override
	public String getName() {
		return "OLYMPUS_TASK";
	}

	@Override
	public InputStream getResourceAsStream(
			final TemplateProcessingParameters templateProcessingParameters,
			final String resourceName) {
		final Path file = FileSystems.getDefault().getPath(resourceName);
		if (!FileAccess.exists(file)) {
			return null;
		}

		String taskDescription;
		try {
			taskDescription = Jsoup.clean(FileAccess.readUTF8String(file),
					OlympusCustomThymeleafResourceResolver.HTML_WHITELIST);

			return new ByteArrayInputStream(
					(OlympusCustomThymeleafResourceResolver.DESCRIPTION_PREFIX
							+ taskDescription + OlympusCustomThymeleafResourceResolver.DESCRIPTION_SUFFIX)
							.getBytes(Charset.forName("UTF-8")));
		} catch (final IOException e) {
			OlympusCustomThymeleafResourceResolver.logger.error(
					"Couldn't open custom markup: {}", e);
			return new ByteArrayInputStream(
					(OlympusCustomThymeleafResourceResolver.DESCRIPTION_PREFIX
							+ "ERROR: couldn't open  open custom markup." + OlympusCustomThymeleafResourceResolver.DESCRIPTION_SUFFIX)
							.getBytes(Charset.forName("UTF-8")));
		}
	}
}