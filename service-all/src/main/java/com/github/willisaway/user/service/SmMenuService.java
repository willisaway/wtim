package com.github.willisaway.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.huaxun.core.base.BaseService;
import com.github.willisaway.user.api.SmUserClient;
import com.github.willisaway.user.dao.SmMenuMapper;
import com.github.willisaway.user.model.SmMenu;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "SmMenu")
public class SmMenuService extends BaseService<SmMenuMapper,SmMenu>{
    @Autowired
    private SmMenuMapper smMenuMapper;
    @Autowired
	SmUserClient userClient;
//    @Autowired
//    private SysDicService sysDicProvider;

    public Page<SmMenu> query(Map<String, Object> params) {
        Page<Long> idPage = getPage(params);
        idPage.setRecords(baseMapper.selectIdByMap(idPage, params));
        Page<SmMenu> pageInfo = getPage(idPage);
//        Map<String, String> menuTypeMap = sysDicProvider.queryDicByDicIndexKey("MENUTYPE");
//        for (SysMenu sysMenu : pageInfo.getRecords()) {
//            if (sysMenu.getMenuType() != null) {
//                sysMenu.setRemark(menuTypeMap.get(sysMenu.getMenuType().toString()));
//            }
//        }
        return pageInfo;
    }
    
    public List<Map<String, String>> getPermissions() {
        return smMenuMapper.getPermissions();
    }

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     * @param menuIds  用户菜单ID
     * @return
     */
    public List<SmMenu> queryListParentId(Long parentId, String menuIds) {
        return smMenuMapper.queryListParentId(parentId);
    }

    /**
     * 获取不包含按钮的菜单列表
     *
     * @return
     */
    public List<SmMenu> queryNotButtonList() {
        return smMenuMapper.queryNotButtonList();
    }

    /**
     * 获取用户菜单列表
     *
     * @return
     */
    public List<SmMenu> getUserMenuList() {

//        long userId = UserUtil.getUserId();
        Long userId = userClient.getCurrentUserId();
        List<SmMenu> menuList = new ArrayList<SmMenu>();
        //系统管理员，拥有最高权限
        if (userId == 1) {
            menuList = smMenuMapper.getAllMenuList();
        } else {
            menuList = smMenuMapper.getMenuListByUserId(userId);
        }
        //用户菜单列表
        //用户菜单列表
//        List<AdMenu> menuIdList = MenuTreeUtil.mergeTreeNode(menuList, 0L);
        return menuList;
    }

    /**
     * 获取所有菜单列表
     *
     * @param menuIdList 需要递归方法显示目录
     * @return
     */
    public List<SmMenu> getAllMenuList(List<Long> menuIdList) {
        List<SmMenu> subMenuList = new ArrayList<SmMenu>();
        return subMenuList;
    }

}
