package frodez.util.spring.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * spring工具类
 * @author Frodez
 * @date 2018-12-21
 */
@Component
public class ContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * 获取项目根路径
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String getRootPath() {
		return ClassUtils.getDefaultClassLoader().getResource("").getPath();
	}

	/**
	 * 从spring上下文中获取HttpServletResponse
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static HttpServletResponse getResponse() {
		return ServletRequestAttributes.class.cast(RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 从spring上下文中获取HttpServletRequest
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static HttpServletRequest getRequest() {
		return ServletRequestAttributes.class.cast(RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取spring上下文环境
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static ApplicationContext get() {
		if (context == null) {
			throw new RuntimeException("获取spring上下文环境失败!");
		}
		return context;
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static <T> T getBean(Class<T> klass) {
		return get().getBean(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param beanName bean的名字
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static <T> T getBean(String beanName, Class<T> klass) {
		return klass.cast(get().getBean(beanName));
	}

}
