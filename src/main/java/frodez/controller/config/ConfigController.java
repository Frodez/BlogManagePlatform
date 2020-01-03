package frodez.controller.config;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.table.config.GlobalData;
import frodez.dao.model.table.config.Setting;
import frodez.dao.param.config.UpdateGlobalData;
import frodez.dao.param.permission.UpdateRoleSetting;
import frodez.service.config.facade.IConfigService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置控制器
 * @author Frodez
 * @date 2020-01-02
 */
@RepeatLock
@RestController
@RequestMapping(value = "/config", name = "配置控制器")
public class ConfigController {

	@Autowired
	private IConfigService configService;

	@GetMapping(value = "/role/page", name = "分页查询角色配置信息接口")
	@Success(value = Setting.class, containerType = Container.PAGE)
	public Result getUsers(@RequestBody QueryPage query) {
		return configService.getSettings(query);
	}

	@GetMapping(value = "/role/byId", name = "获取角色对应的设置信息接口")
	@Success(Setting.class)
	public Result getRoleSettings(@ApiParam("角色ID") Long roleId) {
		return configService.getRoleSettings(roleId);
	}

	@PostMapping(value = "/role/update", name = "更改角色的设置信息接口")
	public Result updateRoleSettings(@RequestBody UpdateRoleSetting param) {
		return configService.updateRoleSettings(param);
	}

	@GetMapping(value = "/global", name = "获取全局配置信息接口")
	@Success(value = GlobalData.class, containerType = Container.LIST)
	public Result getGlobalData() {
		return configService.getGlobalData();
	}

	@PostMapping(value = "/global/set", name = "更新全局配置信息接口")
	public Result setGlobalData(@RequestBody List<UpdateGlobalData> param) {
		return configService.setGlobalData(param);
	}

}
