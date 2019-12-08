package frodez.service.task.base;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.exception.annotation.CatchAndReturn;
import frodez.config.aop.exception.annotation.CatchAndThrow;
import frodez.config.aop.validation.annotation.Check;
import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.config.task.TaskProperties;
import frodez.constant.enums.task.StatusEnum;
import frodez.constant.errors.code.ErrorCode;
import frodez.constant.settings.DefStr;
import frodez.dao.mapper.task.TaskMapper;
import frodez.dao.model.task.Task;
import frodez.dao.param.task.AddTask;
import frodez.dao.result.task.AvailableTaskInfo;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.common.StrUtil;
import frodez.util.common.StreamUtil;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.BeanUtil;
import frodez.util.spring.ContextUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Configuration
@EnableScheduling
public class BaseTaskService {

	@Autowired
	private ThreadPoolTaskScheduler scheduler;

	@Autowired
	private TaskProperties properties;

	@Autowired
	private TaskMapper taskMapper;

	/**
	 * 可用任务信息
	 */
	private List<AvailableTaskInfo> taskServiceInfos;

	/**
	 * 当前任务
	 */
	private Map<Long, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

	/**
	 * 当前任务信息
	 */
	private Map<Long, Task> taskInfoMap = new ConcurrentHashMap<>();

