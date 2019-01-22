package frodez.config.aop.log;

import frodez.util.aop.MethodUtil;
import frodez.util.json.JSONUtil;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 日志管理AOP切面
 * @author Frodez
 * @date 2019-01-12
 */
@Slf4j
@Aspect
@Component
public class LogAOP {

	/**
	 * 打印参数到日志
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2019-01-12
	 */
	@Before("@annotation(frodez.config.aop.log.annotation.ParamLog)")
	public void printParam(JoinPoint point) {
		Parameter[] parameters = MethodUtil.getParams(point);
		Object[] args = point.getArgs();
		Map<String, String> paramMap = new HashMap<>();
		for (int i = 0; i < parameters.length; i++) {
			paramMap.put(parameters[i].getName(), JSONUtil.toJSONString(args[i]));
		}
		log.info(MethodUtil.getFullName(point) + " 请求参数:" + JSONUtil.toJSONString(paramMap));
	}

	/**
	 * 打印返回值到日志
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2019-01-12
	 */
	@AfterReturning(value = "@annotation(frodez.config.aop.log.annotation.ResultLog)", returning = "result")
	public void printResult(JoinPoint point, Object result) {
		log.info(MethodUtil.getFullName(point) + " 返回值:" + JSONUtil.toJSONString(result));
	}

	/**
	 * 打印方法参数和返回值到日志
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @throws Throwable
	 * @date 2018-12-21
	 */
	@Around("@annotation(frodez.config.aop.log.annotation.MethodLog)")
	public Object before(ProceedingJoinPoint point) throws Throwable {
		Parameter[] parameters = MethodUtil.getParams(point);
		Object[] args = point.getArgs();
		Map<String, String> paramMap = new HashMap<>();
		for (int i = 0; i < parameters.length; i++) {
			paramMap.put(parameters[i].getName(), JSONUtil.toJSONString(args[i]));
		}
		log.info(MethodUtil.getFullName(point) + " 请求参数:" + JSONUtil.toJSONString(paramMap));
		Object object = point.proceed();
		log.info(MethodUtil.getFullName(point) + " 返回值:" + JSONUtil.toJSONString(object));
		return object;
	}

}
