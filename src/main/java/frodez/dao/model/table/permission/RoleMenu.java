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
 * @description 角色与菜单对应表
 * @table tb_role_menu
 * @date 2019-12-31
 */
@Data
@Entity
@Table(name = "tb_role_menu")
@ApiModel(description = "角色与菜单对应信息")
public class RoleMenu implements Serializable {

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
     * 菜单ID(不能为空)
     */
    @NotNull
    @Column(name = "menu_id")
    @ApiModelProperty("菜单ID")
    private Long menuId;
}