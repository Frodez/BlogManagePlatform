package frodez.constant.enums.user;

import com.google.common.collect.ImmutableMap;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.annotations.info.Description;
import frodez.util.common.StrUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限类型枚举
 * @author Frodez
 * @date 2018-12-04
 */
@EnumCheckable
@Description(name = "权限类型枚举")
@AllArgsConstructor
public enum PermissionType {

	/**
	 * 0:所有类型请求
	 */
	ALL((byte) 0, "所有类型请求"),
	/**
	 * 1:GET类型请求
	 */
	GET((byte) 1, "GET类型请求"),
	/**
	 * 2:POST类型请求
	 */
	POST((byte) 2, "POST类型请求"),
	/**
	 * 3:DELETE类型请求
	 */
	DELETE((byte) 3, "DELETE类型请求"),
	/**
	 * 4:PUT类型请求
	 */
	PUT((byte) 4, "PUT类型请求");

	/**
	 * 值
	 */
	@Getter
	private Byte val;

	/**
	 * 描述
	 */
	@Getter
	private String desc;

	/**
	 * 所有值
	 */
	@Getter
	private static List<Byte> vals;

	/**
	 * 所有描述
	 */
	@Getter
	private static List<String> descs;

	private static final Map<Byte, PermissionType> enumMap;

	static {
		vals = Arrays.stream(PermissionType.values()).map(PermissionType::getVal).collect(Collectors.toUnmodifiableList());
		descs = Arrays.stream(PermissionType.values()).map((iter) -> {
			return StrUtil.concat(iter.val.toString(), ":", iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		var builder = ImmutableMap.<Byte, PermissionType>builder();
		for (PermissionType iter : PermissionType.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static PermissionType of(Byte value) {
		return enumMap.get(value);
	}

	/**
	 * 默认枚举
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static PermissionType defaultEnum() {
		return PermissionType.ALL;
	}

	/**
	 * 默认值
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static Byte defaultValue() {
		return defaultEnum().val;
	}

}
