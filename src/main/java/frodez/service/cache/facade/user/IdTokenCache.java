package frodez.service.cache.facade.user;

import frodez.config.cache.ICache;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * id-token缓存<br>
 * 用途：用于判断该id的用户是否已登录
 * @author Frodez
 * @date 2019-12-29
 */
public interface IdTokenCache extends ICache {

	/**
	 * 判断id对应用户的token是否存在
	 * @author Frodez
	 * @date 2019-12-29
	 */
	boolean exist(Long id);

	/**
	 * 判断token对应用户的id是否存在
	 * @author Frodez
	 * @date 2019-12-29
	 */
	boolean exist(String token);

	/**
	 * 保存id对应的token<br>
	 * <strong>存在旧数据时,本操作只会更新旧的id-token缓存,不会删除旧的token-id缓存,请先删除旧的token-id缓存。</strong>
	 * @author Frodez
	 * @date 2019-12-29
	 */
	@Transactional
	void save(Long id, String token);

	/**
	 * 删除id对应的token
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void remove(Long id);

	/**
	 * 删除token对应的id
	 * @author Frodez
	 * @date 2019-12-29
	 */
	void remove(String token);

	/**
	 * 根据id获取token
	 * @author Frodez
	 * @date 2019-12-29
	 */
	String getToken(Long id);

	/**
	 * 根据token获取id
	 * @author Frodez
	 * @date 2019-12-29
	 */
	Long getId(String token);

	/**
	 * 根据id批量获取token
	 * @author Frodez
	 * @date 2020-01-02
	 */
	List<String> getTokens(List<Long> ids);

	/**
	 * 批量删除id和token对
	 * @author Frodez
	 * @date 2020-01-02
	 */
	@Transactional
	void batchRemove(List<Long> ids, List<String> tokens);

}
