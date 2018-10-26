
#### 1.使用IDEA快速创建一个SpringBoot项目

##### 1.New Project  选择maven ，next

![1538200311309](pic\newProject.png)

##### 2，填写GroupId，ArtifactId,Version。 next

![1538200440515](pic\newProject2.png)

##### 3.根据自己的需要，修改相关信息，finish。

![1538200626998](pic\newProject3.png)

##### 4.项目创建成功之后右下角会出现一个对话框

选择第二个，说明之后再向项目中添加依赖的时候，idea会自动进行导包操作。

![1538201032118](pic\EnableAutoImport.png)

##### 5,添加一下maven插件。

![1538202065677](pic\maven.png)

##### 6进入spring官网，spring.io; 选择projects

![1538201373188](pic\spring.io1.png)

##### 7.选择springboot

![1538201465368](pic\spring.io2.png)

##### 8.选择Learn，再选择Reference Doc。

![1538201576258](pic\spring.io3.png)

##### 9.ctrl + F ,搜索 pom.xml

![1538201668748](pic\pom.xml.png)

##### 10.复制并添加依赖

```xml
<!-- 添加spring boot的父启动器 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
</parent>

<!-- 添加 spring boot的web启动器 -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>

<!-- 添加打包的插件（可选） -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

下载完成之后，可以在左侧的项目结构中看到

![1538202224089](pic\jar包结构.png)

##### 11.添加SpringbootApplicationDemo.java

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : 阳浩
 * @Date : 2018/9/29
 * @Time : 14:25
 * @Annotation :
 */
@RestController
@SpringBootApplication
public class SpringBootApplicationDemo {

    @RequestMapping(value = "/index")
    public String index(){
        return "This is my first spring boot project!";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationDemo.class,args);
    }

}
```

##### 12.添加对应的jdk

![1538204245870](pic\editJDK.png)



![1538204318292](pic\editJDK1.png)

##### 13.运行main方法

（此时会包一个错误，大概是由于我们把java文件放在了默认包中）

![1538204662239](pic\error.png)

##### 14.创建一个新的package，将其放在其中，并运行。浏览器访问 localhost:8080/index

(正确访问并输出数据，但是这里会发现一个问题，springboot的控制台打印出来的日志是黑白，如何变成彩色的呢。)



![1538203310198](pic\fristResult.png)

##### 15.彩色的日志输出

~~~xml
-Dspring.output.ansi.enabled=ALWAYS
~~~

![1538205050038](pic\彩色Log.png)

![1538205116873](pic\彩色Log2.png)

##### 16.项目结构

![1538205175148](pic\proj.png)

##### 17.使用java -jar运行springboot项目

使用idea，maven工具进行打包

![1538205607284](pic\package.png)

根据控制台的地址找到相应的地址中的springboot jar文件

![1538205795698](pic\SpringBootJar.png)

shift+鼠标右键，选择 在此处打开命令窗口。（也可以自己只用命令进入到指定目录）

~~~xml
java -jar com.ooyhao-1.0-SNAPSHOT.jar
~~~

这里遇到了一个问题：

![1538206820974](pic\javaJarError.png)

原因是：jar包是在jdk1.8环境下打包的，但是计算机的环境是1.7，所有报错了。只需要将计算机的环境变量更换成jdk1.8即可。

再次运行。

![1538206564119](pic\javaJarSuccess.png)



#### 2.SpringBoot读取配置文件

yml文件

~~~yaml
# 配置端口
server:
  port: 8081
  # 请求路径
  servlet:
    path: /springboot

#（冒号后面是有空格的）
person:  
  lastName: zhangsan   			 #常量值
  age: 18			  		    #常量值
  boss: false          		     #常量值
  birth: 1996/03/24              #常量值
  maps: {k1: v1, k2: v2}         #map或是对象
  lists:						#数组
    - lisi
    - zhaoliu
  dog:							#对象
    name: 小狗
    age: 12

~~~





**Person** 类

~~~java
package com.ooyhao.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author : 阳浩
 * @Date : 2018/9/30
 * @Time : 9:39
 * @Annotation :
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;


    public Person() {
    }

    public Person(String lastName, Integer age, Boolean boss, Date birth, Map<String, Object> maps, List<Object> lists, Dog dog) {
        this.lastName = lastName;
        this.age = age;
        this.boss = boss;
        this.birth = birth;
        this.maps = maps;
        this.lists = lists;
        this.dog = dog;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getBoss() {
        return boss;
    }

    public void setBoss(Boolean boss) {
        this.boss = boss;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public List<Object> getLists() {
        return lists;
    }

    public void setLists(List<Object> lists) {
        this.lists = lists;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", age=" + age +
                ", boss=" + boss +
                ", birth=" + birth +
                ", maps=" + maps +
                ", lists=" + lists +
                ", dog=" + dog +
                '}';
    }
}

~~~

**Dog类**

~~~java
package com.ooyhao.demo;

/**
 * @Author : 阳浩
 * @Date : 2018/9/30
 * @Time : 9:42
 * @Annotation :
 */
public class Dog {

    private String name;

    private Integer age;

    public Dog() {
    }

    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

~~~



SpringBootApplicationDemo类

~~~java
package com.ooyhao.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : 阳浩
 * @Date : 2018/9/29
 * @Time : 14:25
 * @Annotation :
 */
@RestController
@SpringBootApplication
public class SpringBootApplicationDemo {

    @Autowired
    private Person person;

    @RequestMapping(value = "/index")
    public String index(){
        return person.toString();
    }




    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationDemo.class,args);
    }


}

~~~

结果图：

![1538274061294](pic\yml1.png)

#### 3.springboot解决时间格式问题

##### **方式一：可以使用@JsonFormat注解在属性上进行标识**

~~~java
 //也可以使用JsonFormat注释
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date created;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updated;
~~~

##### **方式二：可以在时间属性对应的get方法上使用@JsonFormat进行标识**

~~~java
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
~~~

##### **方式三：可以在全局配置文件application.properties/application.yaml中进行配置**

~~~properties
# 设置日期格式
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
~~~

#### 4.注释

~~~java
@ResController = @Controller + @ResponseBody
@GetMapping(value="") = @RequestMapping(value="", method=RequestMethod.GET)
@SpringBootApplication = @ComponentScan+@EnableAutoConfiguration+@Configuration
~~~

#### 5.配置文件

~~~xml
当application.properties 与 application.yml文件共存时，并且存在同样的配置时，优先读取application.properties文件中的内容

~~~

配置文件

~~~properties
## 书信息
demo.book.name=[Spring Boot 2.x Core Action]
demo.book.writer=BYSocket
demo.book.description=${demo.book.writer}'s${demo.book.name}

~~~



使用@Value读取配置文件

~~~java
package demo.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 书属性
 *
 * Created by bysocket on 27/09/2017.
 */
@Component
public class BookProperties {

    /**
     * 书名
     */
    @Value("${demo.book.name}")
    private String name;

    /**
     * 作者
     */
    @Value("${demo.book.writer}")
    private String writer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}

~~~

使用@ConfigurationProperties读取配置文件

~~~java
package demo.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 书属性
 *
 */
@Component
@ConfigurationProperties(prefix = "demo.book")
@Validated
public class BookComponent {

    /**
     * 书名
     */
    @NotEmpty
    private String name;

    /**
     * 作者
     */
    @NotNull
    private String writer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}

~~~

通过@ConfigurationProperties的prefix属性来指定相应的配置信息