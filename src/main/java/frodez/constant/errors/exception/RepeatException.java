package frodez.constant.errors.exception;

import frodez.util.common.StrUtil;

/**
 * 重复请求异常
 * @author Frodez
 * @date 2018-12-21
 */
public class RepeatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepeatException(String... message) {
		super(StrUtil.concat(message), null, false, false);
	}

	public RepeatException() {
		super(null, null, false, false);
	}

}
