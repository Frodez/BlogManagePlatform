package frodez.config.aop.validation.annotation.common;

import frodez.util.constant.setting.DefDesc;
import frodez.util.reflect.ReflectUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 枚举类型验证注解 <br>
 * <strong>对于非空检查的情况,请继续使用@NotNull注解</strong><br>
 * <strong>要求:验证的枚举类必须实现一个方法,这个方法在当参数符合要求时,返回对应的枚举,否则返回null.</strong> <br>
 * 例子:<br>
 * <span>@ValidEnum(message = "状态非法!", type = UserStatusEnum.class, method = "of", nullable = true)</span><br>
 * private Byte status;<br>
 * 以下为注解参数说明:<br>
 * <strong> message: String类型,代表验证失败时的返回信息<br>
 * type: Class类型,代表对应的枚举类.<br>
 * method: String类型,代表验证用的方法,默认值为of.<br>
 * paramType: Class类型,代表验证用方法的参数类型,默认值为byte.class<br>
 * </strong> 以下是枚举类代码.<br>
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
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalEnum.Validator.class)
public @interface LegalEnum {

	/**
	 * 错误信息
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String message() default DefDesc.Warn.ILLEGAL_PARAM_WARN;

	/**
	 * 适用的枚举类
	 * @author Frodez
	 * @date 2019-04-13
	 */
	Class<? extends Enum<?>> type();

	/**
	 * 验证用方法名,默认为of。该方法必须只接受除自己外的一个方法参数，并且在适用的枚举类中存在且可用
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String method() default "of";

	/**
	 * 验证用方法参数的类型,默认为byte.class
	 * @author Frodez
	 * @date 2019-04-13
	 */
	Class<?> paramType() default byte.class;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 枚举验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<LegalEnum, Object> {

		/**
		 * 枚举类
		 */
		private Class<? extends Enum<?>> klass;

		/**
		 * 验证方法,默认值为"of"
		 */
		private String method;

		/**
		 * 验证方法参数类型,默认值为byte.class
		 */
		private Class<?> paramType;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(LegalEnum enumValue) {
			method = enumValue.method();
			klass = enumValue.type();
			paramType = enumValue.paramType();
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				//对于非空检查的情况,请继续使用@NotNull注解
				return true;
			}
			try {
				return ReflectUtil.getFastMethod(klass, method, paramType).invoke(null, new Object[] { ReflectUtil
					.primitiveAdapt(value, paramType) }) != null;
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
