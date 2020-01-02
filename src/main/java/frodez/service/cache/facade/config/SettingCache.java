package frodez.service.cache.facade.config;

import frodez.config.cache.ICache;
import java.util.List;

/**
 * 用户设置缓存<br>
 * 用途：<br>
 * 1.用于判断用户是否具有某条设置<br>
 * @author Frodez
 * @date 2019-12-29
 */
public interface SettingCache extends ICache {

	/**
	 * 更新缓存,重置或者添加某个角色的用户设置
	 * @author Frodez
	 * @date 2019-12-30
	 */
	void refresh(Long roleId, List<String> settings);

	/**
	 * 判断是否具有某条设置
	 * @author Frodez
	 * @date 2019-12-30
	 */
	boolean pass(Long roleId, String setting);

	/**
	 * 判断是否具有某条设置
	 * @author Frodez
	 * @date 2019-12-30
	 */
	boolean reject(Long roleId, String setting);

}
