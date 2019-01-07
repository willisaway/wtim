package com.github.willisaway.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.willisaway.core.base.BaseService;
import com.github.willisaway.core.util.InstanceUtil;
import com.github.willisaway.user.dao.SysAuthorizeMapper;
import com.github.willisaway.user.dao.SmMenuMapper;
import com.github.willisaway.user.dao.SmRoleMenuMapper;
import com.github.willisaway.user.dao.SmUserMenuMapper;
import com.github.willisaway.user.dao.SmUserRoleMapper;
import com.github.willisaway.user.model.SmMenu;
import com.github.willisaway.user.model.SmRoleMenu;
import com.github.willisaway.user.model.SmUserMenu;
import com.github.willisaway.user.model.SmUserRole;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "sysAuthorize")
public class SysAuthorizeService extends BaseService<SmMenuMapper,SmMenu>{
	@Autowired
	private SmUserMenuMapper sysUserMenuMapper;
	@Autowired
	private SmUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SmRoleMenuMapper sysRoleMenuMapper;
	@Autowired
	private SysAuthorizeMapper sysAuthorizeMapper;
	@Autowired
	private SmMenuService sysMenuService;

	@Transactional
	@CacheEvict(value = { "getAuthorize", "sysPermission" }, allEntries = true)
	public void updateUserMenu(List<SmUserMenu> sysUserMenus) {
		sysAuthorizeMapper.deleteUserMenu(sysUserMenus.get(0).getUserId());
		for (SmUserMenu sysUserMenu : sysUserMenus) {
			sysUserMenuMapper.insert(sysUserMenu);
		}
	}

	@Transactional
	@CacheEvict(value = { "getAuthorize", "sysPermission" }, allEntries = true)
	public void updateUserRole(List<SmUserRole> sysUserRoles) {
		sysAuthorizeMapper.deleteUserRole(sysUserRoles.get(0).getUserId());
		for (SmUserRole sysUserRole : sysUserRoles) {
			sysUserRoleMapper.insert(sysUserRole);
		}
	}

	@Transactional
	@CacheEvict(value = { "getAuthorize", "sysPermission" }, allEntries = true)
	public void updateRoleMenu(List<SmRoleMenu> sysRoleMenus) {
		sysAuthorizeMapper.deleteRoleMenu(sysRoleMenus.get(0).getRoleId());
		for (SmRoleMenu sysRoleMenu : sysRoleMenus) {
			sysRoleMenuMapper.insert(sysRoleMenu);
		}
	}

	@Cacheable(value = "getAuthorize")
	public List<SmMenu> queryAuthorizeByUserId(Long userId) {
		List<Long> menuIds = sysAuthorizeMapper.getAuthorize(userId);
		List<SmMenu> menus = sysMenuService.getList(menuIds, SmMenu.class);
		Map<Long, List<SmMenu>> map = InstanceUtil.newHashMap();
		for (SmMenu sysMenu : menus) {
			if (map.get(sysMenu.getParentId()) == null) {
				List<SmMenu> menuBeans = InstanceUtil.newArrayList();
				map.put(sysMenu.getParentId(), menuBeans);
			}
			map.get(sysMenu.getParentId()).add(sysMenu);
		}
		List<SmMenu> result = InstanceUtil.newArrayList();
		for (SmMenu sysMenu : menus) {
			if (sysMenu.getParentId() == 0) {
				sysMenu.setLeaf(0);
				sysMenu.setChildMenus(getChildMenu(map, sysMenu.getRowId()));
				result.add(sysMenu);
			}
		}
		return result;
	}

	// 递归获取子菜单
	private List<SmMenu> getChildMenu(Map<Long, List<SmMenu>> map, Long id) {
		List<SmMenu> menus = map.get(id);
		if (menus != null) {
			for (SmMenu sysMenu : menus) {
				sysMenu.setChildMenus(getChildMenu(map, sysMenu.getRowId()));
			}
		}
		return menus;
	}

	@Cacheable("sysPermission")
	public List<String> queryPermissionByUserId(Long userId) {
		return sysAuthorizeMapper.queryPermissionByUserId(userId);
	}
}
