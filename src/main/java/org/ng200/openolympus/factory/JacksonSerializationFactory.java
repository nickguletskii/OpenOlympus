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
package org.ng200.openolympus.factory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * @author Nick Guletskii
 *
 */
public class JacksonSerializationFactory {
	/**
	 * @author Nick Guletskii Path serialisation module.
	 * 
	 *         TODO: fix jackson-datatype-jdk7 by adding the mix-in.
	 */
	public static class PathSerializationModule extends SimpleModule {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3024181054633334180L;

		public PathSerializationModule() {
			super(Version.unknownVersion());
			addSerializer(new PathSerializer());
			addDeserializer(Path.class, new PathDeserializer());
		}

	}

	private static final class PathSerializer extends StdSerializer<Path> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1163251899706317917L;

		private PathSerializer() {
			super(Path.class);
		}

		@Override
		public void serializeWithType(Path value, JsonGenerator gen,
				SerializerProvider provider, TypeSerializer typeSer)
				throws IOException {
			typeSer.writeTypePrefixForScalar(value, gen);
			serialize(value, gen, provider);
			typeSer.writeTypeSuffixForScalar(value, gen);
		}

		@Override
		public void serialize(Path value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonGenerationException {
			jgen.writeString(value.toString());
		}
	}

	/**
	 * @author Nick Guletskii Deserialises JDK7 Path implementations.
	 */
	private static final class PathDeserializer extends
			StdScalarDeserializer<Path> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4325594504763073992L;

		protected PathDeserializer() {
			super(Path.class);
		}

		@Override
		public Path deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			return Paths.get(jp.getValueAsString());
		}

	}

	/**
	 * @return Jackson ObjectMapper that has the necessary configuration to
	 *         serialise and deserialise Cerberus classes
	 */
	public static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
				.registerModule(new PathSerializationModule())
				.registerModule(new Jdk8Module())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
		try {
			objectMapper.addMixInAnnotations(Path.class, PathMixin.class);
			objectMapper.addMixInAnnotations(
					Class.forName("sun.nio.fs.UnixPath"), PathMixin.class);
			return objectMapper;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@JsonCreator
	public static Path factory(String path) {
		return Paths.get(path);
	}

	/**
	 * @author Nick Guletskii Downcasts all Path implementations down to Path.
	 */
	public static class PathTypeIdResolver implements TypeIdResolver {

		@Override
		public void init(JavaType baseType) {
		}

		@Override
		public String idFromValue(Object value) {
			return Path.class.getName();
		}

		@Override
		public String idFromValueAndType(Object value, Class<?> suggestedType) {
			return Path.class.getName();
		}

		@Override
		public String idFromBaseType() {
			return Path.class.getName();
		}

		@Override
		public JavaType typeFromId(String id) {
			return SimpleType.construct(Path.class);
		}

		@Override
		public Id getMechanism() {
			return JsonTypeInfo.Id.CLASS;
		}

		@Override
		public JavaType typeFromId(DatabindContext context, String id) {
			return SimpleType.construct(Path.class);
		}

	}

	/**
	 * @author Nick Guletskii Jackson mix-in for JDK7's Path types.
	 */
	@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	@JsonTypeIdResolver(PathTypeIdResolver.class)
	public static abstract class PathMixin {
		@JsonCreator
		public static Path factory(String str) {
			return Paths.get(str);
		}
	}
}
