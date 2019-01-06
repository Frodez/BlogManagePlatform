package info.frodez.config.aop.request;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import info.frodez.config.security.settings.SecurityProperties;
import info.frodez.constant.redis.Redis;
import info.frodez.service.redis.RedisService;
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
public class NoRepeatAop {

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
	@Before("@annotation(info.frodez.config.aop.request.NoRepeat)")
	public void before(JoinPoint joinPoint) throws Exception {
		HttpServletRequest request =
			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		NoRepeat noRepeat = getNoRepeatAnnotation(joinPoint);
		String key = getKey(noRepeat, request);
		if (redisService.exists(key)) {
			throw new NoRepeatException("重复请求:IP地址" + HttpUtil.getRealAddr(request));
		}
		if (noRepeat.timeout() > 0) {
			// 如果设置了超时时间,则在redis中设置超时时间
			redisService.set(key, true, noRepeat.timeout());
		} else {
			redisService.set(key, true);
		}
	}

	/**
	 * 在请求后删除redis中key
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@After("@annotation(info.frodez.config.aop.request.NoRepeat)")
	public void after(JoinPoint joinPoint) {
		HttpServletRequest request =
			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		NoRepeat noRepeat = getNoRepeatAnnotation(joinPoint);
		if (noRepeat.timeout() <= 0) {
			// 如果设置了超时时间,则不手动删除redis中key
			redisService.delete(getKey(noRepeat, request));
		}
	}

	/**
	 * 从AOP切点获取NoRepeat注解,不存在则抛出RuntimException
	 * @param JoinPoint AOP切点
	 * @author Frodez
	 * @date 2018-12-21
	 */
	private NoRepeat getNoRepeatAnnotation(JoinPoint joinPoint) {
		Annotation[] annotations =
			((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == NoRepeat.class) {
				return (NoRepeat) annotation;
			}
		}
		throw new RuntimeException("获取注解失败!");
	}

	/**
	 * 根据规则获得redis的key
	 * @param NoRepeat 注解
	 * @param HttpServletRequest 请求
	 * @author Frodez
	 * @date 2018-12-21
	 */
	private String getKey(NoRepeat noRepeat, HttpServletRequest request) {
		if (!properties.match(request.getRequestURI())) {
			// 非登录接口使用token判断,同一token不能重复请求
			String authToken = request.getHeader(properties.getJwt().getHeader());
			authToken = authToken == null ? "" : authToken;
			String key = Redis.Request.NO_REPEAT + noRepeat.value() + ":" + authToken;
			return key;
		} else {
			// 登录接口使用IP判断,同一IP不能重复请求
			String address = HttpUtil.getRealAddr(request);
			return Redis.Request.NO_REPEAT + address;
		}
	}

}
