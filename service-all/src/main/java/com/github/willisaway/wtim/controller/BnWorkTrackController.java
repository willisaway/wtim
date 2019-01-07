package com.github.willisaway.wtim.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.wtim.model.BnWorkTrack;
import com.github.willisaway.wtim.service.BnWorkTrackService;
import com.github.willisaway.core.base.ModuleReturn;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "工作跟踪管理", description = "工作跟踪管理")
@RequestMapping(value = "worktrack")
public class BnWorkTrackController {
	@Autowired
	BnWorkTrackService bnWorkTrackService;
	
	@ApiOperation(value = "查询（分页）")
	@RequestMapping("/private/query")
	public Page<BnWorkTrack> query(@RequestBody Map<String, Object> params) {
		return  bnWorkTrackService.query(params);
	}
	
	@ApiOperation(value = "查询单条")
	@RequestMapping("/private/queryOne")
	public BnWorkTrack queryOne(@RequestBody Map<String, Object> params) {
		return  bnWorkTrackService.queryOne(params);
	}
	
	@ApiOperation(value = "新增或修改", notes = "新增或修改")
	@RequestMapping(value = "/private/update", method = RequestMethod.POST)
	public ModuleReturn update(@RequestBody BnWorkTrack bnWorkTrack) {
		ModuleReturn objRtn = new ModuleReturn(1);
		try {
			if (objRtn.getReturnValue() > 0) {
				bnWorkTrack = bnWorkTrackService.update(bnWorkTrack);
				objRtn.setReturnPara("data", bnWorkTrack);
			}
		} catch (Exception e) {
			e.printStackTrace();
			objRtn.setReturnValue(-1, "保存失败");
		}
		return objRtn;
	}
}
