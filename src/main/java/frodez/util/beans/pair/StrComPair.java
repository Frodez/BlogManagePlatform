package frodez.util.beans.pair;

import lombok.EqualsAndHashCode;

/**
 * 比较键值对<br>
 * 警告!!!本类重写了compareTo方法,以key值为比较对象。
 * @author Frodez
 * @date 2019-03-20
 */
@EqualsAndHashCode(callSuper = true)
public class StrComPair extends ComPair<String, String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
