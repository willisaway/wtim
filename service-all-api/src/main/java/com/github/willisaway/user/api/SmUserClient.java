package com.github.willisaway.user.api;

import io.swagger.annotations.ApiOperation;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.core.support.login.ThirdPartyUser;
import com.github.willisaway.user.model.SmRole;
import com.github.willisaway.user.model.SmUser;

/**
 * SysUser服务接口
 * @author 
 * @version 
 */
@FeignClient(name = "service-all",path="/user")
public interface SmUserClient{
	
	@RequestMapping(value = "/get")
	public SmUser getCurrentUser();
	
	@RequestMapping(value = "/getCurrentUserId",method = RequestMethod.GET)
	public Long getCurrentUserId();
	
//    public Page<SysUser> queryBeans(Map<String, Object> params);

//    public String encryptPassword(String password);
//
//    /** 查询第三方帐号用户Id */
//    public Long queryUserIdByThirdParty(String openId, String provider);
//
//    /** 保存第三方帐号 */
//    public SysUser insertThirdPartyUser(ThirdPartyUser thirdPartyUser);
	@RequestMapping(value = "/public/detail")
	public SmUser detail(@RequestParam(value = "id", required = false) Long id);
	
	// 注册
	@ApiOperation(value = "注册")
	@RequestMapping(value = "public/read/register/{smscodenew}/{sendRowId}")
	public ModuleReturn register(@RequestBody SmUser sysUser,@PathVariable(value = "smscodenew") String smscodenew,@PathVariable(value = "sendRowId") Long sendRowId);
	
	// 当前用户
	@ApiOperation(value = "当前用户信息")
	@RequestMapping(value = "/private/read/current")
	public Object current();
	
	@ApiOperation(value = "新增用户角色")
	// @RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/update/addUserRole/{userId}/{roleId}")
	public void addUserRole(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId);
	
	@ApiOperation(value = "删除用户角色")
	@RequestMapping(value = "/update/deleteUserRole/{userId}/{roleId}")
	public ModuleReturn deleteUserRole(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId);
	
	@RequestMapping(value = "/public/queryByName")
	public SmUser queryByName(@RequestParam(value = "loginName", required = false) String loginName);
	

	// 修改密码
	@ApiOperation(value = "修改密码")
	// @RequiresPermissions("sys.user.update")
	@RequestMapping(value = "/private/update/password/{password}/{oldPassword}")
	public boolean updatePassword(ModelMap modelMap ,@PathVariable(value = "password") String password,@PathVariable(value = "oldPassword") String oldPassword);

    // 用户详细信息
	@ApiOperation(value = "用户详细信息")
	// @RequiresPermissions("sys.user.read")
	@RequestMapping(value = "/public/detail")
	public SmUser detail(ModelMap modelMap, @RequestParam(value = "id", required = false) Long id);    
	
	@ApiOperation(value = "重置密码")
	@RequestMapping(value = "public/reset/password/{phone}/{passWord}/{msg}/{sendRowId}")
	public ModuleReturn resetPassword(@PathVariable(value = "phone") String phone,@PathVariable(value = "passWord") String passWord,@PathVariable(value = "msg") String msg
			,@PathVariable(value = "sendRowId") Long sendRowId);
	
	@ApiOperation(value = "根据ID查询用户角色")
	@RequestMapping(value = "/public/queryUserRoles/{userId}")
	public List<SmRole> queryUserRoles(@PathVariable("userId") Long userId);
}
