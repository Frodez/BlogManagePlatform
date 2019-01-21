package info.frodez.config.error.exception;

/**
 * Result解析异常
 * @author Frodez
 * @date 2019-01-21
 */
public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException() {
		super();
	}

}
