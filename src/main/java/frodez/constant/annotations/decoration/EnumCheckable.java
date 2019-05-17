package frodez.constant.annotations.decoration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本注解标明的类只针对于枚举类,表明该类可以使用LegalEnum注解来进行验证。<br>
 * <strong>要求:验证的枚举类必须实现一个方法,这个方法在当参数符合要求时,返回对应的枚举,否则返回null.</strong> <br>
 * <strong>要求:验证的枚举类必须实现一个方法,这个方法在当参数不符合要求时,返回枚举所对应的所有值,以字符串形式返回.</strong> <br>
 * 以下是示例代码.<br>
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
 *		<span>@Getter</span>
 *		private static String introduction;
 * 		private static final Map<Byte, UserStatusEnum> enumMap;
 * 		static {
 *			vals = Collections.unmodifiableList(Arrays.asList(UserStatusEnum.values()).stream()
 *				.map(UserStatusEnum::getVal).collect(Collectors.toList()));
 *			descs = Collections.unmodifiableList(Arrays.asList(UserStatusEnum.values()).stream()
 *				.map(UserStatusEnum::getDesc).collect(Collectors.toList()));
 *			StringBuilder builder = new StringBuilder();
 *			for (int i = 0; i < vals.size(); i++) {
 *				builder.append(vals.get(i).toString());
 *				if (i != vals.size() - 1) {
 *					builder.append(",");
 *				}
 *			}
 *			introduction = builder.toString();
 *			enumMap = new HashMap<>();
 *			for (UserStatusEnum iter : UserStatusEnum.values()) {
 *				enumMap.put(iter.val, iter);
 *			}
 *		}
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
 * @see frodez.config.aop.validation.annotation.common.LegalEnum
 * @author Frodez
 * @date 2019-03-18
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface EnumCheckable {

}
