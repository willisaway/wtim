package com.github.willisaway.user.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.core.base.BaseController;
import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.common.file.util.UploadUtil;
import com.github.willisaway.core.util.WebUtil;
import com.github.willisaway.user.api.SmUserClient;
import com.github.willisaway.user.model.SmRole;
import com.github.willisaway.user.model.SmUser;
import com.github.willisaway.user.model.SmUserRole;
import com.github.willisaway.user.service.SmUserRoleService;
import com.github.willisaway.user.service.SysAuthorizeService;
import com.github.willisaway.user.service.SmUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/user")
@Api(value = "用户服务", description = "用户服务")
public class SmUserController extends BaseController{
	@Autowired
	private SmUserService sysUserService;
	@Autowired
	private SmUserRoleService smUserRoleService;
	@Autowired
	private SysAuthorizeService authorizeService;
	@Autowired
	SmUserClient userClient;
	
	@RequestMapping(value="/principal/get",method = RequestMethod.GET)
    public Principal get(Principal user){
        return user;
    }
	@RequestMapping(value = "/getCurrentUserId",method = RequestMethod.GET)
	public Long getCurrentUserId(Principal user) {
		SmUser sysUser = sysUserService.queryByName(user.getName());
		return sysUser.getRowId();
	}
	@ApiOperation(value = "当前用户信息")
	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public SmUser current(Principal user) {
		SmUser sysUser = sysUserService.queryByName(user.getName());
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		return sysUser; 
	}
	
	@ApiOperation(value = "校验手机号是否被注册")
	@RequestMapping(value = "/public/queryPhone/{phone}",method = RequestMethod.GET)
	public boolean queryPhone(@PathVariable(value = "phone") String phone) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("phone", phone);
		List<SmUser> sysUser = sysUserService.queryAll(param);
		if (sysUser.size()> 0) {
			return true;
		}
		return false;
	}
	
	// 修改用户信息
	@ApiOperation(value = "修改用户信息")
	// @RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/update")
	public Object update(HttpServletRequest request, ModelMap modelMap, @RequestBody SmUser sysUser) {
		if (StringUtils.isNotBlank(sysUser.getAvatar()) && !sysUser.getAvatar().contains("/")) {
			String filePath = UploadUtil.getUploadDir(request) + sysUser.getAvatar();
			// String avatar = UploadUtil.remove2DFS("sysUser", "user" +
			// sysUser.getId(), filePath).getRemotePath();
			String avatar = UploadUtil.remove2Sftp(filePath, "user" + sysUser.getRowId());
			sysUser.setAvatar(avatar);
		}
		sysUserService.updateUserInfo(sysUser);
		return setSuccessModelReturn(modelMap);
	}
	
	// 修改密码
	@ApiOperation(value = "修改密码")
	// @RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/private/update/password/{password}/{oldPassword}")
	public boolean updatePassword(ModelMap modelMap ,@PathVariable(value = "password") String password,@PathVariable(value = "oldPassword") String oldPassword) {
		Long id = userClient.getCurrentUserId();
		SmUser smUser = sysUserService.queryById(id);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(!encoder.matches(oldPassword, smUser.getPassword())){
			return false;
		}else{
			sysUserService.updatePassword(id, password);
			return true;
		}		
	}
	

	@ApiOperation(value = "新增用户角色")
	@RequestMapping(value = "/update/addUserRole/{userId}/{roleId}")
	public void addUserRole(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId) {
		smUserRoleService.addUserRole(userId,roleId);
	}
	
	@ApiOperation(value = "删除用户角色")
	@RequestMapping(value = "/update/deleteUserRole/{userId}/{roleId}")
	public ModuleReturn deleteUserRole(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId) {
		return smUserRoleService.deleteUserRole(roleId, userId);
	}
	// 用户详细信息
	@ApiOperation(value = "用户详细信息")
	// @RequiresPermissions("sys.user.read")
	@RequestMapping(value = "/public/detail")
	public SmUser detail(ModelMap modelMap, @RequestParam(value = "id", required = false) Long id) {
		SmUser sysUser = sysUserService.queryById(id);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		return sysUser;
	}
	
	// 用户管理
	@ApiOperation(value = "用户查询、用户查询,分页参数pageNum-当前第几页,pageSize-每页大小,roleId-用户类型1-一般用户,2-客服,3-教师.loginName-登录名")
	@PostMapping(value = "/private/query")
	public Page<SmUser> query(@RequestBody Map<String, Object> params) {			
	    return sysUserService.queryPage(params);
	}
	
	// 当前用户
	@ApiOperation(value = "当前用户信息")
	@RequestMapping(value = "/private/read/current")
	public Object current() {
		Long id = userClient.getCurrentUserId();
		SmUser sysUser = sysUserService.queryById(id);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		/*List<SmMenu> menus = authorizeService.queryAuthorizeByUserId(id);*/
		/*modelMap.put("menus", menus);*/
		return sysUser;
	}
	
	@ApiOperation(value = "重置密码")
	@RequestMapping(value = "public/reset/password/{phone}/{passWord}/{msg}/{sendRowId}")
	public ModuleReturn resetPassword(@PathVariable(value = "phone") String phone,@PathVariable(value = "passWord") String passWord,@PathVariable(value = "msg") String msg
			,@PathVariable(value = "sendRowId") Long sendRowId) {
		return sysUserService.newPassword(phone,passWord,msg,sendRowId);
	}
	
	// 注册
	@ApiOperation(value = "注册")
	@RequestMapping(value = "public/read/register/{smscodenew}/{sendRowId}")
	public ModuleReturn register(@RequestBody SmUser sysUser,@PathVariable(value = "smscodenew") String smscodenew,@PathVariable(value = "sendRowId") Long sendRowId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		if(objRtn.getReturnValue()>0){
			sysUserService.register(sysUser);
			objRtn.setReturnValue(1,"注册成功");
		}
		return objRtn;
	}
	
	// 用户详细信息
	@ApiOperation(value = "根据登陆名查找用户")
	//@RequiresPermissions("sys.user.read")
	@RequestMapping(value = "/public/queryByName")
	public SmUser queryByName(@RequestParam(value = "loginName", required = false) String loginName) {
		SmUser sysUser = sysUserService.queryByName(loginName);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		return sysUser;
	}
	
	//
	@ApiOperation(value = "根据ID查询用户角色")
	@RequestMapping(value = "/public/queryUserRoles/{userId}")
	public List<SmRole> queryUserRoles(@PathVariable("userId") Long userId) {
		return sysUserService.queryUserRoles(userId);
	}
}
