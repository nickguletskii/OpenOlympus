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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTaskExecutionResult<T> {
	public static <T> JsonTaskExecutionResult<T> failure(Throwable failure) {
		return new JsonTaskExecutionResult<T>(null, failure);
	}

	public static <T> JsonTaskExecutionResult<T> success(T result) {
		return new JsonTaskExecutionResult<T>(result, null);
	}

	private T result;

	private Throwable error;

	@JsonCreator
	public JsonTaskExecutionResult(
			@JsonProperty(value = "result", required = false) T result,
			@JsonProperty(value = "error", required = false) Throwable error) {
		super();
		this.result = result;
		this.error = error;
	}

	public Throwable getError() {
		return this.error;
	}

	public T getResult() {
		return this.result;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
