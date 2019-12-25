package frodez.dao.model.user;

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
 * @description 页面资源权限表
 * @table tb_page_permission
 * @date 2019-12-24
 */
@Data
@Entity
@Table(name = "tb_page_permission")
@ApiModel(description = "页面资源权限返回数据")
public class PagePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 
     * ID(不能为空)
     */
    @Id
    @NotNull
    @Column(name = "id")
    @ApiModelProperty("ID")
    private Integer id;

    /** 
     * 创建时间(不能为空)
     */
    @NotNull
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 
     * 页面资源权限名称(不能为空)
     */
    @NotBlank
    @Length(max = 100)
    @Column(name = "name", length = 100)
    @ApiModelProperty("页面资源权限名称")
    private String name;

    /** 
     * 描述
     */
    @Nullable
    @Length(max = 1000)
    @Column(name = "description", length = 1000)
    @ApiModelProperty("描述")
    private String description;
}