package frodez.dao.model.table.config;

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
 * @description 角色设置表
 * @table tb_role_setting
 * @date 2020-01-01
 */
@Data
@Entity
@Table(name = "tb_role_setting")
@ApiModel(description = "角色设置信息")
public class RoleSetting implements Serializable {

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
     * 设置ID(不能为空)
     */
    @NotNull
    @Column(name = "setting_id")
    @ApiModelProperty("设置ID")
    private Long settingId;
}