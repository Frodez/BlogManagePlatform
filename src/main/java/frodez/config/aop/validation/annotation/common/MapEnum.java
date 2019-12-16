package frodez.config.aop.validation.annotation.common;

import frodez.config.validator.ValidationUtil;
import frodez.constant.annotations.info.Description;
import frodez.constant.settings.DefEnum;
import frodez.constant.settings.DefStr;
import frodez.util.common.PrimitiveUtil;
import frodez.util.common.StrUtil;
import frodez.util.common.StreamUtil;
import frodez.util.reflect.ReflectUtil;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.reflect.FastMethod;
import springfox.documentation.service.AllowableListValues;

/**
 * 枚举类型映射注解 <br>
 * <strong>将枚举类型同相应映射类型的对象进行映射,并可以验证正确性。</strong><br>
 * <strong>对枚举类型相应的映射类型的参数用本注解标注,可以自动生成swagger文档。作用类似于@ApiParam。</strong>
 * <strong>建议:枚举类上增加@EnumCheckable注解,以增强可读性</strong><br>
 * <strong>枚举类示例代码请参见@EnumCheckable注解的javadoc.</strong><br>
 * 注解使用范例:<br>
 * <code>
 * &#64;MapEnum(message = "状态非法!", value = UserStatusEnum.class, method = "of", descMethod = "getDescs")<br>
 * private Byte status;
 * </code><br>
 * 以下为注解参数说明:<br>
 * message: String类型,代表验证失败时的返回信息<br>
 * type: Class类型,代表对应的枚举类.<br>
 * method: String类型,代表验证用的方法,默认值为of.<br>
 * descMethod: String类型,代表获取枚举所有描述的方法,默认值为getDescs.<br>
 * paramType: Class类型,代表验证用方法的参数类型,默认值为Byte.class.注意,装箱类类型和原类型不能混用!<br>
 * @see frodez.constant.annotations.decoration.EnumCheckable
 * @author Frodez
 * @date 2018-12-03
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MapEnum.Validator.class)
public @interface MapEnum {

	String message() default "";

	/**
	 * 适用的枚举类
	 * @author Frodez
	 * @date 2019-04-13
	 */
	Class<? extends Enum<?>> value();

	/**
	 * 验证用方法名,默认为of。该方法必须只接受除自己外的一个方法参数，并且在适用的枚举类中存在且可用
	 * @author Frodez
	 * @date 2019-04-13
	 */
	String method() default DefEnum.VALIDATE_METHOD_NAME;

	/**
	 * 获取所有枚举描述的方法名,默认为getDescs。
	 * @author Frodez
	 * @date 2019-12-08
	 */
	String descMethod() default DefEnum.DESC_METHOD_NAME;

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
	 * MapEnumHelper工具类,仅供MapEnum相关功能使用!!!
	 * @author Frodez
	 * @date 2019-12-15
	 */
	@UtilityClass
	public static class MapEnumHelper {

		@SneakyThrows
		public static String getDescs(Class<?> klass, String descMethod) {
			FastMethod method = ReflectUtil.getFastMethod(klass, descMethod);
			Object descs = method.invoke(null, ReflectUtil.EMPTY_ARRAY);
			String result = String.join(" ", getName(klass), descs.toString());
			return result;
		}

		public static String getName(Class<?> klass) {
			Description description = klass.getAnnotation(Description.class);
			return description == null ? DefStr.EMPTY : description.name();
		}

		@SneakyThrows
		public static AllowableListValues getAllowableValues(Class<?> klass) {
			FastMethod method = ReflectUtil.getFastMethod(klass, DefEnum.VALS_METHOD_NAME);
			List<?> object = (List<?>) method.invoke(null, ReflectUtil.EMPTY_ARRAY);
			String type = object.get(0).getClass().getSimpleName();
			return new AllowableListValues(StreamUtil.list(object, (item) -> item.toString()), type);
		}

		@SneakyThrows
		public static Object getDefaultValue(Class<?> klass) {
			FastMethod method = ReflectUtil.getFastMethod(klass, DefEnum.DEFAULT_VALUE_METHOD_NAME);
			return method.invoke(null, ReflectUtil.EMPTY_ARRAY);
		}

	}

	/**
	 * 枚举验证器
	 * @author Frodez
	 * @date 2018-12-17
	 */
	class Validator implements ConstraintValidator<MapEnum, Object> {

		/**
		 * 枚举类
		 */
		private Class<? extends Enum<?>> klass;

		/**
		 * 验证方法,默认值为"of"
		 */
		private String method;

		/**
		 * 获取所有枚举描述的方法名,默认为getDescs。
		 */
		private String descMethod;

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
		public void initialize(MapEnum enumValue) {
			method = enumValue.method();
			klass = enumValue.value();
			paramType = enumValue.paramType();
			descMethod = enumValue.descMethod();
		}

		/**
		 * 验证
		 * @author Frodez
		 * @date 2018-12-17
		 */
		@Override
		@SneakyThrows
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			if (value == null) {
				//对于非空检查的情况,请继续使用@NotNull注解
				return true;
			}
			Object[] params = new Object[] { PrimitiveUtil.cast(value, paramType) };
			if (ReflectUtil.getFastMethod(klass, method, paramType).invoke(null, params) != null) {
				return true;
			} else {
				Object valids = ReflectUtil.getFastMethod(klass, descMethod).invoke(null, ReflectUtil.EMPTY_ARRAY);
				String message = StrUtil.concat(value.toString(), "不符合要求,有效值为", valids.toString());
				ValidationUtil.changeMessage(context, message);
				return false;
			}
		}

	}

}
