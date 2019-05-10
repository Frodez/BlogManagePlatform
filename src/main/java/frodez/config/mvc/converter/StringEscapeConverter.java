package frodez.config.mvc.converter;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import frodez.util.constant.setting.DefCharset;

/*
 * Copyright 2002-2018 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

/**
 * 自定义StringHttpMessageConverter,对危险字符进行转义
 * @author Frodez
 * @date 2019-05-09
 */
public class StringEscapeConverter extends AbstractHttpMessageConverter<String> {

	private List<Charset> availableCharsets = new ArrayList<>(Charset.availableCharsets().values());

	/**
	 * 转义处理
	 */
	private Escaper escaper = HtmlEscapers.htmlEscaper();

	/**
	 * 默认使用UTF-8
	 */
	public StringEscapeConverter() {
		super(DefCharset.UTF_8_CHARSET, MediaType.TEXT_PLAIN, MediaType.ALL);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class == clazz;
	}

	@Override
	protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
		//转义处理
		return escaper.escape(StreamUtils.copyToString(inputMessage.getBody(), getContentTypeCharset(inputMessage
			.getHeaders().getContentType())));
	}

	@Override
	protected Long getContentLength(String str, @Nullable MediaType contentType) {
		return (long) str.getBytes(getContentTypeCharset(contentType)).length;
	}

	@Override
	protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
		outputMessage.getHeaders().setAcceptCharset(this.availableCharsets);
		//转义处理
		StreamUtils.copy(escaper.escape(str), getContentTypeCharset(outputMessage.getHeaders().getContentType()),
			outputMessage.getBody());
	}

	private Charset getContentTypeCharset(@Nullable MediaType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			return contentType.getCharset();
		} else if (contentType != null && contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
			// Matching to AbstractJackson2HttpMessageConverter#DEFAULT_CHARSET
			return StandardCharsets.UTF_8;
		} else {
			return getDefaultCharset();
		}
	}

}
