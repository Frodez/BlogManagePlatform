package info.frodez.dao.result.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 权限信息
 * @author Frodez
 * @date 2018-11-14
 */
@Data
public class PermissionInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 权限ID
	 */
	private Long id;
	
	/**
	 * 类型  1:GET  2:POST  3:DELETE  4:PUT
	 */
	private Byte type;
	
	/** 
     * 权限名称
     */
	private String name;
	
	/** 
     * 地址
     */
	private String url;
	
	/** 
     * 描述
     */
	private String description;

}
