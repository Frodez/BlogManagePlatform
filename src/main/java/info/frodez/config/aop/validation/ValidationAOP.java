package info.frodez.config.aop.validation;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import info.frodez.util.aop.AopMethodUtil;
import info.frodez.util.result.PageResult;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import info.frodez.util.validation.ValidationUtil;

/**
 * 验证参数AOP<br>
 * 使用方法:在方法上加入@check注解,然后在请求参数上使用hibernate validation api支持的方式配置验证.<br>
 * 如果方法是某个接口中方法的实现,请在接口中方法上加入@check注解和配置验证,而不是在实现中加入.<br>
 * 如果方法不是某个接口中方法的实现,则直接在方法上加入@check注解和配置验证.<br>
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
	@Around("@annotation(info.frodez.config.aop.validation.Check)")
	public Object validate(ProceedingJoinPoint point) throws Throwable {
		Method method = AopMethodUtil.getMethod(point);
		checkReturnType(method);
		Object[] args = point.getArgs();
		String msg = ValidationUtil.validateParam(point.getTarget(), method, args, "");
		if (StringUtils.isBlank(msg)) {
			return point.proceed();
		} else {
			if (method.getReturnType() == Result.class) {
				return new Result(ResultEnum.FAIL, msg);
			} else {
				return new PageResult(ResultEnum.FAIL, msg);
			}
		}
	}

	private void checkReturnType(Method method) {
		if (method.getReturnType() != Result.class && method.getReturnType() != PageResult.class) {
			throw new RuntimeException(method.getDeclaringClass().getName() + "." + method.getName()
				+ "的返回值必须为Result类型或者PageResult类型!");
		}
	}

}
