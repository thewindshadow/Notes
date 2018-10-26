# Spring

## 1.@Configuration&@Bean

~~~xml
使用@Configuration指定一个配置类 ==> xml文件
使用@Bean类注入一个bean ==><bean></bean>
~~~

### 第一种：使用XML的方式注入Bean

Person.java

~~~java
package com.ooyhao.bean;

import java.io.Serializable;

/**
 * @author ooyhao
 */
public class Person implements Serializable {

    private String name;

    private Integer age;

    public Person() {

    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
//省略Getter/Setter/toString()
}

~~~

beans.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <bean id="person" class="com.ooyhao.bean.Person">
        <property name="name" value="张三"></property>
        <property name="age" value="18"></property>
    </bean>
</beans>
~~~

测试文件：

~~~java
@Test
public void TestXML() {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    Person person = (Person) context.getBean("person");
    System.out.println(person);
}
~~~

![](image/xml.png)

### 第二种：使用注解与配置类的方式

Config.java

~~~java
package com.ooyhao.config;

import com.ooyhao.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ooyhao
 */
//配置类==配置文件
@Configuration //告诉Spring这是一个配置类
public class MainConfig {

    // 给容器中注册一个Bean，类型就是返回值的类型，id默认是用方法名作为id、
    @Bean("person")
    public Person person01(){
        return new Person("李四",20);
    }
}

~~~

测试文件：

~~~java
@Test
public void testConfig(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
    Person person = context.getBean(Person.class);
    System.out.println(person);
    String[] names = context.getBeanNamesForType(Person.class);
    System.out.println(Arrays.toString(names));
}
~~~

![](image/config.png)

### 总结：

~~~java
1.MainConfig.java.相当于beans.xml。（需要使用@Configuration注解指明）
2.@Bean所标注的方法，相当于<bean></bean>.方法返回值的类型就是Bean的类型。方法名默认就是bean的id名。
	但是可以在@Bean注解的value或name属性进行修改。修改后id名就变为@Bean标注的了。
	 String[] names = context.getBeanNamesForType(Person.class);
     System.out.println(Arrays.toString(names));//结果如上：person。而不是person01

注意：不同的spring版本的@Bean可使用的属性不同。这里的4.3.12有name和value。而后面的只有name.


@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    @AliasFor("name")
    String[] value() default {};

    @AliasFor("value")
    String[] name() default {};

    Autowire autowire() default Autowire.NO;

    String initMethod() default "";

    String destroyMethod() default "(inferred)";
} 
    
~~~





## 2.@ComponentScan

### 1.使用xml配置包扫描的方式

~~~xml
<!--包扫描，只要标注了@Controller/@Service/@Repository/@Component就可以被扫描到-->
<context:component-scan base-package="com.ooyhao"></context:component-scan>
~~~

![](image/component.png)

定义相应的Controller，service，dao层，并用相应的注解进行标注。

### 2.使用注解方式进行包扫描

~~~java
//配置类==配置文件
@ComponentScan(value = {"com.ooyhao"})//可以定义多个。
@Configuration //告诉Spring这是一个配置类
public class MainConfig {

    // 给容器中注册一个Bean，类型就是返回值的类型，id默认是用方法名作为id、
    @Bean("person")
    public Person person01(){
        return new Person("李四",20);
    }
}
~~~

注意：

~~~xml
	xml方式，<context:component-scan></context:component-scan>是配置在beans.xml文件中的，所以只需要在MainConfig类上用@ComponentScan(value/basePackages="")来进行扫描。（value与basePackages作用一致）。
~~~

可以定义多个：（部分源码）

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {

	/**
	 * Alias for {@link #basePackages}.
	 * <p>Allows for more concise annotation declarations if no other attributes
	 * are needed &mdash; for example, {@code @ComponentScan("org.my.pkg")}
	 * instead of {@code @ComponentScan(basePackages = "org.my.pkg")}.
	 */
	@AliasFor("basePackages")
	String[] value() default {};

	/**
	 * Base packages to scan for annotated components.
	 * <p>{@link #value} is an alias for (and mutually exclusive with) this
	 * attribute.
	 * <p>Use {@link #basePackageClasses} for a type-safe alternative to
	 * String-based package names.
	 */
	@AliasFor("value")
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages} for specifying the packages
	 * to scan for annotated components. The package of each class specified will be scanned.
	 * <p>Consider creating a special no-op marker class or interface in each package
	 * that serves no purpose other than being referenced by this attribute.
	 */
	Class<?>[] basePackageClasses() default {};
