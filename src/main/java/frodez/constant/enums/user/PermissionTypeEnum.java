package frodez.constant.enums.user;

import frodez.constant.annotations.decoration.EnumCheckable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

	/**
	 * 介绍
	 */
	@Getter
	private static String introduction;

	private static final Map<Byte, PermissionTypeEnum> enumMap;

	static {
		vals = Collections.unmodifiableList(Arrays.asList(PermissionTypeEnum.values()).stream().map(
			PermissionTypeEnum::getVal).collect(Collectors.toList()));
		descs = Collections.unmodifiableList(Arrays.asList(PermissionTypeEnum.values()).stream().map(
			PermissionTypeEnum::getDesc).collect(Collectors.toList()));
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < vals.size(); i++) {
			builder.append(vals.get(i).toString());
			if (i != vals.size() - 1) {
				builder.append(",");
			}
		}
		introduction = builder.toString();
		enumMap = new HashMap<>();
		for (PermissionTypeEnum iter : PermissionTypeEnum.values()) {
			enumMap.put(iter.val, iter);
		}
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
