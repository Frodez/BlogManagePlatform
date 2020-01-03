package frodez.dao.mapper.config;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.config.GlobalData;
import org.springframework.stereotype.Repository;

/**
 * @description 全局数据表
 * @table tb_global_data
 * @date 2020-01-01
 */
@Repository
public interface GlobalDataMapper extends DataMapper<GlobalData> {
}