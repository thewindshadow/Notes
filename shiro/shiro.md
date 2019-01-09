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



## 4.MD5加密

~~~java
@Test
public void testMD5(){
    //密码：明文
    String password = "666";
  
    //密文
    Md5Hash md5Hash = new Md5Hash(password);
    //fae0b27c451c728867a567e8c1bb4e53
    System.out.println(md5Hash);

    md5Hash = new Md5Hash(password,"zhangsan");
    //2f1f526e25fdefa341c7a302b47dd9df
    System.out.println(md5Hash);

    md5Hash = new Md5Hash(password,"zhangsan",3);
    //cd757bae8bd31da92c6b14c235668091
    System.out.println(md5Hash);
}
~~~



## 5.使用ini方式判断是否有角色

ini配置文件：

~~~ini
[users]
#用户zhangsan的密码是123，此用户具有role1和role2两个角色
zhangsan=666,role1,role2
lisi=888,role2

[roles]
#角色role1对资源user拥有create，update权限
role1=user:create,user:update
#角色role2对资源user拥有create，delete权限
role2=user:create,user:delete
#角色role3对资源user拥有create权限
role3=user:create
~~~



~~~JAVA
@Test
public void testHasRole(){
    Factory<SecurityManager> factory =
            new IniSecurityManagerFactory("classpath:shiro-permission.ini");
    SecurityManager securityManager = factory.getInstance();

    SecurityUtils.setSecurityManager(securityManager);

    Subject subject = SecurityUtils.getSubject();

    UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","666");

    subject.login(token);
    //进行授权操作时的前提：用户必须通过验证

    //判断当前用户是否拥有某个角色：返回true表示拥有，false表示没有
    System.out.println(subject.hasRole("role1"));
    //判断当前用户是否拥有一些角色：返回true表示全部拥有，false表示不全部拥有
    System.out.println(subject.hasAllRoles(Arrays.asList("role1","role2","role3")));
    //判断当前用户是否拥有一些角色：返回是boolean类型的数据，true表示拥有某个角色，false表示没有。
    System.out.println(Arrays.toString(subject.hasRoles(Arrays.asList("role1","role2","role3"))));
    //判断当前用户是否拥有某个角色：没有返回值，如果拥有，不做任何操作，没有报异常UnauthorizedException
    subject.checkRoles("role1");
    //判断当前用户是否拥有一些角色
    //org.apache.shiro.authz.UnauthorizedException: Subject does not have role [role3
    subject.checkRoles(Arrays.asList("role1","role2","role3"));

}
~~~



## 6.使用ini方式判断是否有权限

~~~ini
[users]
#用户zhangsan的密码是123，此用户具有role1和role2两个角色
zhangsan=666,role1,role2
lisi=888,role2

[roles]
#角色role1对资源user拥有create，update权限
role1=user:create,user:update
#角色role2对资源user拥有create，delete权限
role2=user:create,user:delete
#角色role3对资源user拥有create权限
role3=user:create
~~~



~~~java

@Test
public void testHasPermission(){
    Factory<SecurityManager> factory =
            new IniSecurityManagerFactory("classpath:shiro-permission.ini");
    SecurityManager securityManager = factory.getInstance();
    SecurityUtils.setSecurityManager(securityManager);
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token =
            new UsernamePasswordToken("zhangsan","666");
    subject.login(token);

    System.out.println(subject.isPermitted("user:delete"));
    System.out.println(subject.isPermittedAll("user:list","user:delete"));
    System.out.println(Arrays.toString(subject.isPermitted("user:list","user:delete")));
    //org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [user:list]
    subject.checkPermission("user:list");
}
~~~



















