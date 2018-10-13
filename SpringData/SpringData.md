SpringData学习



### 1.使用传统方式访问数据库（JDBC）

#### 0).PreparedStatement操作数据库应该调用什么方法

```xml
使用PreparedStatement来操作数据库时，查询使用executeQuery(), 增删改使用executeUpdate()方法 )。
```



#### 1).创建Maven项目

##### 	1.maven工程的目录结构

```xml
	java 用来写java文件的

	resources 目录是用来存放java文件

	test 是用来存放测试文件的

	pom.xml 文件 配置相应的依赖。
```
##### 	2.添加依赖

```xml
<dependencies>
  <!--这是单元测试-->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
  </dependency>
  <!-- 这是mysql的驱动-->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.38</version>
  </dependency>

</dependencies>
```

#### 2).数据库表的准备

```sql
-- 查看数据库
show databases;

-- 创建数据库
create database spring_data;

-- 切换数据库
use spring_data;

-- 查看数据库表
show tables;

-- 创建一张表
create table student(
    id int not null auto_increment,
    name varchar(20) not null,
    age int not null,
    primary key (id)
);

-- 查看表的结构
desc student;

-- 往数据库中插入数据
insert into student (name,age) values("zhangsan",20);
insert into student (name,age) values("lisi",21);
insert into student (name,age) values("wangwu",22);

-- 查询数据库中的数据
select * from student;

```

#### 3) 开发JDBCUtil工具类

```properties
1.获取connection，关闭Connection，Statement，ResultSet;
注意事项：配置的属性放在配置文件中，然后通过代码的方式将配置文件加载进来即可。


// db.properties文件
jdbc.url=jdbc:mysql:///spring_data
jdbc.user=root
jdbc.password=123456
jdbc.driverClass=com.mysql.jdbc.Driver
```

```java
// JDBCUtil工具类

package com.ooyhao.util;

import com.mysql.jdbc.Driver;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC工具类
 * 1.获取Connection
 * 2.释放资源
 */
public class JDBCUtil {

    /**
     * 获取Connection
     * @return 所获得到的JDBC的Connection
     */
    public static Connection getConnection() throws Exception {
        /**
         * 不建议把配置硬编码到代码中。
         *
         * 最佳实践：配置性的建议写到配置文件中。
         */
        /*String url = "jdbc:mysql:///spring_data";
        String user = "root";
        String password = "123456";
        String driverClass = "com.mysql.jdbc.Driver";*/
        InputStream inputStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.password");
        String driverClass = properties.getProperty("jdbc.driverClass");

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * 释放DB相关的资源
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void release(ResultSet resultSet,
                               Statement statement,Connection connection){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}


```

#### 4).建立对象模型，DAO层

```java
package com.ooyhao.dao;

import com.ooyhao.domain.Student;

import java.util.List;

/**
 * StudentDAO 接口
 *
 * @author ooyhao
 */
public interface StudentDAO {

    /**
     * 查询所有学生
     * @return 所有学生
     */
    public List<Student> query();

    /**
     * 添加一个学生
     * @param student 需要添加的学生
     */
    public void save(Student student);

}

```

DAOImpl

```java
package com.ooyhao.dao;

import com.ooyhao.domain.Student;
import com.ooyhao.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO访问接口实现类：通过最原始的JDBC的方法操作
 *
 * @author ooyhao
 */
public class StudentDAOImpl implements StudentDAO {

    @Override
    public List<Student> query() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Student> students = new ArrayList<Student>();
        try {
            //获得Connection对象
            connection = JDBCUtil.getConnection();
            String sql = " select id, name, age from student ";
            //通过Connection 获得 preparedStatement对象
            preparedStatement = connection.prepareStatement(sql);
            //通过preparedStatement对象执行查询
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Student student = new Student();
//                student.setId(Integer.valueOf(resultSet.getString("id")));
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(Integer.valueOf(resultSet.getString("age")));
                students.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }
        return students;
    }

    @Override
    public void save(Student student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = " insert into student (name,age) values (?,?) ";
        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,student.getName());
            preparedStatement.setInt(2,student.getAge());
            //增删改都是用executeUpdate()方法。
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }

    }
}
```



### 2.使用传统方式访问数据库（JDBCTemplate）

##### Maven 依赖

```xml
<!--spring jdbc-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>4.3.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>4.3.5.RELEASE</version>
</dependency>

```



##### **SpringJDBC实现方式**

（JdbcTemplate这里有很多操作方法）

```java
package com.ooyhao.dao;

import com.ooyhao.domain.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO访问接口实现类：通过spring JDBC的方法操作
 *
 * @author ooyhao
 */
public class StudentDAOSpringJDBCImpl implements StudentDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Student> query() {
        final List<Student> students = new ArrayList<>();
        final String sql = " select * from student ";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                //resultSet的索引会自动移动，不需要自己使用while进行判断。
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                students.add(student);
            }
        });
        return students;
    }

    @Override
    public void save(Student student) {
        String sql = " insert into student (name, age) values (?,?) ";
//        Object[] param = new Object[]{
        Object[] param =  {
                student.getName(),student.getAge()
        };
        jdbcTemplate.update(sql,param);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

```







