package com.github.willisaway.user.model;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huaxun.core.base.BaseModel;

import lombok.Data;

@TableName("SM_MENU")
@SuppressWarnings("serial")
@Data
public class SmMenu extends BaseModel {
	private String customType;

    private String menuCode;

    private String menuName;

    private String type;

    private Long parentId;

    private String describes;

    private String url;

    private String leafFlag;

    private String perms;

    private String icon;
	
    @TableField("permission_")
    private String permission;

    @TableField(exist=false)
    private Integer leaf = 1;
    @TableField(exist=false)
	private List<SmMenu> childMenus;
}