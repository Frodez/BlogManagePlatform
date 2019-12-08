package frodez.constant.enums.task;

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
 * 任务启动状态枚举
 * @author Frodez
 * @date 2019-03-20
 */
@EnumCheckable
@Description(name = "任务启动状态枚举")
@AllArgsConstructor
public enum StartNowEnum {

	/**
	 * 1:立刻启动
	 */
	YES((byte) 1, "立刻启动"),
	/**
	 * 2:不活跃
	 */
	NO((byte) 2, "暂不启动");

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

	private static Map<Byte, StartNowEnum> enumMap;

	static {
		vals = Arrays.stream(StartNowEnum.values()).map(StartNowEnum::getVal).collect(Collectors.toUnmodifiableList());
		descs = Arrays.stream(StartNowEnum.values()).map((iter) -> {
			return StrUtil.concat(iter.val.toString(), DefStr.SEPERATOR, iter.desc);
		}).collect(Collectors.toUnmodifiableList());
		var builder = ImmutableMap.<Byte, StartNowEnum>builder();
		for (StartNowEnum iter : StartNowEnum.values()) {
			builder.put(iter.val, iter);
		}
		enumMap = builder.build();
	}

	public static StartNowEnum of(Byte value) {
		return enumMap.get(value);
	}

	/**
	 * 默认枚举
	 * @author Frodez
	 * @date 2019-05-17
	 */
	public static StartNowEnum defaultEnum() {
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
