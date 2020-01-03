package frodez.service.config.facade;

import frodez.config.aop.validation.annotation.Check;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.annotations.decoration.Page;
import frodez.dao.model.table.config.GlobalData;
import frodez.dao.model.table.config.Setting;
import frodez.dao.param.config.UpdateGlobalData;
import frodez.dao.param.permission.UpdateRoleSetting;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配置信息服务<br>
 * 用途:<br>
 * 1.查询设置信息列表<br>
 * 2.查看角色具有的设置信息<br>
 * 3.更改角色具有的设置信息<br>
 * 4.查询,更改全局数据<br>
 * @author Frodez
 * @date 2018-11-14
 */
public interface IConfigService {

	/**
	 * 分页查询设置信息
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Page
	@Check
	@Success(value = Setting.class, containerType = Container.PAGE)
	Result getSettings(@Valid @NotNull QueryPage query);

	/**
	 * 获取角色对应的设置信息
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Success(Setting.class)
	Result getRoleSettings(@NotNull Long roleId);

	/**
	 * 更改角色的设置信息
	 * @author Frodez
	 * @date 2019-12-31
	 */
	@Check
	@Transactional
	Result updateRoleSettings(@Valid @NotNull UpdateRoleSetting param);

	/**
	 * 获取全局配置信息
	 * @author Frodez
	 * @date 2020-01-01
	 */
	@Success(value = GlobalData.class, containerType = Container.LIST)
	Result getGlobalData();

	/**
	 * 更新全局配置信息
	 * @author Frodez
	 * @date 2020-01-01
	 */
	@Check
	@Transactional
	Result setGlobalData(@NotEmpty List<@Valid UpdateGlobalData> param);

}
