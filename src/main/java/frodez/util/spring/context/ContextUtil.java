package frodez.util.spring.context;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
		if (context == null) {
			throw new RuntimeException("获取spring上下文环境失败!");
		}
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
	 * 从spring上下文中获取HttpServletResponse
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static HttpServletResponse response() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 从spring上下文中获取HttpServletRequest
	 * @author Frodez
	 * @date 2019-01-09
	 */
	public static HttpServletRequest request() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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
	 * @param beanName bean的名字
	 * @param klass bean的类型
	 * @author Frodez
	 * @date 2018-12-21
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String beanName, Class<T> klass) {
		return (T) context().getBean(beanName);
	}

	/**
	 * 使用spring上下文环境获取所有端点,按请求方式分类(同一端点可能有多种请求方式)
	 * @author Frodez
	 * @date 2019-03-16
	 */
	public static Map<RequestMethod, List<RequestMappingInfo>> getAllEndPoints() {
		Map<RequestMethod, List<RequestMappingInfo>> endPoints = new EnumMap<>(RequestMethod.class);
		for (RequestMethod method : RequestMethod.values()) {
			endPoints.put(method, BeanFactoryUtils.beansOfTypeIncludingAncestors(context(), HandlerMapping.class, true,
				false).values().stream().filter((iter) -> {
					return iter instanceof RequestMappingHandlerMapping;
				}).map((iter) -> {
					return ((RequestMappingHandlerMapping) iter).getHandlerMethods().keySet();
				}).flatMap(Collection::stream).filter((iter) -> {
					return iter.getMethodsCondition().getMethods().contains(method);
				}).collect(Collectors.toList()));
		}
		return endPoints;
	}

}
