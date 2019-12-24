package frodez.constant.enums.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.annotations.info.Description;
import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态枚举
 * @author Frodez
 * @date 2018-12-04
 */
@EnumCheckable
@Description(name = "删除状态枚举")
@AllArgsConstructor
public enum DeleteStatus {

	/**
	 * 未删除
	 */
	NO((byte) 1, "未删除"),
	/**
	 * 已删除
	 */
	YES((byte) 2, "已删除");

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

	private static final Map<Byte, DeleteStatus> enumMap;

	static {
		vals = new ArrayList<>();
		vals = Arrays.stream(DeleteStatus.values()).map(DeleteStatus::getVal).collect(Collectors.toUnmodifiableList());
		descs = Arrays.stream(DeleteStatus.values()).map((iter) -> {
			return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		Builder<Byte, DeleteStatus> builder = ImmutableMap.<Byte, DeleteStatus>builder();
		for (DeleteStatus iter : DeleteStatus.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static DeleteStatus of(Byte value) {
		return enumMap.get(value);
	}

	/**
	 * 默认枚举
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static DeleteStatus defaultEnum() {
		return DeleteStatus.NO;
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
