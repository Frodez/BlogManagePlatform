package frodez.config.code.rule.hibernate;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.code.rule.CodeCheckRule;
import frodez.constant.errors.exception.CodeCheckException;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.Method;

public class CheckRule implements CodeCheckRule {

	@Override
	public void check(Method method) throws CodeCheckException {
		if (method.getParameterCount() == 0) {
			throw new CodeCheckException("@", Check.class.getName(), "注解不能在无参数的方法", ReflectUtil.getFullMethodName(
				method), "上使用");
		}
	}

}