	/**
	 * 初始化<br>
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@PostConstruct
	private void init() {
		try {
			taskServiceInfos = ContextUtil.beans(ITask.class).entrySet().stream().map((entry) -> {
				AvailableTaskInfo info = new AvailableTaskInfo();
				info.setName(entry.getKey());
				info.setDescription(entry.getValue().getDescription());
				info.setPermitForceInterrupt(!canForcelyIntterrupt(entry.getValue()));
				return info;
			}).collect(Collectors.toList());
			Example example = new Example(Task.class);
			example.createCriteria().andNotIn("target", taskServiceInfos.stream().map(AvailableTaskInfo::getName).collect(Collectors.toList()));
			taskMapper.deleteByExample(example);
			example.clear();
			example.createCriteria().andIsNotNull("target").andEqualTo("status", StatusEnum.ACTIVE.getVal());
			example.orderBy("id");
			List<Task> tasks = taskMapper.selectByExampleAndRowBounds(example, new QueryPage(properties.getMaxSize()).toRowBounds());
			tasks.parallelStream().forEach((task) -> {
				Runnable runnable = null;
				CronTrigger trigger = null;
				try {
					runnable = getRunnable(task.getTarget());
				} catch (ClassNotFoundException e) {
					log.error("[frodez.service.task.base.BaseTaskService.init]", e);
					log.warn("初始化任务——类型初始化失败!类型名:{}", task.getTarget());
					return;
				}
				try {
					trigger = new CronTrigger(task.getCronExp());
				} catch (IllegalArgumentException e) {
					log.error("[frodez.service.task.base.BaseTaskService.init]", e);
					log.warn("初始化任务——时间表达式错误!表达式:{}", task.getCronExp());
					return;
				}
				if (runnable != null && trigger != null) {
					taskMap.put(task.getId(), scheduler.schedule(runnable, trigger));
					taskInfoMap.put(task.getId(), task);
					log.info("第{}号任务启动,任务详情:{}", task.getId(), JSONUtil.string(task));
				}
			});
		} catch (Exception e) {
			log.error("[frodez.service.task.base.BaseTaskService.init]初始化任务错误!应用仍将继续启动...", e);
		}
	}

	/**
	 * 判断是否为可以强行中断的接口
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private boolean canForcelyIntterrupt(Runnable runnable) {
		return isTransactional(runnable);
	}

	/**
	 * 判断是否为带有事务的接口
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private boolean isTransactional(Runnable runnable) {
		try {
			return runnable.getClass().getAnnotation(Transactional.class) != null || runnable.getClass().getMethod("run").getAnnotation(
				Transactional.class) != null;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		}
	}

	/**
	 * 获取可用的定时任务实例用于开启定时任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private Runnable getRunnable(String className) throws ClassNotFoundException {
		return (Runnable) ContextUtil.bean(Class.forName(StrUtil.concat(properties.getPrefix(), DefStr.POINT_SEPERATOR, className)));
	}

	/**
	 * 是否可用
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private boolean isAvailable(String target) {
		return taskServiceInfos.stream().filter((iter) -> {
			return iter.getName().equals(target);
		}).count() != 0;
	}

	/**
	 * 获得可用的任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndReturn
	public Result getAvailableTasks(@Valid @NotNull QueryPage param) {
		List<AvailableTaskInfo> infos = StreamUtil.page(taskServiceInfos, param).collect(Collectors.toList());
		return Result.page(param.getPageNum(), param.getPageSize(), infos.size(), infos);
	}

	/**
	 * 获取正在运行的任务信息
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndReturn
	public Result getRunningTasksInfo(@Valid @NotNull QueryPage param) {
		List<Task> tasks = StreamUtil.page(taskInfoMap.values(), param).collect(Collectors.toList());
		return Result.page(param.getPageNum(), param.getPageSize(), tasks.size(), tasks);
	}

	/**
	 * 获取保存的任务信息
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndReturn
	public Result getTasks(@Valid @NotNull QueryPage param) {
		return Result.page(PageHelper.startPage(param).doSelectPage(() -> {
			taskMapper.selectAll();
		}));
	}

	/**
	 * 强制取消当前所有任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@CatchAndReturn
	public Result cancelAllTasks() {
		int total = taskMap.size();
		int alreadyCanceled = 0;
		for (Entry<Long, ScheduledFuture<?>> entry : taskMap.entrySet()) {
			if (entry.getValue().cancel(true)) {
				taskMap.remove(entry.getKey());
				taskInfoMap.remove(entry.getKey());
				++alreadyCanceled;
			}
		}
		return Result.success(StrUtil.concat("共计", Integer.valueOf(total).toString(), "个任务正在执行,已取消", Integer.valueOf(alreadyCanceled).toString(),
			"个。"));
	}

	/**
	 * 添加任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndThrow(errorCode = ErrorCode.TASK_SERVICE_ERROR)
	@Transactional
	public Result addTask(@Valid @NotNull AddTask param) {
		if (isAvailable(param.getTarget())) {
			return Result.fail("非法的类型!");
		}
		Runnable runnable;
		CronTrigger trigger;
		try {
			runnable = getRunnable(param.getTarget());
			trigger = new CronTrigger(param.getCronExp());
		} catch (ClassNotFoundException e) {
			return Result.fail("类型初始化失败!");
		} catch (IllegalArgumentException e) {
			return Result.fail("时间表达式错误!");
		}
		if (taskMapper.selectCount(null) >= properties.getMaxSize()) {
			return Result.fail("已达可用任务最大数量!");
		}
		Task task = new Task();
		BeanUtil.copy(param, task);
		task.setCreateTime(new Date());
		task.setStatus(param.getStartNow());
		taskMapper.insertUseGeneratedKeys(task);
		switch (StatusEnum.of(param.getStartNow())) {
			case ACTIVE : {
				taskMap.put(task.getId(), scheduler.schedule(runnable, trigger));
				taskInfoMap.put(task.getId(), task);
				break;
			}
			default : {
				break;
			}
		}
		return Result.success(task.getId());
	}

	/**
	 * 中止并删除任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndThrow(errorCode = ErrorCode.TASK_SERVICE_ERROR)
	@Transactional
	public Result deleteTask(@NotNull Long id) {
		Task task = taskMapper.selectByPrimaryKey(id);
		if (task == null) {
			return Result.fail("未找到该任务!");
		}
		ScheduledFuture<?> future = taskMap.get(id);
		if (future != null) {
			Runnable runnable;
			try {
				runnable = getRunnable(task.getTarget());
			} catch (ClassNotFoundException e) {
				return Result.fail("类型初始化失败!");
			}
			boolean mayInterruptIfRunning = canForcelyIntterrupt(runnable);
			if (!future.cancel(mayInterruptIfRunning)) {
				if (mayInterruptIfRunning) {
					return Result.fail("该任务正在执行,且不能强行中止,因此暂不能删除!");
				} else {
					return Result.fail("该任务正在执行,且中止失败,因此暂不能删除!");
				}
			}
		}
		taskMapper.deleteByPrimaryKey(id);
		taskMap.remove(id);
		taskInfoMap.remove(id);
		return Result.success();
	}

	/**
	 * 中止任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndReturn
	public Result cancelTask(@NotNull Long id) {
		ScheduledFuture<?> future = taskMap.get(id);
		if (future == null) {
			return Result.fail("未找到该任务!");
		}
		if (!future.cancel(true)) {
			return Result.fail("强行取消任务失败!");
		}
		taskMap.remove(id);
		taskInfoMap.remove(id);
		return Result.success();
	}

	/**
	 * 更改任务活跃状态
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@CatchAndThrow(errorCode = ErrorCode.TASK_SERVICE_ERROR)
	@Transactional
	public Result changeStatus(@NotNull Long id, @LegalEnum(StatusEnum.class) Byte status) {
		Task task = taskMapper.selectByPrimaryKey(id);
		if (task == null) {
			return Result.fail("未找到该任务!");
		}
		task.setStatus(status);
		taskMapper.updateByPrimaryKeySelective(task);
		return Result.success();
	}

}
