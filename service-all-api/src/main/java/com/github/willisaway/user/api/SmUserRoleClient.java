package com.github.willisaway.user.api;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;


/**
 * SysUser服务接口
 * @author 
 * @version 
 */
@FeignClient(name = "service-user")
public interface SmUserRoleClient{

}
