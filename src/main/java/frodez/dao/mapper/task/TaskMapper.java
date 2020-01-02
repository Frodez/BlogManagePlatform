package frodez.dao.mapper.task;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.task.Task;
import org.springframework.stereotype.Repository;

/**
 * @description 定时任务表
 * @table tb_spring_task
 * @date 2019-03-20
 */
@Repository
public interface TaskMapper extends DataMapper<Task> {
}