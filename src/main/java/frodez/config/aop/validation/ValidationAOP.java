package frodez.config.aop.validation;

import frodez.util.aop.AspectUtil;
import frodez.util.beans.result.Result;
import frodez.util.common.ValidationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 验证参数AOP<br>
 * 使用方法:在方法的实现上加入@check注解,然后在方法实现的请求参数上使用hibernate validation api支持的方式配置验证.<br>
 * @author Frodez
 * @date 2019-01-12
 */
@Aspect
@Component
public class ValidationAOP {

	/**
	 * 对方法参数进行验证
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2019-01-12
	 */
	@Around("@annotation(frodez.config.aop.validation.annotation.Check)")
	public Object validate(ProceedingJoinPoint p) throws Throwable {
		String msg = ValidationUtil.validateParam(p.getTarget(), AspectUtil.method(p), p.getArgs());
		return msg == null ? p.proceed() : Result.errorRequest(msg);
	}

}
