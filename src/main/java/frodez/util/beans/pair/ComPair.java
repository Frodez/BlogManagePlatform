package frodez.util.beans.pair;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ComPair<K extends Comparable<K>, V> extends Pair<K, V> implements Comparable<ComPair<K, V>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(ComPair<K, V> object) {
		return super.getKey().compareTo(object.getKey());
	}

}
