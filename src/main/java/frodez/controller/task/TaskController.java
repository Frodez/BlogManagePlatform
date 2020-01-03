package frodez.controller.task;

import frodez.config.aop.request.annotation.RepeatLock;
import frodez.config.aop.validation.annotation.common.MapEnum;
import frodez.config.swagger.annotation.Success;
import frodez.config.swagger.annotation.Success.Container;
import frodez.constant.enums.task.TaskStatus;
import frodez.dao.model.result.task.AvailableTaskInfo;
import frodez.dao.model.table.task.Task;
import frodez.dao.param.task.AddTask;
import frodez.service.task.base.BaseTaskService;
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
 * 任务控制器
 * @author Frodez
 * @date 2019-03-21
 */
@RepeatLock
@RestController
@RequestMapping(value = "/task", name = "任务控制器")
public class TaskController {

	@Autowired
	private BaseTaskService taskService;

	@GetMapping(value = "/availables", name = "查询可用定时任务接口")
	@Success(value = AvailableTaskInfo.class, containerType = Container.PAGE)
	public Result getAvailableTasks(@RequestBody QueryPage param) {
		return taskService.getAvailableTasks(param);
	}

	@GetMapping(value = "/runnings", name = "查询正在运行定时任务接口")
	@Success(value = Task.class, containerType = Container.PAGE)
	public Result getRunningTaskInfo(@RequestBody QueryPage param) {
		return taskService.getRunningTasksInfo(param);
	}

	@GetMapping(value = "/saves", name = "查询已保存定时任务接口")
	@Success(value = Task.class, containerType = Container.PAGE)
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
	public Result cancelTask(@ApiParam("任务ID") Long id) {
		return taskService.cancelTask(id);
	}

	@PostMapping(value = "/change", name = "更改定时任务活跃状态接口")
	public Result changeStatus(@ApiParam("任务ID") Long id, @MapEnum(TaskStatus.class) Byte status) {
		return taskService.changeStatus(id, status);
	}

	@DeleteMapping(name = "删除定时任务接口")
	public Result deleteTask(@ApiParam(value = "任务ID") Long id) {
		return taskService.deleteTask(id);
	}

}
