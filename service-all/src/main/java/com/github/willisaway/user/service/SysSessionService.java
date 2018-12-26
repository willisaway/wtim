package com.github.willisaway.user.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.huaxun.core.base.BaseService;
import com.huaxun.core.util.InstanceUtil;
import com.huaxun.core.util.RedissonUtil;
import com.github.willisaway.user.dao.SysSessionMapper;
import com.github.willisaway.user.model.SysSession;

/**
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "sysSession")
public class SysSessionService extends BaseService<SysSessionMapper,SysSession>{
	@Autowired
	private SysSessionMapper sessionMapper;

	@CachePut
	@Transactional
	public SysSession update(SysSession record) {
		if (record.getRowId() == null) {
			record.setUpdateTime(new Date());
			Long id = sessionMapper.queryBySessionId(record.getSessionId());
			if (id != null) {
				baseMapper.updateById(record);
			} else {
				record.setCreateTime(new Date());
				baseMapper.insert(record);
			}
		} else {
			baseMapper.updateById(record);
		}
		return record;
	}

	@CacheEvict
	public void delete(final Long id) {
		baseMapper.deleteById(id);
	}

	// 系统触发,由系统自动管理缓存
	public void deleteBySessionId(final String sessionId) {
		sessionMapper.deleteBySessionId(sessionId);
	}

	public Page<SysSession> query(Map<String, Object> params) {
		Page<Long> page = getPage(params);
		page.setRecords(baseMapper.selectIdByMap(page, params));
		return getPage(page);
	}

	public List<String> querySessionIdByAccount(String account) {
		return sessionMapper.querySessionIdByAccount(account);
	}

	//
	public void cleanExpiredSessions() {
		String key = "spring:session:" ;//+ PropertiesUtil.getString("session.redis.namespace") + ":sessions:expires:";
		Map<String, Object> columnMap = InstanceUtil.newHashMap();
		List<Long> ids = baseMapper.selectIdByMap(new RowBounds(), columnMap);
		List<SysSession> sessions = getList(ids);
		for (SysSession sysSession : sessions) {
			//logger.info("检查SESSION : {}", sysSession.getSessionId());
			if (!RedissonUtil.exists(key + sysSession.getSessionId())) {
				//logger.info("移除SESSION : {}", sysSession.getSessionId());
				baseMapper.deleteById(sysSession.getRowId());
			}
		}
	}
}