package frodez.constant.enums.task;

import com.google.common.collect.ImmutableMap;
import frodez.constant.annotations.decoration.EnumCheckable;
import frodez.constant.enums.IEnum;
import frodez.constant.settings.DefStr;
import frodez.util.common.StrUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 * @author Frodez
 * @date 2019-03-20
 */
@EnumCheckable
@AllArgsConstructor
public enum StatusEnum implements IEnum<Byte> {

	/**
	 * 1:活跃中
	 */
	ACTIVE((byte) 1, "活跃中"),
	/**
	 * 2:不活跃
	 */
	PAUSED((byte) 2, "不活跃");

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

	private static final Map<Byte, StatusEnum> enumMap;

	static {
		vals = Arrays.stream(StatusEnum.values()).map(StatusEnum::getVal).collect(Collectors.toUnmodifiableList());
		descs = Arrays.stream(StatusEnum.values()).map((iter) -> {
			return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		var builder = ImmutableMap.<Byte, StatusEnum>builder();
		for (StatusEnum iter : StatusEnum.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	public static StatusEnum of(Byte value) {
		return enumMap.get(value);
	}

}
