# Spring #

## 通过配置文件获得类的实例 ##

```
通过以下方式可以获取一个id为bookDao的实例。

String xmlPath = "applicationContext.xml";

ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);

BookDao dao = (BookDao)context.getBean("bookDao");

创建数据库表。

void execute(String sql)：执行一条sql语句。

Issue a single SQL execute, typically a DDL statement.

```


## update()语句 ##
```java
update()方法可以完成插入，更新和删除数据的操作。在JdbcTemplate类中，提供了一系列的update()方法。如下：

int	update(String sql)

	Issue a single SQL update operation (such as an insert, update or delete statement).

int	update(String sql, Object... args)

	Issue a single SQL update operation (such as an insert, update or delete statement) via a prepared statement, binding the given arguments.

int	update(String sql, Object[] args, int[] argTypes)

	Issue a single SQL update operation (such as an insert, update or delete statement) via a prepared statement, binding the given arguments.

int	update(String sql, PreparedStatementSetter pss)

	Issue an update statement using a PreparedStatementSetter to set bind parameters, with given SQL.

```

## query语句 ##

```java
<T> List<T>	query(String sql, RowMapper<T> rowMapper)

	Execute a query given static SQL, mapping each row to a Java object via a RowMapper.

<T> List<T>	query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)

	Query given SQL to create a prepared statement from SQL and a PreparedStatementSetter implementation that knows how to bind values to the query, mapping each row to a Java object via a RowMapper.

<T> T	queryForObject(String sql, RowMapper<T> rowMapper, Object... args)

	Query given SQL to create a prepared statement from SQL and a list of arguments to bind to the query, mapping a single result row to a Java object via a RowMapper.	

<T> List<T>	queryForList(String sql, Object[] args, Class<T> elementType)

	Query given SQL to create a prepared statement from SQL and a list of arguments to bind to the query, expecting a result list.

```




```java
//以下方式是实现对数据的增删改查的操作。

public class AccountDaoImpl implements AccountDao{

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public int addAccount(Account account) {
		String sql = "insert account (username,balance) values(?,?)";//？是一个占位符
		Object[] obj = {
				account.getUsername(),
				account.getBalance()
		};
		return this.jdbcTemplate.update(sql, obj);
	}

	@Override
	public int updateAccount(Account account) {
		String sql = "update account set username = ?,balance = ? where id = ?";
		Object[] obj = {
			account.getUsername(),
			account.getBalance(),
			account.getId()
		};
		return this.jdbcTemplate.update(sql, obj);
	}

	@Override
	public int deleteAccount(int id) {
		String sql = "delete from account where id = ?";
		
		return this.jdbcTemplate.update(sql, id);
	}
		@Override
	public Account findAccountById(int id) {
		String sql = "select * from account where id = ?";
		RowMapper<Account> rowMapper = new BeanPropertyRowMapper<Account>(Account.class);
		return this.jdbcTemplate.queryForObject(sql,rowMapper, id);
	}
		@Override
	public List<Account> findAllAccount() {
		String sql = "select * from account";
		RowMapper<Account> rowMapper = new BeanPropertyRowMapper<Account>(Account.class);
		return this.jdbcTemplate.query(sql,rowMapper);
	}

}
```





IOC：Inverse of Control 反转控制。
将我们创建对象的方式反转了，以前对象的创建是由我们开发人员自己维护，包括依赖关系也是自己注入，使用spring之后，对象的创建以及依赖关系可以由spring完成创建以及注入。
反转控制：就是反转了对象的创建方式，从我们自己创建反转给了程序（spring）

DI：Dependency Injection. 依赖注入
实现ioc思想，需要DI支持，
注入方式：
	1、set方法注入
	2、构造方法注入
	3、字段注入
注入类型：
	1、值类型注入（8大基本数据类型）
	2、引用类型的注入（将依赖对象注入）



1.2.3.3 BeanFactory 和 ApplicationContext的区别: 
	BeanFactory   :是在 getBean 的时候才会生成类的实例. 
	ApplicationContext  :在加载 applicationContext.xml(容器启动)时候就会创建. 
ApplicationContext:
	ClassPathXmlApplicationContext    :加载类路径下 Spring 的配置文件. 
	FileSystemXmlApplicationContext    :加载本地磁盘下 Spring 的配置文件. 

BeanFactory：每次获得对象是才会创建对象。
ApplicationContext 每次容器启动时就会创建容器中配置的所有对象，并提供更多功能。

