package com.github.willisaway.user.dao;

import org.apache.ibatis.annotations.Param;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SysUserThirdparty;

public interface SysUserThirdpartyMapper extends BaseMapper<SysUserThirdparty> {
    Long queryUserIdByThirdParty(@Param("provider") String provider, @Param("openId") String openId);
}