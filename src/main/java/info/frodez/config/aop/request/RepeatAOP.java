package info.frodez.config.aop.request;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.redis.Redis;
import info.frodez.service.redis.RedisService;
import info.frodez.util.aop.MethodUtil;
import info.frodez.util.http.HttpUtil;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import info.frodez.util.result.ResultUtil;
import info.frodez.util.spring.context.ContextUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
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
	 * redis服务
	 */
	@Autowired
	private RedisService redisService;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 在请求前判断是否存在正在执行的请求,在请求后删除redis中key
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @throws Throwable
	 * @date 2018-12-21
	 */
	@Around("@annotation(info.frodez.config.aop.request.ReLock)")
	public Object before(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ContextUtil.getRequest();
		ReLock lock = MethodUtil.getAnnotation(point, ReLock.class);
		String key = getKey(lock.value(), request);
		if (redisService.exists(key)) {
			log.info("重复请求:IP地址" + HttpUtil.getAddr(request));
			return new Result(ResultUtil.REPEAT_REQUEST_STRING, ResultEnum.REPEAT_REQUEST);
		}
		redisService.set(key, true);
		Object result = point.proceed();
		redisService.delete(getKey(lock.value(), ContextUtil.getRequest()));
		return result;
	}

	/**
	 * 在一定时间段内拦截重复请求
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Around("@annotation(info.frodez.config.aop.request.TimeoutLock)")
	public Object beforeTimeout(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ContextUtil.getRequest();
		TimeoutLock timeoutLock = MethodUtil.getAnnotation(point, TimeoutLock.class);
		String key = getKey(timeoutLock.value(), request);
		if (redisService.exists(key)) {
			log.info("重复请求:IP地址" + HttpUtil.getAddr(request));
			return new Result(ResultUtil.REPEAT_REQUEST_STRING, ResultEnum.REPEAT_REQUEST);
		}
		if (timeoutLock.time() <= 0) {
			throw new RuntimeException("超时时间必须大于0!");
		}
		redisService.set(key, true, timeoutLock.time());
		return point.proceed();
	}

	/**
	 * 根据规则获得redis的key
	 * @param NoRepeat 注解
	 * @param HttpServletRequest 请求
	 * @author Frodez
	 * @date 2018-12-21
	 */
	private String getKey(String value, HttpServletRequest request) {
		if (properties.needVerify(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String fullToken = request.getHeader(properties.getJwt().getHeader());
			fullToken = fullToken == null ? "" : fullToken;
			return Redis.Request.NO_REPEAT + value + fullToken;
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			String address = HttpUtil.getAddr(request);
			return Redis.Request.NO_REPEAT + address;
		}
	}

}
