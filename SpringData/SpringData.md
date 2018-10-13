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



## JDBC

### 1.  jdbc(PreparedStatement)

#### 1.1代码（操作数据库）

 ~~~java
package com.ooyhao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @Description: 提供获取连接和释放资源的方法
 *
 * @author ooyhao
 * @date 2018年5月3日 下午7:09:16
 * @version V1.0
 */
public class JDBCUtils_V3 {

	private static String driver = null;
	private static String url = null;
	private static String username = null;
	private static String password = null;

	/**
	 * 静态代码块加载配置文件信息
	 */
	static {
		try {
			// 1.通过当前类获取类加载器
			ClassLoader loader = JDBCUtils_V3.class.getClassLoader();
			// 2.通过类加载器的方法获取一个输入流
			InputStream in = loader.getResourceAsStream("db.properties");
			// 3.创建一个properties对象
			Properties props = new Properties();
			// 4.加载输入流
			props.load(in);
			// 5.获取相关参数的值
			driver = props.getProperty("driver");
			url = props.getProperty("url");
			username = props.getProperty("username");
			password = props.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取连接方法
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void release(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void release(Connection conn, PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

 ~~~



### 2.  使用javax.sql.DataSource实现自定义连接池

#### 2.1部分代码：

 ~~~java

// 1.创建一个容器用于存储Connection对象
	private static LinkedList<Connection> pool = new LinkedList<>();

	// 2.创建5个连接放到容器中
	static {
		for(int i = 0; i < 5; i++) {
			pool.add(JDBCUtils_V3.getConnection());
		}
	}

	/**
	 * 重写获取连接的方法
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// 3.使用前先判断
		if(pool.size() <= 0) {
			//4.池子中没有，再创建一些
			for(int i = 0; i < 5; i++) {
				pool.add(JDBCUtils_V3.getConnection());
			}
		}
		//5.从池子中获取一个连接对象(返回的是移除对象)
		Connection conn = pool.remove(0);
		return conn;
	}

	/**
	 * 归还连接对象到连接池
	 */
	public void backConnection(Connection conn) {
		pool.add(conn);
	}
	

 ~~~



### 3.  自定义连接池（增强close方法）

#### 3.1部分代码

 ~~~java
//实现同一个接口
public class MyConnection implements Connection {
	//3.定义一个变量
	private Connection conn = null;
	private LinkedList<Connection> pool;
	
	//2.编写一个构造方法(参数使用了面向对象中的多态)
	public MyConnection(Connection conn,LinkedList<Connection> pool) {
		this.conn = conn;
		this.pool = pool;
	}

	@Override
	public void close() throws SQLException {
		pool.add(conn);
	}
	
public class MyDataSource implements DataSource {

	// 1.创建一个容器用于存储Connection对象
	private static LinkedList<Connection> pool = new LinkedList<>();

	// 2.创建5个连接放到容器中
	static {
		for(int i = 0; i < 5; i++) {
			Connection conn = JDBCUtils_V3.getConnection();
			MyConnection myConn = new MyConnection(conn, pool);
			pool.add(myConn);
		}
	}

	/**
	 * 重写获取连接的方法
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// 3.使用前先判断
		if(pool.size() <= 0) {
			//4.池子中没有，再创建一些
			for(int i = 0; i < 5; i++) {
				Connection conn = JDBCUtils_V3.getConnection();
				MyConnection myConn = new MyConnection(conn, pool);
				pool.add(myConn);
			}
		}
		//5.从池子中获取一个连接对象(返回的是移除对象)
		Connection conn = pool.remove(0);
		return conn;
	}

 ~~~



### 4.  C3P0连接池      

c3p0-config.xml(名称一定)

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<c3p0-config>
	<default-config>
		<property name="driverClass">com.mysql.jdbc.Driver</property>
		<property name="jdbcUrl">jdbc:mysql:///user</property>
		<property name="user">root</property>
		<property name="password">123456</property>
		<property name="initialPoolSize">5</property>
		<property name="maxPoolSize">20</property>
	</default-config>

	<named-config name="ooyhao">
		<property name="driverClass">com.mysql.jdbc.Driver</property>
		<property name="jdbcUrl">jdbc:mysql:///user</property>
		<property name="user">root</property>
		<property name="password">123456</property>
	</named-config>
	
</c3p0-config>

~~~



C3P0Utils

~~~java
public class C3P0Utils {
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
}

~~~



### 5.  DBCP连接池

db.properties(DBCP不支持xml文件形式)

~~~properties
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/user
username=root
password=123456
~~~



DBCPUtils

~~~java
public class DBCPUtils {
	
	private static DataSource dataSource= null;
	
	static {
		//通过类加载器加载properties文件装换为输入流
		InputStream in = DBCPUtils.class.getClassLoader().getResourceAsStream("db.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
			//通过工厂类常见一个DataSource;
			dataSource = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取DataSource
	 * @return
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * 获取Connection
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}

}

~~~



### 6.  DBUilts

#### 1.ResultSetHandler

我们知道在执行select语句之后得到的是ResultSet，然后我们还需要对ResultSet进行转换，得到最终我们想要的数据。你可以希望把ResultSet的数据放到一个List中，也可能想把数据放到一个Map中，或是一个Bean中。

DBUtils提供了一个接口ResultSetHandler，它就是用来ResultSet转换成目标类型的工具。你可以自己去实现这个接口，把ResultSet转换成你想要的类型。

DBUtils提供了很多个ResultSetHandler接口的实现，这些实现已经基本够用了，我们通常不用自己去实现ResultSet接口了。

l  MapHandler：单行处理器！把结果集转换成Map<String,Object>，其中列名为键！

l  MapListHandler：多行处理器！把结果集转换成List<Map<String,Object>>；

l  BeanHandler：单行处理器！把结果集转换成Bean，该处理器需要Class参数，即Bean的类型；

l  BeanListHandler：多行处理器！把结果集转换成List<Bean>；

l  ColumnListHandler：多行单列处理器！把结果集转换成List<Object>，使用ColumnListHandler时需要指定某一列的名称或编号，例如：newColumListHandler(“name”)表示把name列的数据放到List中。

l  ScalarHandler：单行单列处理器！把结果集转换成Object。一般用于聚集查询，例如selectcount(*) from tab_student。

Map处理器

![说明: C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml\wps4D8C.tmp.jpg](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image001.jpg) 

 

Bean处理器 

![说明: C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml\wps4D8D.tmp.jpg](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image002.jpg) 

 

Column处理器

![说明: C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml\wps4D8E.tmp.jpg](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image003.jpg) 

Scalar处理器

![说明: C:\Users\ADMINI~1\AppData\Local\Temp\ksohtml\wps4D8F.tmp.jpg](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image004.jpg) 

 

#### 2.QueryRunner之查询

QueryRunner的查询方法是：

public <T> T query(String sql,ResultSetHandler<T> rh, Object… params)

public <T> T query(Connection con, Stringsql, ResultSetHandler<T> rh, Object… params)

query()方法会通过sql语句和params查询出ResultSet，然后通过rh把ResultSet转换成对应的类型再返回。

 

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image006.jpg)

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image008.jpg)![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image010.jpg)

#### 3.  使用DBUtils+C3P0实现CRUD

~~~java
/**
 * @Description: 测试DBUtils工具类
 *
 * @author ooyhao
 * @date 2018年5月4日 下午10:10:07
 * @version V1.0
 */
public class DBUtilsTest {

	/**
	 * 添加用户
	 */
	@Test
	public void testAddUser() {
		// 1.创建核心类QueryRunner类
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		// 2.书写sql语句
		String sql = "insert into tbl_user values (null,?,?);";
		try {
			// 3.参数数组
			Object[] params = { "yanghao", "yanghao" };
			// 4.执行
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils添加成功");
			} else {
				System.out.println("DBUtils添加失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过ID修改用户
	 */
	@Test
	public void testUpdateUserById() {
		// 1.创建核心类QueryRunner类
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		// 2.书写sql语句
		String sql = "update tbl_user set upassword = ? where uid = ?";
		try {
			// 3.参数数组
			Object[] params = { "ouyanghao", 14 };
			// 4.执行
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils修改成功");
			} else {
				System.out.println("DBUtils修改失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过Id删除用户
	 */
	@Test
	public void testDeleteUserById() {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from tbl_user where uid = ?";
		Object[] params = { 14 };
		try {
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils删除成功");
			} else {
				System.out.println("DBUtils删除失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		ResultSetHandler<List<User>> handler = new BeanListHandler<User>(User.class);
		try {
			List<User> users = qr.query(sql, handler);
			for (User u : users) {
				System.out.println(u);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser1() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		ResultSetHandler<List<Map<String,Object>>> handler = new MapListHandler();
		try {
			List<Map<String, Object>> users = qr.query(sql, handler);
			for(Map<String, Object> map : users) {
				System.out.println(map);
//				System.out.println(map.get("uid")+"-"+map.get("uname")+"-"+map.get("upassword"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser2() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		//获取指定列的集合
//		ResultSetHandler<List<Object>> handler = new ColumnListHandler();
//		ResultSetHandler<List<Object>> handler = new ColumnListHandler(2);
		ResultSetHandler<List<Object>> handler = new ColumnListHandler("uname");
		try {
			List<Object> users = qr.query(sql, handler);
			for(Object o : users) {
				System.out.println(o);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过用户id查询指定用户
	 */
	@Test
	public void testQueryUserById(){
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user where uid = ? ";
		Object[] params = {2};
		ResultSetHandler<User> hander = new BeanHandler<User>(User.class);
		User u;
		try {
			u = qr.query(sql, hander, params);
			System.out.println(u);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询用户的个数
	 */
	@Test
	public void testQueryCount() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tbl_user";
		//返回的是一个Long类型的数据
		Long count = (Long) qr.query(sql, new ScalarHandler());
		System.out.println(count.intValue());
	}

}

~~~



### 7.  事务

mysql默认会开启事务和提交事务。(每一条sql语句就是一个事务)

手动开启事务：start transaction;

手动提交事务：commit;

事务回滚：rollback;（事务回滚不包括事务提交）

#### 7.1JDBC事务操作

（控制事务的connection必须是同一个，执行SQL语句的connection与开启事务的connection必须是同一个才能对事务进行控制）

通过jdbc的api进行手动事务管理：

l  开启事务：conn.setAutoCommit(false);

l  提交事务：conn.commit();

l  回滚事务：conn.rollback();

#### 7.2DBUtils进行事务操作

![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image012.jpg)

 

#### 7.3DBUtils事务操作

##### 7.3.1QueryRunner

有参构造：QueryRunner runner = new QueryRunner(DataSource dataSource);

有参构造将数据源(连接池)作为参数传入QueryRunner，QueryRunner会从连接池中获得一个数据库    连  接资源操作数据库，所以直接使用无Connection参数的update方法即可操作数据库

无参构造：QueryRunner runner = new QueryRunner();

无参的构造没有将数据源（连接池）作为参数传入QueryRunner，那么我们在使用QueryRunner对象 操作数据库时要使用有Connection参数的方法

##### 7.3.1代码

TransferDao

~~~java
/**
 * @Description: 测试DBUtils工具类
 *
 * @author ooyhao
 * @date 2018年5月4日 下午10:10:07
 * @version V1.0
 */
public class DBUtilsTest {

	/**
	 * 添加用户
	 */
	@Test
	public void testAddUser() {
		// 1.创建核心类QueryRunner类
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		// 2.书写sql语句
		String sql = "insert into tbl_user values (null,?,?);";
		try {
			// 3.参数数组
			Object[] params = { "yanghao", "yanghao" };
			// 4.执行
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils添加成功");
			} else {
				System.out.println("DBUtils添加失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过ID修改用户
	 */
	@Test
	public void testUpdateUserById() {
		// 1.创建核心类QueryRunner类
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		// 2.书写sql语句
		String sql = "update tbl_user set upassword = ? where uid = ?";
		try {
			// 3.参数数组
			Object[] params = { "ouyanghao", 14 };
			// 4.执行
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils修改成功");
			} else {
				System.out.println("DBUtils修改失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过Id删除用户
	 */
	@Test
	public void testDeleteUserById() {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from tbl_user where uid = ?";
		Object[] params = { 14 };
		try {
			int rows = qr.update(sql, params);
			if (rows > 0) {
				System.out.println("DBUtils删除成功");
			} else {
				System.out.println("DBUtils删除失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		ResultSetHandler<List<User>> handler = new BeanListHandler<User>(User.class);
		try {
			List<User> users = qr.query(sql, handler);
			for (User u : users) {
				System.out.println(u);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser1() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		ResultSetHandler<List<Map<String,Object>>> handler = new MapListHandler();
		try {
			List<Map<String, Object>> users = qr.query(sql, handler);
			for(Map<String, Object> map : users) {
				System.out.println(map);
//				System.out.println(map.get("uid")+"-"+map.get("uname")+"-"+map.get("upassword"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询所有用户
	 */
	@Test
	public void testQueryAllUser2() {

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user";
		//获取指定列的集合
//		ResultSetHandler<List<Object>> handler = new ColumnListHandler();
//		ResultSetHandler<List<Object>> handler = new ColumnListHandler(2);
		ResultSetHandler<List<Object>> handler = new ColumnListHandler("uname");
		try {
			List<Object> users = qr.query(sql, handler);
			for(Object o : users) {
				System.out.println(o);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过用户id查询指定用户
	 */
	@Test
	public void testQueryUserById(){
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tbl_user where uid = ? ";
		Object[] params = {2};
		ResultSetHandler<User> hander = new BeanHandler<User>(User.class);
		User u;
		try {
			u = qr.query(sql, hander, params);
			System.out.println(u);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询用户的个数
	 */
	@Test
	public void testQueryCount() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tbl_user";
		//返回的是一个Long类型的数据
		Long count = (Long) qr.query(sql, new ScalarHandler());
		System.out.println(count.intValue());
	}

}

~~~



TransferService

~~~java
public class TransferService {

	public boolean transfer(String out, String in, double money) {
		boolean isTransferSuccess = true;
		// 创建Dao对象
		TransferDao dao = new TransferDao();
		Connection conn = C3P0Utils.getConnection();
		try {
			// 手动开启事务
			conn.setAutoCommit(false);
			// 转出钱的方法
			dao.out(conn, out, money);
			int i = 1 / 0;// 模拟出现异常
			// 转入钱的方法
			dao.in(conn, in, money);

		} catch (Exception e) {
			isTransferSuccess = false;
			try {
				// 进行事务回滚(回滚不包括提交)
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			// 进行事务提交
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isTransferSuccess;
	}

}

~~~



#### 7.4使用ThreadLocal实现事务管理

（一个ThreadLocal只能保存一个值（如果需要多个值，可以使用集合））

工具类

~~~java
public class MyDataSourceUtils {
	//获得Connection 从连接池中获取
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	//创建ThreadLocal(值的泛型,键固定为当前线程)
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	
	//开启事务
	public static void startTransaction() throws SQLException {
		//开启事务
		getCurrentConnection().setAutoCommit(false);
	}
	
	//回滚事务
	public static void rollback() throws SQLException {
		getCurrentConnection().rollback();
	}
	
	//提交事务
	public static void commit() throws SQLException {
		Connection conn = getCurrentConnection();
		conn.commit();
		//事务结束，将Connection从ThreadLocal中移除
		tl.remove();
		conn.close();
	}
	
	public static Connection getCurrentConnection() throws SQLException {
		//从ThreadLocal中找，是否有connection
		Connection conn = tl.get();
		if(conn == null) {
			//如果当前线程没有绑定Connection，则从池中拿一个connection，并将其存入到ThreadLocal
			conn = dataSource.getConnection();
			//将connection资源绑定到ThreadLocal上面
			tl.set(conn);
		}
		return conn;
	}
}

~~~



Dao层

~~~java
public class TransferDao {

	public void out(String out, double money) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "update account set money = money-? where name = ?";
		Object[] params = { money, out };
		runner.update(MyDataSourceUtils.getCurrentConnection(), sql, params);

	}
	public void in(String in, double money) throws SQLException {
		QueryRunner runner = new QueryRunner();
		String sql = "update account set money = money+? where name = ?";
		Object[] params = { money, in };
		runner.update(MyDataSourceUtils.getCurrentConnection(), sql, params);
	}

}

~~~



Service层

~~~java
public class TransferService {

	public boolean transfer(String out, String in, double money) {
		boolean isTransferSuccess = true;
		// 创建Dao对象
		TransferDao dao = new TransferDao();

		try {
			MyDataSourceUtils.startTransaction();
			// 转出钱的方法
			dao.out(out, money);
//			int i = 1 / 0;// 模拟出现异常
			// 转入钱的方法
			dao.in(in, money);
		} catch (Exception e) {
			isTransferSuccess = false;
			try {
				// 进行事务回滚(回滚不包括提交)
				MyDataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			// 进行事务提交
			try {
				MyDataSourceUtils.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isTransferSuccess;
	}
}

~~~



### 8.事务的特性和隔离级别（概念性问题---面试）

#### 8.1事务的特性ACID

         1）原子性（Atomicity）原子性是指事务是一个不可分割的工作单位，事务中的操作

要么都发生，要么都不发生。 

         2）一致性（Consistency）一个事务中，事务前后数据的完整性必须保持一致。

         3）隔离性（Isolation）多个事务，事务的隔离性是指多个用户并发访问数据库时，       一个用户的事务不能被其它用户的事务所干扰，多个并发事务之间数据要相互隔离。

         4）持久性（Durability）持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来即使数据库发生故障也不应该对其有任何影响。（事务提交，真正的持久化到数据库）

 

#### 8.2并发访问问题----由隔离性引起

如果不考虑隔离性，事务存在3中并发访问问题。

1）脏读：B事务读取到了A事务尚未提交的数据  ------  要求B事务要读取A事  务提交的数据

2）不可重复读：一个事务中两次读取的数据的内容不一致  ----- 要求的是一个事  务中多次读取时数据是一致的  --- update

3）幻读/虚读：一个事务中两次读取的数据的数量不一致  ----- 要求在一个事务多次读取的数据的数量是一致的 --insert  delete

#### 8.3事务的隔离级别

1）readuncommitted : 读取尚未提交的数据 ：哪个问题都不能解决

2）readcommitted：读取已经提交的数据 ：可以解决脏读 ---- oracle默认的

3）repeatableread：重读读取：可以解决脏读 和 不可重复读 ---mysql默认的

4）serializable：串行化：可以解决脏读不可重复读和虚读---相当于锁表

 

注意：mysql数据库默认的隔离级别

查看mysql数据库默认的隔离级别：select @@tx_isolation

设置mysql的隔离级别：set session transaction isolation level 设置事务隔离级别 

|      |                                                              |
| ---- | ------------------------------------------------------------ |
|      | ![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image014.jpg) |

 

|      |                                                              |
| ---- | ------------------------------------------------------------ |
|      | ![img](file:///C:\Users\ADMINI~1\AppData\Local\Temp\msohtmlclip1\01\clip_image016.jpg) |

 

#### 8.4总结

​                   mysql的事务控制：

​                            开启事务：start transaction;

​                            提交：commit；

​                            回滚：rollback；

​                   JDBC事务控制：

​                            开启事务：conn.setAutoCommit(false);

​                            提交：conn.commit()；

​                            回滚：conn.rollback()；

​                     DBUtils的事务控制 也是通过jdbc

​                     ThreadLocal：实现的是通过线程绑定的方式传递参数

​                     概念：

​                            事务的特性ACID

​                            并发问题：脏读、不可重读、虚读\幻读

​                            解决并发：设置隔离级别

​                                   readuncommitted

​                                   readcommitted

​                                   repeatableread （mysql默认）

​                                   serialazable

​                            隔离级别的性能：

​                                   readuncommitted>read committed>repeatable read>serialazable 

​                            安全性：

​                                   readuncommitted<read committed<repeatable read<serialazable

### 9.案例（增删改查CRUD）

#### 9.1案例一

使用request.getParameterMap 和 BeanUtils.*populate*(product,properties)结合实现数据的绑定

~~~java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//处理中文乱码
		//1.获取数据
		Map<String, String[]> properties = request.getParameterMap();
		//2.封装数据
		Product product = new Product();
		try {
			BeanUtils.populate(product, properties);
			
		} catch (IllegalAccessException | InvocationTargetException  e) {
			e.printStackTrace();
		}
		//此时Product已经封装完毕---使用BeanUtils将表单的数据封装完毕
		//手动设置表单没有的数据
		product.setPid(UUID.randomUUID().toString());
		product.setPimage("products/c_0003.jpg");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		product.setPdate(sdf.format(new Date()));//上架时间
		product.setPflag(0);//商品是否下架 0代表为未下架
		//3.传递数据给service层
		AdminProductService service = new AdminProductService();
		try {
			service.addProduct(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//跳转到list页面
		response.sendRedirect(request.getContextPath()+"/adminProductList");
	}

~~~



增删改查的Dao层实现

~~~java
public class AdminProductDao {

	public List<Product> findAllProduct() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product";
		ResultSetHandler<List<Product>> handler = new BeanListHandler<Product>(Product.class);
		return runner.query(sql, handler);
	}

	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from category";
		ResultSetHandler<List<Category>> handler = new BeanListHandler<Category>(Category.class);
		return runner.query(sql, handler);
	}

	public void addProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?);";
		Object[] params = {
			product.getPid(),
			product.getPname(),
			product.getMarket_price(),
			product.getShop_price(),
			product.getPimage(),
			product.getPdate(),
			product.getIs_hot(),
			product.getPdesc(),
			product.getPflag(),
			product.getCid()
		};
		runner.update(sql, params);
	}

	public void delProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from product where pid = ?";
		Object[] params = {pid};
		runner.update(sql,params);
	}

	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product where pid = ?";
		Object[] params = {pid};
		ResultSetHandler<Product> handler = new BeanHandler<Product>(Product.class);
		return runner.query(sql, handler,params);
	}

	public void updateProduct(Product product) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update product set pname = ?, "
				+ "market_price = ?,"
				+ "shop_price = ?,"
				+ "pimage = ?,"
				+ "pdate = ?,"
				+ "is_hot = ?,"
				+ "pdesc = ?,"
				+ "pflag = ?,"
				+ "cid = ? "
				+ "where pid = ?";

		Object[] params = {
			product.getPname(),
			product.getMarket_price(),
			product.getShop_price(),
			product.getPimage(),
			product.getPdate(),
			product.getIs_hot(),
			product.getPdesc(),
			product.getPflag(),
			product.getCid(),
			product.getPid()
		};
		runner.update(sql, params);
	}

}

~~~



#### 9.2多条件搜索

VO类，用户数据传输的类

~~~java
public class Condition {
	
	private String pname;
	private String isHot;
	private String cid;
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}	
}

~~~



数据访问层

~~~java
public List<Product> findProductListByCondition(Condition condition) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		StringBuilder sb = new StringBuilder("select * from product where 1 = 1");
		ArrayList<Object> list = new ArrayList<Object>();
		if(condition.getPname()!= null && !condition.getPname().trim().equals("")) {
			sb.append(" and pname like ? ");
			//模糊查询
			list.add("%"+condition.getPname()+"%");
		}
		if(condition.getIsHot()!= null && !condition.getIsHot().trim().equals("")) {
			sb.append(" and is_hot = ? ");
			list.add(condition.getIsHot());
		}
		if(condition.getCid()!= null && !condition.getCid().trim().equals("")) {
			sb.append(" and cid = ? ");
			list.add(condition.getCid());
		}
		Object[] params =(Object[]) list.toArray();
		String sql = sb.toString();
		ResultSetHandler<List<Product>> handler = new BeanListHandler<Product>(Product.class);
		return runner.query(sql, handler, params);
	}

~~~



web层

~~~java
protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 解决post提交中文乱码问题
		request.setCharacterEncoding("utf-8");
		// 1.收集表单的数据
		Map<String, String[]> properties = request.getParameterMap();
		// 2.将散装的查询数据封装到一个VO实体中
		Condition condition = new Condition();
		try {
			BeanUtils.populate(condition, properties);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// 3.将实体传递给service层
		AdminProductService service = new AdminProductService();
		List<Product> productList = null;
		try {
			productList = service.findProductListByCondition(condition);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 获得所有商品的分类数据
		List<Category> categoryList = null;
		try {
			categoryList = service.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("condition", condition);
		request.setAttribute("categoryList", categoryList);
		// 4.根据结果进行页面跳转
		request.setAttribute("productList", productList);
		request.getRequestDispatcher("/admin/product/list.jsp").forward(request, response);
	}

~~~



### 10.             分页（重点）

#### 10.1PageBean类

~~~java
/**
 * @Description: 分页类
 *
 * @author ooyhao
 * @date 2018年5月6日 上午8:28:54
 * @version V1.0
 */
public class PageBean<T> {
	// 当前页
	private Integer currentPage;
	// 当前页显示的条数
	private Integer currentCount;
	// 总条数
	private Integer totalCount;
	// 总页数
	private Integer totalPage;
	// 当前页显示的数据
	private List<T> productList = new ArrayList<T>();
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(Integer currentCount) {
		this.currentCount = currentCount;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public List<T> getProductList() {
		return productList;
	}
	public void setProductList(List<T> productList) {
		this.productList = productList;
	}
	@Override
	public String toString() {
		return "PageBean [currentPage=" + currentPage + ", currentCount=" + currentCount + ", totalCount=" + totalCount
				+ ", totalPage=" + totalPage + ", productList=" + productList + "]";
	}
}

~~~



#### 10.2 Service层

~~~java
public PageBean<Product> findPageBean(int currentPage, int currentCount) throws SQLException {
		PageBean<Product> pageBean = new PageBean<Product>();
		// 想办法封装一个pageBean返回
		// 1.当前页private Integer currentPage;
		pageBean.setCurrentPage(currentPage);

		// 2.当前页显示的条数private Integer currentCount;
		pageBean.setCurrentCount(currentCount);

		// 3.总条数private Integer totalCount;
		ProductDao dao = new ProductDao();
		int count = dao.getTotalCount();
		pageBean.setTotalCount(count);

		// 4.总页数private Integer totalPage;
		// pageBean.setTotalPage(count%currentCount==0 ? count/currentCount :
		// count/currentCount+1);
		pageBean.setTotalPage((int) Math.ceil(count * 1.0 / currentCount));

		// 5.当前页显示的数据private List<T> productList = new ArrayList<T>();
		List<Product> productList = dao.findProductListForPageBean((currentPage - 1) * currentCount, currentCount);
		pageBean.setProductList(productList);

		return pageBean;
	}

~~~



#### 10.3Dao层

~~~java
//获得所有商品的数量
	public int getTotalCount() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from product";
		Long count = (Long) runner.query(sql, new ScalarHandler());
		return count.intValue();
	}

	// 获得分页的商品数据
	public List<Product> findProductListForPageBean(int index, int currentCount) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from product limit ? , ? ";
		Object[] params = {index,currentCount};
		ResultSetHandler<List<Product>> handler = new BeanListHandler<Product>(Product.class);
		return runner.query(sql, handler,params);
	}

~~~



#### 10.4web层

~~~java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		
		//需要将数据传递过来
		String currentPageStr = request.getParameter("currentPage");
		int currentPage = 0;
		if(currentPageStr == null) {
			currentPage = 1;
		}else {			
			currentPage = Integer.parseInt(currentPageStr);
		}
		//每页显示多少条
		int currentCount = 12;
		PageBean<Product> pageBean = null;
		try {
			pageBean = service.findPageBean(currentPage,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("pageBean", pageBean);
		System.out.println(pageBean);
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

~~~



#### 10.5 前台页面jsp

~~~java
<!--分页 -->
	<div style="width: 380px; margin: 0 auto; margin-top: 50px;">
		<ul class="pagination" style="text-align: center; margin-top: 10px;">

			<c:if test="${pageBean.currentPage == 1}">
				<li class="disabled"><a href="javascript:void(0)"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>
			</c:if>
			<c:if test="${pageBean.currentPage != 1}">
				<li><a
					href="${pageContext.request.contextPath }/productList?currentPage=${pageBean.currentPage-1}"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>
			</c:if>

			<c:forEach begin="1" end="${pageBean.totalPage }" var="page">
				<c:choose>
					<c:when test="${pageBean.currentPage == page }">
						<li class="active"><a href="javascript:void(0)">${page}</a></li>
					</c:when>
					<c:otherwise>
						<li><a
							href="${pageContext.request.contextPath }/productList?currentPage=${page}">${page}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${pageBean.currentPage == pageBean.totalPage}">
				<li class="disabled"><a href="javascript:void(0)"
					aria-label="Next"> <span aria-hidden="true">&raquo;</span></a></li>
			</c:if>
			<c:if test="${pageBean.currentPage != pageBean.totalPage}">
				<li><a
					href="${pageContext.request.contextPath }/productList?currentPage=${pageBean.currentPage+1}"
					aria-label="Next"> <span aria-hidden="true">&raquo;</span></a></li>
			</c:if>
		</ul>
	</div>
<!-- 分页结束 -->

~~~



 

 