Bean元素：使用该元素描述需要spring容器管理的对象
	class属性：被管理对象的完整类名。
	name属性：给被管理的对象起一个名字，获得对象时根据给名称获得对象。可
			以重复，可以使用特殊字符。
	id属性：与name属性一模一样，名称不可重复，不能使用特殊字符
	
结论：尽量使用name属性。


spring创建对象的方式：
	1.空参构造函数

```xml
【无参数的构造方法的方式:】 

<!-- 方式一：无参数的构造方法的实例化 --> （**）

	<bean id="bean1" class="cn.itcast.spring.demo3.Bean1"></bean> 

<!-- 方式二：静态工厂实例化 Bean --> 调用class类的factory-method方法创建名为id的对象

	<bean  id="bean2"  class="cn.itcast.spring.demo3.Bean2Factory" factory-method="getBean2"/> 

<!-- 方式三：实例工厂实例化 Bean --> 

	<bean id="bean3Factory" class="cn.itcast.spring.demo3.Bean3Factory"></bean> 

	<bean id="bean3" factory-bean="bean3Factory" factory-method="getBean3"></bean> 

```

scope属性
	singleton(默认值):单例对象.被标识为单例的对象在spring容器中只会存在一个实例（绝大多数都是）
	prototype:多例原型.被标识为多例的对象,每次再获得才会创建.每次创建都是新的对象.整合struts2时,ActionBean必须配置为多例的.
	request:web环境下.对象与request生命周期一致.（不用）
	session:web环境下,对象与session生命周期一致.（不用）

分模块配置：
通过<import resource = "">导入其他Spring配置文件

属性注入：
注入值类型和引用数据类型：（值类型用value，引用类型用ref）
	
	<bean name="user" class="com.ooyhao.c_injection.User">
	
			<!-- 为user对象中为名为name属性注入tom作为值 -->
	
			<property name="name" value="tom"></property>
	
			<property name="age" value="18"></property>
	
			<property name="car" ref="car"></property>
	
		</bean>
	
	<bean name="car" class="com.ooyhao.c_injection.Car">
		<!-- 为user对象中为名为name属性注入tom作为值 -->
		<property name="name" value="兰博基尼"></property>
		<property name="color" value="黄色"></property>
	</bean>

构造注入：
name:构造函数的参数名
index：构造函数的参数索引
type：构造函数的参数类型
	
	<!-- **************************构造函数注入********************************* -->
	<bean name="user2" class ="com.ooyhao.c_injection.User" >
		<constructor-arg name = "name" value="jerry"></constructor-arg>
		<constructor-arg name = "car" ref="car"></constructor-arg>
	</bean>
	
	<bean name="car" class="com.ooyhao.c_injection.Car">
		<!-- 为user对象中为名为name属性注入tom作为值 -->
		<property name="name" value="兰博基尼"></property>
		<property name="color" value="黄色"></property>
	</bean>

	<!-- **************************构造函数注入********************************* -->
	<bean name="user2" class ="com.ooyhao.c_injection.User" >
		<constructor-arg name = "name" value="jerry" index="0"></constructor-arg>
		<constructor-arg name = "car" ref="car" index="1" ></constructor-arg>
	</bean>



	<!-- ************************复杂类型注入***************************** -->
	
		<!-- array注入 -->
	
		<bean name = "cb1" class="com.ooyhao.c_injection.CollectionBean">
	
		<!-- 如果数组中只准备注入一个值（对象），直接使用value或ref即可 -->
	
			<property name="arr" value="tom"></property>
	
		</bean>
	
	<bean name = "cb2" class="com.ooyhao.c_injection.CollectionBean">
	<!-- 多个元素注入 -->
		<property name="arr">
			<array>
				<value>tom</value>
				<value>jerry</value>
				<ref bean="user3"/>
			</array>
		</property>
	</bean>
	
	<!-- list注入 -->
	<bean name = "cb3" class="com.ooyhao.c_injection.CollectionBean">
		<!-- 如果list中只准备注入一个值（对象），直接使用value或ref即可 -->
		<!-- <property name="list" value="jack"></property> -->
		<property name="list">
			<list>
				<value>tom</value>
				<value>rose</value>
				<ref bean="car"/>
			</list>
		</property>
	</bean>
	
	<!-- map注入 -->
	<bean name = "cb4" class="com.ooyhao.c_injection.CollectionBean">
		<property name="map">
			<map>
				<entry key="url" value="jdbc:mysql://3306"></entry>
				<entry key="car" value-ref="car"></entry>
				<entry key-ref="user3" value-ref="user2"></entry>
			</map>
		</property>
	</bean>
	
	<!-- properties类型注入 -->
	<bean name = "cb5" class="com.ooyhao.c_injection.CollectionBean">
		<property name="prop">
			<props>
				<prop key="driverClass">com.jdbc.mysql.Driver</prop>
				<prop key="user">root</prop>
				<prop key="password">123456</prop>
			</props>
		</property>
	</bean>

