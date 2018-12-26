package com.github.willisaway.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.huaxun.core.base.FileType;
import com.huaxun.core.base.ModuleReturn;
import com.huaxun.core.stream.BufferedServletRequestWrapper;
import com.huaxun.core.stream.InputStreamWrapper;
import com.huaxun.core.stream.StreamUtil;
import com.huaxun.core.util.UploadUtil;

import lombok.Data;

@Service
@Data
public class FileSaveService {
	@Value("${file.path}")
	public String apath;
	
	public ModuleReturn saveFile(MultipartFile file,String rpath) throws Exception{
		return UploadUtil.saveFileToDisk(file, apath, rpath);
	}
	
	public ModuleReturn deleteFile(String rpath) {
		return UploadUtil.delFileFromDisk(apath, rpath);
	}
}
