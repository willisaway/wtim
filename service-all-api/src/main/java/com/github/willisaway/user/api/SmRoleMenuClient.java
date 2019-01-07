package com.github.willisaway.user.api;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

import com.github.willisaway.core.base.ModuleReturn;

@FeignClient(name = "service-user")
public interface SmRoleMenuClient {

	/**
	 * 新增/修改
	 */
	ModuleReturn save(ModuleReturn objRtnIn);

	/**
	 * 给角色绑定默认的操作
	 */
	ModuleReturn defaultOperation(Map<String, Object> params);
}
