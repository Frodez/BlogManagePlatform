package frodez.util.beans.pair;

import java.io.Serializable;
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

}
