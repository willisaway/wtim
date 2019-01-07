package com.github.willisaway.file.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.willisaway.user.api.SmUserClient;
import com.github.willisaway.user.model.SmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.willisaway.core.base.BaseService;
import com.github.willisaway.core.base.ModuleReturn;
import com.github.willisaway.core.stream.BufferedServletRequestWrapper;
import com.github.willisaway.core.support.fastdfs.FileClient;
import com.github.willisaway.core.support.security.BASE64Encoder;
import com.github.willisaway.core.util.ExceptionUtil;
import com.github.willisaway.core.util.FileUtil;
import com.github.willisaway.core.util.LockUtil;
import com.github.willisaway.file.dao.AffixMapper;
import com.github.willisaway.user.model.Affix;

/**
 * @Description:TODO
 * @author SONG
 * @time:2017年8月4日 下午2:05:26
 */

@Service
//@CacheConfig(cacheNames = "CmAffix")
public class AffixService extends BaseService<AffixMapper,Affix> {

	@Autowired
	private FileSaveService fileSaveService;
	@Autowired
	private SmUserClient smUserClient;

	@SuppressWarnings("rawtypes")
	public List<ModuleReturn> uploads(HttpServletRequest request, HttpServletResponse response, Map param) {
		List<ModuleReturn> objRtns = new ArrayList();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (!multipartResolver.isMultipart(request)) {
			return null;
		}
		
		try {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				try {
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next().toString());
					if (file != null) {
						BufferedServletRequestWrapper httpRequest = new BufferedServletRequestWrapper(multiRequest);
						try {
							ModuleReturn objRtn = upload(httpRequest, response, file, param);
							objRtns.add(objRtn);
							
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTraceAsString(e));
		}finally {
			
		}

		return objRtns;
	}

	@SuppressWarnings("rawtypes")
	public ModuleReturn upload(BufferedServletRequestWrapper request,HttpServletResponse response,MultipartFile file, Map param) throws Exception{
		ModuleReturn objRtn = new ModuleReturn(1);
		Affix affix=null;
		String rpath = param.get("businessId").toString()+"\\"+param.get("affixType")+"\\"+file.getOriginalFilename();
		
		//先查询附件表，如果前台分片上传，已有之前的分片 2018年3月26日14:50:58 增加
		if (objRtn.getReturnValue() > 0) {
			Map paraMap = new HashMap();
			paraMap.put("businessId", param.get("businessId"));
			paraMap.put("businessCode", param.get("businessCode"));
			paraMap.put("affixType", param.get("affixType"));
			paraMap.put("fileName", file.getOriginalFilename());
			if (param.get("rowId") != null) {
				paraMap.put("rowId", param.get("rowId"));
			}
			affix = queryOne(paraMap);
		}
		if (objRtn.getReturnValue() > 0) {
			try {
				objRtn = fileSaveService.saveFile(file, rpath);
			} catch (Exception e) {
				e.printStackTrace();
				objRtn.setReturnValue(-1, "上传文件异常!");
			}
		}
		
		//维护附件表
		if (objRtn.getReturnValue() > 0) {
			try {
				if (objRtn.getReturnValue()>0) {
					if(affix==null) {
						affix = new Affix();
						affix.setFileName(file.getOriginalFilename());
						affix.setSaveFileName(file.getOriginalFilename());
						affix.setFilePath(rpath);
						affix.setBusinessId(param.get("businessId") != null ? Long.valueOf(param.get("businessId").toString()) : Long.valueOf(-1));
						affix.setBusinessCode(param.get("businessCode") != null ? param.get("businessCode").toString() : null);
						affix.setAffixType(param.get("affixType") != null ? param.get("affixType").toString() : null);
						//web uploader前端的文件id
						affix.setFrontFileId(param.get("id") != null ? param.get("id").toString() : null);
						if(param.get("size")!=null) {
							affix.setFileSize(BigDecimal.valueOf(Double.parseDouble(param.get("size").toString())));
						}else {
							affix.setFileSize(BigDecimal.valueOf(file.getSize()));
						}
						if(param.get("type")!=null) {
							affix.setFileType(param.get("type").toString());
						}else {
							String fileSuffix = FileUtil.getFileSuffix(file.getOriginalFilename());
							if(FileUtil.isImage(fileSuffix)) {
								affix.setFileType("image");
							}else {
								affix.setFileType("other");
							}
						}
						if(objRtn.getReturnPara("md5String") != null) {
							affix.setMd5String(objRtn.getReturnPara("md5String").toString());
						}
						affix = this.update(affix);
					}else {
						//如果传相同的文件，覆盖原有的，仅仅更新下文件大小
						if(param.get("size")!=null) {
							affix.setFileSize(BigDecimal.valueOf(Double.parseDouble(param.get("size").toString())));
						}else {
							affix.setFileSize(BigDecimal.valueOf(file.getSize()));
						}
						affix = this.update(affix);
					}
					objRtn.setReturnPara("affix", affix);
				}
			} catch (Exception e) {
				objRtn.setReturnValue(-1, "保存附件失败");
			}
		}
		return objRtn;
	}

	public ModuleReturn delete(Long rowId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		try {
			Affix affix = queryById(rowId);
			objRtn = fileSaveService.deleteFile(affix.getFilePath());
			if(objRtn.getReturnValue()>0) {
				delete(rowId, smUserClient.getCurrentUserId());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTraceAsString(e));
			objRtn.setReturnValue(-1, "删除失败");
		}
		
		return objRtn;
	}
	
	public ModuleReturn updateBusinessId(String businessCode,String affixType,Long oldBusinessId, Long newBusinessId,Long userId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		if (objRtn.getReturnValue() > 0) {
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				if(businessCode!=null) {
					params.put("businessCode", businessCode);
				}
				if(affixType!=null) {
					params.put("affixType", affixType);
				}
				params.put("businessId", oldBusinessId);
				List<Affix> attachList = queryAll(params);
				for (Affix updateBean : attachList) {
					updateBean.setBusinessId(newBusinessId);
					update(updateBean, userId);
				}
			} catch (Exception e) {
				e.printStackTrace();
				objRtn.setReturnValue(-1, "更新失败");
			}
		}
		return objRtn;
	}

	public ModuleReturn updateBusinessId(String businessCode,String affixType,Long oldBusinessId, Long newBusinessId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		objRtn = this.updateBusinessId(businessCode,affixType,oldBusinessId, newBusinessId,smUserClient.getCurrentUserId());
		return objRtn;
	}
	public ModuleReturn updateBusinessId(Long oldBusinessId, Long newBusinessId) {
		ModuleReturn objRtn = new ModuleReturn(1);
		objRtn = this.updateBusinessId(null,null,oldBusinessId, newBusinessId,smUserClient.getCurrentUserId());
		return objRtn;
	}
	
	public List<Affix> queryAllEncoded(Map<String, Object> params){
		List<Affix> affixList = queryAll(params);
		for(int i=0;i<affixList.size();i++) {
			Affix affix = affixList.get(i);
			affix.setFilePath(new BASE64Encoder().encode(affix.getFilePath().getBytes()));
		}
		return affixList;
	}
}
