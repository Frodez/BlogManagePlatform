package frodez.constant.errors.exception;

import frodez.util.common.StrUtil;

/**
 * 代码检查异常
 * @author Frodez
 * @date 2019-05-23
 */
public class CodeCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CodeCheckException(Throwable throwable, String... message) {
		super(StrUtil.concat(message), throwable);
	}

	public CodeCheckException(Throwable throwable) {
		super(throwable);
	}

	public CodeCheckException(String... message) {
		super(StrUtil.concat(message));
	}

	public CodeCheckException() {
		super();
	}

}
