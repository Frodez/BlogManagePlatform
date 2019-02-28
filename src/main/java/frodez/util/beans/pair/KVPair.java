package frodez.util.beans.pair;

import java.io.Serializable;
import lombok.Data;

@Data
public class KVPair<K, V> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 键
	 */
	private K key;

	/**
	 * 值
	 */
	private V value;

}