1.spring介绍
	spring并不局限于某一层.
	spring是对象的容器,帮我们"管理"项目中的所有对象
	
2.spring搭建
	1>导包 4+2
	2>准备类
	3>书写配置(src/applicationContext.xml)
	4>书写代码测试
	
3.spring中的概念
	ioc: 反转控制. 创建对象的方式反转了.从我们自己创建对象,反转给spring(程序)来创建.
	
	di: 依赖注入.将必须的属性注入到对象当中.是实现ioc思想必须条件.
	
	beanFactory与ApplicationContext

4.配置文件详解
	
	bean元素
		id:	给Bean起个名字		不能重复,不能使用特殊字符.早期属性.
		name:给Bean起个名字		能重复,能使用特殊字符.后来属性.
		class:类的完整类名
	生命周期属性
		init-method		指出初始化方法 对象创建之后
		destory-method  指出销毁方法 容器销毁之前
	作用范围
		scope: 
			singleton(默认值):单例.创建容器时会立即创建单例对象
			prototype :多例.每次获得对象时,才会创建对象,并且每次都会创建新的对象
	分模块开发
		<import  />

5.Bean的创建方式
	*空参构造创建<bean name = "", class = ""/>
	静态工厂
	实例工厂

6.注入方式
	*set方法
	*构造方法
	p名称空间
	spEL表达式
	
7.复杂属性注入
	Array
	List
	Map
	Properties
8.在WEB环境中使用Spring容器
	1>导包 4+2+1(spring-web)
	2>在web.xml中配置listener => ContextLoaderListener
		|-配置参数,指定spring配置路径 
	3>在Action中,使用工具类获得容器.
		|-WebApplicationContextUtils.getWebApplicationContext(ServletContext sc)


Annotation。
值类型注入：
方式一：（使用反射的Field赋值，破坏了封装性）
	@Value("tom")
	private String name;
方式二：（使用setter方法赋值，推荐使用）
	@Value("tom")
	public void setName(String name) {
		this.name = name;
	}

@Value("tom") = @Value(value = "tom") 如果注解中只有一个需要赋值的话，并且名称为value的话，可省略不写。

引用数据类型：
	@Autowired//自动装配
	private Car car;
【使用自动装配，如果容器中有多个同类对象存在，容器将不知道选择哪一个进行装配】
可以还用以下两个注解联合使用的方式，来标注是哪一个对象。告诉spring容器自动装配哪一个对象
	@Autowired//自动装配
	@Qualifier("car2")
	private Car car;
【可以使用resource属性，手动注入，指定哪一个名称的对象，推荐使用】
	@Resource(name = "car2")//手动注入，指定哪一个名称的对象
	private Car car;

【初始化和销毁方法，使用注解PostConstruct和@PreDestroy】
	@PostConstruct//在对象被创建后调用。init-method
	public void init() {
		System.out.println("这是初始化方法");
	}
	
	@PreDestroy//在对象销毁之前调用。destory-method
	public void destory() {
		System.out.println("这是销毁方法");
	}

aop思想：【动态代理】+【cglib】可以对任何类进行代理
	横向重复，纵向抽取
spring能够为容器中管理的对象生成动态代理对象。（有接口会优先使用动态代理，没有接口会使用cglib代理） 
【动态代理】
动态代理对象必须要实现接口才能产生代理对象。如果没有接口将不能生成动态代理对象。

```java
//使用动态代理实现切面编程。
public class JdkProxy implements InvocationHandler{
	
	//声明目标类对象。
	private UserDao userDao;
	
	//创建代理方法
	public Object getProxy(UserDao userDao) {
		this.userDao = userDao;
		//1.类加载器
		ClassLoader loader = JdkProxy.class.getClassLoader();
		//2.被代理对象实现的所有接口
		Class[] interfaces = this.userDao.getClass().getInterfaces();
		InvocationHandler handler = this;
		//3.使用代理类，进行增强，返回的是代理后的对象。
		return Proxy.newProxyInstance(loader, interfaces, handler);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//创建切面对象
		MyAspect aspect = new MyAspect();
		//前增强
		aspect.check_Permissions();
		//在目标类上调用方法，并传入参数。(之前需要执行的方法)
		//第一个参数：需要进行代理的对象。
		//第二个参数：方法执行的参数。
		Object obj = method.invoke(userDao, args);
		//后增强
		aspect.log();
		return obj;
	}

}
```

