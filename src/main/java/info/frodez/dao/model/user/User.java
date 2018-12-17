package info.frodez.dao.model.user;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description 用户表
 * @table tb_user
 * @date 2018-11-26
 */
@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    /** 
     * 主键ID
     */
    @Id
    @NotNull
    @Column(name = "id")
    private Long id;

    /** 
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    /** 
     * 用户名
     */
    @NotNull
    @Column(name = "name", length = 50)
    private String name;

    /** 
     * 密码
     */
    @NotNull
    @Column(name = "password", length = 2000)
    private String password;

    /** 
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /** 
     * 邮箱地址
     */
    @Column(name = "email", length = 255)
    private String email;

    /** 
     * 电话号码
     */
    @Column(name = "phone", length = 255)
    private String phone;

    /** 
     * 用户状态  0:禁用  1:正常
     */
    @NotNull
    @Column(name = "status")
    private Byte status;

    /** 
     * 角色ID
     */
    @NotNull
    @Column(name = "role_id")
    private Long roleId;

    private static final long serialVersionUID = 1L;
}