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
 * @description 全局数据表
 * @table tb_global_data
 * @date 2020-01-01
 */
@Data
@Entity
@Table(name = "tb_global_data")
@ApiModel(description = "全局数据信息")
public class GlobalData implements Serializable {

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
     * 字段名(不能为空)
     */
    @NotBlank
    @Length(max = 100)
    @Column(name = "name", length = 100)
    @ApiModelProperty("字段名")
    private String name;

    /** 
     * 字段类型 1:bool 2:int 4:double 5:string(不能为空)
     */
    @NotNull
    @Column(name = "type")
    @ApiModelProperty("字段类型 1:bool 2:int 4:double 5:string")
    private Byte type;

    /** 
     * 字段内容(不能为空)
     */
    @NotBlank
    @Length(max = 1000)
    @Column(name = "content", length = 1000)
    @ApiModelProperty("字段内容")
    private String content;

    /** 
     * 描述
     */
    @Nullable
    @Length(max = 1000)
    @Column(name = "description", length = 1000)
    @ApiModelProperty("描述")
    private String description;
}