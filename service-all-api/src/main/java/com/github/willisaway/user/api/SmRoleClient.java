package com.github.willisaway.user.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.user.model.SmRole;

@FeignClient(name = "service-user")
public interface SmRoleClient{
    public Page<SmRole> queryBean(Map<String, Object> params);

    /** 根据角色Id获取权限选项value */
    public List<Map<String, String>> getPermissions(Long id);
}
