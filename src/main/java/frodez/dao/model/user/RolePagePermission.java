package frodez.dao.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 角色页面资源权限表
 * @table tb_role_page_permission
 * @date 2019-12-24
 */
@Data
@Entity
@Table(name = "tb_role_page_permission")
@ApiModel(description = "角色页面资源权限返回数据")
public class RolePagePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 
     * ID(不能为空)
     */
    @Id
    @NotNull
    @Column(name = "id")
    @ApiModelProperty("ID")
    private Long id;

    /** 
     * 创建时间(不能为空)
     */
    @NotNull
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 
     * 角色ID(不能为空)
     */
    @NotNull
    @Column(name = "role_id")
    @ApiModelProperty("角色ID")
    private Long roleId;

    /** 
     * 页面资源权限ID(不能为空)
     */
    @NotNull
    @Column(name = "page_permission_id")
    @ApiModelProperty("页面资源权限ID")
    private Long pagePermissionId;
}