package frodez.config.aop.request.checker.facade;

public interface Checker {

	/**
	 * 根据key判断是否满足条件,满足返回true,否则返回false
	 * @param key 对应key
	 * @author Frodez
	 * @date 2019-01-21
	 */
	boolean check(String key);

}
