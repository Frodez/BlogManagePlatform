package frodez.util.reflect.tool;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectASMUtil {

	@SuppressWarnings("rawtypes")
	private static final Map<String, ConstructorAccess> constructorCache = new ConcurrentHashMap<>();

	private static final Map<String, MethodAccess> methodCache = new ConcurrentHashMap<>();

	private static final Map<String, FieldAccess> fieldCache = new ConcurrentHashMap<>();

	/**
	 * 高效获取某类实例
	 * @author Frodez
	 * @date 2019-01-16
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> klass) {
		if (constructorCache.containsKey(klass.getName())) {
			return (T) constructorCache.get(klass.getName()).newInstance();
		} else {
			ConstructorAccess<T> constructor = ConstructorAccess.get(klass);
			constructorCache.put(klass.getName(), constructor);
			return constructor.newInstance();
		}
	}

	/**
	 * 获取某类方法
	 * @author Frodez
	 * @date 2019-01-22
	 */
	public static <T> MethodAccess GetMethods(Class<T> klass) {
		if (methodCache.containsKey(klass.getName())) {
			return methodCache.get(klass.getName());
		} else {
			MethodAccess methodAccess = MethodAccess.get(klass);
			methodCache.put(klass.getName(), methodAccess);
			return methodAccess;
		}
	}

	/**
	 * 获取某类成员
	 * @author Frodez
	 * @date 2019-01-22
	 */
	public static <T> FieldAccess getFields(Class<T> klass) {
		if (fieldCache.containsKey(klass.getName())) {
			FieldAccess fieldAccess = fieldCache.get(klass.getName());
			return fieldAccess;
		} else {
			FieldAccess fieldAccess = FieldAccess.get(klass);
			fieldCache.put(klass.getName(), fieldAccess);
			return fieldAccess;
		}
	}

}
