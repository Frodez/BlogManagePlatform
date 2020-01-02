package frodez.constant.enums;

import java.util.List;

public interface IEnum<V, E extends Enum<E>> {

	static <V> V getVal() {
		return null;
	}

	static String getDesc() {
		return null;
	}

	static <V> List<V> getVals() {
		return null;
	}

}
