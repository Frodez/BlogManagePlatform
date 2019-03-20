package frodez.controller.task;

import frodez.dao.model.task.Task;
import frodez.dao.param.task.AddTask;
import frodez.dao.result.task.AvailableTaskInfo;
import frodez.service.task.base.BaseTaskService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.constant.setting.DefDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务控制器
 * @author Frodez
 * @date 2019-03-21
 */
@RestController
@RequestMapping("/task")
@Api(tags = "任务控制器")
public class TaskController {

	@Autowired
	private BaseTaskService taskService;

	@GetMapping("/availables")
	@ApiOperation(value = "查询可用定时任务接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AvailableTaskInfo.class) })
	public Result getAvailableTasks(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) QueryPage param) {
		return taskService.getAvailableTasks(param);
	}

	@GetMapping("/runnings")
	@ApiOperation(value = "查询正在运行定时任务接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Task.class) })
	public Result getRunningTaskInfo(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) QueryPage param) {
		return taskService.getRunningTasksInfo(param);
	}

	@GetMapping("/saves")
	@ApiOperation(value = "查询已保存定时任务接口")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = Task.class) })
	public Result getTasks(@RequestBody @ApiParam(value = DefDesc.Message.PAGE_QUERY,
		required = true) QueryPage param) {
		return taskService.getTasks(param);
	}

	@PostMapping("/add")
	@ApiOperation(value = "添加新定时任务接口")
	public Result addTask(@RequestBody @ApiParam(value = "新增定时任务请求参数", required = true) AddTask param) {
		return taskService.addTask(param);
	}

	@PostMapping("/cancel/all")
	@ApiOperation(value = "取消所有定时任务接口")
	public Result cancelAll() {
		return taskService.cancelAllTasks();
	}

	@PostMapping("/cancel")
	@ApiOperation(value = "取消定时任务接口")
	public Result cancelTask(@RequestParam("id") @ApiParam(value = "任务ID", required = true) Long id) {
		return taskService.cancelTask(id);
	}

	@PostMapping("/change")
	@ApiOperation(value = "更改定时任务活跃状态接口")
	public Result changeStatus(@RequestParam("id") @ApiParam(value = "任务ID", required = true) Long id,
		@RequestParam("status") @ApiParam(value = "是否立刻启动 1:立刻启动 2:暂不启动", required = true) Byte status) {
		return taskService.changeStatus(id, status);
	}

	@DeleteMapping
	@ApiOperation(value = "删除定时任务接口")
	public Result deleteTask(@RequestParam("id") @ApiParam(value = "任务ID", required = true) Long id) {
		return taskService.deleteTask(id);
	}

}
