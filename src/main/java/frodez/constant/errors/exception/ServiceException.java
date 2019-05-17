package frodez.constant.errors.exception;

import frodez.constant.errors.ErrorCode;
import frodez.util.beans.result.Result;

/**
 * 标准服务异常
 * @author Frodez
 * @date 2019-01-21
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Result result) {
		super(result.getMessage());
	}

	public ServiceException(ErrorCode error) {
		super(error.getDescription());
	}

	public ServiceException() {
		super();
	}

}
