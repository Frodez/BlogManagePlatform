package frodez.controller.user;

import frodez.service.user.facade.IUserService;
import frodez.util.beans.param.PageQuery;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户角色信息控制器
 * @author Frodez
 * @date 2018-12-01
 */
@RestController
@RequestMapping("/user/roles")
@Api(tags = "用户角色信息控制器")
public class RoleController {

	@Autowired
	private IUserService userService;

	/**
	 * 分页查询角色信息
	 * @author Frodez
	 * @date 2019-03-06
	 */
	@GetMapping
	@ApiOperation(value = "分页查询角色信息接口")
	public Result getRoles(@RequestBody @ApiParam(value = PageQuery.DEFAULT_DESC, required = true) PageQuery param) {
		return userService.getRoles(param);
	}

}
