package frodez.constant.errors.code;

import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;

/**
 * 标准服务异常
 * @author Frodez
 * @date 2019-01-21
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(Throwable throwable, String... message) {
		super(StrUtil.concat(message), throwable);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}

	public ServiceException(String... message) {
		super(StrUtil.concat(message));
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
