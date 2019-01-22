package frodez.config.aop.request.checker.inter;

public interface Checker {

	/**
	 * 检查key是否存在,存在返回true,否则返回false
	 * @param key 对应key
	 * @author Frodez
	 * @date 2019-01-21
	 */
	boolean check(String key);
	
}
