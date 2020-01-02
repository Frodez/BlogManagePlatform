package frodez.util.beans.pair;

import lombok.EqualsAndHashCode;

/**
 * 比较键值对<br>
 * 警告!!!本类重写了compareTo方法,以key值为比较对象。
 * @see frodez.util.beans.pair.Pair
 * @author Frodez
 * @date 2019-03-20
 */
@EqualsAndHashCode(callSuper = true)
public class ComPair<K extends Comparable<K>, V> extends Pair<K, V> implements Comparable<ComPair<K, V>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ComPair(K key, V value) {
		super(key, value);
	}

	@Override
	public int compareTo(ComPair<K, V> object) {
		return super.getKey().compareTo(object.getKey());
	}

}