【cglib】
第三方代理技术，cglib代理，可以对任何类生成代理，代理的原理是对目标对象继承代理。
如果目标对象被final修饰，那么该类无法被cglib代理。

```java
//采用cglib代理技术实现切面编程
public class CglibProxy implements MethodInterceptor{
	
	public Object getProxy(Object target) {
		//创建一个动态代理对象
		Enhancer enhancer = new Enhancer();
		//指明需要代理的类。设置其父类
		enhancer.setSuperclass(target.getClass());
		//添加回调函数
		enhancer.setCallback(this);
		//返回创建的代理类。
		return enhancer.create();
		
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] arg2, MethodProxy arg3) throws Throwable {

		//创建切面对象
		MyAspect aspect = new MyAspect();
		//前增强
		aspect.check_Permissions();
		//回调需要代理的方法，第一个参数是代理类。第二个参数的方法执行需要的参数。
		Object obj = arg3.invokeSuper(proxy, arg2);
		//后增强
		aspect.log();
		return obj;
	}

}
```


【使用proxyFactoryBean 和 MethodInterceptor】

```java
//通知类
public class MyAspect implements MethodInterceptor{
		public void check_Permissions() {
			System.out.println("模拟检查权限。。。");
		}
		public void log() {
			System.out.println("模拟记录日志。。。");
		}
		@Override
		public Object invoke(MethodInvocation arg0) throws Throwable {
			check_Permissions();
			Object obj = arg0.proceed();
			log();
			return obj;
	}
	
}
```

```xml
<!-- 1.配置目标类对象 -->
<bean name = "userDao" class="com.ooyhao.jdk.UserDaoImpl"></bean>
<!-- 2.配置切面类对象 -->
<bean name="myAdvice" class="com.ooyhao.proxyFactoryBean.MyAspect"></bean>
<!-- 3.使用spring代理工厂定义一个名称为userDaoProxy的代理对象 -->
<bean name="userDaoProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
	<property name="target" ref="userDao"></property>
	<property name="proxyInterfaces" value="com.ooyhao.jdk.UserDao"></property>
	<property name="interceptorNames" value ="myAdvice"></property>
	<property name="proxyTargetClass" value="true"></property>
</bean>
```

【springaop （aspectj）】

```java
//通知类
public class MyAdvice {
		//前置通知
		//目标方法运行之前通知
		public void before() {
			System.out.println("这是前置通知");
		}

		//后置通知（如果出现异常就不会调用）
		//目标方法运行之后通知
		public void afterReturning() {
			System.out.println("这是后置通知（如果出现异常就不会调用）");
		}

		//环绕通知
		//目标方法运行之前和之后都调用
		public Object around(ProceedingJoinPoint pjp) throws Throwable {
			System.out.println("这是环绕通知之前的部分");
			Object proceed = pjp.proceed();//调用目标方法
			System.out.println("这是环绕通知之后的部分");
			return proceed;
		}

		//异常拦截通知
		//如果出现异常，就会调用
		public void afterException() {
			System.out.println("出事了，出现异常了！！");
		}
		//后置通知（无论是否出现异常都会调用）

			//在目标方法之后调用

		public void after() {
			System.out.println("后置通知，出现异常也会调用");
		}
	}

【xml配置】

```



