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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializationDelegatingTask<V, T extends ExceptionalProducer<V>>
		extends AbstractTask<String> {

	private static final ObjectMapper OBJECT_MAPPER = JacksonSerializationFactory
			.createObjectMapper();
	private String value;
	private String[] taskClassesUrls;

	public JacksonSerializationDelegatingTask(T task, List<URL> classloaderURLs)
			throws JsonProcessingException {
		value = OBJECT_MAPPER.writeValueAsString(task);
		taskClassesUrls = classloaderURLs.stream().map(x -> x.toString())
				.toArray(n -> new String[n]);
	}

	@Override
	public void run() {

		Thread.currentThread()
				.setContextClassLoader(
						new URLClassLoader(
								Arrays.stream(taskClassesUrls)
										.map(url -> {
											try {
												return new URL(url);
											} catch (Exception e) {
												throw new GeneralNestedRuntimeException(
														"Couldn't set up the classloader: ",
														e);
											}
										}).toArray(n -> new URL[n]), Thread
										.currentThread()
										.getContextClassLoader()));

		JsonTaskExecutionResult<V> result;
		try {
			result = JsonTaskExecutionResult.<V> success((V) OBJECT_MAPPER
					.readValue(value, ExceptionalProducer.class).run());
		} catch (Throwable throwable) {
			result = JsonTaskExecutionResult.failure(throwable);
		}
		try {
			setResult(OBJECT_MAPPER.writeValueAsString(result));
		} catch (JsonProcessingException e) {
			try {
				setResult(OBJECT_MAPPER
						.writeValueAsString(JsonTaskExecutionResult.failure(e)));
			} catch (JsonProcessingException e1) {
				setThrowable(e1);
			}
		}
	}

	public JsonTaskExecutionResult<V> getResultOrThrowable()
			throws JsonParseException, JsonMappingException, IOException {
		return OBJECT_MAPPER.readValue(getResult(),
				JsonTaskExecutionResult.class);
	}
}
