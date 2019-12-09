package frodez.constant.annotations.decoration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本注解标明的类只针对于枚举类,表明该类可以使用MapEnum注解来进行验证。<br>
 * <strong>1.要求:验证的枚举类必须实现一个方法(方法名建议为of,如果有变动,请在使用MapEnum注解时显式配置),<br>
 * 这个方法在当参数符合要求时,返回对应的枚举,否则返回null.<br>
 * 2.要求:验证的枚举类必须实现一个方法(方法名建议为getDescs,如果有变动,请在使用MapEnum注解时显式配置),<br>
 * 这个方法在当参数不符合要求时,返回枚举所对应的所有值,以List形式返回.</strong> <br>
 * 以下是示例代码(这里的getDescs方法由lombok自动生成,表现为descs上有@Getter注解).<br>
 *
 * <pre>
 * 	<span>@EnumCheckable</span>
 * 	<span>@AllArgsConstructor</span>
 * 	public enum UserStatusEnum {
 * 		FORBIDDEN((byte) 0, "禁用"),
 * 		NORMAL((byte) 1, "正常");
 * 		<span>@Getter</span>
 *		private byte val;
 *		<span>@Getter</span>
 *		private String desc;
 *		<span>@Getter</span>
 *		private static List vals;
 *		<span>@Getter</span>
 *		private static List descs;
 * 		private static final Map<Byte, UserStatusEnum> enumMap;
 * 		static {
 *			vals = Arrays.stream(UserStatusEnum.values()).map(UserStatusEnum::getVal).collect(Collectors.toUnmodifiableList());
 *			descs = Arrays.stream(UserStatusEnum.values()).map((iter) -> {
 *				return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
 *			}).collect(Collectors.toUnmodifiableList());
 *			var builder = ImmutableMap.<Byte, UserStatusEnum>builder();
 *			for (UserStatusEnum iter : UserStatusEnum.values()) {
 *				builder.put(iter.val, iter);
 *			}
 *			enumMap = builder.build();
 *		}
 * 		public UserStatusEnum of(byte value) {
 * 			return enumMap.get(value);
 * 		}
 * 	}
 * </pre>
 *
 * @see frodez.config.aop.validation.annotation.common.MapEnum
 * @author Frodez
 * @date 2019-03-18
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumCheckable {

}
