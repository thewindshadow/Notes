### Hibernate笔记

#### 1.使用Hibernate实现基本的增删改查

**项目结构图**

![](C:\Users\5161\Desktop\Hibernate项目结构图.png)

**实体类**

```java
package com.ooyhao.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author : 阳浩
 * Date : 2018/8/30
 * Time : 16:49
 * Annotation : 客户持久化类
 */
@Entity
@Table(name = "cst_customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_name")
    private String custName;

    @Column(name = "cust_source")
    private String custSource;

    @Column(name = "cust_industry")
    private String custIndustry;

    @Column(name = "cust_level")
    private String custLevel;

    @Column(name = "cust_phone")
    private String custPhone;

    @Column(name = "cust_mobile")
    private String custMobile;

    public Customer() {
    }

    public Customer(String custName, String custSource, String custIndustry, String custLevel, String custPhone, String custMobile) {
        this.custName = custName;
        this.custSource = custSource;
        this.custIndustry = custIndustry;
        this.custLevel = custLevel;
        this.custPhone = custPhone;
        this.custMobile = custMobile;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custSource='" + custSource + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custMobile='" + custMobile + '\'' +
                '}';
    }
}
```

**Hibernate工具类**

```java
package com.ooyhao.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Author : 阳浩
 * Date : 2018/8/30
 * Time : 17:12
 * Annotation : Hibernate工具类
 */

public class HibernateUtils {

    private static SessionFactory sessionFactory = null;

    static {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     * 获得一个全新的session
     * @return
     */
    public static Session openSession(){
        return sessionFactory.openSession();
    }

    /**
     * 获得一个当前线程的session
     * @return
     */
    public static Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

}
```

**Hibernate主配置文件**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration SYSTEM
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<!--主配置文件-->
<hibernate-configuration>
    <session-factory>
        <!--jdbc驱动-->
        <property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
        <!--数据库连接URL-->
        <property name="hibernate.connection.url">
            jdbc:mysql:///hibernate?useUnicode=true&amp;characterEncoding=UTF8
        </property>
        <!--数据库连接用户名-->
        <property name="hibernate.connection.username">
            root
        </property>
        <!--数据库连接密码-->
        <property name="hibernate.connection.password">
            root
        </property>

        <!--设置方言-->
        <property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>

        <!--显示sql输出至控制台-->
        <property name="hibernate.show_sql">
            true
        </property>
        <!--格式化sql-->
        <property name="hibernate.format_sql">
            true
        </property>
        <property name="hibernate.hbm2ddl.auto">
            update
        </property>

        <!--如实体类不使用注释-->
        <!--<mapping resource="com/ooyhao/domain/Customer.hbm.xml"/>-->
        <!--实体类使用注释-->
        <mapping class="com.ooyhao.domain.Customer"></mapping>
    </session-factory>
</hibernate-configuration>
```

**测试类**

```java
import com.ooyhao.domain.Customer;
import com.ooyhao.utils.HibernateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

/**
 * Author : 阳浩
 * Date : 2018/8/30
 * Time : 16:44
 * Annotation :
 */
public class HibernateTest {

    //查询
    @Test
    public void testQuery(){
        Session session = HibernateUtils.openSession();
        Query query = session.createQuery("from Customer");
        List<Customer> list = query.list();
        for(Customer customer : list){
            System.out.println(customer);
        }
        session.close();
    }

    //保存
    @Test
    public void testSave(){
        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = new Customer();
        customer.setCustName("林黛玉");
        customer.setCustSource("红楼梦");
        customer.setCustIndustry("大观园");
        customer.setCustLevel("2");
        customer.setCustMobile("11223344");
        customer.setCustPhone("22331122");
        session.save(customer);
        transaction.commit();
        session.close();
    }


    //更新
    @Test
    public void testUpdate(){
        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();
        //根据id更新数据
        Customer customer = session.get(Customer.class, 15L);
        customer.setCustName("齐天大圣");
        session.update(customer);
        transaction.commit();
        session.close();

    }

    //删除
    @Test
    public void testDelete(){
        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, 15L);
        session.delete(customer);
        transaction.commit();
        session.close();
    }

}
```

如果实体类不使用注释，则需要添加下面的配置文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<!--配置表和实体对象的关系-->
<!--package属性：填写一个报名，在元素内部凡是需要书写完整类名的属性，可以直接写简单类名了-->
<hibernate-mapping package="com.ooyhao.domain">
    <!--建立类和表的一个映射关系-->
    <!--
        class 标签：用来建立类和表的映射
          name属性：类的全路径
          table属性：表名（如果类名和表名是一致的，那么table属性可以忽略）
          catalog属性：数据库名称，可以忽略
    -->
    <!--由于上面mapping配置了package，这里就可以简写了-->
    <class name="Customer" table="cst_customer">
        <!--建立类中的属性与表中的主键字段对应-->
          <!--
          id标签：用来建立类中的属性（id）和表中的主键字段对应
            name属性：类中的属性名
            column属性（可选）:表中字段名（如果类的属性名与表中的字段名一致，那么省略column）列名默认会使用属性名
            length属性（可选）：字段的长度
            type属性（可选）：类型。写Java的数据类型，Hibernate数据类型（默认），SQL类型
            not-null属性（可选）：设置指定属性能否为空
            -->
        <id name="cust_id" column="cust_id">
            <!--主键生成策略-->
            <generator class="native"/>
        </id>
        <!--建立类中的普通属性与表中字段的映射
            property标签：用来建立类中的普通属性（除id之外）与表中的字段对应

            name属性：类中的属性名
            column属性（可选）：表中字段名（如果类中的属性名和表中的字段名一致，那么省略column）
            length属性（可选）：字段的长度
            type属性（可选）：类型，写java数据类型 ，Hibernate属性类型（默认），SQL类型
            not-null属性（可选）：设置指定属性能否为空
        -->
        <property name="cust_name" column="cust_name"/>
        <property name="cust_source" column="cust_source"/>
        <property name="cust_industry" column="cust_industry"/>
        <property name="cust_level" column="cust_level"/>
        <property name="cust_phone" column="cust_phone"/>
        <property name="cust_mobile" column="cust_mobile"/>

    </class>

</hibernate-mapping>
```

