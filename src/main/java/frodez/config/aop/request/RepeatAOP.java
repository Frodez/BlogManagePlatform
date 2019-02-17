package frodez.config.aop.request;

import frodez.config.aop.request.checker.facade.ManualChecker;
import frodez.config.aop.request.checker.impl.KeyGenerator;
import frodez.util.aop.MethodUtil;
import frodez.util.http.ServletUtil;
import frodez.util.result.Result;
import frodez.util.result.ResultEnum;
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
public class RepeatAOP {

	/**
	 * 阻塞型重复请求检查
	 */
	@Autowired
	@Qualifier("repeatGuavaChecker")
	private ManualChecker checker;

	/**
	 * 缓存key生成器
	 */
	@Autowired
	private KeyGenerator generator;

	/**
	 * 在请求前判断是否存在正在执行的请求,在请求后删除redis中key
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @throws Throwable
	 * @date 2018-12-21
	 */
	@Around("@annotation(frodez.config.aop.request.annotation.RepeatLock)")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ContextUtil.getRequest();
		String key = generator.servletKey(MethodUtil.getFullName(point), request);
		try {
			if (checker.check(key)) {
				log.info("重复请求:IP地址{}", ServletUtil.getAddr(request));
				return new Result(ResultEnum.REPEAT_REQUEST);
			}
			checker.lock(key);
			return point.proceed();
		} finally {
			checker.free(key);
		}
	}

}
