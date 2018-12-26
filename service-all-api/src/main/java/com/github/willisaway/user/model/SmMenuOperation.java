package com.github.willisaway.user.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.huaxun.core.base.BaseModel;

@TableName("sm_menu_operation")
@SuppressWarnings("serial")
public class SmMenuOperation extends BaseModel {
	private Long menuId;

	private String isDefault;

	private String permissionCode;

	private String permissionName;

	private String permissionDesc;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault == null ? null : isDefault.trim();
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode == null ? null : permissionCode
				.trim();
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName == null ? null : permissionName
				.trim();
	}

	public String getPermissionDesc() {
		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc == null ? null : permissionDesc
				.trim();
	}
}