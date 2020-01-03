package frodez.dao.model.table.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 角色与标签对应表
 * @table tb_role_tag
 * @date 2019-12-31
 */
@Data
@Entity
@Table(name = "tb_role_tag")
@ApiModel(description = "角色与标签对应信息")
public class RoleTag implements Serializable {

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
     * 角色ID(不能为空)
     */
    @NotNull
    @Column(name = "role_id")
    @ApiModelProperty("角色ID")
    private Long roleId;

    /** 
     * 标签ID(不能为空)
     */
    @NotNull
    @Column(name = "tag_id")
    @ApiModelProperty("标签ID")
    private Long tagId;
}