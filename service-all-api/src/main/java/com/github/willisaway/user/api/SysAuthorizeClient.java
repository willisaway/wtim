package com.github.willisaway.user.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import com.github.willisaway.user.model.SmMenu;
import com.github.willisaway.user.model.SmRoleMenu;
import com.github.willisaway.user.model.SmUserMenu;
import com.github.willisaway.user.model.SmUserRole;

@FeignClient(name = "service-user")
public interface SysAuthorizeClient{

    public void updateUserMenu(List<SmUserMenu> sysUserMenus);

    public void updateUserRole(List<SmUserRole> sysUserRoles);

    public void updateRoleMenu(List<SmRoleMenu> sysRoleMenus);

    public List<SmMenu> queryAuthorizeByUserId(Long userId);

    public List<String> queryPermissionByUserId(Long userId);
}
