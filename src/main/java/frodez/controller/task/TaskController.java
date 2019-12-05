package frodez.controller.task;

import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.ContainerType;
import frodez.dao.model.task.Task;
import frodez.dao.param.task.AddTask;
import frodez.dao.result.task.AvailableTaskInfo;
import frodez.service.task.base.BaseTaskService;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping(value = "/availables", name = "查询可用定时任务接口")
	@Success(value = AvailableTaskInfo.class, containerType = ContainerType.PAGE)
	public Result getAvailableTasks(@RequestBody QueryPage param) {
		return taskService.getAvailableTasks(param);
	}

	@GetMapping(value = "/runnings", name = "查询正在运行定时任务接口")
	@Success(value = Task.class, containerType = ContainerType.PAGE)
	public Result getRunningTaskInfo(@RequestBody QueryPage param) {
		return taskService.getRunningTasksInfo(param);
	}

	@GetMapping(value = "/saves", name = "查询已保存定时任务接口")
	@Success(value = Task.class, containerType = ContainerType.PAGE)
	public Result getTasks(@RequestBody QueryPage param) {
		return taskService.getTasks(param);
	}

	@PostMapping(value = "/add", name = "添加新定时任务接口")
	public Result addTask(@RequestBody AddTask param) {
		return taskService.addTask(param);
	}

	@PostMapping(value = "/cancel/all", name = "取消所有定时任务接口")
	public Result cancelAll() {
		return taskService.cancelAllTasks();
	}

	@PostMapping(value = "/cancel", name = "取消定时任务接口")
	public Result cancelTask(@ApiParam(value = "任务ID") Long id) {
		return taskService.cancelTask(id);
	}

	@PostMapping(value = "/change", name = "更改定时任务活跃状态接口")
	public Result changeStatus(@ApiParam(value = "任务ID") Long id, @ApiParam(value = "活跃状态 1:活跃中 2:不活跃") Byte status) {
		return taskService.changeStatus(id, status);
	}

	@DeleteMapping(name = "删除定时任务接口")
	public Result deleteTask(@ApiParam(value = "任务ID") Long id) {
		return taskService.deleteTask(id);
	}

}
