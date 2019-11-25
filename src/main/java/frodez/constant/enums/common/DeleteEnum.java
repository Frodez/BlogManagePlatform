package frodez.constant.enums.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import frodez.constant.annotations.decoration.EnumCheckable;
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
@AllArgsConstructor
public enum DeleteEnum {

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

	private static final Map<Byte, DeleteEnum> enumMap;

	static {
		vals = new ArrayList<>();
		vals = Arrays.stream(DeleteEnum.values()).map(DeleteEnum::getVal).collect(Collectors.toUnmodifiableList());
		descs = Arrays.stream(DeleteEnum.values()).map((iter) -> {
			return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		Builder<Byte, DeleteEnum> builder = ImmutableMap.<Byte, DeleteEnum>builder();
		for (DeleteEnum iter : DeleteEnum.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	/**
	 * 转化
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static DeleteEnum of(Byte value) {
		return enumMap.get(value);
	}

}
