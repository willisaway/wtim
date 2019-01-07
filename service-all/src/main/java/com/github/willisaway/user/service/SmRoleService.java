package com.github.willisaway.user.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.core.base.BaseService;
import com.github.willisaway.user.dao.SmRoleMapper;
import com.github.willisaway.user.dao.SmRoleMenuMapper;
import com.github.willisaway.user.model.SmRole;


/**
 * @author ShenHuaJie
 * @version 2016年5月31日 上午11:01:33
 */
@Service
@CacheConfig(cacheNames = "SmRole")
public class SmRoleService extends BaseService<SmRoleMapper,SmRole>{
    @Autowired
    private SmRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SmRoleMapper smRoleMapper;

    public Page<SmRole> query(Map<String, Object> params) {
        Page<Long> page = getPage(params);
        page.setRecords(baseMapper.selectIdByMap(page, params));
        return getPage(page);
    }

    public Page<SmRole> queryBean(Map<String, Object> params) {
        Page<Long> idPage = getPage(params);
        idPage.setRecords(baseMapper.selectIdByMap(idPage, params));
        Page<SmRole> pageInfo = getPage(idPage, SmRole.class);
        // 权限信息
        for (SmRole bean : pageInfo.getRecords()) {
            List<String> permissions = sysRoleMenuMapper.queryPermission(bean.getRowId());
            for (String permission : permissions) {
                if (StringUtils.isBlank(bean.getPermission())) {
                    bean.setPermission(permission);
                } else {
                    bean.setPermission(bean.getPermission() + ";" + permission);
                }
            }
        }
        return pageInfo;
    }
    
    public List<Map<String, String>> getPermissions(Long id) {
        return sysRoleMenuMapper.getPermissions(id);
    }

    /**
     * 获取已授权的用户
     *
     * @param roleId
     * @return
     */
    public List getAssingnUser(String roleId) {
        return smRoleMapper.getUserForRole(roleId);
    }

    /**
     * 获取未授权的用户
     *
     * @param roleId
     * @return
     */
    public List getNotAssingnUser(String roleId) {
        return smRoleMapper.getUserForRole(roleId);
    }


    /**
     * 授权菜单
     *
     * @param
     * @return
     */
    public Boolean doAssignMenu() {
        return true;
    }

}
