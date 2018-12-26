# 商城后台服务实现

服务列表：
- `common`:服务共用工具类 
- `discovery`:(端口：8761)服务注册和发现的基本模块
- `gateway`:(端口：80)边界网关(所有微服务都在它之后)
- `service-masterdata`:(端口：8000)主数据
- `server-user`:(端口：8001)用户,认证授权中心
- `service-products`:(端口：8002)产品
- `service-order`:(端口：8003)订单
- `service-activity`:(端口：8004)活动
- `service-bi`:(端口：8005)报表
- `service-evaluate`:(端口：8006)评价
- `service-file`:(端口：8007)附件服务
- `service-finance`:(端口：8008)财务
- `service-message`:(端口：8009)消息
- `service-bbs`:(端口：8010)论坛、
- `service-et`:(端口：8011)考试
- `service-search`:(端口：8012)搜索引擎
- `service-sso`:(端口：8013)三方登录
- `service-webcrawler`:(端口：8014)爬虫
- `service-scheduled`:(端口：8015)定时任务
- `service-scheduled`:(端口：8016)arvr后台服务
- `web-pc`:(端口：8081)前端

###常见错误：

`找不到mapper类`
```
Field omsOrderMapper in com.juhe.order.service.OdOrderService required a bean of type 'com.juhe.order.dao.OdOrderMapper' that could not be found.


Action:

Consider defining a bean of type 'com.juhe.order.dao.OdOrderMapper' in your configuration.
```

原因及解决方法:
一般是没有扫描到mapper类导致,在启动类上增加注解
```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.juhe.order.dao")
public class OrderApplication 
```

`启动报错`
```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'requestMappingHandlerMapping' defined in class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Invocation of init method failed; nested exception is java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'odOrderController' method 
public com.juhe.core.mybatis.Page<T> com.juhe.core.base.BaseController.query(java.util.Map<java.lang.String, java.lang.Object>)
to {[/order/query],methods=[POST]}: There is already 'odOrderController' bean method
public java.lang.Object com.juhe.order.controller.OdOrderController.query(javax.servlet.http.HttpServletRequest,org.springframework.ui.ModelMap) mapped.
```

原因及解决方法:
因为自己的Controller中的RequestMapping跟BaseController中的RequestMapping重复了导致,不要用BaseController中的路径名
```java
BaseController.java
	@RequestMapping(value = "/base/queryById/{id}")
	public T queryById(@PathVariable Long id) {
		return baseService.queryById(id);
	}
	
	@RequestMapping(value = "/base/query")
	public Page<T> query(Map<String, Object> params){
		return baseService.query(params);
	}
	
	@RequestMapping(value = "/base/queryAll")
	public List<T> queryAll(Map<String, Object> params){
		return baseService.queryAll(params);
	}
```

`找不到mapper类`
```
Field omsOrderMapper in com.juhe.order.service.OdOrderService required a bean of type 'com.juhe.order.dao.OdOrderMapper' that could not be found.


Action:

Consider defining a bean of type 'com.juhe.order.dao.OdOrderMapper' in your configuration.
```

原因及解决方法:
一般是没有扫描到mapper类导致,在启动类上增加注解
```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.juhe.order.dao")
public class OrderApplication 
```