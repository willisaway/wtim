package com.github.willisaway.file.controller;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.willisaway.core.base.BaseController;
import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.core.stream.BufferedServletRequestWrapper;
import com.github.willisaway.core.util.LockUtil;
import com.github.willisaway.core.util.WebUtil;
import com.github.willisaway.wtim.model.Affix;
import com.github.willisaway.file.service.AffixService;
import com.github.willisaway.auth.api.SmUserClient;

/**
 * @Description:附件控制器
 * @author
 * @time:2017年8月4日 下午2:16:07
 */
// @CrossOrigin
@RestController
@RequestMapping(value = "affix")
public class AffixController extends BaseController<AffixService,Affix> {
	@Autowired
	private AffixService affixService;
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/public/uploads")
	public List<ModuleReturn> uploadspublic(StandardMultipartHttpServletRequest multipartrequest,HttpServletResponse response) throws Exception{
		return uploads(multipartrequest, response);
	}
	
	// @CrossOrigin(allowCredentials="true", allowedHeaders="*",
	// methods={RequestMethod.GET,
	// RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
	// RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
	@SuppressWarnings("rawtypes")
	@PostMapping("uploads")
	public List<ModuleReturn> uploads(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModuleReturn objRtn = new ModuleReturn();
		// 解析非文件参数
		Enumeration params = request.getParameterNames();
		Map<String, Object> param = new HashMap<String, Object>();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			String value = request.getParameter(name);
			value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
			param.put(name, value);
		}
		if (param.get("parameterSize") != null) {
			param.put("parameterSize",
					"&op=zoom&resolutions=" + param.get("parameterSize"));
		}
		List<ModuleReturn> objRtns = affixService.uploads(request, response, param);
		return objRtns;
	}

	/**
	 * @Description:查询符合条件的附件，返回ModuleReturn对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/query")
	public ModuleReturn query(@RequestParam Map<String, Object> params)throws Exception {
		ModuleReturn objRtn = new ModuleReturn();
		List<Affix> affixList = null;//TODO affixService.queryAll(params);
		objRtn.putData("affixList", affixList);
		return objRtn;
	}
	
	@PostMapping("/query/{businessCode}/{businessId}/{affixType}")
	public ModuleReturn query(@PathVariable String businessCode,@PathVariable Long businessId,@PathVariable String affixType)throws Exception {
		ModuleReturn objRtn = new ModuleReturn();
		Map<String, Object> params=new HashMap();
		params.put("businessCode", businessCode);
		params.put("businessId", businessId);
		params.put("affixType", affixType);
		List<Affix> affixList = null;//TODO affixService.queryAll(params);
		objRtn.putData("affixList", affixList);
		return objRtn;
	}

	/**
	 * @Description:查询符合条件的附件，返回附件集合
	 * @param request
	 * @param response
	 * @return List<Attachment>
	 * @throws Exception
	 */
	@PostMapping("/queryAll")
	public List<Affix> queryAll(@RequestBody Map<String, Object> params)throws Exception {
		List<Affix> attachList = null;//TODO affixService.queryAll(params);
		return attachList;
	}
	
	@PostMapping("/queryAll/{businessCode}/{businessId}/{affixType}")
	public List<Affix> queryAll(@PathVariable String businessCode,@PathVariable Long businessId,@PathVariable String affixType)throws Exception {
		Map<String, Object> params=new HashMap();
		params.put("businessCode", businessCode);
		params.put("businessId", businessId);
		params.put("affixType", affixType);
		List<Affix> attachList = null;//TODO affixService.queryAll(params);
		return attachList;
	}

	@RequestMapping("/queryById")
	public Affix queryById(@RequestParam("rowId") Long rowId) {
		Affix affix = affixService.queryById(rowId);
		return affix;
	}
	
	@RequestMapping("/delet/{businessId}/{businessCode}/{affixType}")
	public void delet(@RequestParam("businessId") Long businessId,@RequestParam("businessCode") String businessCode,@RequestParam("affixType") String affixType) {
		Map<String, Object> params=new HashMap();
		params.put("BUSINESS_ID", businessId);
		params.put("BUSINESS_CODE", businessCode);
		params.put("AFFIX_TYPE", affixType);
		affixService.deleteByMap(params);
	}
}
