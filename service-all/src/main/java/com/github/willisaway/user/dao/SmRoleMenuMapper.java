package com.github.willisaway.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SmRoleMenu;

public interface SmRoleMenuMapper extends BaseMapper<SmRoleMenu> {
	List<String> queryPermission(@Param("roleId") Long id);

	List<Map<String, String>> getPermissions(@Param("roleId") Long id);

	/**
	 * 删除单品
	 *
	 * @param params
	 */
	void deleteRoleMenu(Map<String, Object> params);
}
