package frodez.config.aop.validation;

import frodez.util.aop.MethodUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.result.Result;
import frodez.util.result.ResultEnum;
import frodez.util.validation.ValidationUtil;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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
	@Around("@annotation(frodez.config.aop.validation.Check)")
	public Object validate(ProceedingJoinPoint point) throws Throwable {
		Method method = MethodUtil.getMethod(point);
		if (method.getReturnType() != Result.class) {
			throw new RuntimeException(ReflectUtil.getFullName(method) + "的返回值必须为" + Result.class.getName() + "类型!");
		}
		String msg = ValidationUtil.validateParam(point.getTarget(), method, point.getArgs(), "");
		return StringUtils.isBlank(msg) ? point.proceed() : new Result(msg, ResultEnum.FAIL);
	}

}
