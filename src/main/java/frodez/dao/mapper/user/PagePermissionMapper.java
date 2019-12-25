package frodez.dao.mapper.user;

import frodez.config.mybatis.DataMapper;
import frodez.dao.model.user.PagePermission;
import org.springframework.stereotype.Repository;

/**
 * @description 页面资源权限表
 * @table tb_page_permission
 * @date 2019-12-24
 */
@Repository
public interface PagePermissionMapper extends DataMapper<PagePermission> {
}