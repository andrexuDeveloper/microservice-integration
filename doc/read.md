


#### gw for token

  http://localhost:9099/login/oauth/token?=grant_type=password

Bearer.1111111.2
```


```




####  health

http://LXUHONGCHAO.xwbank.com:9099/actuator/health

####  认证鉴权与API权限控制在微服务架构中的设计与实现（二）

http://blueskykong.com/2017/10/22/security2/
2. 详细的描述了security的集成
3. spring-security-oauth2-2.0.13.RELEASE.jar
4. \auth-server\src\main\java\com\blueskykong\auth\security 自定义了oauth的扩展与修改

5.  auth-server ,get token access

```
http://127.0.0.1:9091/oauth/token

header

Authorization : Basic Z2F0ZXdheTpnYXRld2F5

form
token: 10001


```

6. checkTokenEndpoint 对于token合法性验证首先是识别请求体中的token
JdbcTokenStore 



####  直接访问

http://localhost:9099/three/hello



#####  通过网关的LB访问后端服务

1. 例子http://localhost:9099/web/hello

2. 最开始遇到后端app不能通过注册服务发现来调用，问题主要authdemo没有成功注册，日志有error
3. 排查原因consul-client的版本应该与server保持一致。
4. 开启gateway服务所有完成health信息。
5. 