~~~

测试代码：

~~~java
@Test
public void testComponent(){
    AnnotationConfigApplicationContext context = new 
        AnnotationConfigApplicationContext(MainConfig.class);
    String[] names = context.getBeanDefinitionNames();
    for(String name:names){
        System.out.println(name);
    }
}
~~~

结果：

![](image/ComponentScan.png)



### 3.@Component的扫描规则

#### 1.FilterType的源码（取值）：

~~~java
public enum FilterType {
	ANNOTATION,			//按照注解的方式
	ASSIGNABLE_TYPE,	//按照给定的类型
	ASPECTJ,			//按照ASPECTJ表达式
	REGEX,				//按照正则表达式
	CUSTOM				//自定义
}
~~~

#### 2.@Componenet的源码

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)  //可以重复，说明可以写多个ComponentScan
public @interface ComponentScan {

	@AliasFor("basePackages")
	String[] value() default {};

	@AliasFor("value")
	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};

	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

	Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;

	ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;

	String resourcePattern() default ClassPathScanningCandidateComponentProvider.DEFAULT_RESOURCE_PATTERN;

	boolean useDefaultFilters() default true;

	Filter[] includeFilters() default {};

	Filter[] excludeFilters() default {};

	boolean lazyInit() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target({})
	@interface Filter {

		FilterType type() default FilterType.ANNOTATION;

		@AliasFor("classes")
		Class<?>[] value() default {};

		@AliasFor("value")
		Class<?>[] classes() default {};

		String[] pattern() default {};

	}

}

~~~



#### 3.MainConfig.java（排除）

~~~java
//配置类==配置文件
@ComponentScan(value = {"com.ooyhao"},excludeFilters = {
        @Filter(type = FilterType.ANNOTATION,value = {Controller.class}),
        @Filter(type = FilterType.ASSIGNABLE_TYPE,value = {BookService.class})
})
@Configuration //告诉Spring这是一个配置类
public class MainConfig {

    // 给容器中注册一个Bean，类型就是返回值的类型，id默认是用方法名作为id、
    @Bean("person")
    public Person person01(){
        return new Person("李四",20);
    }

}
~~~

（排除了@Controller标注的，以及BookService类型的）

![](image/exclude.png)



#### 4.MainConfig.java（包含）

xml方式：

~~~xml
<!--包扫描，只要标注了@Controller/@Service/@Repository/@Component就可以被扫描到-->
<context:component-scan base-package="com.ooyhao" use-default-filters="false"></context:component-scan>
<!--需要使用use-default-filters来禁用默认的扫描规则-->
~~~

注解方式：

~~~java
//配置类==配置文件
/*@ComponentScan(value = {"com.ooyhao"},excludeFilters = {
        @Filter(type = FilterType.ANNOTATION,value = {Controller.class}),
        @Filter(type = FilterType.ASSIGNABLE_TYPE,value = {BookService.class})
})*/
@ComponentScan(basePackages = "com.ooyhao",includeFilters = {
        @Filter(type = FilterType.ANNOTATION,value = {Controller.class})
},useDefaultFilters = false)
@Configuration //告诉Spring这是一个配置类
public class MainConfig {
~~~

(只包含@Controller标注的)

![](image/include.png)



~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)  //可以重复，说明可以写多个ComponentScan
public @interface ComponentScan {
~~~

可同时写多个@ComponentScan(JDK1.8)。也可以使用@ComponentScans();

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScans {
	ComponentScan[] value();
}
~~~

(配置多个@ComponentScan)

~~~java
@ComponentScan(basePackages = "com.ooyhao",includeFilters = {
        @Filter(type = FilterType.ANNOTATION,value = {Controller.class})//（按照注解）
},useDefaultFilters = false)
@ComponentScan(basePackages = "com.ooyhao",includeFilters = {
        @Filter(type = FilterType.ANNOTATION,value = {Service.class})//包括子类（按照指定类型）
},useDefaultFilters = false)
@Configuration //告诉Spring这是一个配置类
public class MainConfig {
~~~

![](image/componentScans.png)

#### 5.自定义扫描规则

~~~java
通过实现TypeFilter接口来自定义扫描规则的Filter

public interface TypeFilter {

