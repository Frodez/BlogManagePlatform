package frodez.util.reflect;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.types.ResolvedInterfaceType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * 类型处理工具类
 * @author Frodez
 * @date 2020-01-02
 */
@UtilityClass
public class TypeUtil {

	private static final TypeResolver TYPE_RESOLVER = new TypeResolver();

	/**
	 * 解析类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public ResolvedType resolve(Type type) {
		return TYPE_RESOLVER.resolve(type);
	}

	/**
	 * 判断是否为无泛型类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean isSimpleType(Type type) {
		return isSimpleType(TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为无泛型类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean isSimpleType(ResolvedType type) {
		return type instanceof ResolvedObjectType || type instanceof ResolvedPrimitiveType;
	}

	/**
	 * 判断是否为泛型类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean isComplexType(Type type) {
		return isComplexType(TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为泛型类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean isComplexType(ResolvedType type) {
		return type instanceof ResolvedInterfaceType;
	}

	/**
	 * 判断是否为无泛型的某个类型或者其子类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean belongToSimpleType(Class<?> simpleClass, Type type) {
		return belongToComplexType(simpleClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为无泛型的某个类型或者其子类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean belongToSimpleType(Class<?> simpleClass, ResolvedType type) {
		return type instanceof ResolvedObjectType && simpleClass.isAssignableFrom(type.getErasedType());
	}

	/**
	 * 判断是否为带有泛型的某个类型或者其子类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean belongToComplexType(Class<?> complexClass, Type type) {
		return belongToComplexType(complexClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 判断是否为带有泛型的某个类型或者其子类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public boolean belongToComplexType(Class<?> complexClass, ResolvedType type) {
		return type instanceof ResolvedInterfaceType && complexClass.isAssignableFrom(type.getErasedType());
	}

	/**
	 * 获得具有两个泛型参数的类型的泛型参数具体类型
	 * @return A,B 泛型类型中A的类型和B的类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public List<ResolvedType> resolveGenericType(Class<?> complexClass, Type type) {
		return resolveGenericType(complexClass, TYPE_RESOLVER.resolve(type));
	}

	/**
	 * 获得具有泛型参数的类型的泛型参数具体类型
	 * @return 泛型类型中泛型参数具体类型
	 * @author Frodez
	 * @date 2020-01-02
	 */
	public List<ResolvedType> resolveGenericType(Class<?> complexClass, ResolvedType type) {
		if (type instanceof ResolvedObjectType) {
			return null;
		} else if (type instanceof ResolvedInterfaceType) {
			if (complexClass.isAssignableFrom(type.getErasedType())) {
				return type.getTypeBindings().getTypeParameters();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
