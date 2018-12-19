package info.frodez.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import info.frodez.dao.param.user.LoginDTO;
import info.frodez.service.IUserAuthorityService;
import info.frodez.util.result.Result;
import info.frodez.util.result.ResultEnum;
import info.frodez.util.validation.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Frodez
 * @date 2018-12-01
 */
@Slf4j
@RestController("/login")
public class LoginController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private IUserAuthorityService authorityService;
    
    @ResponseBody
    @RequestMapping
    public Result login(@RequestBody LoginDTO param) {
    	String msg = ValidationUtil.validate(param);
    	if(!StringUtils.isBlank(msg)) {
			log.info("[login]", msg);
			return new Result(ResultEnum.FAIL, msg);
		}
        final Authentication authentication = authenticationManager
        	.authenticate(new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    	return authorityService.login(param);
    }
	
}