```xml
<!-- 准备工作，导入aop约束 -->
<!-- 1.配置目标对象 -->
<bean name = "userDaoTarget" class="com.ooyhao.jdk.UserDaoImpl"></bean>
<!-- 2.配置通知对象 -->
<bean name="myAdvice" class="com.ooyhao.aspect.MyAdvice"></bean>
<!-- 3.配置将通知织入目标对象 -->
<aop:config>
<!-- 配置切入点 
	public void com.ooyhao.jdk.UserDaoImpl.addUser() 空参 共有 无返回值
|	void com.ooyhao.jdk.UserDaoImpl.addUser()
|	* com.ooyhao.jdk.UserDaoImpl.addUser()
|	* com.ooyhao.jdk.UserDaoImpl.*()
|	* com.ooyhao.jdk.UserDaoImpl.*(..) （..表示对方法的参数不做要求）
|	* com.ooyhao.jdk.*DaoImpl.*(..)
V	* com.ooyhao.jdk..*DaoImpl.*(..) 包含子包
演化	
-->

		<aop:pointcut expression="execution(* com.ooyhao.jdk.DaoImpl.(..))" id="pc"/>

		<aop:aspect ref="myAdvice">

			<!-- 指定名为before的方法作为前置通知 -->

			<aop:before method="before" pointcut-ref="pc"/>

			<!-- 后置通知 -->

			<aop:after-returning method="afterReturning" pointcut-ref="pc" />

			<!-- 环绕通知 -->

			<aop:around method="around" pointcut-ref="pc"/>

			<!-- 异常处理通知 -->

			<aop:after-throwing method="afterException" pointcut-ref="pc"/>

			<!-- 后置通知 -->

			<aop:after method="after" pointcut-ref="pc"/>

		/aop:aspect

	/aop:config

```
 


【基于注解的springaop】

```xml
<!-- 开启使用注释完成织入 -->
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
<!-- 开启包扫描，进行注解解析 -->
<context:component-scan base-package="com.ooyhao.jdk,com.ooyhao.spring_annotationAop"></context:component-scan>
```

//通知类


```java
	@Aspect  //表示该类是一个通知类

	@Component(value = "myAdvice")

	public class MyAdvice {

		//前置通知

			//目标方法运行之前通知

		@Before("execution(* com.ooyhao.jdk.DaoImpl.(..))")

		public void before() {

			System.out.println("这是前置通知");

		}

		//后置通知（如果出现异常就不会调用）

			//目标方法运行之后通知

		@AfterReturning("execution(* com.ooyhao.jdk.DaoImpl.(..))")

		public void afterReturning() {

			System.out.println("这是后置通知（如果出现异常就不会调用）");

		}

		//环绕通知

			//目标方法运行之前和之后都调用

		@Around("execution(* com.ooyhao.jdk.DaoImpl.(..))")

		public Object around(ProceedingJoinPoint pjp) throws Throwable {

			System.out.println("这是环绕通知之前的部分");

			Object proceed = pjp.proceed();//调用目标方法

			System.out.println("这是环绕通知之后的部分");

			return proceed;

		}

		//异常拦截通知

			//如果出现异常，就会调用

		@AfterReturning("execution(* com.ooyhao.jdk.DaoImpl.(..))")

		public void afterException() {

			System.out.println("出事了，出现异常了！！");

		}

		//后置通知（无论是否出现异常都会调用）

			//在目标方法之后调用

		@After("execution(* com.ooyhao.jdk.DaoImpl.(..))")

		public void after() {

			System.out.println("后置通知，出现异常也会调用");

		}

	}

```

​	

【目标类，需要增加的】	
	
	@Repository(value = "userDao")
	public class UserDaoImpl implements UserDao{
	
		@Override
		public void addUser() {
			System.out.println("添加用户");
		}
	
		@Override
		public void deleteUser() {
			System.out.println("删除用户");
		}
	
	}


复习	

	一.注解代替xml配置
	
		准备工作:
	
			4+2 + spring-aop包
	
			xml中导入context约束
	
			在xml中开启扫描包中类的注解
	
		注解:
	
			@Component("BeanName") 将对象注册到spring容器
	
				|-	@Controler
	
				|-	@Service
	
				|-	@Repository
	
		@Scope	指定对象的作用范围
			|- singleton
			|- prototype
			
		@Value 值类型属性注入
			
		@Autowired 自动属性注入.根据类型注入.
		@Qulifier 指定注入的对象的名称
		
		@Resource 指定对象的名称注入
		
		@PostConstruct 初始化方法
		@PreDestory    销毁方法

