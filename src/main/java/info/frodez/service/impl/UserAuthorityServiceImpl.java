package info.frodez.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import info.frodez.config.security.realization.JwtTokenUtil;
import info.frodez.constant.user.UserStatusEnum;
import info.frodez.dao.mapper.user.PermissionMapper;
import info.frodez.dao.mapper.user.RoleMapper;
import info.frodez.dao.mapper.user.UserMapper;
import info.frodez.dao.model.user.Permission;
import info.frodez.dao.model.user.Role;
import info.frodez.dao.model.user.User;
import info.frodez.dao.param.user.LoginDTO;
import info.frodez.dao.result.user.PermissionInfo;
import info.frodez.dao.result.user.UserInfo;
import info.frodez.service.IUserAuthorityService;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import info.frodez.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户授权服务
 * @author Frodez
 * @date 2018-11-14
 */
@Slf4j
@Service
public class UserAuthorityServiceImpl implements IUserAuthorityService {
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	
	
	/**
	 * 获取用户授权信息
	 * @author Frodez
	 * @param userName 用户姓名(唯一)
	 * @date 2018-11-14
	 */
	public Result getUserInfoByName(String userName) {
		try {
			if(StringUtils.isBlank(userName)) {
				log.info("[getUserAuthority]", "用户姓名不能为空!");
				return new Result(ResultEnum.FAIL, "用户姓名不能为空!");
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", userName);
			User user = userMapper.selectOneByExample(example);
			if(user == null) {
				log.info("[getUserAuthority]", "未查询到用户信息!");
				return new Result(ResultEnum.FAIL, "未查询到用户信息!");
			}
			if(user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				log.info("[getUserAuthority]", "用户已禁用!");
				return new Result(ResultEnum.FAIL, "用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if(role == null) {
				log.info("[getUserAuthority]", "未查询到用户角色信息!");
				return new Result(ResultEnum.FAIL, "未查询到用户角色信息!");
			}
			List<PermissionInfo> permissionList = permissionMapper.getPermissions(user.getRoleId());
			UserInfo data = new UserInfo();
			data.setId(user.getId());
			data.setPassword(user.getPassword());
			data.setName(userName);
			data.setNickname(user.getNickname());
			data.setEmail(user.getEmail());
			data.setPhone(user.getPhone());
			data.setRoleId(user.getRoleId());
			data.setRoleName(role.getName());
			data.setRoleLevel(role.getLevel());
			data.setRoleDescription(role.getDescription());
			data.setPermissionList(permissionList);
			return new Result(ResultEnum.SUCCESS, data);
		} catch (Exception e) {
			log.error("[getUserAuthority]", e);
			return new Result(ResultEnum.FAIL);
		}
	}
	
	/**
	 * 获取所有权限信息
	 * @author Frodez
	 * @date 2018-12-04
	 */
	public Result getAllPermissions() {
		try {
			List<Permission> permissions = permissionMapper.selectAll();
			return new Result(ResultEnum.SUCCESS, permissions);
		} catch (Exception e) {
			log.error("[getAllPermissions]", e);
			return new Result(ResultEnum.FAIL);
		}
	}
	
	/**
	 * 用户登录
	 * @author Frodez
	 * @param LoginDTO 用户登录请求参数
	 * @date 2018-12-03
	 */
	public Result login(LoginDTO param) {
		try {
			String msg = ValidationUtil.validate(param);
			if(StringUtils.isBlank(msg)) {
				log.info("[login]", msg);
				return new Result(ResultEnum.FAIL, msg);
			}
			Example example = new Example(User.class);
			example.createCriteria().andEqualTo("name", param.getUsername());
			User user = userMapper.selectOneByExample(example);
			if(user == null) {
				log.info("[login]", "用户名或密码错误!");
				return new Result(ResultEnum.FAIL, "用户名或密码错误!");
			}
			//登陆时的密码未加密,需要加密再比较
			if(!passwordEncoder.encode(param.getPassword())
				.equals(user.getPassword())) {
				log.info("[login]", "用户名或密码错误!");
				return new Result(ResultEnum.FAIL, "用户名或密码错误!");
			}
			if(user.getStatus().equals(UserStatusEnum.FORBIDDEN.getValue())) {
				log.info("[login]", "用户已禁用!");
				return new Result(ResultEnum.FAIL, "用户已禁用!");
			}
			Role role = roleMapper.selectByPrimaryKey(user.getRoleId());
			if(role == null) {
				log.info("[login]", "未查询到用户角色信息!");
				return new Result(ResultEnum.FAIL, "未查询到用户角色信息!");
			}
			List<String> authorities = permissionMapper
				.getPermissions(role.getId()).stream()
				.map(PermissionInfo::getName).collect(Collectors.toList());
			String token = jwtTokenUtil.generate(param.getUsername(), authorities);
			return new Result(ResultEnum.SUCCESS, token);
		} catch (Exception e) {
			log.error("[login]", e);
			return new Result(ResultEnum.FAIL);
		}
	}

}
