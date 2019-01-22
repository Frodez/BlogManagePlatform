package frodez.config.aop.request.checker.inter;

import javax.servlet.http.HttpServletRequest;

/**
 * key生成器接口
 * @author Frodez
 * @date 2019-01-21
 */
public interface ServletKeyGenerator {

	/**
	 * 根据规则获得key
	 * @param sault 盐
	 * @param HttpServletRequest 请求
	 * @author Frodez
	 * @date 2018-12-21
	 */
	String getKey(String sault, HttpServletRequest request);
	
}
