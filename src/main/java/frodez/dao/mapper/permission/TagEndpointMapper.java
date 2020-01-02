package frodez.dao.mapper.permission;

import frodez.config.mybatis.mapper.DataMapper;
import frodez.dao.model.table.permission.TagEndpoint;
import org.springframework.stereotype.Repository;

/**
 * @description 标签与接口对应表
 * @table tb_tag_endpoint
 * @date 2019-12-31
 */
@Repository
public interface TagEndpointMapper extends DataMapper<TagEndpoint> {
}