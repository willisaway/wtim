package com.github.willisaway.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.huaxun.core.config.RedisConfig;
import com.huaxun.core.support.jedis.MyRedisTokenStore;
import com.huaxun.core.util.SpringUtil;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
	JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Bean
    public MyRedisTokenStore tokenStore() {
        return new MyRedisTokenStore(jedisConnectionFactory);
    }

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
		.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("client")
			.scopes("xx") // 此处的scopes是无用的，可以随意设置
			.secret(passwordEncoder.encode("client"))
			.autoApprove(".*")
			.authorizedGrantTypes("password", "authorization_code", "refresh_token","client_credentials")
			.accessTokenValiditySeconds(30*24*60*60)
			.and()
			.withClient("webapp")
			.scopes("xx")
			.authorizedGrantTypes("implicit")
			.accessTokenValiditySeconds(30*24*60*60);
	}
}
