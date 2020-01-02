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
 * @description 菜单与接口对应表
 * @table tb_menu_endpoint
 * @date 2019-12-31
 */
@Data
@Entity
@Table(name = "tb_menu_endpoint")
@ApiModel(description = "菜单与接口对应信息")
public class MenuEndpoint implements Serializable {

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
     * 菜单ID(不能为空)
     */
    @NotNull
    @Column(name = "menu_id")
    @ApiModelProperty("菜单ID")
    private Long menuId;

    /** 
     * 接口ID(不能为空)
     */
    @NotNull
    @Column(name = "endpoint_id")
    @ApiModelProperty("接口ID")
    private Long endpointId;
}