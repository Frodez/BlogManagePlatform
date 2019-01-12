package info.frodez.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 枚举类型验证注解
 * <br>
 * <strong>要求:验证的枚举类必须实现一个方法,这个方法在当参数符合要求时,返回对应的枚举,否则返回null.</strong>
 * <br>
 * 例子:<br>
 * <span>@ValidEnum(message = "状态非法!", type = UserStatusEnum.class, method =
 * "of", nullable = true)</span><br>
 * private Byte status;<br>
 * 以下为注解参数说明:<br>
 * <strong>
 * message: String类型,代表验证失败时的返回信息,默认值为"参数非法!"<br>
 * type: Class类型,代表对应的枚举类,<br>
 * method: String类型,代表验证用的方法,默认值为of,<br>
 * nullable: boolean类型,代表对空值的处理方式,默认值为false.为true时空值可以通过验证,为false时空值不可以通过验证.<br>
 * </strong>
 * 以下是枚举类代码.<br>
 *
 * <pre>
 * 	<span>@Getter</span>
 * 	<span>@AllArgsConstructor</span>
 * 	public enum UserStatusEnum {
 * 		FORBIDDEN((byte) 0, "禁用"),
 * 		NORMAL((byte) 1, "正常");
 * 		private byte value;
 * 		private String description;
 * 		public UserStatusEnum of(byte value) {
 * 			for(UserStatusEnum iter : UserStatusEnum.values()) {
 * 				if(iter.value == value) {
 * 					return iter;
 * 				}
 * 			}
 * 			return null;
 * 		}
 * 	}
 * </pre>
 *
 * @author Frodez
 * @date 2018-12-03
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEnum.Validator.class)
public @interface ValidEnum {

	// 错误信息
	String message() default "参数非法!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	// 枚举类
	Class<? extends Enum<?>> type();

	// 验证方法
	String method() default "of";

	// 是否可为空
	boolean nullable() default false;

	public static final Class<Validator> by = ValidEnum.Validator.class;

	/**
	 * 枚举验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<ValidEnum, Object> {

		/**
		 * 枚举类
		 */
		private Class<? extends Enum<?>> klass;

		/**
		 * 验证方法,默认值为"of"
		 */
		private String method;

		/**
		 * 接受空值,默认值为false true:当为空时,直接通过验证 false:当为空时,拒绝通过验证
		 */
		private boolean nullable;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(ValidEnum enumValue) {
			method = enumValue.method();
			klass = enumValue.type();
			nullable = enumValue.nullable();
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				return nullable;
			}
			Method m = getMethod(klass, method);
			Class<?> parameterClass = m.getParameterTypes()[0];
			try {
				return m.invoke(null, castValue(parameterClass, value)) != null;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 根据方法名获取方法
		 * @author Frodez
		 * @date 2018-12-17
		 */
		private static Method getMethod(Class<? extends Enum<?>> klass, String method)
			throws RuntimeException {
			Method[] methods = klass.getMethods();
			for (Method m : methods) {
				if (m.getName().equals(method)) {
					return m;
				}
			}
			throw new RuntimeException("验证方法不存在!");
		}

		/**
		 * 基本数据类型转换<br>
		 * 涉及类型:byte, short, int, long以及对应装箱类
		 * @author Frodez
		 * @date 2018-12-17
		 */
		private static Object castValue(Class<?> parameterClass, Object value) {
			Class<?> valueClass = value.getClass();
			if (valueClass == byte.class || valueClass == Byte.class) {
				return castByteValue(parameterClass, (Byte) value);
			}
			if (valueClass == short.class || valueClass == Short.class) {
				return castShortValue(parameterClass, (Short) value);
			}
			if (valueClass == int.class || valueClass == Integer.class) {
				return castIntValue(parameterClass, (Integer) value);
			}
			if (valueClass == long.class || valueClass == Long.class) {
				return castLongValue(parameterClass, (Long) value);
			}
			return value;
		}

		private static Object castByteValue(Class<?> parameterClass, Byte value) {
			if (parameterClass == byte.class || parameterClass == Byte.class) {
				return value;
			}
			if (parameterClass == short.class || parameterClass == Short.class) {
				return value.shortValue();
			}
			if (parameterClass == int.class || parameterClass == Integer.class) {
				return value.intValue();
			}
			if (parameterClass == long.class || parameterClass == Long.class) {
				return value.longValue();
			}
			return value;
		}

		private static Object castShortValue(Class<?> parameterClass, Short value) {
			if (parameterClass == byte.class || parameterClass == Byte.class) {
				return value.byteValue();
			}
			if (parameterClass == short.class || parameterClass == Short.class) {
				return value;
			}
			if (parameterClass == int.class || parameterClass == Integer.class) {
				return value.intValue();
			}
			if (parameterClass == long.class || parameterClass == Long.class) {
				return value.longValue();
			}
			return value;
		}

		private static Object castIntValue(Class<?> parameterClass, Integer value) {
			if (parameterClass == byte.class || parameterClass == Byte.class) {
				return value.byteValue();
			}
			if (parameterClass == short.class || parameterClass == Short.class) {
				return value.shortValue();
			}
			if (parameterClass == int.class || parameterClass == Integer.class) {
				return value;
			}
			if (parameterClass == long.class || parameterClass == Long.class) {
				return value.longValue();
			}
			return value;
		}

		private static Object castLongValue(Class<?> parameterClass, Long value) {
			if (parameterClass == byte.class || parameterClass == Byte.class) {
				return value.byteValue();
			}
			if (parameterClass == short.class || parameterClass == Short.class) {
				return value.shortValue();
			}
			if (parameterClass == int.class || parameterClass == Integer.class) {
				return value.intValue();
			}
			if (parameterClass == long.class || parameterClass == Long.class) {
				return value;
			}
			return value;
		}

	}

}
