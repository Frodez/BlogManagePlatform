package frodez.config.mybatis.result;

import frodez.config.mybatis.result.CustomHandler.CustomHandlerContext;
import frodez.util.reflect.ReflectUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.stereotype.Component;

/**
 * 自定义CustomHandler拦截器
 * @author Frodez
 * @date 2019-12-30
 */
@Slf4j
@Component
public class CustomHandlerInterceptor implements Interceptor {

	private Interceptor interceptor = this;

	private Class<?>[] interfaces = new Class<?>[] { Executor.class };

	@Override
	public Object plugin(Object target) {
		//只拦截Executor对象，减少目标被代理的次数
		if (target instanceof Executor) {
			return Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaces, (InvocationHandler) (proxy, method, args) -> {
				if (method.getName().equals("query")) {
					return interceptor.intercept(new Invocation(target, method, args));
				}
				return method.invoke(target, args);
			});
		} else {
			return target;
		}
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameter = args[1];
		if (args[3] != null) {
			return invocation.proceed();
		}
		//用自定义handler
		CustomHandler handler = HandlerResolver.resolve(ms, parameter);
		if (handler == null) {
			return invocation.proceed();
		}
		args[3] = handler;
		invocation.proceed();
		return handler.getResult();
	}

	private static class HandlerResolver {

		/**
		 * 不配置CustomHandler的方法id
		 */
		private static Set<String> noCustomes = new HashSet<>();

		/**
		 * 方法id--CustomHandler类型
		 */
		private static Map<String, Class<? extends CustomHandler>> cache = new ConcurrentHashMap<>();

		/**
		 * 方法id--对应CustomHandler的Context
		 */
		private static Map<String, CustomHandlerContext> contextCache = new ConcurrentHashMap<>();

		/**
		 * 寻找合适的CustomHandler
		 * @author Frodez
		 * @date 2019-12-30
		 */
		public static CustomHandler resolve(MappedStatement ms, Object parameter) {
			String id = ms.getId();
			if (noCustomes.contains(id)) {
				//如果在未设置的名单里，直接返回
				return null;
			}
			Class<? extends CustomHandler> handler = cache.get(id);
			if (handler != null) {
				return resolveExist(id, handler);
			}
			Method method = resolveMethod(id, parameter);
			if (method == null) {
				//如果未找到method,则纳入未设置的名单，直接返回null
				return resolveNo(id);
			}
			//优先判断方法，再判断返回值类型
			CustomResultHandler annotation = method.getAnnotation(CustomResultHandler.class);
			if (annotation != null) {
				return resolveNew(id, method, ms, annotation);
			}
			//如果未找到handler,则纳入未设置的名单，直接返回null
			return resolveNo(id);
		}

		/**
		 * 找到目前执行的mapper方法
		 * @author Frodez
		 * @date 2019-12-27
		 */
		@SneakyThrows
		private static Method resolveMethod(String id, Object parameter) {
			int index = id.lastIndexOf(".");
			String className = id.substring(0, index);
			String methodName = id.substring(index + 1, id.length());//要去掉那个.
			Class<?> klass = Class.forName(className);
			List<Method> matches = new ArrayList<>();
			for (Method method : klass.getMethods()) {
				if (method.getName().equals(methodName)) {
					matches.add(method);
				}
			}
			if (matches.size() == 0) {
				return null;
			} else if (matches.size() == 1) {
				return matches.get(0);
			} else {
				log.warn("不建议在{}类中存在多个同名方法{},暂时不能使用自定义CustomResultHandler注解处理", klass.getCanonicalName(), methodName);
				return null;
			}
		}

		private static CustomHandler resolveNo(String id) {
			noCustomes.add(id);
			return null;
		}

		private static CustomHandler resolveExist(String id, Class<? extends CustomHandler> handler) {
			//每次都必须是一个新对象
			CustomHandler instance = ReflectUtil.instance(handler);
			//设置已有的上下文
			instance.setContext(contextCache.get(id));
			return instance;
		}

		private static CustomHandler resolveNew(String id, Method method, MappedStatement ms, CustomResultHandler annotation) {
			Class<? extends CustomHandler> instanceClass = annotation.value();
			//每次都必须是一个新对象
			CustomHandler instance = ReflectUtil.instance(instanceClass);
			List<ResultMap> resultMaps = ms.getResultMaps();
			if (!instance.support(resultMaps)) {
				//如果不支持
				noCustomes.add(id);
				String string = resultMaps.stream().map((item) -> item.getType().getCanonicalName()).collect(Collectors.joining(","));
				log.warn("{}方法的返回值类型{}不被{}所支持,将不会使用该CustomHandler", id, string, instanceClass.getCanonicalName());
				return null;
			}
			cache.put(id, instanceClass);
			//在需要返回结果的情况下,需要记录下返回值类型
			CustomHandlerContext context = instance.resolveContext(method, ms.getConfiguration());
			contextCache.put(id, context);
			instance.setContext(context);
			return instance;
		}

	}

}
