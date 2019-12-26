package frodez.controller.user;

import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.dao.model.user.PagePermission;
import frodez.dao.param.user.AddPagePermission;
import frodez.dao.param.user.QueryRolePermission;
import frodez.dao.param.user.UpdatePagePermission;
import frodez.dao.result.user.PagePermissionDetail;
import frodez.dao.result.user.PagePermissionInfo;
import frodez.service.user.facade.IAuthorityService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户页面资源权限信息控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping(value = "/frontEnd/permission", name = "用户页面资源权限信息控制器")
public class FrontEndPermissionController {

	@Autowired
	private IAuthorityService authorityService;

	@GetMapping(name = "查询页面资源权限信息接口")
	@Success(PagePermissionDetail.class)
	public Result getPagePermission(@ApiParam("页面资源权限ID") Long id) {
		return authorityService.getPagePermission(id);
	}

	@GetMapping(value = "/page", name = "分页查询页面资源权限信息接口")
	@Success(value = PagePermission.class, containerType = Container.PAGE)
	public Result getPagePermissions(@RequestBody QueryPage param) {
		return authorityService.getPagePermissions(param);
	}

	@GetMapping(value = "/byRoleId", name = "根据角色ID获取页面资源权限信息接口")
	@Success(value = PagePermissionInfo.class, containerType = Container.PAGE)
	public Result getRolePagePermissions(@RequestBody QueryRolePermission param) {
		return authorityService.getRolePagePermissions(param);
	}

	@DeleteMapping(name = "删除页面资源权限接口")
	public Result removePagePermission(@ApiParam("页面资源权限ID") Long id) {
		return authorityService.removePagePermission(id);
	}

	@PostMapping(value = "/add", name = "添加新页面资源权限接口")
	public Result addPagePermission(@RequestBody AddPagePermission param) {
		return authorityService.addPagePermission(param);
	}

	@PostMapping(value = "/update", name = "修改页面资源权限接口")
	public Result updatePagePermission(@RequestBody UpdatePagePermission param) {
		return authorityService.updatePagePermission(param);
	}

}
