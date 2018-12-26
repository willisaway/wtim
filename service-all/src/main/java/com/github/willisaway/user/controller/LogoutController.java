package com.github.willisaway.user.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huaxun.core.base.ModuleReturn;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/logout")
@Api(value = "用户退出", description = "用户退出")
public class LogoutController {
	@Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;
	
	@ApiOperation(value = "退出登录")
	@RequestMapping("/exit")
    public ModuleReturn exit(@RequestParam String access_token) {
        ModuleReturn objRtn = new ModuleReturn(1);
        try {
        	if(consumerTokenServices.revokeToken(access_token)) {
        		objRtn.setReturnValue(1,"退出成功");
        	}else {
        		objRtn.setReturnValue(-1,"退出失败");
        	}
        } catch (Exception e) {
            e.printStackTrace();
            objRtn.setReturnValue(-1, "退出失败");
        }
        return objRtn;
    }
}
