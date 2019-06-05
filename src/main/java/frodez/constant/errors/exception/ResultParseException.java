package frodez.constant.errors.exception;

import frodez.util.common.StrUtil;

/**
 * Result解析异常
 * @author Frodez
 * @date 2019-01-21
 */
public class ResultParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResultParseException(Throwable throwable, String... message) {
		super(StrUtil.concat(message), throwable);
	}

	public ResultParseException(Throwable throwable) {
		super(throwable);
	}

	public ResultParseException(String... message) {
		super(StrUtil.concat(message));
	}

	public ResultParseException() {
		super();
	}

}
