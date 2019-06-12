package frodez.annotation;

import frodez.config.aop.exception.annotation.CatchAndReturn;
import frodez.util.beans.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalServiceA {

	@Autowired
	private TransactionalServiceB transactionalService;

	@Transactional
	@CatchAndReturn
	public Result run() {
		Result result = transactionalService.throwResult();
		return result;
	}

}
