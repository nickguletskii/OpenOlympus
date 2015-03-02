package org.ng200.openolympus.jppfsupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTaskExecutionResult<T> {
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

	public static <T> JsonTaskExecutionResult<T> success(T result) {
		return new JsonTaskExecutionResult<T>(result, null);
	}

	public static <T> JsonTaskExecutionResult<T> failure(Throwable failure) {
		return new JsonTaskExecutionResult<T>(null, failure);
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

}
