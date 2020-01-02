package frodez.service.config.impl;

import frodez.constant.enums.common.ModifyType;
import frodez.constant.keys.config.GlobalDataKey;
import frodez.dao.mapper.config.RoleSettingMapper;
import frodez.dao.mapper.config.SettingMapper;
import frodez.dao.mapper.user.RoleMapper;
import frodez.dao.model.table.config.RoleSetting;
import frodez.dao.model.table.config.Setting;
import frodez.dao.param.config.UpdateGlobalData;
import frodez.dao.param.permission.UpdateRoleSetting;
import frodez.service.cache.facade.config.IGlobalDataCache;
import frodez.service.cache.facade.config.SettingCache;
import frodez.service.config.facade.IConfigService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.EmptyUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConfigService implements IConfigService {

	@Autowired
	@Qualifier("globalDataMapCache")
	private IGlobalDataCache globalDataCache;

	@Autowired
	@Qualifier("settingMapCache")
	private SettingCache settingCache;

	@Autowired
	private SettingMapper settingMapper;

	@Autowired
	private RoleSettingMapper roleSettingMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Result getSettings(QueryPage query) {
		return query.start(() -> settingMapper.selectAll());
	}

	@Override
	public Result getRoleSettings(Long roleId) {
		return Result.success(settingMapper.getRoleSettings(roleId));
	}

	@Override
	public Result updateRoleSettings(UpdateRoleSetting param) {
		Long roleId = param.getRoleId();
		if (roleMapper.existsWithPrimaryKey(roleId)) {
			return Result.fail("该角色不存在");
		}
		List<Long> settingIds = param.getSettingIds();
		switch (ModifyType.of(param.getModifyType())) {
			case INSERT : {
				if (EmptyUtil.no(settingIds) || !settingMapper.existByIds(settingIds)) {
					return Result.fail("不合法的角色设置ID");
				}
				if (EmptyUtil.no(settingIds, roleSettingMapper.partialEqual("setting_id", "role_id", roleId))) {
					return Result.fail("该角色已经拥有的角色设置和要添加的角色设置存在重复!");
				}
				List<RoleSetting> roleSettings = settingIds.stream().map((item) -> {
					RoleSetting setting = new RoleSetting();
					setting.setRoleId(roleId);
					setting.setSettingId(item);
					return setting;
				}).collect(Collectors.toList());
				roleSettingMapper.insertList(roleSettings);
				break;
			}
			case UPDATE : {
				if (EmptyUtil.no(settingIds)) {
					List<RoleSetting> roleSettings = settingIds.stream().map((item) -> {
						RoleSetting setting = new RoleSetting();
						setting.setRoleId(roleId);
						setting.setSettingId(item);
						return setting;
					}).collect(Collectors.toList());
					roleSettingMapper.insertList(roleSettings);
				}
				roleSettingMapper.deleteEqual("role_id", roleId);
				break;
			}
			case DELETE : {
				if (EmptyUtil.no(settingIds) || !settingMapper.existByIds(settingIds)) {
					return Result.fail("不合法的角色设置ID");
				}
				if (!roleSettingMapper.<Long>partialEqual("setting_id", "role_id", roleId).containsAll(settingIds)) {
					return Result.fail("需要删除的角色设置不在角色享有角色设置之内!");
				}
				roleSettingMapper.deleteEqual("role_id", roleId);
				break;
			}
		}
		List<Setting> settings = settingMapper.getRoleSettings(roleId);
		settingCache.refresh(roleId, settings.stream().map(Setting::getName).collect(Collectors.toList()));
		return Result.success();
	}

	@Override
	public Result getGlobalData() {
		return Result.success(globalDataCache.getAll());
	}

	@Override
	public Result setGlobalData(List<UpdateGlobalData> param) {
		saveGlobalData(param);
		return Result.success();
	}

	@SuppressWarnings("unchecked")
	private <V> void saveGlobalData(List<UpdateGlobalData> param) {
		for (UpdateGlobalData item : param) {
			Enum<? extends GlobalDataKey<V>> enumKey = (Enum<? extends GlobalDataKey<V>>) GlobalDataKey.check(item.getType(), item.getName());
			if (enumKey == null) {
				throw new IllegalArgumentException("非法的字段名或者字段类型!");
			}
			GlobalDataKey<V> key = (GlobalDataKey<V>) enumKey;
			globalDataCache.save(enumKey, key.revert(item.getContent()));
		}
	}

}
