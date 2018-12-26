package com.github.willisaway.user.dao;

import com.huaxun.core.base.BaseMapper;
import com.github.willisaway.user.model.SmRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SmRoleMapper extends BaseMapper<SmRole> {

    /**
     * 通过role获取用户相关信息
     *
     * @param roleId
     * @return
     */
    public List<Map> getUserForRole(String roleId);

    /**
     * 通过sql执行
     *
     * @param sql
     * @return
     */
    public int updateBySql(@Param("sql") String sql);


}