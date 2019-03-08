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

	private static MethodSignature methodSignature(final JoinPoint point) {
		return MethodSignature.class.cast(point.getSignature());
	}

	/**
	 * 获取织入方法
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static Method method(final JoinPoint point) {
		return methodSignature(point).getMethod();
	}

	/**
	 * 获取织入点的方法名称
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static String methodName(final JoinPoint point) {
		return methodSignature(point).getMethod().getName();
	}

	/**
	 * 获取织入点的方法全限定名
	 * @author Frodez
	 * @date 2019-01-12
	 */
	public static String fullMethodName(final JoinPoint point) {
		Method method = methodSignature(point).getMethod();
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 获取织入点的方法指定类型注解
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static <T extends Annotation> T annotation(final JoinPoint point, final Class<T> klass) {
		return methodSignature(point).getMethod().getAnnotation(klass);
	}

	/**
	 * 获取织入点的方法参数
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static Parameter[] params(final JoinPoint point) {
		return methodSignature(point).getMethod().getParameters();
	}

}
