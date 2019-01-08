# Shiro

## 1.认证步骤：

- 1.拷贝依赖

  ~~~xml
  <!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-core -->
  <dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.4.0</version>
  </dependency>
  <!-- https://mvnrepository.com/artifact/commons-logging/commons-logging -->
  <dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
  </dependency>
  ~~~

- 2.配置shiro.ini文件，模拟数据库用户列表。

  ![](Shiro/ini文件.png)

- 3.执行shiro登录/登出操作

  ![](Shiro/shiro.png)

- 4.认证操作常见的异常

  - UnknownAccountException:不知道用户账号异常。
  - IncorrentCredentialsException:账号正常，密码错误异常。

  ![](Shiro/exception.png)

  ​

## 2.认证过程示意图

![](Shiro/验证过程.png)

1.调用subject.login()方法进行登录，其会自动委托给securityManager.login()方法进行登录。

2.securityManager通过Authenticator（认证器）进行认证。

3.Authenticator的实现ModularRealmAuthenticator调用realm从ini配置文件取用户真实的账号和密码，这里使用的是IniRealm（shiro自带的，相当于数据源）。

4.IniRealm先根据token中的账号去ini中找该账号，如果找不到则给ModularRealmAuthenticator返回null，如果找到则匹配密码，匹配密码成功则认证通过。

5.最后调用subject.logout进行登出操作。

登录过程：

![](Shiro/登录过程.png)







## 3.Realm

![](Shiro/Realm.png)

继承AuthorizingRealm，就具有了缓存，认证，授权的功能。



步骤：

1.自定义realm。继承AuthorizingRealm，重写三个方法：

- getName()
- doGetAuthorizationInfo(PrincipalCollection principalCollection),
- doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException

![](Shiro/自定义MyRealm.png)



2.配置ini文件，指定使用自定realm

![](Shiro/MyRealm.png)



3.加载配置文件shiro-realm.ini。执行登录操作

![](Shiro/MyRealmLogin.png)



