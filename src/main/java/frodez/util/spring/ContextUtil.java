package frodez.util.spring;

import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * spring工具类
 * @author Frodez
 * @date 2018-12-21
 */
@Component("contextUtil")
public class ContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		Assert.notNull(context, "context must not be null");
	}

	/**
	 * 获取项目根路径
	 * @author Frodez
	 * @date 2019-01-13
	 */
	public static String rootPath() {
		return ClassUtils.getDefaultClassLoader().getResource("").getPath();
	}

	/**
	 * 获取spring上下文环境
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static ApplicationContext context() {
		return context;
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	public static <T> T get(Class<T> klass) {
		return context().getBean(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param klass bean的类型
	 * @author Frodez
	 * @param <T>
	 * @date 2018-12-21
	 */
	public static <T> Map<String, T> gets(Class<T> klass) {
		return context().getBeansOfType(klass);
	}

	/**
	 * 使用spring上下文环境获取bean
	 * @param beanName bean的名字
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String beanName, Class<T> klass) {
		return (T) context().getBean(beanName);
	}

}
