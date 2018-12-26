package com.github.willisaway.file.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huaxun.core.support.fastdfs.FileClient;
import com.github.willisaway.file.service.AffixService;
import com.github.willisaway.user.api.SmUserClient;
import com.github.willisaway.user.model.SmUser;

@RestController
@RequestMapping(value = "test")
public class TestController {
	@Autowired
	AffixService affixService;
	@Autowired
	SmUserClient smUserClient;
	
	@RequestMapping("/public/test")
	public void test() throws Exception{
		
	}
	
	@RequestMapping("/currentUser")
	public SmUser currentUser() throws Exception{
		return smUserClient.getCurrentUser();
	}
}
