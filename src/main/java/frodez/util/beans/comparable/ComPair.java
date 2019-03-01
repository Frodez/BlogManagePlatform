package frodez.util.beans.comparable;

import java.io.Serializable;
import lombok.Data;

@Data
public class ComPair<K extends Comparable<K>, V> implements Serializable, Comparable<ComPair<K, V>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private K key;

	private V value;

	@Override
	public int compareTo(ComPair<K, V> object) {
		return this.key.compareTo(object.key);
	}

}
