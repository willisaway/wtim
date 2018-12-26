package com.github.willisaway.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.huaxun.core.base.BaseService;
import com.github.willisaway.user.dao.SmMenuOperationMapper;
import com.github.willisaway.user.model.SmMenuOperation;
import com.github.willisaway.user.model.SmMenuOperationForm;

@Service
@CacheConfig(cacheNames = "SmMenuOperation")
public class SmMenuOperationService extends BaseService<SmMenuOperationMapper,SmMenuOperation>{
	@Autowired
	private SmMenuOperationMapper sysMenuOperationMapper;

	@Autowired
	private SmMenuService sysMenuProvider;

	public List<SmMenuOperationForm> getMenuOperationList(
			Map<String, Object> params) {
		params.put("customCondition", "group by menu_id");
		List<Long> menuIds = sysMenuOperationMapper.selectMeunIdByMap(params);
		List<SmMenuOperationForm> sysMenuOperationFormList = new ArrayList<SmMenuOperationForm>();
		for (Long menuId : menuIds) {
			SmMenuOperationForm sysMenuOperationForm = new SmMenuOperationForm();
			sysMenuOperationForm.setMenuId(menuId);
			sysMenuOperationForm.setMenuName(sysMenuProvider.queryById(menuId)
					.getMenuName());
			Map<String, Object> operation_params = new HashMap<String, Object>();
			operation_params.put("menuId", menuId);
			List<SmMenuOperation> optionList = this.queryAll(operation_params);
			sysMenuOperationForm.setOption(optionList);
			sysMenuOperationFormList.add(sysMenuOperationForm);
		}
		return sysMenuOperationFormList;
	}
}
