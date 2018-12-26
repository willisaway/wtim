/**
 * 
 */
package com.github.willisaway.user.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 
 */
@FeignClient(name = "service-user")
public interface SysSessionClient{
	@Transactional
	public void deleteBySessionId(final String sessionId);

	public List<String> querySessionIdByAccount(String account);

	public void delete(Long id);
	
	public void cleanExpiredSessions();
}
