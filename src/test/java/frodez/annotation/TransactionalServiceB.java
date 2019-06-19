package frodez.annotation;

import frodez.config.aop.exception.annotation.CatchAndReturn;
import frodez.util.beans.result.Result;
import org.springframework.stereotype.Component;

@Component
public class TransactionalServiceB {

	@CatchAndReturn
	public Result throwResult() {
		throw new RuntimeException();
	}

}
