package frodez.service.task.base;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.validation.annotation.Check;
import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.config.task.TaskProperties;
import frodez.dao.mapper.task.TaskMapper;
import frodez.dao.model.task.Task;
import frodez.dao.param.task.AddTask;
import frodez.dao.result.task.AvailableTaskInfo;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import frodez.util.constant.setting.DefStr;
import frodez.util.constant.task.StatusEnum;
import frodez.util.error.ErrorCode;
import frodez.util.error.exception.ServiceException;
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
			taskServiceInfos = ContextUtil.gets(ITask.class).entrySet().stream().map((entry) -> {
				AvailableTaskInfo info = new AvailableTaskInfo();
				info.setName(entry.getKey());
				info.setDescription(entry.getValue().getDescription());
				info.setPermitForceInterrupt(!isTransactional(entry.getValue()));
				return info;
			}).collect(Collectors.toList());
			Example example = new Example(Task.class);
			example.createCriteria().andNotIn("target", taskServiceInfos.stream().map(AvailableTaskInfo::getName)
				.collect(Collectors.toList()));
			taskMapper.deleteByExample(example);
			example.clear();
			example.createCriteria().andIsNotNull("target").andEqualTo("status", StatusEnum.ACTIVE.getVal());
			example.orderBy("id");
			PageHelper.startPage(new QueryPage(properties.getMaxSize()));
			List<Task> tasks = taskMapper.selectByExample(example);
			tasks.parallelStream().forEach((task) -> {
				Runnable runnable = null;
				CronTrigger trigger = null;
				try {
					runnable = getRunnable(task.getTarget());
				} catch (ClassNotFoundException e) {
					log.error("init", e);
					log.warn("初始化任务——类型初始化失败!类型名:{}", task.getTarget());
					return;
				}
				try {
					trigger = new CronTrigger(task.getCronExp());
				} catch (IllegalArgumentException e) {
					log.error("init", e);
					log.warn("初始化任务——时间表达式错误!表达式:{}", task.getCronExp());
					return;
				}
				if (runnable != null && trigger != null) {
					taskMap.put(task.getId(), scheduler.schedule(runnable, trigger));
					taskInfoMap.put(task.getId(), task);
				}
			});
		} catch (Exception e) {
			log.error("初始化任务错误!应用仍将继续启动...", e);
		}
	}

	/**
	 * 判断是否为带有事务的接口
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private boolean isTransactional(ITask task) {
		try {
			return task.getClass().getAnnotation(Transactional.class) != null || task.getClass().getMethod("run")
				.getAnnotation(Transactional.class) != null;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		}
	}

	/**
	 * 判断是否为带有事务的接口
	 * @author Frodez
	 * @date 2019-03-21
	 */
	private boolean isTransactional(Runnable runnable) {
		try {
			return runnable.getClass().getAnnotation(Transactional.class) != null || runnable.getClass().getMethod(
				"run").getAnnotation(Transactional.class) != null;
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
		return (Runnable) ContextUtil.get(Class.forName(properties.getPrefix().concat(DefStr.POINT_SEPERATOR).concat(
			className)));
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
	public Result getAvailableTasks(@Valid @NotNull QueryPage param) {
		try {
			param = QueryPage.resonable(param);
			List<AvailableTaskInfo> infos = taskServiceInfos.stream().skip((param.getPageNum() - 1) * param
				.getPageSize()).limit(param.getPageNum() * param.getPageSize()).collect(Collectors.toList());
			return Result.page(param.getPageNum(), param.getPageSize(), infos.size(), infos);
		} catch (Exception e) {
			log.error("[getAllAvailableTasks]", e);
			return Result.errorService();
		}
	}

	/**
	 * 获取正在运行的任务信息
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	public Result getRunningTasksInfo(@Valid @NotNull QueryPage param) {
		try {
			param = QueryPage.resonable(param);
			List<Task> tasks = taskInfoMap.values().stream().skip((param.getPageNum() - 1) * param.getPageSize()).limit(
				param.getPageNum() * param.getPageSize()).collect(Collectors.toList());
			return Result.page(param.getPageNum(), param.getPageSize(), tasks.size(), tasks);
		} catch (Exception e) {
			log.error("[getRunningTasksInfo]", e);
			return Result.errorService();
		}
	}

	/**
	 * 获取保存的任务信息
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	public Result getTasks(@Valid @NotNull QueryPage param) {
		try {
			return Result.page(PageHelper.startPage(QueryPage.resonable(param)).doSelectPage(() -> {
				taskMapper.selectAll();
			}));
		} catch (Exception e) {
			log.error("[getRunningTasks]", e);
			return Result.errorService();
		}
	}

	/**
	 * 强制取消当前所有任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	public Result cancelAllTasks() {
		try {
			int total = taskMap.size();
			int alreadyCanceled = 0;
			for (Entry<Long, ScheduledFuture<?>> entry : taskMap.entrySet()) {
				if (entry.getValue().cancel(true)) {
					taskMap.remove(entry.getKey());
					taskInfoMap.remove(entry.getKey());
					alreadyCanceled++;
				}
			}
			return Result.success("共计" + total + "个任务正在执行,已取消" + alreadyCanceled + "个。");
		} catch (Exception e) {
			log.error("[cancelAllTasks]", e);
			return Result.errorService();
		}
	}

	/**
	 * 添加任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@Transactional
	public Result addTask(@Valid @NotNull AddTask param) {
		try {
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
			if (param.getStartNow() == StatusEnum.ACTIVE.getVal()) {
				taskMap.put(task.getId(), scheduler.schedule(runnable, trigger));
				taskInfoMap.put(task.getId(), task);
			}
			return Result.success(task.getId());
		} catch (Exception e) {
			log.error("[addJob]", e);
			throw new ServiceException(ErrorCode.TASK_SERVICE_ERROR);
		}
	}

	/**
	 * 中止并删除任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	public Result deleteTask(@NotNull Long id) {
		try {
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
				boolean isTransactional = isTransactional(runnable);
				if (!future.cancel(isTransactional)) {
					if (isTransactional) {
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
		} catch (Exception e) {
			log.error("[removeJob]", e);
			throw new ServiceException(ErrorCode.TASK_SERVICE_ERROR);
		}
	}

	/**
	 * 中止任务
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	public Result cancelTask(@NotNull Long id) {
		try {
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
		} catch (Exception e) {
			log.error("[removeJob]", e);
			return Result.errorService();
		}
	}

	/**
	 * 更改任务活跃状态
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@Check
	@Transactional
	public Result changeStatus(@NotNull Long id, @LegalEnum(message = "是否立刻启动不能为空!",
		type = StatusEnum.class) Byte status) {
		try {
			Task task = taskMapper.selectByPrimaryKey(id);
			if (task == null) {
				return Result.fail("未找到该任务!");
			}
			task.setStatus(status);
			taskMapper.updateByPrimaryKeySelective(task);
			return Result.success();
		} catch (Exception e) {
			log.error("[removeJob]", e);
			throw new ServiceException(ErrorCode.TASK_SERVICE_ERROR);
		}
	}

}
