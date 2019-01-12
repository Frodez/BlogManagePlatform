package info.frodez.config.aop.request;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.redis.Redis;
import info.frodez.service.redis.RedisService;
import info.frodez.util.aop.AopMethodUtil;
import info.frodez.util.http.HttpUtil;

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
	 * 在请求前判断是否存在正在执行的请求
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Before("@annotation(info.frodez.config.aop.request.ReLock)")
	public void before(JoinPoint point) throws Exception {
		HttpServletRequest request = HttpUtil.getContextRequest();
		ReLock lock = AopMethodUtil.getAnnotation(point, ReLock.class);
		String key = getKey(lock.value(), request);
		if (redisService.exists(key)) {
			throw new RepeatException("重复请求:IP地址" + HttpUtil.getRealAddr(request));
		}
		redisService.set(key, true);
	}

	/**
	 * 在请求后删除redis中key
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@After("@annotation(info.frodez.config.aop.request.ReLock)")
	public void after(JoinPoint point) {
		ReLock lock = AopMethodUtil.getAnnotation(point, ReLock.class);
		redisService.delete(getKey(lock.value(), HttpUtil.getContextRequest()));
	}

	/**
	 * 在请求前判断是否存在正在执行的请求
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@Before("@annotation(info.frodez.config.aop.request.TimeoutLock)")
	public void beforeTimeout(JoinPoint point) throws Exception {
		HttpServletRequest request = HttpUtil.getContextRequest();
		TimeoutLock timeoutLock = AopMethodUtil.getAnnotation(point, TimeoutLock.class);
		String key = getKey(timeoutLock.value(), request);
		if (redisService.exists(key)) {
			throw new RepeatException("重复请求:IP地址" + HttpUtil.getRealAddr(request));
		}
		if (timeoutLock.timeout() <= 0) {
			throw new RuntimeException("超时时间必须大于0!");
		}
		redisService.set(key, true, timeoutLock.timeout());
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
			String authToken = request.getHeader(properties.getJwt().getHeader());
			authToken = authToken == null ? "" : authToken;
			return Redis.Request.NO_REPEAT + value + authToken;
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			String address = HttpUtil.getRealAddr(request);
			return Redis.Request.NO_REPEAT + address;
		}
	}

}