	/**
	 * @param metadataReader ：当前正在扫描的类的信息
	 * @param metadataReaderFactory ：可以获取到其他类的信息
	 */
	boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException;
}

~~~

自定义扫描过滤器

~~~java
public class MyTypeFilter implements TypeFilter{

    /**
     * @param metadataReader ：当前正在扫描的类的信息
     * @param metadataReaderFactory ：可以获取到其他类的信息
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获得当前类注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获得当前类资源信息（类的路径等）
        Resource resource = metadataReader.getResource();
//        String className = classMetadata.getClassName();
        System.out.println(annotationMetadata.getAnnotationTypes());//获得注解类型
        System.out.println(resource.getURL());//获得资源路径
        System.out.println(classMetadata.getClassName());//获得类名

        if(classMetadata.getClassName().contains("er")) {
            return true;
        }
        return false;
    }
}

~~~

在Config.java上配置

~~~java
@ComponentScan(basePackages = "com.ooyhao",includeFilters = {
        /*@Filter(type = FilterType.ANNOTATION,value = {Controller.class}),*/
        @Filter(type = FilterType.CUSTOM,value = {MyTypeFilter.class})
},useDefaultFilters = false)
@Configuration //告诉Spring这是一个配置类
public class MainConfig {
~~~



![](image/customTypeFilter.png)

总结：

为什么myTypeFilter会被扫描到：

​	由于我们扫描的基本包是com.ooyhao，所有在这个基本包下面的所有的类都会被扫描到进行过滤，而只有上述结果中的三个是包含"er"的，所有就会被扫描到。



## 3.@Scope作用域

### 1.@Scope的源码

~~~java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	@AliasFor("scopeName")
	String value() default "";

	/**
	 * prototype 多实例
	 * singleton 单实例
	 * request 	同一个请求创建一个实例（一般不用）  
	 * session  同一个session会话创建一个实例（一般不用）
	 * 如果是web环境我们一般将对象存至request域&Session域
	 */
	@AliasFor("value")
	String scopeName() default "";
    
	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;

}
~~~

MainConfig.java

~~~java

@Configuration
public class MainConfig2 {
    
    @Scope(scopeName = "prototype")
    @Bean
    public Person person(){
        return new Person("李四",25);
    }
}
~~~

测试：

~~~java
@Test
    public void testScope(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        String[] names = context.getBeanDefinitionNames();
        for(String name:names){
            System.out.println(name);
        }
        Person person1 = (Person) context.getBean("person");
        Person person2 = (Person) context.getBean("person");
        System.out.println(person1 == person2);
    }
~~~



### **2.singleton:**

​	Bean组件在单实例（默认）的情况下，ioc容器在创建后就会调用方法把对象放到ioc容器中。以后每次获取就是直接从容器（类似map.get()）中拿，而不会再次创建，所有person1 == person2 为true。

### **3.prototype:**

​	Bean组件在多实例的情况下，ioc容器创建时不会直接调用方法将对象创建出来，而是在每一次获取对象的时候，ioc容器都会创建一个新的对象返回，即每次获取的对象都是不同的。所有person1 == person2 为false；



## 4.@Lazy懒加载

懒加载：

​	单实例bean，默认是在容器启动的时候创建对象。

​	使用懒加载时，将对象的创建推迟到第一次获取的时候。

### 1.@Lazy源码

~~~java
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lazy {

	/**
	 * Whether lazy initialization should occur.
	 */
	boolean value() default true;
}

~~~

### 2.不使用懒加载：

~~~java
@Configuration
public class MainConfig2 {
    //@Lazy
    @Scope(scopeName = "singleton")
    @Bean
    public Person person(){
        System.out.println("创建person对象");
        return new Person("李四",25);
    }
}
~~~

测试文件：

~~~java
@Test
public void testLazy(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("ioc容器创建完成");
}
~~~

![](image/notLazy.png) 

### 3.使用懒加载：

MainConfig.java把@Lazy注解释放

测试文件：

~~~java
@Test
public void testLazy(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
    System.out.println("ioc容器创建完成");
    System.out.println("获取person对象");
    context.getBean("person");
}
~~~

![](image/lazy.png)

总结：

​	可以看出，不使用lazy懒加载时，容器创建时就创建了person对象。而使用了lazy时，就推迟到获取person

的时候创建的。



## 5.@Conditional

### 1.@Conditional的源码：

~~~java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
	Class<? extends Condition>[] value();
}