#### 2.hibernate映射文件

```xml
你需要以格式 <classname>.hbm.xml保存映射文件。我们保存映射文件在 Employee.hbm.xml 中。让我们来详细地看一下在映射文件中使用的一些标签:

    映射文件是一个以 <hibernate-mapping> 为根元素的 XML 文件，里面包含所有<class>标签。
    <class> 标签是用来定义从一个 Java 类到数据库表的特定映射。Java 的类名使用 name 属性来表示，数据库表明用 table 属性来表示。
    <meta> 标签是一个可选元素，可以被用来修饰类。
    <id> 标签将类中独一无二的 ID 属性与数据库表中的主键关联起来。id 元素中的 name 属性引用类的性质，column 属性引用数据库表的列。type 属性保存 Hibernate 映射的类型，这个类型会将从 Java 转换成 SQL 数据类型。
    在 id 元素中的 <generator> 标签用来自动生成主键值。设置 generator 标签中的 class 属性可以设置 native 使 Hibernate 可以使用 identity, sequence 或 hilo 算法根据底层数据库的情况来创建主键。
    <property> 标签用来将 Java 类的属性与数据库表的列匹配。标签中 name 属性引用的是类的性质，column 属性引用的是数据库表的列。type 属性保存 Hibernate 映射的类型，这个类型会将从 Java 转换成 SQL 数据类型。

```

#### 3.Hibernate注释

```xml
@Entity 注释

EJB 3 标准的注释包含在 javax.persistence 包，所以我们第一步需要导入这个包。第二步我们对 Employee 类使用 @Entity 注释，标志着这个类为一个实体 bean，所以它必须含有一个没有参数的构造函数并且在可保护范围是可见的。

@Table 注释

@table 注释允许您明确表的详细信息保证实体在数据库中持续存在。

@table 注释提供了四个属性，允许您覆盖的表的名称，目录及其模式,在表中可以对列制定独特的约束。现在我们使用的是表名为 EMPLOYEE。
@Id 和 @GeneratedValue 注释

每一个实体 bean 都有一个主键，你在类中可以用 @Id 来进行注释。主键可以是一个字段或者是多个字段的组合，这取决于你的表的结构。

默认情况下，@Id 注释将自动确定最合适的主键生成策略，但是你可以通过使用 @GeneratedValue 注释来覆盖掉它。strategy 和 generator 这两个参数我不打算在这里讨论，所以我们只使用默认键生成策略。让 Hibernate 确定使用哪些生成器类型来使代码移植于不同的数据库之间。
@Column Annotation

@Column 注释用于指定某一列与某一个字段或是属性映射的细节信息。您可以使用下列注释的最常用的属性:

    name 属性允许显式地指定列的名称。
    length 属性为用于映射一个值，特别为一个字符串值的列的大小。
    nullable 属性允许当生成模式时，一个列可以被标记为非空。
    unique 属性允许列中只能含有唯一的内容

```

#### 4.Hibernate主配置文件

```
Hibernate 需要事先知道在哪里找到映射信息，这些映射信息定义了 Java 类怎样关联到数据库表。Hibernate 也需要一套相关数据库和其它相关参数的配置设置。所有这些信息通常是作为一个标准的 Java 属性文件提供的，名叫 hibernate.properties。又或者是作为 XML 文件提供的，名叫 hibernate.cfg.xml。 

我们将考虑 hibernate.cfg.xml 这个 XML 格式文件，来决定在我的例子里指定需要的 Hibernate 应用属性。这个 XML 文件中大多数的属性是不需要修改的。这个文件保存在应用程序的类路径的根目录里。

```

**Hibernate 属性**

下面是一个重要的属性列表，你可能需要表中的属性来在单独的情况下配置数据库。

| **S.N.** | **属性和描述**                                               |
| -------- | ------------------------------------------------------------ |
| 1        | **hibernate.dialect**  这个属性使 Hibernate 应用为被选择的数据库生成适当的 SQL。 |
| 2        | **hibernate.connection.driver_class**  JDBC 驱动程序类。     |
| 3        | **hibernate.connection.url**  数据库实例的 JDBC URL。        |
| 4        | **hibernate.connection.username**  数据库用户名。            |
| 5        | **hibernate.connection.password**  数据库密码。              |
| 6        | **hibernate.connection.pool_size**  限制在 Hibernate 应用数据库连接池中连接的数量。 |
| 7        | **hibernate.connection.autocommit**  允许在 JDBC 连接中使用自动提交模式。 |

如果您正在使用 JNDI 和数据库应用程序服务器然后您必须配置以下属性:

| **S.N.** | **属性和描述**                                               |
| -------- | ------------------------------------------------------------ |
| 1        | **hibernate.connection.datasource**  在应用程序服务器环境中您正在使用的应用程序 JNDI 名。 |
| 2        | **hibernate.jndi.class**  JNDI 的 InitialContext 类。        |
| 3        | **hibernate.jndi.<JNDIpropertyname>** 在 JNDI的 InitialContext 类中通过任何你想要的 Java 命名和目录接口属性。 |
| 4        | **hibernate.jndi.url**  为 JNDI 提供 URL。                   |
| 5        | **hibernate.connection.username**  数据库用户名。            |
| 6        | **hibernate.connection.password**  数据库密码。              |