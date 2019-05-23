package frodez.constant.errors.exception;

/**
 * 代码检查异常
 * @author Frodez
 * @date 2019-05-23
 */
public class CodeCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CodeCheckException(String message) {
		super(message);
	}

	public CodeCheckException() {
		super();
	}

}
