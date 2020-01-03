package frodez.util.beans.pair;

import lombok.EqualsAndHashCode;

/**
 * @see frodez.util.beans.pair.Pair
 * @author Frodez
 * @date 2019-03-27
 */
@EqualsAndHashCode(callSuper = true)
public class StrPair extends Pair<String, String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public StrPair(String key, String value) {
		super(key, value);
	}

}
