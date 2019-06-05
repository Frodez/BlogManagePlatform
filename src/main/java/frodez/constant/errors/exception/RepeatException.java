package frodez.constant.errors.exception;

import frodez.util.common.StrUtil;

/**
 * 重复请求异常
 * @author Frodez
 * @date 2018-12-21
 */
public class RepeatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepeatException(Throwable throwable, String... message) {
		super(StrUtil.concat(message), throwable);
	}

	public RepeatException(Throwable throwable) {
		super(throwable);
	}

	public RepeatException(String... message) {
		super(StrUtil.concat(message));
	}

	public RepeatException() {
		super();
	}

}
