package com.github.willisaway.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.huaxun.core.base.BaseMapper;
import com.github.willisaway.user.model.SmUser;

public interface SmUserMapper extends BaseMapper<SmUser> {
    List<Long> selectIdByMap(@Param("cm") Map<String, Object> params);

    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     *
     * @param userId
     * @return
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据userId和roleId保存中间表
     *
     * @param userId
     * @param roleId
     * @return
     */
    int updateBySql(String userId, String roleId);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<SmUser> queryUserList();
}
