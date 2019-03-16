package frodez.dao.param.user;

import frodez.config.aop.validation.annotation.common.LegalEnum;
import frodez.util.constant.user.PermissionTypeEnum;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 修改权限请求参数
 * @author Frodez
 * @date 2019-03-17
 */
@Data
@NoArgsConstructor
public class UpdatePermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限ID
	 */
	@NotNull(message = "权限ID不能为空!")
	private Long id;

	/**
	 * 类型 0:ALL 1:GET 2:POST 3:DELETE 4:PUT
	 */
	@LegalEnum(message = "类型错误!", type = PermissionTypeEnum.class)
	private Byte type;

	/**
	 * 权限名称
	 */
	@Length(max = 255)
	private String name;

	/**
	 * 地址
	 */
	private String url;

	/**
	 * 描述
	 */
	@Length(max = 1000)
	private String description;

}
