package frodez.config.mybatis.mapper;

import frodez.config.mybatis.mapper.count.CountMapper;
import frodez.config.mybatis.mapper.equal.EqualMapper;
import frodez.config.mybatis.mapper.ids.IdsMapper;
import frodez.config.mybatis.mapper.in.InMapper;
import frodez.config.mybatis.mapper.partial.SelectPartialMapper;

/**
 * 标准自定义mapper方法
 * @author Frodez
 * @date 2019-12-25
 */
public interface NormalCustomMapper<T> extends IdsMapper<T>, EqualMapper<T>, InMapper<T>, SelectPartialMapper, CountMapper {

}
