package frodez.util.beans.pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 通用键值对<br>
 * 对于那些需要同时返回多个值的情况,有时为其单独创建一个类会过于麻烦。此时便可使用该类型。
 * @author Frodez
 * @date 2019-03-27
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pair<K, V> implements Serializable {

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

	/**
	 * 从map转化成Pair的List
	 * @author Frodez
	 * @date 2019-12-31
	 */
	public static <K, V> List<Pair<K, V>> transfer(Map<K, V> map) {
		List<Pair<K, V>> collection = new ArrayList<>();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(new Pair<>(entry.getKey(), entry.getValue()));
		}
		return collection;
	}

	/**
	 * 从map转化成Pair的collection
	 * @author Frodez
	 * @date 2019-12-31
	 */
	public static <C extends Collection<Pair<K, V>>, K, V> C transfer(Map<K, V> map, Supplier<C> supplier) {
		C collection = supplier.get();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(new Pair<>(entry.getKey(), entry.getValue()));
		}
		return collection;
	}

	/**
	 * 从map转化成Pair的collection
	 * @author Frodez
	 * @date 2019-12-31
	 */
	public static <C extends Collection<P>, P extends Pair<K, V>, K, V> C transfer(Map<K, V> map, Supplier<C> collectionSupplier, BiFunction<K, V,
		P> pairSupplier) {
		C collection = collectionSupplier.get();
		for (Entry<K, V> entry : map.entrySet()) {
			collection.add(pairSupplier.apply(entry.getKey(), entry.getValue()));
		}
		return collection;
	}

}
