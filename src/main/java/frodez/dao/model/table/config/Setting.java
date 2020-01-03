package frodez.dao.model.table.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * @description 设置表
 * @table tb_setting
 * @date 2019-12-30
 */
@Data
@Entity
@Table(name = "tb_setting")
@ApiModel(description = "设置信息")
public class Setting implements Serializable {

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
     * 设置关键字(不能为空)
     */
    @NotBlank
    @Length(max = 100)
    @Column(name = "name", length = 100)
    @ApiModelProperty("设置关键字")
    private String name;

    /** 
     * 默认拥有设置角色最低等级（0-9且0最高9最低）(不能为空)
     */
    @NotNull
    @Column(name = "default_level")
    @ApiModelProperty("默认拥有设置角色最低等级（0-9且0最高9最低）")
    private Byte defaultLevel;

    /** 
     * 描述
     */
    @Nullable
    @Length(max = 1000)
    @Column(name = "description", length = 1000)
    @ApiModelProperty("描述")
    private String description;
}