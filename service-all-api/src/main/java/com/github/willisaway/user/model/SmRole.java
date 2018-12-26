package com.github.willisaway.user.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.huaxun.core.base.BaseModel;

import lombok.Data;

@TableName("SM_ROLE")
@SuppressWarnings("serial")
@Data
public class SmRole extends BaseModel {
	private String roleCode;
    private String roleName;
    private String roleType;
    @TableField(exist=false)
    private String permission;
}