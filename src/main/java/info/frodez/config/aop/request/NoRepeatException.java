package info.frodez.config.aop.request;

/**
 * 重复请求异常
 * @author Frodez
 * @date 2018-12-21
 */
public class NoRepeatException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoRepeatException(String message) {
		super(message);
	}

	public NoRepeatException() {
		super();
	}

}
