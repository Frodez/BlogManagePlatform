package frodez.config.mvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import frodez.util.beans.result.Result;
import frodez.util.json.JSONUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.lang.Nullable;

public class JsonHttpMessageConverer extends AbstractGenericHttpMessageConverter<Object> {

	/**
	 * The default charset used by the converter.
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private ObjectMapper objectMapper;

	private TypeFactory typeFactory;

	private Map<Type, JavaType> noContextCache = new ConcurrentHashMap<>();

	private Map<String, JavaType> contextCache = new ConcurrentHashMap<>();

	@Nullable
	private Boolean prettyPrint;

	public JsonHttpMessageConverer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		setDefaultCharset(DEFAULT_CHARSET);
		typeFactory = this.objectMapper.getTypeFactory();
	}

	public JsonHttpMessageConverer(ObjectMapper objectMapper, MediaType supportedMediaType) {
		this(objectMapper);
		setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
	}

	public JsonHttpMessageConverer(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
		this(objectMapper);
		setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
	}

	/**
	 * Whether to use the {@link DefaultPrettyPrinter} when writing JSON. This is a shortcut for setting up an
	 * {@code ObjectMapper} as follows:
	 *
	 * <pre class="code">
	 * ObjectMapper mapper = new ObjectMapper();
	 * mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	 * converter.setObjectMapper(mapper);
	 * </pre>
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
		configurePrettyPrint();
	}

	private void configurePrettyPrint() {
		if (this.prettyPrint != null) {
			this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint);
		}
	}

	@Override
	public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
		return canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
		if (!canRead(mediaType)) {
			return false;
		}
		JavaType javaType;
		if (contextClass == null) {
			javaType = noContextCache.computeIfAbsent(type, (t) -> this.typeFactory.constructType(type));
		} else {
			javaType = contextCache.computeIfAbsent(type.getTypeName().concat(contextClass.getName()), (
				t) -> this.typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass)));
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		if (this.objectMapper.canDeserialize(javaType, causeRef)) {
			return true;
		}
		logWarningIfNecessary(javaType, causeRef.get());
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		if (!canWrite(mediaType)) {
			return false;
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		if (this.objectMapper.canSerialize(clazz, causeRef)) {
			return true;
		}
		logWarningIfNecessary(clazz, causeRef.get());
		return false;
	}

	/**
	 * Determine whether to log the given exception coming from a {@link ObjectMapper#canDeserialize} /
	 * {@link ObjectMapper#canSerialize} check.
	 * @param type the class that Jackson tested for (de-)serializability
	 * @param cause the Jackson-thrown exception to evaluate (typically a {@link JsonMappingException})
	 * @since 4.3
	 */
	private void logWarningIfNecessary(Type type, @Nullable Throwable cause) {
		if (cause == null) {
			return;
		}
		// Do not log warning for serializer not found (note: different message wording on Jackson 2.9)
		boolean debugLevel = cause instanceof JsonMappingException && cause.getMessage().startsWith("Cannot find");
		if (debugLevel ? logger.isDebugEnabled() : logger.isWarnEnabled()) {
			String msg = "Failed to evaluate Jackson " + (type instanceof JavaType ? "de" : "")
				+ "serialization for type [" + type + "]";
			if (debugLevel) {
				logger.debug(msg, cause);
			} else if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return readJavaType(noContextCache.computeIfAbsent(clazz, (t) -> this.typeFactory.constructType(clazz)),
			inputMessage);
	}

	@Override
	public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return readJavaType(contextCache.computeIfAbsent(type.getTypeName().concat(contextClass.getName()), (
			t) -> this.typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass))), inputMessage);
	}

	private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) throws IOException {
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		} catch (InvalidDefinitionException ex) {
			throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex, inputMessage);
		}
	}

	@Override
	protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage)
		throws IOException, HttpMessageNotWritableException {
		try {
			if (object instanceof Result) {
				outputMessage.getBody().write(((Result) object).toString().getBytes());
				outputMessage.getBody().flush();
			} else {
				JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(),
					getJsonEncoding(outputMessage.getHeaders().getContentType()));
				generator.writeRaw(JSONUtil.string(object));
				generator.flush();
			}
		} catch (InvalidDefinitionException ex) {
			throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
		}
	}

	/**
	 * Determine the JSON encoding to use for the given content type.
	 * @param contentType the media type as requested by the caller
	 * @return the JSON encoding to use (never {@code null})
	 */
	private JsonEncoding getJsonEncoding(@Nullable MediaType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			Charset charset = contentType.getCharset();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

	@Override
	@Nullable
	protected MediaType getDefaultContentType(Object object) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getDefaultContentType(object);
	}

	@Override
	protected Long getContentLength(Object object, @Nullable MediaType contentType) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getContentLength(object, contentType);
	}

}
