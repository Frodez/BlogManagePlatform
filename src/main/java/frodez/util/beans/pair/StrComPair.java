package frodez.util.beans.pair;

import lombok.EqualsAndHashCode;

/**
 * @see frodez.util.beans.pair.ComPair
 * @see frodez.util.beans.pair.Pair
 * @author Frodez
 * @date 2019-03-20
 */
@EqualsAndHashCode(callSuper = true)
public class StrComPair extends ComPair<String, String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public StrComPair(String key, String value) {
		super(key, value);
	}

}
