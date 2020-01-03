package frodez.constant.enums.permission;

import com.google.common.collect.ImmutableMap;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.annotations.info.Description;
import frodez.constant.settings.DefStr;
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
 * @date 2018-11-14
 */
@EnumCheckable
@Description(name = "权限类型枚举")
@AllArgsConstructor
public enum PermissionType {

	/**
	 * 0:菜单权限
	 */
	MENU((byte) 0, "菜单权限"),
	/**
	 * 1:标签权限
	 */
	TAG((byte) 1, "标签权限");

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
			return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		var builder = ImmutableMap.<Byte, PermissionType>builder();
		for (PermissionType iter : PermissionType.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	public static PermissionType of(Byte value) {
		return enumMap.get(value);
	}

	/**
	 * 默认枚举
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static PermissionType defaultEnum() {
		return null;
	}

	/**
	 * 默认值
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static Byte defaultValue() {
		return null;
	}

}
