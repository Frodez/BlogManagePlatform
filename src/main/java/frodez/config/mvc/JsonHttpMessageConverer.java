package frodez.config.mvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
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
import org.springframework.lang.Nullable;

public class JsonHttpMessageConverer extends AbstractGenericHttpMessageConverter<Object> {

	/**
	 * The default charset used by the converter.
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private ObjectMapper objectMapper;

	private Map<String, Boolean> contextDeserializeCache = new ConcurrentHashMap<>();

	private Map<Type, Boolean> noContextDeserializeCache = new ConcurrentHashMap<>();

	private Map<Class<?>, Boolean> serializeCache = new ConcurrentHashMap<>();

	public JsonHttpMessageConverer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		setDefaultCharset(DEFAULT_CHARSET);
	}

	public JsonHttpMessageConverer(ObjectMapper objectMapper, MediaType supportedMediaType) {
		this(objectMapper);
		setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
	}

	public JsonHttpMessageConverer(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
		this(objectMapper);
		setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
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
		boolean hasContextClass = contextClass != null;
		Boolean cacheResult;
		if (hasContextClass) {
			cacheResult = contextDeserializeCache.get(type.getTypeName().concat(contextClass.getName()));
		} else {
			cacheResult = noContextDeserializeCache.get(type);
		}
		if (cacheResult != null) {
			return cacheResult;
		}
		JavaType javaType = objectMapper.getTypeFactory().constructType(GenericTypeResolver.resolveType(type,
			contextClass));
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		if (this.objectMapper.canDeserialize(javaType, causeRef)) {
			if (hasContextClass) {
				contextDeserializeCache.put(type.getTypeName().concat(contextClass.getName()), true);
			} else {
				noContextDeserializeCache.put(type, true);
			}
			return true;
		}
		logWarningIfNecessary(javaType, causeRef.get());
		if (hasContextClass) {
			contextDeserializeCache.put(type.getTypeName().concat(contextClass.getName()), false);
		} else {
			noContextDeserializeCache.put(type, false);
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		if (!canWrite(mediaType)) {
			return false;
		}
		Boolean cacheResult = serializeCache.get(clazz);
		if (cacheResult != null) {
			return cacheResult;
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		if (this.objectMapper.canSerialize(clazz, causeRef)) {
			serializeCache.put(clazz, true);
			return true;
		}
		logWarningIfNecessary(clazz, causeRef.get());
		serializeCache.put(clazz, false);
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
		return JSONUtil.as(inputMessage.getBody(), clazz);
	}

	@Override
	public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return JSONUtil.as(inputMessage.getBody(), GenericTypeResolver.resolveType(type, contextClass));
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
		return super.getDefaultContentType(object);
	}

	@Override
	protected Long getContentLength(Object object, @Nullable MediaType contentType) throws IOException {
		return super.getContentLength(object, contentType);
	}

}
