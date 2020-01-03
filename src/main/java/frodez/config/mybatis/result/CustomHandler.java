package frodez.config.mybatis.result;

import frodez.util.reflect.BeanUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;

/**
 * 自定义ResultHandler<br>
 * 目的在于不要使用默认的ResultHandler。
 * @author Frodez
 * @date 2019-12-27
 */
public interface CustomHandler extends ResultHandler<Object> {

	/**
	 * 是否支持本方法的返回值类型。
	 * @author Frodez
	 * @date 2019-12-28
	 */
	boolean support(List<ResultMap> resultMaps);

	/**
	 * 获取返回值<br>
	 * <strong>因为PageHelper插件中返回值首先被解析为List，所以这里必须返回List</strong><br>
	 * 如果没有PageHelper插件,请自己按需修改。<br>
	 * @see com.github.pagehelper.PageInterceptor#intercept(org.apache.ibatis.plugin.Invocation)
	 * @author Frodez
	 * @date 2019-12-28
	 */
	List<?> getResult();

	/**
	 * 设置上下文
	 * @author Frodez
	 * @date 2019-12-28
	 */
	void setContext(CustomHandlerContext context);

	/**
	 * 获取本Handler在本Method下的上下文
	 * @author Frodez
	 * @date 2019-12-27
	 */
	CustomHandlerContext resolveContext(Method method, Configuration configuration);

	/**
	 * 上下文接口
	 * @author Frodez
	 * @date 2019-12-28
	 */
	public interface CustomHandlerContext {

	}

	/**
	 * 类型转换上下文
	 * @author Frodez
	 * @date 2019-12-28
	 */
	public static class TypeCondition {

		private Class<?> klass;

		private boolean isResolved = false;

		private TypeCondition(Class<?> klass, boolean isResolved) {
			this.klass = klass;
			this.isResolved = isResolved;
		}

		public static TypeCondition generate(Class<?> klass, Configuration configuration) {
			boolean isResolved = configuration.getTypeHandlerRegistry().hasTypeHandler(klass);
			TypeCondition condition = new TypeCondition(klass, isResolved);
			return condition;
		}

		@SuppressWarnings("unchecked")
		public Object resolve(Object object) {
			if (Map.class.isAssignableFrom(object.getClass())) {
				if (isResolved) {
					return ((Map<String, Object>) object).values().iterator().next();
				} else {
					return BeanUtil.as((Map<String, Object>) object, this.klass);
				}
			} else {
				return object;
			}
		}

	}

}