~~~

### 2.Condition的源码：

```java
public interface Condition {

   /**
    * Determine if the condition matches.
    * @param context the condition context
    * @param metadata metadata of the {@link org.springframework.core.type.AnnotationMetadata class}
    * or {@link org.springframework.core.type.MethodMetadata method} being checked.
    * @return {@code true} if the condition matches and the component can be registered
    * or {@code false} to veto registration.
    */
   boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);

}
```

如果我们要自定义条件，可以通过实现Condition接口，同时matchs的返回值来控制条件、

### 3.自定义Condition

**MainConfig.java配置类**

~~~java
@Configuration
public class MainConfig2 {

    @Lazy
    @Scope(scopeName = "singleton")
    @Bean
    public Person person(){
        System.out.println("创建person对象");
        return new Person("李四",25);
    }

    /**
     * @Conditional({Condition}) 按照一定的条件进行判断，满足条件给容器中注册Bean。
     *
     * 如果系统是Windows，给容器中注册("bill")
     * 如果系统是Linux，给容器中添加（"linus"）
     */

    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01(){
        return new Person("Bill Gates",62);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linus")
    public Person person02(){
        return new Person("Linus",48);
    }
}

~~~

WindowsCondition.java

~~~java
/**
 * 判断系统是否为Windows
 */
public class WindowsCondition implements Condition {

    /**
     *
     * @param context  判断条件能使用的上下文条件
     * @param metadata 注释信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //判断是否为Windows系统
        String osName = context.getEnvironment().getProperty("os.name");
        if(osName.contains("Windows")){
            return true;
        }
        return false;
    }
}

~~~

LinuxCondition.java

~~~java
/**
 * 判断系统是否为Linux。
 */
public class LinuxCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //判断是否为Linux系统

        //1.能获得到ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2.能获得类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3.获得当前环境信息
        Environment environment = context.getEnvironment();
        //4.获得到Bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        String osName = context.getEnvironment().getProperty("os.name");
        if(osName.contains("Linux")){
            return true;
        }
        return false;
    }
}
~~~

测试方法：

```java
@Test
public void testConditional(){
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
    String[] names = applicationContext.getBeanNamesForType(Person.class);
    for(String name : names){
        System.out.println(name);
    }
    Map<String, Person> beansOfType = applicationContext.getBeansOfType(Person.class);
    System.out.println(beansOfType);
    String osName = applicationContext.getEnvironment().getProperty("os.name");
    System.out.println(osName);

}
```

系统默认结果：

![](image/Windows.png)

将我们的运行环境更改一下：

![](image/Runtime.png)

结果：

![](image/Linux.png)



总结：

- 通过根据运行环境来判断哪一个Bean应该进行添加到容器中。

- @Target({ElementType.TYPE, ElementType.METHOD}) 可以放到类/接口上。也可以放在方法上、

  ​

## 6.给容器中导入组件

### 0.导入组件的几种方法：

~~~java
/**
 *  给容器中注册组件：
 *  1) 包扫描+组件标注注解（@Controller,@Service,@Repository,@Component）[自己写的类]
 *  2) @Bean [导入的第三方包里面的组件，由于其可能没有标注上述注解，我们就可以使用@Bean添加到容器中去]
 *  3) @Import [快速的给容器中导入组件]
 *      1，@Import({要导入组件的class})
 *      2，@Import({ImportSelector.class})
 *      3，@Import({ImportBeanDefinitionRegistrar.class})
 *  4) 使用Spring提供的FactoryBean（工厂Bean）
 *    
 */
~~~



### 1.@Import源码：

~~~java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
	 * or regular component classes to import.
	 */
	Class<?>[] value();
}
~~~

### 2.ImportSelector的源码

~~~java
public interface ImportSelector {

	/**
	 * Select and return the names of which class(es) should be imported based on
	 * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
	 */
	String[] selectImports(AnnotationMetadata importingClassMetadata);
}
~~~



### 3.ImportBeanDefinitionRegistrar源码

~~~java
public interface ImportBeanDefinitionRegistrar {

