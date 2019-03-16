package frodez.dao.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @description 用户表
 * @table tb_user
 * @date 2019-03-15
 */
@Data
@Entity
@Table(name = "tb_user")
@ApiModel(description = "用户返回数据")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID(不能为空)
     */
    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "用户ID")
    private Long id;

    /** 
     * 创建时间(不能为空)
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 
     * 用户名(不能为空)
     */
    @Column(name = "name", length = 50)
    @ApiModelProperty(value = "用户名")
    private String name;

    /** 
     * 密码(不能为空)
     */
    @Column(name = "password", length = 2000)
    @ApiModelProperty(value = "密码")
    private String password;

    /** 
     * 昵称
     */
    @Nullable
    @Column(name = "nickname", length = 50)
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /** 
     * 邮箱地址
     */
    @Nullable
    @Column(name = "email", length = 255)
    @ApiModelProperty(value = "邮箱地址")
    private String email;

    /** 
     * 电话号码
     */
    @Nullable
    @Column(name = "phone", length = 255)
    @ApiModelProperty(value = "电话号码")
    private String phone;

    /** 
     * 用户状态  0:禁用  1:正常(不能为空,默认值:1)
     */
    @Column(name = "status")
    @ApiModelProperty(value = "用户状态  0:禁用  1:正常", example = "1")
    private Byte status = 1;

    /** 
     * 角色ID(不能为空)
     */
    @Column(name = "role_id")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}