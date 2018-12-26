package com.github.willisaway.user.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huaxun.core.base.BaseModel;

import lombok.Data;

@TableName("SM_ROLE_MENU")
@SuppressWarnings("serial")
@Data
public class SmRoleMenu extends BaseModel {
    private Long roleId;
    private Long menuId;
    @TableField("permission_")
    private String permission;
}
