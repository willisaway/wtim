package com.github.willisaway.user.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "service-user")
public interface SmMenuClient{

    /** 获取所有权限选项(value-text) */
	
    public List<Map<String, String>> getPermissions();
}
