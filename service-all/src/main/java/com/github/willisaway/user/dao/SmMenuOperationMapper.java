package com.github.willisaway.user.dao;

import java.util.List;
import java.util.Map;

import com.github.willisaway.core.base.BaseMapper;
import com.github.willisaway.user.model.SmMenuOperation;

public interface SmMenuOperationMapper extends BaseMapper<SmMenuOperation> {

	List<Long> selectMeunIdByMap(Map<String, Object> params);
}