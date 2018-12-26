package com.github.willisaway.user.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huaxun.core.base.BaseService;
import com.huaxun.core.base.ModuleReturn;
import com.huaxun.core.util.CheckObject;
import com.github.willisaway.user.dao.SmUserRoleMapper;
import com.github.willisaway.user.model.SmUserRole;

/**
 * SysUser服务实现类
 * @author ShenHuaJie
 * @version 2016-08-27 22:39:42
 */
@Service
@CacheConfig(cacheNames = "SmUserRole")
public class SmUserRoleService extends BaseService<SmUserRoleMapper,SmUserRole>{
	@Autowired
	private SmUserRoleMapper smUserRoleMapper;

	public void addUserRole(Long userId,Long roleId) {
		SmUserRole role = new SmUserRole();
		role.setRoleId(roleId);
		role.setUserId(userId);
		role.setDeletedFlag("0");
		this.update(role);
		return;
	}

	public void update(Map<String, Object> params) {
		SmUserRole role = new SmUserRole();
		role.setRowId(CheckObject.checkLongByObject(params.get("rowId")));
		role.setRoleId(CheckObject.checkLongByObject(params.get("roleId")));
		role.setUserId(CheckObject.checkLongByObject(params.get("userId")));
		role.setDeletedFlag("0");
		this.update(role);
		return;
	}

	/**
	 * @param roleIds
	 * @param userId
	 * @return
	 */
	public Boolean saveSmUserRoleGrant(String roleIds, String userId) {
		return true;
	}

	/**
	 * 查询未赋权角色
	 *
	 * @param userId
	 * @return
	 */
	public List querySmUserRoleUnCheck(Long userId) {
		return smUserRoleMapper.getUserForRole(userId);
	}

	/**
	 * 删除已赋权角色
	 *
	 * @param userId
	 * @return
	 */
	public Boolean deleteSmUserRoleCheck(String userId) {
		return true;
	}

	/**
	 * 角色赋用户
	 *
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public Boolean saveSmUserRoleByUser(String roleId, String userId) {
		return true;
	}

	/**
	 * 删除角色用户
	 *
	 * @param roleId
	 * @param selectRoleId
	 * @return
	 */
	public ModuleReturn deleteUserRole(Long roleId, Long userId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		Map params = new HashMap();
		params.put("roleId", roleId);
		params.put("userId", userId);
		List<SmUserRole> smUserRoles = queryAll(params);
		for(SmUserRole smUserRole:smUserRoles) {
			delete(smUserRole.getRowId(), -1L);
		}
		return objRtn;
	}

	/**
	 * 检查用户是否有角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public Boolean checkUserRole(Long userId, Long roleId) {
		Boolean bResult = false;
		Map<String, Object> params = new HashMap();
		params.put("userId", userId);
		params.put("roleId", roleId);
		List<SmUserRole> userRoles = queryAll(params);
		if (userRoles != null && userRoles.size() > 0) {
			bResult = true;
		}
		return bResult;
	}

	public List<SmUserRole> queryUserRoles(Long userId) {
		Map<String, Object> params = new HashMap();
		params.put("userId", userId);
		List<SmUserRole> userRoles = queryAll(params);
		return userRoles;
	}
}
