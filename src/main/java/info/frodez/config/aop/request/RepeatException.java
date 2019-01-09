package info.frodez.config.aop.request;

/**
 * 重复请求异常
 * @author Frodez
 * @date 2018-12-21
 */
public class RepeatException extends Exception {

	private static final long serialVersionUID = 1L;

	public RepeatException(String message) {
		super(message);
	}

	public RepeatException() {
		super();
	}

}
