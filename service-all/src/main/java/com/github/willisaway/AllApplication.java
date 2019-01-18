package com.github.willisaway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

import com.github.willisaway.core.annotation.ExcludeFromComponentScan;
import com.github.willisaway.core.loadbalancer.RibbonConfiguration;
import com.github.willisaway.core.util.SpringUtil;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages={"com.huaxun.*","com.github.willisaway.*"},excludeFilters={@Filter(type=FilterType.ANNOTATION,value=ExcludeFromComponentScan.class)})
@MapperScan("com.github.willisaway.*.dao")
@EnableOAuth2Client
@EnableCaching
public class AllApplication {
	/**
	 * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
	 * 
	 * @return restTemplate
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(AllApplication.class, args);
		SpringUtil.setApplicationContext(applicationContext);
	}
}
