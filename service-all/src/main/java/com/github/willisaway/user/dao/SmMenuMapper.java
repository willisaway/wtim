package com.github.willisaway.user.dao;

import java.util.List;
import java.util.Map;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SmMenu;

public interface SmMenuMapper extends BaseMapper<SmMenu> {
    /** 获取所有权限 */
    List<Map<String, String>> getPermissions();

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     * @return
     */
    List<SmMenu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     *
     * @return
     */
    List<SmMenu> queryNotButtonList();

    /**
     * 查询所有的菜单
     */
    List<SmMenu> getAllMenuList();

    /**
     * 根据父菜单，查询子菜单
     *
     * @param userId 用户ID
     */
    List<SmMenu> getMenuListByUserId(Long userId);
}