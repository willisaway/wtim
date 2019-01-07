package com.github.willisaway.user.dao;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SmUserRole;

import java.util.List;
import java.util.Map;

public interface SmUserRoleMapper extends BaseMapper<SmUserRole> {

    /**
     * sql 语句查询
     *
     * @param sql
     */
    void updateBySql(String sql);

    /**
     * userId 查询
     *
     * @param userId
     * @return
     */
    List getUserForRole(Long userId);
}