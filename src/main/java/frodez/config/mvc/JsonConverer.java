package frodez.config.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
import frodez.util.constant.setting.DefCharset;
import frodez.util.json.JSONUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
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

/**
 * 自定义jacksonHttpMessageConverter
 * @author Frodez
 * @date 2019-03-14
 */
public class JsonConverer extends AbstractGenericHttpMessageConverter<Object> {

	private Map<String, Boolean> contextDeserializeCache = new ConcurrentHashMap<>();

	private Map<Type, Boolean> noContextDeserializeCache = new ConcurrentHashMap<>();

	private Map<Class<?>, Boolean> serializeCache = new ConcurrentHashMap<>();

	public JsonConverer(MediaType... supportedMediaTypes) {
		setDefaultCharset(DefCharset.UTF_8_CHARSET);
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
			cacheResult = contextDeserializeCache.get(StrUtil.concat(type.getTypeName(), contextClass.getName()));
		} else {
			cacheResult = noContextDeserializeCache.get(type);
		}
		if (cacheResult != null) {
			return cacheResult;
		}
		JavaType javaType = JSONUtil.mapper().getTypeFactory().constructType(GenericTypeResolver.resolveType(type,
			contextClass));
		AtomicReference<Throwable> causeRef = new AtomicReference<>();
		cacheResult = JSONUtil.mapper().canDeserialize(javaType, causeRef);
		logWarningIfNecessary(javaType, causeRef.get());
		if (hasContextClass) {
			contextDeserializeCache.put(StrUtil.concat(type.getTypeName(), contextClass.getName()), cacheResult);
		} else {
			noContextDeserializeCache.put(type, cacheResult);
		}
		return cacheResult;
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
		cacheResult = JSONUtil.mapper().canSerialize(clazz, causeRef);
		logWarningIfNecessary(clazz, causeRef.get());
		serializeCache.put(clazz, cacheResult);
		return cacheResult;
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
			String msg = StrUtil.concat("Failed to evaluate Jackson ", type instanceof JavaType ? "de" : "",
				"serialization for type [", type.toString(), "]");
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
			OutputStream outputStream = outputMessage.getBody();
			if (object.getClass() == Result.class) {
				//对通用Result采用特殊的优化过的方式
				outputStream.write(((Result) object).json().getBytes());
				outputStream.flush();
			} else {
				outputStream.write(JSONUtil.string(object).getBytes());
				outputStream.flush();
			}
		} catch (InvalidDefinitionException ex) {
			throw new HttpMessageConversionException(StrUtil.concat("Type definition error: ", ex.getType().toString()),
				ex);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException(StrUtil.concat("Could not write JSON: ", ex.getOriginalMessage()),
				ex);
		}
	}

}