二.spring AOP开发
		

	aop思想: 纵向重复,横向抽取.
	
		|- filter中
	
		|- 动态代理
	
		|- interceptor中
	
	spring AOP: 封装了动态代理技术.来体现aop.	
	
	springaop实现: 	可以对所有对象进行代理
	
		|- 动态代理	 代理需要实现接口.
	
		|- cglib代理 对目标对象继承代理.
	
	springaop名词:
	
		join point: 连接点.所有可以织入通知的方法.
	
		point cut : 切入点.需要|已经织入通知的方法.
	
		advice:		需要增强的代码.
	
		weaving:	动词.将通知应用的切点的过程.
	
		target: 目标对象.
	
		proxy:	代理对象
	
		aspect: 切面. 切入点+通知
	
	步骤:
	
		1.导包
			4+2
	
			2 aop+aspect
	
			2 aop联盟+weaving
	
		2.准备目标对象
	
	3.准备通知类
		前置通知
	    后置通知 方法正常结束
		环绕通知
		异常拦截通知
		后置通知 无论如何都执行
		
	4.配置文件中配置,导入aop约束
		1>目标对象
		2>通知对象
		3><aop:config>
				<aop:ponint-cut id="切点名称" expression="execution(切点表达式)" />
				<aop:aspect ref="通知对象名称" >
					<aop:before method="" ponintcut-ref=""  />
					<aop:after-returning method="" ponintcut-ref=""  />
					<aop:around method="" ponintcut-ref=""  />
					<aop:after-throwing method="" ponintcut-ref=""  />
					<aop:after method="" ponintcut-ref=""  />
				</aop:aspect>

扩展:使用注解完成aop
	1.导包
		4+2
		2 aop+aspect
		2 aop联盟+weaving
	2.准备目标对象

	3.准备通知类
	4.配置文件中配置,导入aop约束
		1>目标对象
		2>通知对象
		3><aop:aspectj-autoproxy> 开启注解aop
				
	5.注解
		@Aspect 指定当前类是通知类	
		@Before 前置通知方法
		@after-returning 后置通知方法
		@around 环绕通知方法
		@after-throwing 异常拦截通知方法
		@after 后通知方法
		@PointCut 抽取切点表达式

# spring操作数据库 #
spring中提供了一个对象可以对数据库进行操作，对象封装了jdbc技术。

【xml文件】
	

```xml
<bean name = "userDao" class="com.ooyhao.jdbcTemplate.UserDaoImpl">

		<property name="jt" ref="jdbcTemplate"></property>

	</bean>

	<bean name = "dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">

		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>

		<property name="url" value="jdbc:mysql://localhost/spring"/>

		<property name="username" value="root"/>

		<property name="password" value="123456"/>		

	</bean>

	<bean name = "jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">

		<property name="dataSource" ref="dataSource"></property>

	</bean>

```

【dao类】

```java
//使用JDBC模板实现增删改查

public class UserDaoImpl implements UserDao {

	private JdbcTemplate jt;
	
	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
	
	@Override
	public void save(User user) {
		String sql = "insert into t_user values(null,?)";
		jt.update(sql, user.getName());
	}

	@Override
	public void delete(Integer id) {
		String sql = "delete from t_user where id = ?";
		jt.update(sql, id);
	}

	@Override
	public void update(User user) {
		String sql = "update t_user set name = ? where id = ?";
		jt.update(sql, user.getName(),user.getId());
	}

	@Override
	public User getById(Integer id) {
		String sql = "select * from t_user where id = ?";
		
		return jt.queryForObject(sql, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int arg1) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				return user;
			}
			
		}, id);
	}

	@Override
	public int getTotalCount() {
		String sql = "select count(*) from t_user";
		return jt.queryForObject(sql, Integer.class);
	}

	@Override
	public List<User> getAll() {
		String sql = "select * from t_user";
		return jt.query(sql, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int arg1) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				return user;
			}
			
		});
	}

}
```

【通过继承JdbcDaoSupport来实现】

```java
//使用JDBC模板实现增删改查

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	@Override
	public void save(User user) {
		String sql = "insert into t_user values(null,?)";
		this.getJdbcTemplate().update(sql,user.getName());
	}

	@Override
	public void delete(Integer id) {
		String sql = "delete from t_user where id = ?";
		this.getJdbcTemplate().update(sql, id);
	}

	@Override
	public void update(User user) {
		String sql = "update t_user set name = ? where id = ?";
		this.getJdbcTemplate().update(sql, user.getName(),user.getId());
	}

	@Override
	public User getById(Integer id) {
		String sql = "select * from t_user where id = ?";
		
		return this.getJdbcTemplate().queryForObject(sql, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int arg1) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				return user;
			}
			
		}, id);
	}

	@Override
	public int getTotalCount() {
		String sql = "select count(*) from t_user";
		return this.getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	@Override
	public List<User> getAll() {
		String sql = "select * from t_user";
		return this.getJdbcTemplate().query(sql, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int arg1) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				return user;
			}
			
		});
	}

}
```

