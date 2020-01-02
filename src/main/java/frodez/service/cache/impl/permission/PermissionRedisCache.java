package frodez.service.cache.impl.permission;

import frodez.config.cache.CacheProperties;
import frodez.constant.keys.cache.CacheKey;
import frodez.dao.model.result.permission.PermissionDetail;
import frodez.service.cache.facade.permission.IPermissionCache;
import frodez.util.json.JSONUtil;
import frodez.util.spring.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("permissionRedisCache")
public class PermissionRedisCache implements IPermissionCache {

	private BoundHashOperations<String, String, String> cache;

	public PermissionRedisCache(@Autowired CacheProperties properties, @Autowired StringRedisTemplate template) {
		cache = template.boundHashOps(CacheKey.PermissionCache.ROLE_ID);
	}

	@Override
	public void clear() {
		StringRedisTemplate template = ContextUtil.bean(StringRedisTemplate.class);
		template.delete(CacheKey.PermissionCache.ROLE_ID);
	}

	@Override
	public boolean exist(Long roleId) {
		return cache.hasKey(roleId.toString());
	}

	@Override
	public PermissionDetail get(Long roleId) {
		String json = cache.get(roleId.toString());
		return json == null ? null : JSONUtil.as(json, PermissionDetail.class);
	}

	@Override
	public void save(Long roleId, PermissionDetail detail) {
		cache.put(roleId.toString(), JSONUtil.string(detail));
	}

}
