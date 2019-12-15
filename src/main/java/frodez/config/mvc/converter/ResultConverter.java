package frodez.config.mvc.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import frodez.constant.settings.DefCharset;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
import frodez.util.json.JSONUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 自定义jacksonHttpMessageConverter(仅用于Result)
 * @author Frodez
 * @date 2019-03-14
 */
public class ResultConverter extends AbstractGenericHttpMessageConverter<Object> {

	public ResultConverter() {
		setDefaultCharset(DefCharset.UTF_8_CHARSET);
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		Assert.isTrue(JSONUtil.mapper().canSerialize(Result.class), "Result can't be serialized!");
		Assert.isTrue(JSONUtil.mapper().canDeserialize(JSONUtil.mapper().getTypeFactory().constructType(Result.class)),
			"Result can't be deserialized!");
	}

	@Override
	public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
		return canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
		return type == Result.class;
	}

	@Override
	public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
		return clazz == Result.class;
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return Result.reader().readValue(inputMessage.getBody());
	}

	@Override
	public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
		HttpMessageNotReadableException {
		return Result.reader().readValue(inputMessage.getBody());
	}

	@Override
	protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException,
		HttpMessageNotWritableException {
		try {
			OutputStream outputStream = outputMessage.getBody();
			//对通用Result采用特殊的优化过的方式
			byte[] cacheBytes = ((Result) object).cacheBytes();
			if (cacheBytes != null) {
				outputStream.write(cacheBytes);
			} else {
				Result.writer().writeValue(outputStream, object);
			}
			outputStream.flush();
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException(StrUtil.concat("Could not write JSON: ", ex.getOriginalMessage()), ex);
		}
	}

}