【xml文件配置信息】

```xml
	<bean name = "userDao" class="com.ooyhao.jdbcTemplate.UserDaoImpl">

		<property name="dataSource" ref="dataSource"></property>

	</bean>

	<bean name = "dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">

		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>

		<property name="url" value="jdbc:mysql://localhost/spring"/>

		<property name="username" value="root"/>

		<property name="password" value="123456"/>		

	</bean>

```


​	

【spring中使用properties文件。使用${}来取值】
	
```xml
<!--spring中使用  <context:property-placeholder>来引入properties文件 -->

<context:property-placeholder location="classpath:db.properties"/>

<bean name="userDao" class="com.ooyhao.jdbcTemplate.UserDaoImpl">
	<!-- <property name="jt" ref="jdbcTemplate"></property> -->
	<property name="dataSource" ref="dataSource"></property>
</bean>
<bean name="dataSource"
	class="org.springframework.jdbc.datasource.DriverManagerDataSource">
	<property name="driverClassName" value="${jdbc.driverClassName}" />
	<property name="url" value="${jdbc.url}" />
	<property name="username" value="${jdbc.username}" />
	<property name="password" value="${jdbc.password}" />
</bean>
```


# spring实现事物管理 #
>>手动实现事务[一般使用]
>业务逻辑代码
	public class AccountServiceImpl implements AccountService {
		private AccountDao dao;

		private TransactionTemplate transactionTemplate;


		public void setTt(TransactionTemplate transactionTemplate) {
			this.transactionTemplate = transactionTemplate;
		}
	
		public void setAccountDao(AccountDao dao) {
			this.dao = dao;
		}
		
		@Override
		public void transfer(Integer from, Integer to, Double money) {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					dao.decreasebMoney(from, money);
					int i  = 1/0;
					dao.increaseMoney(to, money);
				}
			});
			//1.打开事物
			//2.调用doInTransactionWithoutResult
			//3.关闭事物
			
		}
	
	}

>>xml文件配置
	<!--spring中使用  <context:property-placeholder>来引入properties文件 -->
	<context:property-placeholder location="classpath:db.properties"/>


​	
	<bean name = "transactionManager" class = "org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref = "dataSource"></property>
	</bean>
	
	<!-- 事物模板对象 -->
	<bean name = "transactionTemplate" class = "org.springframework.transaction.support.TransactionTemplate">
		<property name="transactionManager" ref="transactionManager"></property>
	</bean>
	
	<bean name="accountDao" class="com.ooyhao.dao.AccountDaoImpl">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	<bean name="dataSource"
		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="${jdbc.driverClassName}" />
		<property name="url" value="${jdbc.url}" />
		<property name="username" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
	</bean>
	
	<bean name = "accountService" class="com.ooyhao.service.AccountServiceImpl">
		<property name="accountDao" ref="accountDao"></property>
		<property name="transactionTemplate" ref="transactionTemplate"></property>
	</bean>

【基于xml文件的aop 事务管理】

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
		xmlns:tx="http://www.springframework.org/schema/tx" xmlns:aop="http://www.springframework.org/schema/aop"
		xsi:schemaLocation="http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans-4.3.xsd 
		http://www.springframework.org/schema/context 
		http://www.springframework.org/schema/context/spring-context-4.3.xsd 
		http://www.springframework.org/schema/tx 
		http://www.springframework.org/schema/tx/spring-tx-4.3.xsd 
		http://www.springframework.org/schema/aop 
		http://www.springframework.org/schema/aop/spring-aop-4.3.xsd ">
	
		<!--spring中使用 <context:property-placeholder>来引入properties文件 -->
		<context:property-placeholder location="classpath:db.properties" />
	
		<!-- 配置数据源 -->
		<bean name="dataSource"
			class="org.springframework.jdbc.datasource.DriverManagerDataSource">
			<property name="driverClassName" value="${jdbc.driverClassName}" />
			<property name="url" value="${jdbc.url}" />
			<property name="username" value="${jdbc.username}" />
			<property name="password" value="${jdbc.password}" />
		</bean>
		<!-- 由于AccountDao实现了 JdbcDaoSupport接口，所以直接将数据源配置到AccountDao中即可 -->
		<bean name="accountDao" class="com.ooyhao.dao.AccountDaoImpl">
			<property name="dataSource" ref="dataSource"></property>
		</bean>
	
		<!-- 事务管理器，依赖于数据源 -->
		<bean name="transactionManager"
			class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref="dataSource"></property>
		</bean>
	
		<!-- 事物模板对象 ，依赖于事务管理器 -->
		<bean name="transactionTemplate"
			class="org.springframework.transaction.support.TransactionTemplate">
			<property name="transactionManager" ref="transactionManager"></property>
		</bean>
		<!-- 业务逻辑方法，依赖于数据访问层和事务模板 -->
		<bean name="accountService" class="com.ooyhao.service.AccountServiceImpl">
			<property name="accountDao" ref="accountDao"></property>
			<property name="transactionTemplate" ref="transactionTemplate"></property>
		</bean>


		<!-- 配置事务通知 -->
		<tx:advice id="txAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<!-- 以方法为单位，指定方法应用什么事务属性 isolation:隔离级别 propagation：传播行为 read-only：是否可读 -->
				<tx:method name="transfer" isolation="REPEATABLE_READ"
					propagation="REQUIRED" read-only="false" />
			</tx:attributes>
		</tx:advice>
	
		<!-- 配置织入 -->
		<aop:config>
			<aop:pointcut expression="execution(* com.ooyhao.service.*ServiceImpl.*(..))"
				id="txPc" />
			<aop:advisor advice-ref="txAdvice" pointcut-ref="txPc" />
		</aop:config>
	</beans>