	/**
	 * Register bean definitions as necessary based on the given annotation metadata of
	 * the importing {@code @Configuration} class.
	 * <p>Note that {@link BeanDefinitionRegistryPostProcessor} types may <em>not</em> be
	 * registered here, due to lifecycle constraints related to {@code @Configuration}
	 * class processing.
	 * @param importingClassMetadata annotation metadata of the importing class
	 * @param registry current bean definition registry
	 */
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);

}

~~~





### 4.使用自定义ImportSelector进行组件导入



~~~java
@Configuration
@Import({Color.class, Red.class, MyImportSelector.class}) //可以导入多个
//@Import导入组件，id默认是组件的全类名
public class MainConfig2 {

    @Lazy
    @Scope(scopeName = "singleton")
    @Bean
    public Person person(){
        System.out.println("创建person对象");
        return new Person("李四",25);
    }

    /**
     * @Conditional({Condition}) 按照一定的条件进行判断，满足条件给容器中注册Bean。
     *
     * 如果系统是Windows，给容器中注册("bill")
     * 如果系统是Linux，给容器中添加（"linus"）
     */

    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01(){
        return new Person("Bill Gates",62);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linus")
    public Person person02(){
        return new Person("Linus",48);
    }
}

~~~

自定义ImportSelector类

~~~java
public class MyImportSelector implements ImportSelector {

    /**
     *
     * @param importingClassMetadata  ：当前标注@Import注解类的所有注解信息
     * @return 返回值就是导入到容器中的组件的全类名。
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{Blue.class.getName(), Yellow.class.getName()};
    }
}

~~~



测试类：

~~~java

@Test
public void testImport(){
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
    String[] names = applicationContext.getBeanDefinitionNames();
    for(String name : names){
        System.out.println(name);
    }
}
~~~

结果图：

![](image/MyImportSelector.png)

### 5.使用自定义ImportBeanDefinitionRegistrar进行组件导入



#### 1.自定义ImportBeanDefinitionRegistrar

~~~java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry  BeanDefinition注册类。
     *                     把所有需要添加到容器中的bean，
     *                     调用 BeanDefinitionRegistry.registerBeanDefinition手工注册进来
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        boolean definition1 = registry.containsBeanDefinition(Red.class.getName());
        boolean definition2 = registry.containsBeanDefinition(Blue.class.getName());
        if(definition1 && definition2){
            //指定Bean的定义信息，（Bean的Scope等）
            BeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class);
            registry.registerBeanDefinition("rainBow",beanDefinition);
        }

    }
}

~~~

#### 2.MainConfig.java的@Import进行引入

~~~java
@Configuration
@Import({Color.class, Red.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class}) //可以导入多个
//@Import导入组件，id默认是组件的全类名
public class MainConfig2 {

    @Lazy
    @Scope(scopeName = "singleton")
    @Bean
    public Person person(){
        System.out.println("创建person对象");
        return new Person("李四",25);
    }

    /**
     * @Conditional({Condition}) 按照一定的条件进行判断，满足条件给容器中注册Bean。
     *
     * 如果系统是Windows，给容器中注册("bill")
     * 如果系统是Linux，给容器中添加（"linus"）
     */

    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01(){
        return new Person("Bill Gates",62);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linus")
    public Person person02(){
        return new Person("Linus",48);
    }
}

~~~

#### 3.测试结果：

![](image/MyImportBeanDefinitionRegistrar.png)



### 6.FactoryBean

#### 1.FactoryBean的源码

~~~java
package org.springframework.beans.factory;

public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
~~~

#### 2.自定义FactoryBean

~~~java
public class ColorFactoryBean implements FactoryBean<Color> {


    //返回一个Color对象，这个对象会添加到容器中
    @Override
    public Color getObject() throws Exception {
        System.out.println("ColorFactoryBean...");
        return new Color();
    }

    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }

    //设置是否为单例

