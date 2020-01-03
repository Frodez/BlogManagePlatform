package frodez.service.cache.facade.config;

import frodez.config.cache.ICache;
import frodez.constant.keys.config.GlobalDataKey;
import frodez.dao.model.table.config.GlobalData;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 全局配置缓存<br>
 * 用途:<br>
 * 1.存放全局配置,更新全局配置。<br>
 * @author Frodez
 * @date 2020-01-01
 */
public interface IGlobalDataCache extends ICache {

	/**
	 * 获取全部全局配置
	 * @author Frodez
	 * @date 2020-01-01
	 */
	List<GlobalData> getAll();

	/**
	 * 保存全局设置
	 * @author Frodez
	 * @date 2020-01-01
	 */
	@Transactional
	<V> void save(Enum<? extends GlobalDataKey<V>> key, V value);

	/**
	 * 获取全局设置
	 * @author Frodez
	 * @date 2020-01-01
	 */
	<V> V get(Enum<? extends GlobalDataKey<V>> key);

}
