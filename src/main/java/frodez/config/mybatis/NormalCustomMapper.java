package frodez.config.mybatis;

import frodez.config.mybatis.count.CountMapper;
import frodez.config.mybatis.equal.EqualMapper;
import frodez.config.mybatis.ids.IdsMapper;
import frodez.config.mybatis.in.InMapper;
import frodez.config.mybatis.partial.SelectPartialMapper;

/**
 * 标准自定义mapper方法
 * @author Frodez
 * @date 2019-12-25
 */
public interface NormalCustomMapper<T> extends IdsMapper<T>, EqualMapper<T>, InMapper<T>, SelectPartialMapper, CountMapper {

}