    /***
     * @return true:单例 false:多例
     */
    @Override
    public boolean isSingleton() {
        return false;
    }
}

~~~

#### 3.在MainConfig.java中注册Bean

~~~java
@Bean
public ColorFactoryBean colorFactoryBean(){
    return new ColorFactoryBean();
}
~~~

#### 4.测试：

```java
@Test
public void testImport(){
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
    String[] names = applicationContext.getBeanDefinitionNames();
    for(String name : names){
        System.out.println(name);
    }
    //工厂Bean获取的是调用getObject创建的对象。
    Object bean1 = applicationContext.getBean("colorFactoryBean");
    Object bean2 = applicationContext.getBean("colorFactoryBean");
    System.out.println(bean1.getClass());
//        System.out.println("设置的是单实例："+ (bean1 == bean2));//设置的是单实例：true
    System.out.println("设置的是多实例："+ (bean1 == bean2));//设置的是多实例：false


}
```

#### 5.结果：

**(Singleton)**

![](image/singleton.png)

**(Prototype)**

![](image/prototype.png)



**如果需要获得工厂类本身**

~~~java
Object bean = applicationContext.getBean("&colorFactoryBean");
~~~

### 7.总 结：

```java
/**
 *  给容器中注册组件：
 *  1) 包扫描+组件标注注解（@Controller,@Service,@Repository,@Component）[自己写的类]
 *  2) @Bean [导入的第三方包里面的组件，由于其可能没有标注上述注解，我们就可以使用@Bean添加到容器中去]
 *  3) @Import [快速的给容器中导入组件]
 *      1，@Import({要导入组件的class}):容器中就会自动注入这个组件，id默认是全类名。
 *      2，@Import({ImportSelector.class}):返回需要导入的组件的全类名数组。
 *      3，@Import({ImportBeanDefinitionRegistrar.class})：手动注册Bean到容器中
 *  4) 使用Spring提供的FactoryBean（工厂Bean）
 *      1)默认获取到的工厂bean调用getObject创建的对象。
 *      2）如果要获得的是工厂Bean本身，我们需要在id前面加一个&
 *          $colorFactoryBean
 */
```



## 7.@Bean bean的声明周期

~~~java
/**
 * bean的生命周期：
 * 		bean创建---初始化----销毁的过程
 * 容器管理bean的生命周期；
 * 我们可以自定义初始化和销毁方法；容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 * 
 * 构造（对象创建）
 * 		单实例：在容器启动的时候创建对象
 * 		多实例：在每次获取的时候创建对象
 * 
 * BeanPostProcessor.postProcessBeforeInitialization
 * 初始化：
 * 		对象创建完成，并赋值好，调用初始化方法。。。
 * BeanPostProcessor.postProcessAfterInitialization
 * 销毁：
 * 		单实例：容器关闭的时候
 * 		多实例：容器不会管理这个bean；容器不会调用销毁方法；
 * 
 * 
 * 遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization，
 * 一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization
 * 
 * BeanPostProcessor原理
 * populateBean(beanName, mbd, instanceWrapper);给bean进行属性赋值
 * initializeBean
 * {
 * 		applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 * 		invokeInitMethods(beanName, wrappedBean, mbd);执行自定义初始化
 * 		applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *}
 * 
 * 
 * 
 * 1）、指定初始化和销毁方法；
 * 		通过@Bean指定init-method和destroy-method；
 * 2）、通过让Bean实现InitializingBean（定义初始化逻辑），
 * 				DisposableBean（定义销毁逻辑）;
 * 3）、可以使用JSR250；
 * 		@PostConstruct：在bean创建完成并且属性赋值完成；来执行初始化方法
 * 		@PreDestroy：在容器销毁bean之前通知我们进行清理工作
 * 4）、BeanPostProcessor【interface】：bean的后置处理器；
 * 		在bean初始化前后进行一些处理工作；
 * 		postProcessBeforeInitialization:在初始化之前工作
 * 		postProcessAfterInitialization:在初始化之后工作
 * 
 * Spring底层对 BeanPostProcessor 的使用；
 * 		bean赋值，注入其他组件，@Autowired，生命周期注解功能，@Async,xxx BeanPostProcessor;
 * 
 *
 *
 */
~~~

### 一，通过指定初始化和销毁方法。

#### 1.在一个需要声明为Bean的类中自定义一个初始化和一个销毁方法。

~~~java

public class Car {

    public Car(){
        System.out.println("car construct...");
    }

    public void init(){
        System.out.println("car init...");
    }

    public void destroy(){
        System.out.println("car destroy...");
    }
}
~~~

#### 2.在主配置类中使用@Bean注解注册一个Bean实例，并通过initMethod属性和destroyMethod方法指定其初始化方法和销毁方法。

~~~java
@Configuration
public class MainConfig {

