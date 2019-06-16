package frodez.constant.enums.user;

import com.google.common.collect.ImmutableMap;
import frodez.constant.annotations.decoration.EnumCheckable;
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
@AllArgsConstructor
public enum PermissionTypeEnum {

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

	private static final Map<Byte, PermissionTypeEnum> enumMap;

	static {
		vals = Arrays.asList(PermissionTypeEnum.values()).stream().map(PermissionTypeEnum::getVal).collect(Collectors
			.toUnmodifiableList());
		descs = Arrays.asList(PermissionTypeEnum.values()).stream().map(PermissionTypeEnum::getDesc).collect(Collectors
			.toUnmodifiableList());
		var builder = ImmutableMap.<Byte, PermissionTypeEnum>builder();
		for (PermissionTypeEnum iter : PermissionTypeEnum.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static PermissionTypeEnum of(Byte value) {
		return enumMap.get(value);
	}

}
