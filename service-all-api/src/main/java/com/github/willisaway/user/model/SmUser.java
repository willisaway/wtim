package com.github.willisaway.user.model;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.github.willisaway.core.base.BaseModel;

import lombok.Data;

/**
 * SmUser实体类
 * @author liyg
 * @version 
 */
@TableName("SM_USER")
@SuppressWarnings("serial")
@Data
public class SmUser extends BaseModel {
	private String userCode;

    private String userName;

    private String password;

    private String avatar;
    
    private Long orgId;

    private String orgName;

    private String sex;

    private String email;

    private String phone;

    private String userType;
    
    @TableField(exist=false)
    private String userTypeText;
    
    @TableField(exist=false)
    private List<SmUserRole> smUserRole;
    
    private String statusFlag;
}
