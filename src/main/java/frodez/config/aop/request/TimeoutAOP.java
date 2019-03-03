package frodez.config.aop.request;

import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.aop.request.checker.facade.AutoChecker;
import frodez.config.aop.request.checker.impl.KeyGenerator;
import frodez.util.aop.AspectUtil;
import frodez.util.beans.result.Result;
import frodez.util.http.ServletUtil;
import frodez.util.spring.context.ContextUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 控制重复请求AOP切面<br>
 * <strong>原理:</strong><br>
 * 在请求处理方法前后设点.<br>
 * 进入请求处理方法前,根据规则获得key,然后查询redis中是否存在对应value.<br>
 * 如果存在对应value,说明出现重复请求,抛出NoRepeatException异常.<br>
 * 如果不存在对应value,说明没有重复请求,继续执行.<br>
 * 请求处理方法结束后,根据规则获得key,然后删除redis中对应key.<br>
 * @author Frodez
 * @date 2018-12-21
 */
@Slf4j
@Aspect
@Component
public class TimeoutAOP {

	/**
	 * 自动超时型重复请求检查
	 */
	@Autowired
	@Qualifier("timeoutGuavaChecker")
	private AutoChecker checker;

	/**
	 * 在一定时间段内拦截重复请求
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Around("@annotation(frodez.config.aop.request.annotation.TimeoutLock)")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ContextUtil.getRequest();
		TimeoutLock timeoutLock = AspectUtil.getAnnotation(point, TimeoutLock.class);
		String key = KeyGenerator.servletKey(AspectUtil.getFullName(point), request);
		if (checker.check(key)) {
			log.info("重复请求:IP地址{}", ServletUtil.getAddr(request));
			return Result.errorRequest();
		}
		checker.lock(key, timeoutLock.value());
		return point.proceed();
	}

}
