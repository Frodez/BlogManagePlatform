package frodez.util.beans;

import java.io.Serializable;
import lombok.Data;

@Data
public class StrKV implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 键
	 */
	private String key;

	/**
	 * 值
	 */
	private String value;

}
