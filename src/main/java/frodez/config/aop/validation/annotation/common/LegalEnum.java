package frodez.config.aop.validation.annotation.common;

import frodez.util.common.StrUtil;
import frodez.util.common.ValidationUtil;
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
 * <strong>建议:枚举类上增加@EnumCheckable注解,以增强可读性</strong><br>
 * <strong>枚举类示例代码请参见@EnumCheckable注解的javadoc.</strong><br>
 * 注解使用范例:<br>
 * <span>@ValidEnum(message = "状态非法!", type = UserStatusEnum.class, method = "of", valuesMethod = "getVals")</span><br>
 * private Byte status;<br>
 * 以下为注解参数说明:<br>
 * message: String类型,代表验证失败时的返回信息<br>
 * type: Class类型,代表对应的枚举类.<br>
 * method: String类型,代表验证用的方法,默认值为of.<br>
 * valuesMethod: String类型,代表获取枚举所有值的方法,默认值为getVals.<br>
 * paramType: Class类型,代表验证用方法的参数类型,默认值为Byte.class.注意,装箱类类型和原类型不能混用!<br>
 * @see frodez.constant.annotations.decoration.EnumCheckable
 * @author Frodez
 * @date 2018-12-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalEnum.Validator.class)
public @interface LegalEnum {

	String message() default "";

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
	 * 获取枚举所有值的方法名,默认为getVals。
	 * @author Frodez
	 * @date 2019-05-17
	 */
	String valuesMethod() default "getVals";

	/**
	 * 验证用方法参数的类型,默认为Byte.class<br>
	 * <strong>装箱类类型和原类型不能混用!</strong><br>
	 * @author Frodez
	 * @date 2019-04-13
	 */
	Class<?> paramType() default Byte.class;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 枚举验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<LegalEnum, Object> {

		private static final Object[] NULLPARAM_OBJECTS = new Object[] { null };

		/**
		 * 枚举类
		 */
		private Class<? extends Enum<?>> klass;

		/**
		 * 验证方法,默认值为"of"
		 */
		private String method;

		/**
		 * 获取枚举所有值的方法名,默认为getVals。
		 */
		private String valuesMethod;

		/**
		 * 验证方法参数类型,默认值为Byte.class
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
			valuesMethod = enumValue.valuesMethod();
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				//对于非空检查的情况,请继续使用@NotNull注解
				return true;
			}
			try {
				if (ReflectUtil.getFastMethod(klass, method, paramType).invoke(null, new Object[] { ReflectUtil
					.primitiveAdapt(value, paramType) }) != null) {
					return true;
				} else {
					ValidationUtil.changeMessage(context, StrUtil.concat("${validatedValue}不符合要求,有效值为", ReflectUtil
						.getFastMethod(klass, valuesMethod).invoke(null, NULLPARAM_OBJECTS).toString()));
					return false;
				}
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
