package com.github.willisaway.auth.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true,jsr250Enabled = true,securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            .and()
            .authorizeRequests()
            .antMatchers("/swagger-ui.html","/v2/api-docs","/swagger-resources/**","/webjars/**"
            		,"/**/login/**"
            		,"/templates/**"
            		,"/static/**"
            		,"/**/css/**"
            		,"/**/plugins/**"
            		,"/**/images/**"
            		,"/**/js/**"
            		,"/**/*.html"
            		,"/**/public/**"
            		,"/affix/query"
            		,"/affix/queryAll"
            		,"/affix/queryById"
            		,"/info"
            		,"/")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and().headers().frameOptions().disable();
    }
}
