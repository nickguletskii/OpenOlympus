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
package org.ng200.openolympus.customPageSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CustomPageConfig implements ResourceLoaderAware {

	@Value("${customPageMapFile}")
	String customPageMapFile;

	private ResourceLoader resourceLoader;

	@Bean
	public List<CustomPage> customPages() throws JsonProcessingException,
	IOException {
		final List<CustomPage> customPages = new ArrayList<>();

		if (StringUtils.isEmpty(this.customPageMapFile)) {
			return customPages;
		}

		final ObjectMapper mapper = new ObjectMapper();
		try (InputStream inputStream = this.resourceLoader.getResource(
				this.customPageMapFile).getInputStream()) {

			final JsonNode root = mapper.readTree(inputStream);
			if (!root.isArray()) {
				throw new JsonMappingException(
						"Custom page map file root must be an array!");
			}
			for (final JsonNode node : root) {
				final CustomPage customPage = new CustomPage();

				if (!node.isObject()) {
					throw new JsonMappingException(
							"Custom page map file nodes must be objects!");
				}
				final JsonNode titleNode = node.get("title");
				if (!titleNode.isObject()) {
					throw new JsonMappingException(
							"Custom page map file title nodes must be objects!");
				}
				final Iterator<Map.Entry<String, JsonNode>> iter = titleNode
						.fields();
				while (iter.hasNext()) {
					final Map.Entry<String, JsonNode> title = iter.next();
					if (!title.getValue().isTextual()) {
						throw new JsonMappingException(
								"Custom page map file titles must be strings!");
					}
					customPage.addTitle(new Locale(title.getKey()), title
							.getValue().asText());
				}

				if (!node.has("path")) {
					throw new JsonMappingException(
							"Custom page must have a path!");
				}

				if (!node.get("path").isTextual()) {
					throw new JsonMappingException(
							"Custom page paths must be strings!");
				}

				if (!node.has("templatePath")) {
					throw new JsonMappingException(
							"Custom page must have a template path!");
				}

				if (!node.get("templatePath").isTextual()) {
					throw new JsonMappingException(
							"Custom page map paths must be strings!");
				}

				customPage.setTemplatePath(node.get("templatePath").asText());
				customPage.setURL(node.get("path").asText());

				customPages.add(customPage);
			}
		}
		return customPages;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
