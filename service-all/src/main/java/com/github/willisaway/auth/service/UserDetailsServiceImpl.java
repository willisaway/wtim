package com.github.willisaway.auth.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.willisaway.core.util.RequestUtil;
import com.github.willisaway.user.model.SmUser;
import com.github.willisaway.user.model.User;
import com.github.willisaway.user.service.SmUserRoleService;
import com.github.willisaway.user.service.SmUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	SmUserService sysUserService;
	@Autowired
	SmUserRoleService smUserRoleService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SmUser sysuser = sysUserService.queryByName(username);
		
		if(sysuser == null) {
			throw new UsernameNotFoundException("不存在的用户");
		}
		
		User user = null;
		if(sysuser!=null) {
			Set<GrantedAuthority> grantedAuthorities=sysUserService.getAuthorities(sysuser.getRowId());
			user=new User(sysuser.getUserCode(),sysuser.getUserName(),sysuser.getPassword(),grantedAuthorities);
		}
		return user;
	}

}