    @Scope("singleton")
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }
}
~~~

**此时@Scope设置为singleton**

结果：

![](image/@BeanSingleton.png)

**将@Scope设置为prototype**

测试文件如下：

~~~java
@Test
public void testImport(){

    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(MainConfig.class);
    String[] names = context.getBeanDefinitionNames();
    for(String name: names){
        System.out.println(name);
    }
    //-------prototype新增的一句获取语句-------
    Object bean = context.getBean("car");
    context.close();
}
~~~

结果：

![](image/@BeanPrototypr.png)



#### 3.总结：

~~~xml
	如果使用的是通过自己指定初始化方法和销毁方法的话，那么我们需要在该类中自己定义相应的两个方法，在主配置类中通过@Bean注解进行注册到ioc容器中。同时通过@Bean注解的initMethod属性和destroyMethod属性分别对初始化方法和销毁方法进行指定。例如：
	@Scope("prototype")
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }
并且可以看出，单例模式下，ioc容器创建时就会创建bean放入其中。并能控制其生命周期，即可以控制bean的创建，				初始化，和销毁方法。
			多例模式下，ioc容器创建时不会创建bean instancec.只有当调用它的时候，才会进行创建和初始				化。
由上面两图可以看出。
~~~

### 二，通过让Bean实现InitializingBean（定义初始化逻辑），DisposableBean（定义销毁逻辑）;

#### 1.cat类如下：

~~~java
@Component
public class Cat implements InitializingBean,DisposableBean {

    public Cat(){
        System.out.println("cat construct...");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("cat afterPropertiesSet...");
    }

    public void destroy() throws Exception {
        System.out.println("cat destroy...");
    }
}
~~~

#### 2.MainConfig.java主配置类

~~~java
@Configuration
@ComponentScan("com.ooyhao") //通过宝扫描的方式
public class MainConfig {
}
~~~

#### 3.结果

![](C:\Users\5161\AppData\Local\Temp\1540546258730.png)

#### 4.总结：

~~~xml
	通过让Bean实现InitializingBean与DisposableBean两个接口，并重写其未实现的方法。然后将这个组件注册到ioc容器中去，当bean初始化和销毁的时机到了，就会调用这两个接口中的两个方法，进行初始化和销毁。
~~~





### 三.使用JSR250中@PostConstruct和@PreDestroy注解指定创建销毁



~~~xml
@PostConstruct：在bean创建完成并且属性赋值完成；来执行初始化方法
@PreDestroy：在容器销毁bean之前通知我们进行清理工作
~~~

Dog.java Bean

~~~java
@Component
public class Dog  {

    public Dog(){
        System.out.println("Dog construct...");
    }
    @PostConstruct
    public void init(){
        System.out.println("Dog @PostConstruct...");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("Dog @PreDestroy...");
    }

}

~~~

使用@ComponentScan进行包扫描，

结果：

![](image/Dog.png)

总结：

~~~xml
由上图可以知道，使用@PostConstrut和@PreDestroy注解，也可以实现指定方法作为bean的初始化和销毁方法。
使用JSR250；
     @PostConstruct：在bean创建完成并且属性赋值完成；来执行初始化方法
     @PreDestroy：在容器销毁bean之前通知我们进行清理工作
~~~



### 四，通过实现BeanPostProcessor接口

**MyBeanPostProcessor.java**

~~~java
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    public MyBeanPostProcessor(){
        System.out.println("MyBeanPostProcessor construct...");
    }

    //初始化之前
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization ... "+beanName+"-->"+bean);
        return bean;
    }

    //初始化之后
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization ... "+beanName+"-->"+bean);
        return bean;
    }

}

~~~

**结果：**

![](image/MyBeanPostProcessor.png)



**总结：**

~~~xml
BeanPostProcessor【interface】：bean的后置处理器；
 * 		在bean初始化前后进行一些处理工作；
 * 		postProcessBeforeInitialization:在初始化之前工作
 * 		postProcessAfterInitialization:在初始化之后工作

这个接口并不是控制自身的生命周期，而是通过这个bean实现这个接口后，所有bean在初始化之前都会调用postProcessBeforesInitialization方法，而bean初始化完成之后会执行postProcessAfterInitialization方法 
~~~







