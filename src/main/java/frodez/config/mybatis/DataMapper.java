package frodez.config.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 基础Mapper
 * @author Frodez
 * @date 2018-12-13
 */
public interface DataMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