【基于annotation的aop事务管理】
>>业务方法
>
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,readOnly = false)
	public void transfer(Integer from, Integer to, Double money) {
		dao.decreasebMoney(from, money);
		int i = 1/0;
		dao.increaseMoney(to, money);

	}

>>xml文件配置
>
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
		xmlns:tx="http://www.springframework.org/schema/tx" xmlns:aop="http://www.springframework.org/schema/aop"
		xsi:schemaLocation="http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans-4.3.xsd 
		http://www.springframework.org/schema/context 
		http://www.springframework.org/schema/context/spring-context-4.3.xsd 
		http://www.springframework.org/schema/tx 
		http://www.springframework.org/schema/tx/spring-tx-4.3.xsd 
		http://www.springframework.org/schema/aop 
		http://www.springframework.org/schema/aop/spring-aop-4.3.xsd ">

		<!--spring中使用 <context:property-placeholder>来引入properties文件 -->
		<context:property-placeholder location="classpath:db.properties" />
	
		<!-- 配置数据源 -->
		<bean name="dataSource"
			class="org.springframework.jdbc.datasource.DriverManagerDataSource">
			<property name="driverClassName" value="${jdbc.driverClassName}" />
			<property name="url" value="${jdbc.url}" />
			<property name="username" value="${jdbc.username}" />
			<property name="password" value="${jdbc.password}" />
		</bean>
		<!-- 由于AccountDao实现了 JdbcDaoSupport接口，所以直接将数据源配置到AccountDao中即可 -->
		<bean name="accountDao" class="com.ooyhao.dao.AccountDaoImpl">
			<property name="dataSource" ref="dataSource"></property>
		</bean>
	
		<!-- 事务管理器，依赖于数据源 -->
		<bean name="transactionManager"
			class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref="dataSource"></property>
		</bean>
	
		<!-- 事物模板对象 ，依赖于事务管理器 -->
		<bean name="transactionTemplate"
			class="org.springframework.transaction.support.TransactionTemplate">
			<property name="transactionManager" ref="transactionManager"></property>
		</bean>
		<!-- 业务逻辑方法，依赖于数据访问层和事务模板 -->
		<bean name="accountService" class="com.ooyhao.service.AccountServiceImpl">
			<property name="accountDao" ref="accountDao"></property>
			<property name="transactionTemplate" ref="transactionTemplate"></property>
		</bean>


		<!-- 开启使用注解管理aop事务 -->
		<tx:annotation-driven/>
	</beans>

>可以加在类上也可以加在方法上
	@Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,readOnly = true)
	public class AccountServiceImpl implements AccountService {
		private AccountDao dao;

		private TransactionTemplate transactionTemplate;


		public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
			this.transactionTemplate = transactionTemplate;
		}
	
		public void setAccountDao(AccountDao dao) {
			this.dao = dao;
		}
		
		@Override
		@Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,readOnly = false)
		public void transfer(Integer from, Integer to, Double money) {
			dao.decreasebMoney(from, money);
			int i = 1/0;
			dao.increaseMoney(to, money);
			
		}
	}







