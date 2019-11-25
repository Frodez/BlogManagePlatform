package frodez.constant.errors.exception;

import frodez.util.common.StrUtil;

/**
 * Result解析异常
 * @author Frodez
 * @date 2019-01-21
 */
public class ResultParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResultParseException(String... message) {
		super(StrUtil.concat(message), null, false, false);
	}

	public ResultParseException() {
		super(null, null, false, false);
	}

}
