package com.github.willisaway.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huaxun.core.base.BaseService;
import com.huaxun.core.base.ModuleReturn;
import com.github.willisaway.user.dao.SmRoleMenuMapper;
import com.github.willisaway.user.model.SmRoleMenu;
import com.github.willisaway.user.model.SmUser;

/**
 * SmRoleMenu服务实现类
 *
 * @author zhaiss
 * @version 2017-06-12 11:28:42
 */
@Service
@CacheConfig(cacheNames = "SmRoleMenu")
public class SmRoleMenuService extends BaseService<SmRoleMenuMapper, SmRoleMenu> {

    @Autowired
    private SmRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SmUserRoleService sysUserRoleProvider;

//	@Autowired
//	private SysMenuOperationService sysMenuOperationProvider;

    @SuppressWarnings("unchecked")
    @Transactional
    public ModuleReturn save(ModuleReturn objRtnIn) {
        ModuleReturn objRtn = new ModuleReturn(1);
        List<SmRoleMenu> roleMenuList = (List<SmRoleMenu>) objRtnIn
                .getReturnPara("roleMenuList");
        SmUser sysUser = (SmUser) objRtnIn.getReturnPara("sysUser");
        Long userid = sysUser.getRowId();
        if (roleMenuList != null && roleMenuList.size() > 0) {
            Map<String, Object> delete_params = new HashMap<String, Object>();
            delete_params.put("roleId", roleMenuList.get(0).getRoleId());
            sysRoleMenuMapper.deleteRoleMenu(delete_params);// 根据角色id删除
            for (SmRoleMenu sysRoleMenu : roleMenuList) {
                this.update(sysRoleMenu, userid);
            }
            objRtn.setReturnMessage("保存成功");
        }
        return objRtn;
    }

//	@Transactional
//	public ModuleReturn defaultOperation(Map<String, Object> params) {
//		ModuleReturn objRtn = new ModuleReturn(1);
//		List<SysMenuOperation> operationList = sysMenuOperationProvider
//				.queryAll(params);
//		Long roleId = Long.valueOf(params.get("roleId").toString());
//		Long userId = Long.valueOf(params.get("userId").toString());
//		for (SysMenuOperation sysMenuOperation : operationList) {
//			SysRoleMenu sysRoleMenu = new SysRoleMenu();
//			sysRoleMenu.setPermission(sysMenuOperation.getPermissionCode());
//			sysRoleMenu.setMenuId(sysMenuOperation.getMenuId());
//			sysRoleMenu.setRoleId(roleId);
//			this.update(sysRoleMenu, userId);
//		}
//		return objRtn;
//	}

    /**
     * 根据menuId和roleIds保存中间表
     *
     * @param menuId
     * @param roleIds
     * @return
     */
    public Boolean saveSmRoleMenuByMenuId(String menuId, String roleIds) {
        return true;
    }

    /**
     * 删除角色菜单
     *
     * @param roleId
     * @param selectMenuIds
     * @return
     */
    public Boolean deleteUserMenu(String roleId, String selectMenuIds) {
        return true;
    }
}
