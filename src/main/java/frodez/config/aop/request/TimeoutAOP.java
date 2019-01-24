package frodez.config.aop.request;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.aop.request.checker.facade.TimeoutChecker;
import frodez.config.aop.request.checker.impl.KeyGenerator;
import frodez.util.aop.MethodUtil;
import frodez.util.http.HttpUtil;
import frodez.util.result.Result;
import frodez.util.result.ResultEnum;
import frodez.util.result.ResultUtil;
import frodez.util.spring.context.ContextUtil;
import lombok.extern.slf4j.Slf4j;

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
	private TimeoutChecker checker;
	
	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private KeyGenerator generator;

	/**
	 * 在一定时间段内拦截重复请求
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Around("@annotation(frodez.config.aop.request.annotation.TimeoutLock)")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ContextUtil.getRequest();
		TimeoutLock timeoutLock = MethodUtil.getAnnotation(point, TimeoutLock.class);
		String key = generator.servletKey(timeoutLock.value(), request);
		if (checker.check(key)) {
			log.info("重复请求:IP地址" + HttpUtil.getAddr(request));
			return new Result(ResultUtil.REPEAT_REQUEST_STRING, ResultEnum.REPEAT_REQUEST);
		}
		checker.lock(key, timeoutLock.time());
		return point.proceed();
	}

}
