package frodez.config.aop.validation.annotation.common;

import frodez.util.reflect.ReflectUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 枚举类型验证注解 <br>
 * <strong>要求:验证的枚举类必须实现一个方法,这个方法在当参数符合要求时,返回对应的枚举,否则返回null.</strong> <br>
 * 例子:<br>
 * <span>@ValidEnum(message = "状态非法!", type = UserStatusEnum.class, method = "of", nullable = true)</span><br>
 * private Byte status;<br>
 * 以下为注解参数说明:<br>
 * <strong> message: String类型,代表验证失败时的返回信息,默认值为"参数非法!"<br>
 * type: Class类型,代表对应的枚举类.<br>
 * method: String类型,代表验证用的方法,默认值为of.<br>
 * paramType: Class类型,代表验证用方法的参数类型,默认值为byte.class<br>
 * nullable: boolean类型,代表对空值的处理方式,默认值为false.为true时空值可以通过验证,为false时空值不可以通过验证.<br>
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
	 * 错误信息,默认为"参数非法!"
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String message() default "参数非法!";

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

	/**
	 * 是否允许null,默认为false不允许
	 * @author Frodez
	 * @date 2019-04-13
	 */
	boolean nullable() default false;

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
		 * 接受空值,默认值为false true:当为空时,直接通过验证 false:当为空时,拒绝通过验证
		 */
		private boolean nullable;

		/**
		 * 根据注解信息初始化验证器
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public void initialize(LegalEnum enumValue) {
			method = enumValue.method();
			klass = enumValue.type();
			nullable = enumValue.nullable();
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
				return nullable;
			}
			try {
				return ReflectUtil.getFastMethod(klass, method, paramType).invoke(null, new Object[] { ReflectUtil
					.primitiveAdapt(value, paramType) }) != null;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
