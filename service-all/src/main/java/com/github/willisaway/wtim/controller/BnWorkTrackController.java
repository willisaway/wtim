package com.github.willisaway.wtim.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.wtim.model.BnWorkTrack;
import com.github.willisaway.wtim.service.BnWorkTrackService;
import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.core.context.ContextHolder;
import com.github.willisaway.core.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "工作跟踪管理", description = "工作跟踪管理")
@RequestMapping(value = "worktrack")
public class BnWorkTrackController {
	@Autowired
	BnWorkTrackService bnWorkTrackService;
	
	//@PreAuthorize("hasAuthority('.RR2')")
	@ApiOperation(value = "查询（分页）")
	@RequestMapping("/private/query")
	public Page<BnWorkTrack> query(@RequestBody Map<String, Object> params) {
	    ContextHolder.getUserId();
	    //throw new BusinessException("出错啦");
		return  bnWorkTrackService.queryPage(params);
	}
	
	@ApiOperation(value = "查询单条")
	@RequestMapping("/private/queryOne")
	public BnWorkTrack queryOne(@RequestBody Map<String, Object> params) {
		return  bnWorkTrackService.queryOne(params);
	}
	
	@ApiOperation(value = "查询单条")
    @RequestMapping("/public/queryById/{rowId}")
    public BnWorkTrack queryById(@PathVariable("rowId") Long rowId) {
        return  bnWorkTrackService.queryById(rowId);
    }
	
	@ApiOperation(value = "新增或修改", notes = "新增或修改")
	@RequestMapping(value = "/private/update", method = RequestMethod.POST)
	public ModuleReturn update(@RequestBody BnWorkTrack bnWorkTrack) {
		ModuleReturn objRtn = new ModuleReturn();
		try {
		    bnWorkTrack = bnWorkTrackService.update(bnWorkTrack);
            objRtn.putData("data", bnWorkTrack);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("保存失败");
		}
		return objRtn;
	}
}
