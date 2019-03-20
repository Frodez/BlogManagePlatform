package frodez.util.spring;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * springMVC相关工具类
 * @author Frodez
 * @date 2019-03-19
 */
@UtilityClass
public class MVCUtil {

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
	 * 使用spring上下文环境获取所有端点,按请求方式分类(同一端点可能有多种请求方式)
	 * @author Frodez
	 * @date 2019-03-16
	 */
	public static Map<RequestMethod, List<RequestMappingInfo>> endPoints() {
		Map<RequestMethod, List<RequestMappingInfo>> endPoints = new EnumMap<>(RequestMethod.class);
		for (RequestMethod method : RequestMethod.values()) {
			endPoints.put(method, BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(),
				HandlerMapping.class, true, false).values().stream().filter((iter) -> {
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
