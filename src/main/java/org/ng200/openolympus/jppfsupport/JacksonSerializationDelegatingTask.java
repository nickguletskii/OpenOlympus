/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
package org.ng200.openolympus.jppfsupport;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.jppf.node.protocol.AbstractTask;
import org.ng200.openolympus.cerberus.util.ExceptionalProducer;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.factory.JacksonSerializationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializationDelegatingTask<V, T extends ExceptionalProducer<V>>
		extends AbstractTask<String> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4648573373270853023L;

	private static final Logger logger = LoggerFactory
			.getLogger(JacksonSerializationDelegatingTask.class);

	private static final ObjectMapper OBJECT_MAPPER = JacksonSerializationFactory
			.createObjectMapper();
	private final String value;
	private final String[] taskClassesUrls;

	public JacksonSerializationDelegatingTask(T task, List<URL> classloaderURLs)
			throws JsonProcessingException {
		this.value = JacksonSerializationDelegatingTask.OBJECT_MAPPER
				.writeValueAsString(task);
		this.taskClassesUrls = classloaderURLs.stream().map(x -> x.toString())
				.toArray(n -> new String[n]);
	}

	public JsonTaskExecutionResult<V> getResultOrThrowable()
			throws JsonParseException, JsonMappingException, IOException {
		try {
			return JacksonSerializationDelegatingTask.OBJECT_MAPPER.readValue(
					this.getResult(),
					JsonTaskExecutionResult.class);
		} catch (JsonMappingException e) {
			logger.error("Couldn't deserialize execution result: {}",
					this.getResult());
			throw e;
		}
	}

	@Override
	public void run() {

		Thread.currentThread()
				.setContextClassLoader(
						new URLClassLoader(
								Arrays.stream(this.taskClassesUrls)
										.map(url -> {
											try {
												return new URL(url);
											} catch (final Exception e) {
												throw new GeneralNestedRuntimeException(
														"Couldn't set up the classloader: ",
														e);
											}
										}).toArray(n -> new URL[n]),
								Thread
										.currentThread()
										.getContextClassLoader()));

		JsonTaskExecutionResult<V> result;
		try {
			result = JsonTaskExecutionResult.<V> success(
					(V) JacksonSerializationDelegatingTask.OBJECT_MAPPER
							.readValue(this.value, ExceptionalProducer.class)
							.run());
		} catch (final Throwable throwable) {
			result = JsonTaskExecutionResult.failure(throwable);
		}
		try {
			this.setResult(JacksonSerializationDelegatingTask.OBJECT_MAPPER
					.writeValueAsString(result));
		} catch (final JsonProcessingException e) {
			try {
				this.setResult(JacksonSerializationDelegatingTask.OBJECT_MAPPER
						.writeValueAsString(
								JsonTaskExecutionResult.failure(e)));
			} catch (final JsonProcessingException e1) {
				this.setThrowable(e1);
			}
		}
	}
}
