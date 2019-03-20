package frodez.dao.mapper.task;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.task.Task;
import org.springframework.stereotype.Repository;

/**
 * @description 定时任务表
 * @table tb_spring_task
 * @date 2019-03-20
 */
@Repository
public interface TaskMapper extends DataMapper<Task> {
}