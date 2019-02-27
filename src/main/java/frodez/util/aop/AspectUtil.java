package frodez.util.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * AOP方法织入工具类
 * @author Frodez
 * @date 2019-01-09
 */
public class AspectUtil {

	private static MethodSignature getMethodSignature(JoinPoint point) {
		return MethodSignature.class.cast(point.getSignature());
	}

	/**
	 * 获取织入方法
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static Method getMethod(JoinPoint point) {
		return getMethodSignature(point).getMethod();
	}

	/**
	 * 获取织入点的方法名称
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static String getName(JoinPoint point) {
		return getMethodSignature(point).getMethod().getName();
	}

	/**
	 * 获取织入点的方法全限定名
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static String getFullName(JoinPoint point) {
		Method method = getMethodSignature(point).getMethod();
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 获取织入点的方法指定类型注解
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static <T extends Annotation> T getAnnotation(JoinPoint point, Class<T> klass) {
		return getMethodSignature(point).getMethod().getAnnotation(klass);
	}

	/**
	 * 获取织入点的方法参数
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static Parameter[] getParams(JoinPoint point) {
		return getMethodSignature(point).getMethod().getParameters();
	}

}
