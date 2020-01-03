package frodez.dao.mapper.config;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.config.mybatis.result.CustomResultHandler;
import frodez.config.mybatis.result.handler.MapResultHandler;
import frodez.dao.model.table.config.Setting;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description 设置表
 * @table tb_setting
 * @date 2019-12-30
 */
@Repository
public interface SettingMapper extends DataMapper<Setting> {

	/**
	 * 获取所有设置以及具有该设置的用户id
	 * @author Frodez
	 * @date 2019-12-30
	 */
	@CustomResultHandler(MapResultHandler.class)
	Map<String, Set<Long>> getSettingRoles();

	/**
	 * 获取角色对应的设置
	 * @author Frodez
	 * @date 2019-12-31
	 */
	List<Setting> getRoleSettings(@Param("roleId") Long roleId);

}
