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
 * @description 用户角色表
 * @table tb_role
 * @date 2019-03-06
 */
@Data
@Entity
@Table(name = "tb_role")
@ApiModel(description = "用户角色返回数据")
public class Role implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** 
     * 用户角色ID
     */
    @Id
    @NotNull
    @Column(name = "id")
    @ApiModelProperty(value = "用户角色ID")
    private Long id;

    /** 
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 
     * 角色名称
     */
    @NotNull
    @Column(name = "name", length = 255)
    @ApiModelProperty(value = "角色名称")
    private String name;

    /** 
     * 角色等级  0-9  0最高,9最低
     */
    @NotNull
    @Column(name = "level")
    @ApiModelProperty(value = "角色等级  0-9  0最高,9最低", example = "0")
    private Byte level = 0;

    /** 
     * 描述
     */
    @Column(name = "description", length = 1000)
    @ApiModelProperty(value = "描述")
    private String description;
}