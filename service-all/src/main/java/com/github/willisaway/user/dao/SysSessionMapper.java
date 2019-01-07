package com.github.willisaway.user.dao;

import java.util.List;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SysSession;

public interface SysSessionMapper extends BaseMapper<SysSession> {

    void deleteBySessionId(String sessionId);

    Long queryBySessionId(String sessionId);

    List<String> querySessionIdByAccount(String account);
}