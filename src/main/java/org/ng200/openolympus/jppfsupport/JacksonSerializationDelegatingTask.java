package org.ng200.openolympus.jppfsupport;

import java.io.IOException;

import org.jppf.node.protocol.AbstractTask;
import org.ng200.openolympus.cerberus.util.ExceptionalProducer;
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

	public JacksonSerializationDelegatingTask(T task)
			throws JsonProcessingException {
		value = OBJECT_MAPPER.writeValueAsString(task);
	}

	@Override
	public void run() {
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
