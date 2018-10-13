

#### EasyUI 数据表格（DataGrid）——第一节

JS文件

```js
$(function () {
    $("#box").datagrid({
        //设置请求路径
        url:"#",
        //设置表格宽度
        width: 500,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls:"icon-search",
        //设置表头标题
        title:"EasyUI数据表格",
        //设置是否显示行号
        rownumbers:true,
        //设置是否分页
        pagination:true,
        //设置分页时初始化页数
        pageNumber:1,
        //设置每页显示的条数
        pageSize:5,
        //设置显示条数的下拉列表
        pageList:[5,10,20],
        //设置分页导航的位置
        pagePosition:"bottom",
        //设置列适应
        fitColumns : true,
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100
            }, {
                title: "邮箱",
                field: "email",
                width: 100
            },{
                title:"日期",
                field:"date",
                width:100
            }
        ]],
        data:[
            {
                user:"贾宝玉",
                email:"jiabaoyu@qq.com",
                date:"1996-01-01"
            },{
                user:"林黛玉",
                email:"lindaiyu@qq.com",
                date:"1996-01-02"
            },{
                user:"薛宝钗",
                email:"xuebaochai@qq.com",
                date:"1996-01-03"
            },{
                user:"贾惜春",
                email:"jiaxichun@qq.com",
                date:"1996-01-04"
            },{
                user:"袭人",
                email:"xiren@qq.com",
                date:"1996-01-05"
            },{
                user:"贾迎春",
                email:"jiayingchun@qq.com",
                date:"1996-01-06"
            }
        ]
    });
});
```

jsp文件

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid.js"></script>
</head>
<body>
    <table id="box"></table>
</body>
</html>
```

效果图：（数据加载方式为本地加载）

![1532831582682](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\1532831582682.png)



#### EasyUI 数据表格（DataGrid）——第二节

使用SSM+DataGrid实现数据表格的展示，分页，排序功能

数据库设计：

![](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\1532831582683.png)



![2018-07-29_141402](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\2018-07-29_141402.png)

Mapper文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ooyhao.dao.UserMapper">

    <select id="findAllUsers" resultType="User">
        select * from tb_user order by ${sort} ${order} limit #{start} ,#{rows};
    </select>

    <select id="findCount" resultType="Integer">
        select count(*) from tb_user;
    </select>

</mapper>
```

DAO：

```java
package com.ooyhao.dao;

import com.ooyhao.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ooyhao
 */
public interface UserMapper {

    public List<User> findAllUsers(@Param("start") Integer start, @Param("rows") Integer rows,@Param("sort") String sort,@Param("order") String order);

    public Integer findCount();
}

```

Service And ServiceImpl

```java
package com.ooyhao.service;

import com.ooyhao.dao.UserMapper;
import com.ooyhao.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ooyhao
 */
public interface UserService {

    //分页获得用户列表
    public List<User> findAllUsers(Integer page,Integer rows,String sort, String order);
    //获得用户总记录数
    public Integer findCount();
}




package com.ooyhao.service;

import com.ooyhao.dao.UserMapper;
import com.ooyhao.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ooyhao
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAllUsers(Integer page, Integer rows, String sort, String order) {
        return userMapper.findAllUsers((page-1)*rows,rows,sort,order);
    }

    @Override
    public Integer findCount() {
        return userMapper.findCount();
    }
}

```

POJO类

```java
package com.ooyhao.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ooyhao
 */
public class User implements Serializable {
    //id
    private Integer id;
    //用户名
    private String user;
    //邮箱
    private String email;
    //日期
    private Date date;

    public User() {
    }

    public User(Integer id, String user, String email, Date date) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }
}

```

DataGridVo类

```java
package com.ooyhao.EasyUIVo;

import java.util.List;
import java.util.Map;

/**
 * @author ooyhao
 */
public class DataGridVo<T> {

    //总记录数
    private Integer total;
    //每一页的数据
    private List<T> rows;
    //表脚显示的数据
    private Map<String,Object> footer;

    public DataGridVo() {
    }

    public DataGridVo(Integer total, List<T> rows, Map<String, Object> footer) {
        this.total = total;
        this.rows = rows;
        this.footer = footer;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Map<String, Object> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, Object> footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "DataGridVo{" +
                "total=" + total +
                ", rows=" + rows +
                ", footer=" + footer +
                '}';
    }
}

```

Controller类

```java
package com.ooyhao.controller;

import com.ooyhao.EasyUIVo.DataGridVo;
import com.ooyhao.pojo.User;
import com.ooyhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ooyhao
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/init")
    public String init(String username, String password, Model model){
        model.addAttribute("message",
                "username:"+username+", password:"+password);
        return "message";
    }

    @RequestMapping("/initDataGrid")
    public String initDataGrid(){
        return "DataGrid";
    }
    @RequestMapping("/initDataGrid2")
    public String initDataGrid2(){
        return "DataGrid2";
    }

    @RequestMapping("/getDataGridList")
    @ResponseBody
    public DataGridVo<User> getDataGridList(Integer page, Integer rows,String sort, String order){
        DataGridVo<User> dataGridVo = new DataGridVo<User>();
        dataGridVo.setTotal(userService.findCount());
        List<User> users = userService.findAllUsers(page,rows,sort,order);
        dataGridVo.setRows(users);
        dataGridVo.setFooter(null);
        return dataGridVo;
    }

}

```

jsp文件

```jsp
<%--
  Created by IntelliJ IDEA.
  User: ooyhao
  Date: 2018/7/29 0029
  Time: 9:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid2.js"></script>
</head>
<body>
    <table id="box"></table>
</body>
</html>

```

JS文件

```javascript
$(function () {
    $("#box").datagrid({
        //设置请求路径
        url:"getDataGridList.action",
        //设置表格宽度
        width: 500,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls:"icon-search",
        //设置表头标题
        title:"EasyUI数据表格",
        //设置是否显示行号
        rownumbers:true,
        //设置是否分页
        pagination:true,
        //设置分页时初始化页数
        pageNumber:1,
        //设置每页显示的条数
        pageSize:5,
        //设置显示条数的下拉列表
        pageList:[5,10,20],
        //设置是否远程加载进行排序
        remoteSort : false,
        //设置默认排序的名称
        sortName : "date",
        //设置默认排序的方式
        sortOrder : "asc",
        //设置分页导航的位置
        pagePosition:"bottom",
        //设置列适应
        fitColumns : true,
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable:true,
                order:"desc",
            },{
                title:"日期",
                field:"date",
                width:100,
                sortable:true,
                order:"desc",
                //进行了日期的格式化
                formatter:function (value,row,index) {
                    var time = row.date;
                    var date = new Date(time);
                    return date.getFullYear()+"年"+(date.getMonth()+1)+" 月"+date.getDate()+" 日";
                }
            }
        ]],
        /*data:[
            {
                user:"贾宝玉",
                email:"jiabaoyu@qq.com",
                date:"1996-01-01"
            },{
                user:"林黛玉",
                email:"lindaiyu@qq.com",
                date:"1996-01-02"
            },{
                user:"薛宝钗",
                email:"xuebaochai@qq.com",
                date:"1996-01-03"
            },{
                user:"贾惜春",
                email:"jiaxichun@qq.com",
                date:"1996-01-04"
            },{
                user:"袭人",
                email:"xiren@qq.com",
                date:"1996-01-05"
            },{
                user:"贾迎春",
                email:"jiayingchun@qq.com",
                date:"1996-01-06"
            }
        ]*/
    });
});
```

效果图

![1532845335362](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\1532845335362.png)



#### EasyUI 数据表格（DataGrid）——第三节

新加一个行属性和列属性（代码中有标识）

JS代码

```javascript
$(function () {
    $("#box").datagrid({
        //设置请求路径
        url:"getDataGridList.action",
        //设置表格宽度
        width: 500,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls:"icon-search",
        //设置表头标题
        title:"EasyUI数据表格",
        //------------------------------------（小节三新加的属性）
        //
        //设置是否显示斑马线效果
        striped:true,
        //设置是否允许在一行显示所有数据
        nowrap:false,
        //设置列适应
        fitColumns : true,
        //请求数据时，但还未得到数据时显示的提示信息
        loadMeg:"俺正在努力的跑...",
        //设置是否只能选中一行数据
        singleSelect:true,
        //是否显示表头
        showHeader:true,
        //是否显示表脚（同上）
        showFooter:true,//需要在返回的数据中设置有footer数据
        scrollbarSize:60,
        //设置一列的样式
        rowStyler:function (index,row) {
            if(index % 2 == 0){
                return "background-color:#ccc";
            }else{
                return "background-color:cyan";
            }
        },
        //------------------------------------
        //设置是否显示行号
        rownumbers:true,
        //设置是否分页
        pagination:true,
        //设置分页时初始化页数
        pageNumber:1,
        //设置每页显示的条数
        pageSize:5,
        //设置显示条数的下拉列表
        pageList:[5,10,20],
        //设置是否远程加载进行排序
        remoteSort : false,
        //设置默认排序的名称
        sortName : "date",
        //设置默认排序的方式
        sortOrder : "asc",
        //设置分页导航的位置
        pagePosition:"bottom",
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100,
                //---------------------------------------(小节三新加的属性)
                //
                //设置列的对齐方式
                align:"center",
                //设置标题的对齐方式
                halign:"center",
                //设置是否可以改变大小
                resizable:false,
                //设置是否隐藏
                hidden:false,
                //formatter设置单元格格式，下面有用到
                // styler设置单元格样式，类似于上面的rowStyler.
                //-----------------------------------------
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable:true,
                order:"desc",
            },{
                title:"日期",
                field:"date",
                width:100,
                sortable:true,
                order:"desc",
                //进行了日期的格式化
                formatter:function (value,row,index) {
                    var time = row.date;
                    var date = new Date(time);
                    return date.getFullYear()+"年"+(date.getMonth()+1)+" 月"+date.getDate()+" 日";
                }
            }
        ]],

    });
});
```

是否允许在一行显示数据：

![](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\2018-07-29_142740.png)

是否显示表头

![2018-07-29_143144](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\2018-07-29_143144.png)

设置滚动条的大小

![2018-07-29_143341](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\2018-07-29_143341.png)

设置列的样式rowStyler

![2018-07-29_143658](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\2018-07-29_143658.png)

#### EasyUI 数据表格（DataGrid）——第四节

使用easyui实现查询的功能

```sql
<!--
    在项目中查询时间段的sql语句(时间类型为varchar)(数据库中的时间类型)：

    <if test="beginTime!=null and beginTime!=''">
    　　AND tm.add_time&gt;=#{beginTime}
    </if>
    <if test="endTime!=null and endTime!=''">
     　　AND tm.add_time &lt;=#{endTime}
    </if>
-->
```

Mapper文件

```xml
<!--
    在项目中查询时间段的sql语句(时间类型为varchar)(数据库中的时间类型)：

    <if test="beginTime!=null and beginTime!=''">
    　　AND tm.add_time&gt;=#{beginTime}
    </if>
    <if test="endTime!=null and endTime!=''">
     　　AND tm.add_time &lt;=#{endTime}
    </if>
    -->
    <select id="findAllUsers" resultType="User">
        select * from tb_user
        <where>
            <if test="user != null and user.length > 0">
                user like concat('%','${user}','%')
            </if>
            <if test="from != null">
                and date &gt;= #{from}
            </if>
            <if test="to != null">
                and date &lt;= #{to}
            </if>

        </where>

        order by ${sort} ${order} limit #{start} ,#{rows};
    </select>
```

JSP文件

```jsp
<%--
  Created by IntelliJ IDEA.
  User: ooyhao
  Date: 2018/7/29 0029
  Time: 9:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid4.js"></script>
    <style rel="stylesheet" type="text/css">
        .textbox{
            height:20px;
            margin:0;
            padding:0 2px;
            box-sizing:content-box;
        }
    </style>
</head>
<body>
    <table id="box"></table>
    <div id="tb" style="padding: 5px; height: auto;">
        <div style="margin-bottom: 5px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" <%--plain="true"--%>>添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" <%--plain="true"--%>>修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" <%--plain="true"--%>>删除</a>
        </div>
        <div style="padding:0 0 0 7px;">
            查询账号：<input type="text" class="textbox" name = "user" style="width:130px;">
            创建时间从：<input type="text" class="easyui-datebox" name="date_from" style="width:130px;">
            到：<input type="text" class="easyui-datebox" editable="false" name="date_to" style="width:130px;">
            <a href="#" class="easyui-linkbutton" editable="false" iconCls="icon-search" onclick="obj.search();">查询</a>
        </div>
    </div>

</body>
</html>

```

JS文件

```js


$(function () {

    //注意；如果定义在外面的话，可以不加var，也可以加var定义
    //但是写在这里面就不能写var将其变成隐式全局变量，否则无法访问
    obj = {
        search:function () {
            $('#box').datagrid("load",{
                user: $.trim($("input[name='user']").val()),
                dateFrom:$.trim($("input[name='date_from']").val()),
                dateTo:$.trim($("input[name='date_to']").val())
            });
        }
    };
    $("#box").datagrid({
        //设置请求路径
        url:"getDataGridList.action",
        //设置表格宽度
        width: 700,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls:"icon-search",
        //方式一
        toolbar:"#tb",
        //方式二
        /*toolbar:[
            {
                text:"添加",
                iconCls:"icon-add",
                handler:function () {
                    alert("添加");//进行相应的事件处理
                }
            },{
                text:"修改",
                iconCls:"icon-edit",
                handler:function () {
                    alert("修改");
                }
            },{
                text:"删除",
                iconCls:"icon-remove",
                handler:function () {
                    alert("删除");
                }
            }
        ],*/

        //设置表头标题
        title:"EasyUI数据表格",
        //设置是否显示斑马线效果
        striped:true,
        //设置列适应
        fitColumns : true,
        //设置是否显示行号
        rownumbers:true,
        //设置是否分页
        pagination:true,
        //设置分页时初始化页数
        pageNumber:1,
        //设置每页显示的条数
        pageSize:5,
        //设置显示条数的下拉列表
        pageList:[5,10,20],
        //设置是否远程加载进行排序
        remoteSort : false,
        //设置默认排序的名称
        sortName : "date",
        //设置默认排序的方式
        sortOrder : "asc",
        //设置分页导航的位置
        pagePosition:"bottom",
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100,
                //设置列的对齐方式
                align:"center",
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable:true,
                order:"desc",
            },{
                title:"日期",
                field:"date",
                width:100,
                sortable:true,
                order:"desc",
                //进行了日期的格式化
                formatter:function (value,row,index) {
                    var time = row.date;
                    var date = new Date(time);
                    return date.getFullYear()+"年"+(date.getMonth()+1)+" 月"+date.getDate()+" 日";
                }
            }
        ]],

    });

});
```

效果图

![1532855967341](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\1532855967341.png)



#### EasyUI 数据表格（DataGrid）——第五节

数据表格添加功能

JSP文件（需要在工具栏上添加保存和取消的两个按钮）

```jsp
<%--
  Created by IntelliJ IDEA.
  User: ooyhao
  Date: 2018/7/29 0029
  Time: 9:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid4.js"></script>
    <style rel="stylesheet" type="text/css">
        .textbox{
            height:20px;
            margin:0;
            padding:0 2px;
            box-sizing:content-box;
        }
    </style>
</head>
<body>
    <table id="box"></table>
    <div id="tb" style="padding: 5px; height: auto;">
        <div style="margin-bottom: 5px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="obj.add();">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" >修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" >删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" style="display: none;" id="save" onclick="obj.save();" >保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-redo" style="display: none;" id="redo" onclick="obj.redo();">取消编辑</a>
        </div>
        <div style="padding:0 0 0 7px;">
            查询账号：<input type="text" class="textbox" name = "user" style="width:130px;">
            创建时间从：<input type="text" class="easyui-datebox" editable="false" name="date_from" style="width:130px;">
            到：<input type="text" class="easyui-datebox" editable="false" name="date_to" style="width:130px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="obj.search();">查询</a>
        </div>
    </div>

</body>
</html>

```

JS文件

1.使用dateagrid的insertRow方法在指定位置添加一行（本例子是在第一行），我们也可以使用appendRow在这一页的最后一行后追加一行。

2.insertRow方法中的index是要插入的位置。row是要添加的数据。

3.在添加时需要将保存和取消两个按钮显示，同时使用datagrid的beginEdit方法将该行设置为可（开始）编辑状态。

```js
this.editRow = true;
$("#box").datagrid("insertRow", {
    index: 0,
    row: {}
});
$("#box").datagrid("beginEdit", 0);
// $("#save").css("display","inline-block");
// $("#redo").css("display","inline-block");
$("#save,#redo").show();
```

4.此时可以使用datagrid的列editor属性队该列的输入框进行设置

type:表示输入框的类型

options:进行相应的参数设置

```js
 editor: {
    // type:"datebox",//datebox只有年月日
    type: "datetimebox",//datetimebox既有年月日又有时分秒
    options: {
        //设置必须填
        required: true,
        //设置不可编辑
        editable: false,
    }

```

5.编辑之后，点击保存，触发保存按钮所绑定的事件。

将使用datagrid的endEdit方法设置该行为结束编辑状态。

当设置为结束编辑状态时，会立即触发datagrid的属性事件onAfterEdit，接受三个参数

​	rowIndex,行的下标

​	rowData,对应于完成编辑的行的记录

​	changes，更改后的字段(键)/值对

```js
save:function () {
    //需要在保存操作成功之后再执行。
    // $("#box,#redo").hide();
    // this.editRow = false;
    //将第一行设置为结束编辑
    $("#box").datagrid("endEdit",0);
},
    
    
//结束编辑触发
onAfterEdit:function (rowIndex,rowData,changes) {
    $("#save,#redo").hide();
    obj.editRow = false;
    console.log(rowData.user);
    console.log(rowData.email);
    console.log(rowData.date);
},
```

6.说redo，使用datagrid的rejectChanges方法可以是表格回到编辑前的状态，此时需要隐藏保存和取消两个按钮。

```js
redo:function () {
    $("#box").datagrid("rejectChanges");
    $("#box,#redo").hide();
    this.editRow = false;
}
```

使用editRow是用来标记不能一次添加多行。



完整代码：

```JS
$(function () {

    //注意；如果定义在外面的话，可以不加var，也可以加var定义
    //但是写在这里面就不能写var将其变成隐式全局变量，否则无法访问
    obj = {
        editRow: false,
        search: function () {
            $('#box').datagrid("load", {
                user: $.trim($("input[name='user']").val()),
                dateFrom: $.trim($("input[name='date_from']").val()),
                dateTo: $.trim($("input[name='date_to']").val())
            });
        },
        add: function () {
            //在本页末尾追加一行
            /* $('#box').datagrid("appendRow",{
                 user : "admin",
                 email: "admin@163.com",
                 date: "2018-8-8",
             });*/
            //在指定索引添加一行
            /*$("#box").datagrid("insertRow",{
                index:0,
                row:{
                    user : "admin",
                    email: "admin@163.com",
                    date: "2018-8-8",
                }
            });*/
            if(this.editRow == false){//设置只能添加一行
                this.editRow = true;
                $("#box").datagrid("insertRow", {
                    index: 0,
                    row: {}
                });
                $("#box").datagrid("beginEdit", 0);
                // $("#save").css("display","inline-block");
                // $("#redo").css("display","inline-block");
                $("#save,#redo").show();
            }else{
                alert("不允许重复添加");
            }

        },
        save:function () {
            //需要在保存操作成功之后再执行。
            // $("#box,#redo").hide();
            // this.editRow = false;
            //将第一行设置为结束编辑
            $("#box").datagrid("endEdit",0);
        },
        redo:function () {
            $("#box").datagrid("rejectChanges");
            $("#box,#redo").hide();
            this.editRow = false;
        }
    };

    $("#box").datagrid({
        //设置请求路径
        url: "getDataGridList.action",
        //设置表格宽度
        width: 700,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls: "icon-search",
        //方式一
        toolbar: "#tb",
        //方式二
        /*toolbar:[
            {
                text:"添加",
                iconCls:"icon-add",
                handler:function () {
                    alert("添加");//进行相应的事件处理
                }
            },{
                text:"修改",
                iconCls:"icon-edit",
                handler:function () {
                    alert("修改");
                }
            },{
                text:"删除",
                iconCls:"icon-remove",
                handler:function () {
                    alert("删除");
                }
            }
        ],*/

        //设置表头标题
        title: "EasyUI数据表格",
        //设置是否显示斑马线效果
        striped: true,
        //设置列适应
        fitColumns: true,
        //设置是否显示行号
        rownumbers: true,
        //设置是否分页
        pagination: true,
        //设置分页时初始化页数
        pageNumber: 1,
        //设置每页显示的条数
        pageSize: 5,
        //设置显示条数的下拉列表
        pageList: [5, 10, 20],
        //设置是否远程加载进行排序
        remoteSort: false,
        //设置默认排序的名称
        sortName: "date",
        //设置默认排序的方式
        sortOrder: "asc",
        //设置分页导航的位置
        pagePosition: "bottom",
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100,
                //设置列的对齐方式
                align: "center",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                    }
                }
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable: true,
                order: "desc",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                        validType: "email",
                    }
                }
            }, {
                title: "日期",
                field: "date",
                width: 100,
                sortable: true,
                order: "desc",
                //进行了日期的格式化
                formatter: function (value, row, index) {
                    var time = row.date;
                    if (time == null) {
                        return "";
                    }
                    var date = new Date(time);
                    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                       +"&nbsp;&nbsp;" +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                }, editor: {
                    // type:"datebox",//datebox只有年月日
                    type: "datetimebox",//datetimebox既有年月日又有时分秒
                    options: {
                        //设置必须填
                        required: true,
                        //设置不可编辑
                        editable: false,
                    }
                }
            }
        ]],
        //结束编辑触发
        onAfterEdit:function (rowIndex,rowData,changes) {
            $("#save,#redo").hide();
            obj.editRow = false;
            console.log(rowData.user);
            console.log(rowData.email);
            console.log(rowData.date);
        },

    });

    //扩展dateTimeBox(手册上的代码)
    $.extend($.fn.datagrid.defaults.editors, {
        datetimebox: {
            init: function (container, options) {
                var input = $('<input type="text">').appendTo(container);
                options.editable = false;
                input.datetimebox(options);
                return input;
            },
            getValue: function (target) {
                return $(target).datetimebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datetimebox('setValue', value);
            },
            resize: function (target, width) {
                $(target).datetimebox('resize', width);
            },
            destroy: function (target) {
                $(target).datetimebox('destroy');
            },
        }
    });

});
```

效果图：

![1532869024596](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\1532869024596.png)

#### EasyUI 数据表格（DataGrid）——第六节

修改表格数据

```jsp
<%--
  Created by IntelliJ IDEA.
  User: ooyhao
  Date: 2018/7/29 0029
  Time: 9:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid5.js"></script>
    <style rel="stylesheet" type="text/css">
        .textbox{
            height:20px;
            margin:0;
            padding:0 2px;
            box-sizing:content-box;
        }
    </style>
</head>
<body>
    <table id="box"></table>
    <div id="tb" style="padding: 5px; height: auto;">
        <div style="margin-bottom: 5px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="obj.add();">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="obj.edit();" >修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" >删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" style="display: none;" id="save" onclick="obj.save();" >保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-redo" style="display: none;" id="redo" onclick="obj.redo();">取消编辑</a>
        </div>
        <div style="padding:0 0 0 7px;">
            查询账号：<input type="text" class="textbox" name = "user" style="width:130px;">
            创建时间从：<input type="text" class="easyui-datebox" editable="false" name="date_from" style="width:130px;">
            到：<input type="text" class="easyui-datebox" editable="false" name="date_to" style="width:130px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="obj.search();">查询</a>
        </div>
    </div>
        
</body>
</html>

```



Js文件

```js
$(function () {

    //注意；如果定义在外面的话，可以不加var，也可以加var定义
    //但是写在这里面就不能写var将其变成隐式全局变量，否则无法访问
    obj = {
        editRow: false,
        editCurrentIndex:-1,
        search: function () {
            $('#box').datagrid("load", {
                user: $.trim($("input[name='user']").val()),
                dateFrom: $.trim($("input[name='date_from']").val()),
                dateTo: $.trim($("input[name='date_to']").val())
            });
        },
        add: function () {
            //在本页末尾追加一行
            /* $('#box').datagrid("appendRow",{
                 user : "admin",
                 email: "admin@163.com",
                 date: "2018-8-8",
             });*/
            //在指定索引添加一行
            /*$("#box").datagrid("insertRow",{
                index:0,
                row:{
                    user : "admin",
                    email: "admin@163.com",
                    date: "2018-8-8",
                }
            });*/
            if (this.editRow == false) {//设置只能添加一行
                this.editRow = true;
                $("#box").datagrid("insertRow", {
                    index: 0,
                    row: {}
                });
                $("#box").datagrid("beginEdit", 0);
                // $("#save").css("display","inline-block");
                // $("#redo").css("display","inline-block");
                $("#save,#redo").show();
                obj.editCurrentIndex = 0;
            } else {
                alert("不允许重复添加");
            }

        },
        save: function () {
            //需要在保存操作成功之后再执行。
            // $("#box,#redo").hide();
            // this.editRow = false;
            //将第一行设置为结束编辑
            $("#box").datagrid("endEdit", obj.editCurrentIndex);
        },
        redo: function () {
            $("#box").datagrid("rejectChanges");
            $("#box,#redo").hide();
            this.editRow = false;
        },
        edit: function () {
            var rows = $("#box").datagrid("getSelections");
            console.log(rows);
            console.log(rows);

            $("#box").datagrid("beginEdit", 0);

        }
    };

    $("#box").datagrid({
        //设置请求路径
        url: "getDataGridList.action",
        //设置表格宽度
        width: 700,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls: "icon-search",
        //方式一
        toolbar: "#tb",
        //方式二
        /*toolbar:[
            {
                text:"添加",
                iconCls:"icon-add",
                handler:function () {
                    alert("添加");//进行相应的事件处理
                }
            },{
                text:"修改",
                iconCls:"icon-edit",
                handler:function () {
                    alert("修改");
                }
            },{
                text:"删除",
                iconCls:"icon-remove",
                handler:function () {
                    alert("删除");
                }
            }
        ],*/
        singleSelect: true,
        //设置表头标题
        title: "EasyUI数据表格",
        //设置是否显示斑马线效果
        striped: true,
        //设置列适应
        fitColumns: true,
        //设置是否显示行号
        rownumbers: true,
        //设置是否分页
        pagination: true,
        //设置分页时初始化页数
        pageNumber: 1,
        //设置每页显示的条数
        pageSize: 5,
        //设置显示条数的下拉列表
        pageList: [5, 10, 20],
        //设置是否远程加载进行排序
        remoteSort: false,
        //设置默认排序的名称
        sortName: "user",
        //设置默认排序的方式
        sortOrder: "asc",
        //设置分页导航的位置
        pagePosition: "bottom",
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100,
                //设置列的对齐方式
                align: "center",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                    }
                }
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable: true,
                order: "desc",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                        validType: "email",
                    }
                }
            }, {
                title: "日期",
                field: "date",
                width: 100,
                sortable: true,
                order: "desc",
                // //进行了日期的格式化
                // formatter: function (value, row, index) {
                //
                //     return 2018-01-10;
                //
                // //     var time = row.date;
                // //     var date = new Date(time);
                // //     // return date;
                // //     return (date.getFullYear()<=9?'0'+date.getFullYear():date.getFullYear()) + "-" +
                // //         ((date.getMonth() + 1)<=9?'0'+date.getMonth():date.getMonth()) + "-" +
                // //         (date.getDate()<=9?'0'+date.getDate():date.getDate());
                // //         // + "&nbsp;" + (date.getHours()<=9?'0'+date.getHours():date.getHours())
                // //         // + ":" + (date.getMinutes()<=9?'0'+date.getMinutes():date.getMinutes())
                // //         // + ":" + (date.getSeconds()<=9?'0'+date.getSeconds():date.getSeconds());
                // //     // return "";
                // // //
                // },
                    editor: {
                    // type:"datebox",//datebox只有年月日
                    type: "datetimebox",//datetimebox既有年月日又有时分秒
                    options: {
                        //设置必须填
                        required: true,
                        //设置不可编辑
                        editable: false,
                    }
                }
            }
        ]],
        //rowIndex点击行的索引值 从0开始
        //rowData点击行的记录
        //由于使用双击进入编辑状态与jQuery冲突，就不进行测试了。
        onDblClickRow:function (rowIndex,rowData) {
            $("#box").datagrid("endEdit",obj.editCurrentIndex);
            $("#box").datagrid("beginEdit",rowIndex);
            obj.editCurrentIndex = rowIndex;
            $("#save,#redo").show();
         },
        //结束编辑触发
        onAfterEdit: function (rowIndex, rowData, changes) {
            $("#save,#redo").hide();
            obj.editRow = false;
            console.log(rowData.user);
            console.log(rowData.email);
            console.log(rowData.date);
        },


    });

    //扩展dateTimeBox(手册上的代码)
    $.extend($.fn.datagrid.defaults.editors, {
        datetimebox: {
            init: function (container, options) {
                var input = $('<input type="text">').appendTo(container);
                options.editable = false;
                input.datetimebox(options);
                return input;
            },
            getValue: function (target) {
                return $(target).datetimebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datetimebox('setValue', value);
            },
            resize: function (target, width) {
                $(target).datetimebox('resize', width);
            },
            destroy: function (target) {
                $(target).datetimebox('destroy');
            },
        }
    });

});
```



可以看到，可以使用双击的方式来选中指定行，并使该行变成可编辑状态。

如果是只能同时编辑一行的话，需要记录下当前行的下标，在点击下一行，使得下一行变成可编辑状态前，需要将该行变成结束状态。（这里只是在前台页面进行了数据保存）。

**问题**：

​	在代码的运行过程中，如果后台传递的时间是Date的话，到前台会将其解析成一个毫秒值，尽管我之前使用formatter将数据进行格式化（yyyy-MM-dd HH:mm:ss），但是其数据本身还是一个毫秒值，所以在单元格变成可编辑状态前，datetimebox无法识别一个毫秒值，所以报错了。s.split()出错，

**原因**：

​	原因是datetimebox在解析的时候获得的是一个有格式的时间，yyyy-MM-dd  HH:mm:ss，但是一个毫秒值不满足这种格式，在内部执行分割函数的时候，就报错了。

**解决方式**：

​	将后台的数据格式有Date改为了String，所以是将有格式的时间通过字符串的方式传递到前台的，不会将其转化为一个毫秒值。（如果大家有什么好的解决方法，欢迎联系我）。

#### EasyUI 数据表格（DataGrid）——第七节

使用SSM实现删除，增加，更新操作，至此，前后台实现了增删改查。

项目的目录结构：

![1533568026115](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\7_1.png)

**数据库设计：**

​	数据库设计请查看第二节

**PoJo类：**

```java
package com.ooyhao.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ooyhao
 */
public class User implements Serializable {
    //id
    private Integer id;
    //用户名
    private String user;
    //邮箱
    private String email;
    //日期
    private String date;

    public User() {

    }

    public User(Integer id, String user, String email, Date date) {
        this.id = id;
        this.user = user;
        this.email = email;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = simpleDateFormat.format(date);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = simpleDateFormat.format(date);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }
}

```

VO类

```java
package com.ooyhao.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ooyhao
 */
public class UserVo implements Serializable {
    //id
    private Integer id;
    //用户名
    private String user;
    //邮箱
    private String email;
    //日期
    private String date;

    public UserVo() {

    }
    public UserVo(Integer id, String user, String email, String date) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.date = date;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

```

**Mapper接口：**

```java
package com.ooyhao.dao;

import com.ooyhao.pojo.User;
import com.ooyhao.pojo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author ooyhao
 */
public interface UserMapper {

    public List<User> findAllUsers(@Param("start") Integer start,
                                   @Param("rows") Integer rows,
                                   @Param("sort") String sort,
                                   @Param("order") String order,
                                   @Param("user") String user,
                                   @Param("from") Date from,
                                   @Param("to") Date to
    );

    public Integer findCount();

    public void deleteUser(@Param("id") Integer id);

    public void addUser(UserVo user);

    public void updateUser(UserVo userVo);
}

```

**Mapper xml文件：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ooyhao.dao.UserMapper">
    <!--
    在项目中查询时间段的sql语句(时间类型为varchar)(数据库中的时间类型)：

    <if test="beginTime!=null and beginTime!=''">
    　　AND tm.add_time&gt;=#{beginTime}
    </if>
    <if test="endTime!=null and endTime!=''">
     　　AND tm.add_time &lt;=#{endTime}
    </if>
    -->
    <select id="findAllUsers" resultType="User">
        select * from tb_user
        <where>
            <if test="user != null and user.length > 0">
                user like concat('%','${user}','%')
            </if>
            <if test="from != null">
                and date &gt;= #{from}
            </if>
            <if test="to != null">
                and date &lt;= #{to}
            </if>

        </where>

        order by ${sort} ${order} limit #{start} ,#{rows};
    </select>

    <select id="findCount" resultType="Integer">
        select count(*) from tb_user;
    </select>

    <!--删除用户-->
    <delete id="deleteUser" parameterType="Integer">
        delete from tb_user where id = #{id};
    </delete>

    <!--增加用户-->
    <insert id="addUser" parameterType="UserVo">
        insert into tb_user (user,email,date)values(#{user},#{email},#{date});
    </insert>

    <!--更新用户-->
    <update id="updateUser" parameterType="UserVo">
        UPDATE tb_user
        <set>
            <if test="user != null ">
                user = #{user},
            </if>
            <if test="email != null">
                email = #{email},
            </if>
            <if test="date != null">
                date = #{date},
            </if>
        </set>
        WHERE id = #{id}
    </update>
</mapper>
```

**Service文件：**

```java
package com.ooyhao.service;

import com.ooyhao.dao.UserMapper;
import com.ooyhao.pojo.User;
import com.ooyhao.pojo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author ooyhao
 */
public interface UserService {

    //分页获得用户列表
    public List<User> findAllUsers(Integer page, Integer rows, String sort, String order, String user, Date from, Date to);
    //获得用户总记录数
    public Integer findCount();
    //删除用户
    void deleteUser(Integer id);
    //新增用户
    void addUser(UserVo user);

    void updateUser(UserVo userVo);
}

```

**ServiceImpl文件：**

```java
package com.ooyhao.service;

import com.ooyhao.dao.UserMapper;
import com.ooyhao.pojo.User;
import com.ooyhao.pojo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ooyhao
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAllUsers(Integer page, Integer rows, String sort, String order,String user, Date from, Date to) {
        return userMapper.findAllUsers((page-1)*rows,rows,sort,order,user,from,to);
    }

    @Override
    public Integer findCount() {
        return userMapper.findCount();
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void addUser(UserVo userVo) {
        userMapper.addUser(userVo);
    }

    @Override
    public void updateUser(UserVo userVo) {
        userMapper.updateUser(userVo);
    }
}

```

**Controller文件：**

```java
package com.ooyhao.controller;

import com.ooyhao.EasyUIVo.DataGridVo;
import com.ooyhao.pojo.User;
import com.ooyhao.pojo.UserVo;
import com.ooyhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ooyhao
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/init")
    public String init(String username, String password, Model model){
        model.addAttribute("message",
                "username:"+username+", password:"+password);
        return "message";
    }

    @RequestMapping("/initDataGrid")
    public String initDataGrid(){
        return "DataGrid";
    }

    @RequestMapping("/initDataGrid4")
    public String initDataGrid2(){
        return "DataGrid4";
    }
    @RequestMapping("/initDataGrid5")
    public String initDataGrid5(){
        return "DataGrid5";
    }


    @RequestMapping("/getDataGridList")
    @ResponseBody
    public DataGridVo<User> getDataGridList(Integer page, Integer rows,String sort, String order,
                                            String user,String dateFrom,String dateTo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date from = null ;
        Date to = null;
        if(dateFrom != null && dateFrom.trim().length()>0){
            try {
                from = simpleDateFormat.parse(dateFrom);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(dateTo != null && dateTo.trim().length()>0){
            try {
                to = simpleDateFormat.parse(dateTo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        DataGridVo<User> dataGridVo = new DataGridVo<User>();
        dataGridVo.setTotal(userService.findCount());
        List<User> users = userService.findAllUsers(page,rows,sort,order,user,from,to);
//        System.out.println(users);
        dataGridVo.setRows(users);
        dataGridVo.setFooter(null);
        return dataGridVo;
    }

    //删除用户
    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public String deleteUser(Integer id){
        userService.deleteUser(id);
        return "success";
    }

    //保存用户
    @RequestMapping(value = "/saveUser")
    @ResponseBody
    public String saveUser(@RequestBody UserVo userVo){
        System.out.println(userVo);
        if(userVo.getId() == null){
            //表示是新增保存
            userService.addUser(userVo);
        }else{
            //表示是更新保存
            userService.updateUser(userVo);
        }
        return "success";
    }




}

```

**DataGridVo类：**

```java
package com.ooyhao.EasyUIVo;

import java.util.List;
import java.util.Map;

/**
 * @author ooyhao
 */
public class DataGridVo<T> {

    //总记录数
    private Integer total;
    //每一页的数据
    private List<T> rows;
    //表脚显示的数据
    private Map<String,Object> footer;

    public DataGridVo() {
    }

    public DataGridVo(Integer total, List<T> rows, Map<String, Object> footer) {
        this.total = total;
        this.rows = rows;
        this.footer = footer;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Map<String, Object> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, Object> footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "DataGridVo{" +
                "total=" + total +
                ", rows=" + rows +
                ", footer=" + footer +
                '}';
    }
}
```

**JSP文件：**

```java
<%--
  Created by IntelliJ IDEA.
  User: ooyhao
  Date: 2018/7/29 0029
  Time: 9:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DataGrid数据表格</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/DataGrid5.js"></script>
    <style rel="stylesheet" type="text/css">
        .textbox{
            height:20px;
            margin:0;
            padding:0 2px;
            box-sizing:content-box;
        }
    </style>
</head>
<body>
    <table id="box"></table>
    <div id="tb" style="padding: 5px; height: auto;">
        <div style="margin-bottom: 5px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="obj.add();">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="obj.edit();" >修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="obj.remove();" >删除</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" style="display: none;" id="save" onclick="obj.save();" >保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-redo" style="display: none;" id="redo" onclick="obj.redo();">取消编辑</a>
        </div>
        <div style="padding:0 0 0 7px;">
            查询账号：<input type="text" class="textbox" name = "user" style="width:130px;">
            创建时间从：<input type="text" class="easyui-datebox" editable="false" name="date_from" style="width:130px;">
            到：<input type="text" class="easyui-datebox" editable="false" name="date_to" style="width:130px;">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="obj.search();">查询</a>
        </div>
    </div>

</body>
</html>

```

**JS文件：**

```js
$(function () {

    //注意；如果定义在外面的话，可以不加var，也可以加var定义
    //但是写在这里面就不能写var将其变成隐式全局变量，否则无法访问
    obj = {
        editRow: false,
        editCurrentIndex:-1,
        search: function () {
            $('#box').datagrid("load", {
                user: $.trim($("input[name='user']").val()),
                dateFrom: $.trim($("input[name='date_from']").val()),
                dateTo: $.trim($("input[name='date_to']").val())
            });
        },
        add: function () {
            //在本页末尾追加一行
            /* $('#box').datagrid("appendRow",{
                 user : "admin",
                 email: "admin@163.com",
                 date: "2018-8-8",
             });*/
            //在指定索引添加一行
            /*$("#box").datagrid("insertRow",{
                index:0,
                row:{
                    user : "admin",
                    email: "admin@163.com",
                    date: "2018-8-8",
                }
            });*/
            if (this.editRow == false) {//设置只能添加一行
                this.editRow = true;
                $("#box").datagrid("insertRow", {
                    index: 0,
                    row: {}
                });
                $("#box").datagrid("beginEdit", 0);
                // $("#save").css("display","inline-block");
                // $("#redo").css("display","inline-block");
                $("#save,#redo").show();
                obj.editCurrentIndex = 0;
            } else {
                alert("不允许重复添加");
            }

        },
        save: function () {
            //需要在保存操作成功之后再执行。
            // $("#box,#redo").hide();
            // this.editRow = false;
            //将第一行设置为结束编辑
            $("#box").datagrid("endEdit", obj.editCurrentIndex);
        },
        redo: function () {
            $("#box").datagrid("rejectChanges");
            $("#save,#redo").hide();
            this.editRow = false;
        },
        edit: function () {
            var rows = $("#box").datagrid("getSelected");
            var index = $("#box").datagrid("getRowIndex",rows);
            console.log(index);
            if(index == -1){
                $.messager.alert("提示","请选择一条您要修改的行","info");
                return;
            }
            $("#box").datagrid("beginEdit", index);

            // this.editRow = true;
            $("#box").datagrid("endEdit", obj.editCurrentIndex);
            $("#save,#redo").show();
            obj.editCurrentIndex = index;
        },
        remove : function () {
            //仅仅做删除单个记录
            var row = $("#box").datagrid("getSelected");
            if (row == null || row.id == null) {
                $.messager.alert("操作提示", "请选择一条需要删除的记录", "info");
                return;
            }
            $.messager.confirm("操作提示", "您确定要进行删除操作吗？", function (data) {
                if (data) {
                    //确认需要删除，发送请求到后台。
                    $.ajax({
                        url: "deleteUser.action",
                        data :{
                            id : row.id,
                        },
                        type: "post",
                        success: function (data) {
                            if(data == "success"){
                                $("#box").datagrid("load",{});
                            }else{
                                $.messager.alert("操作失败");
                            }
                        }
                    });
                } else {
                    return;
                }
            });
        }

    };

    $("#box").datagrid({
        //设置请求路径
        url: "getDataGridList.action",
        //设置表格宽度
        width: 700,
        //设置请求方式
        type: "GET",
        //设置表头图标
        iconCls: "icon-search",
        //方式一
        toolbar: "#tb",
        //方式二
        /*toolbar:[
            {
                text:"添加",
                iconCls:"icon-add",
                handler:function () {
                    alert("添加");//进行相应的事件处理
                }
            },{
                text:"修改",
                iconCls:"icon-edit",
                handler:function () {
                    alert("修改");
                }
            },{
                text:"删除",
                iconCls:"icon-remove",
                handler:function () {
                    alert("删除");
                }
            }
        ],*/
        singleSelect: true,
        //设置表头标题
        title: "EasyUI数据表格",
        //设置是否显示斑马线效果
        striped: true,
        //设置列适应
        fitColumns: true,
        //设置是否显示行号
        rownumbers: true,
        //设置是否分页
        pagination: true,
        //设置分页时初始化页数
        pageNumber: 1,
        //设置每页显示的条数
        pageSize: 5,
        //设置显示条数的下拉列表
        pageList: [5, 10, 20],
        //设置是否远程加载进行排序
        remoteSort: false,
        //设置默认排序的名称
        sortName: "user",
        //设置默认排序的方式
        sortOrder: "asc",
        //设置分页导航的位置
        pagePosition: "bottom",
        //设置表格中的列
        columns: [[
            {
                //每一列的名字
                title: "用户名",
                //数据中的字段名（数据库中的字段名）
                field: "user",
                //每一列的宽度
                width: 100,
                //设置列的对齐方式
                align: "center",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                    }
                }
            }, {
                title: "邮箱",
                field: "email",
                width: 100,
                sortable: true,
                order: "desc",
                editor: {
                    // type:"text",
                    type: "validatebox",
                    options: {
                        required: true,
                        validType: "email",
                    }
                }
            }, {
                title: "日期",
                field: "date",
                width: 100,
                sortable: true,
                order: "desc",
                // //进行了日期的格式化
                // formatter: function (value, row, index) {},
                    editor: {
                    // type:"datebox",//datebox只有年月日
                    type: "datetimebox",//datetimebox既有年月日又有时分秒
                    options: {
                        //设置必须填
                        required: true,
                        //设置不可编辑
                        editable: false,
                    }
                }
            }
        ]],
        //rowIndex点击行的索引值 从0开始
        //rowData点击行的记录
        onDblClickRow:function (rowIndex,rowData) {
            $("#box").datagrid("endEdit",obj.editCurrentIndex);
            $("#box").datagrid("beginEdit",rowIndex);
            obj.editCurrentIndex = rowIndex;
            $("#save,#redo").show();
         },
        //结束编辑触发
        onAfterEdit: function (rowIndex, rowData, changes) {
            //结束编辑都会触发这里，所以在这里需要将数据提交到后台去。
            $("#save,#redo").hide();
            //将数据格式化为一个json字符串
            var _data = JSON.stringify(rowData);
            console.log(_data);
            $.ajax({
                url:"saveUser.action",
                type:"post",
                contentType: "application/json;charset=utf-8",
                // headers	:{'Content-Type': 'application/json'},
                // contentType:'application/json;charset=UTF-8',//关键是要加上这行
                data:JSON.stringify(rowData),
                // data:{
                //     user:_data,
                // },
                dataType:"text",
                success:function (data) {
                    $("#box").datagrid("load",{});
                    if(data == "success"){
                    }
                }

            });
            obj.editRow = false;
            console.log(rowData.user);
            console.log(rowData.email);
            console.log(rowData.date);
        },


    });

    //扩展dateTimeBox(手册上的代码)
    $.extend($.fn.datagrid.defaults.editors, {
        datetimebox: {
            init: function (container, options) {
                var input = $('<input type="text">').appendTo(container);
                options.editable = false;
                input.datetimebox(options);
                return input;
            },
            getValue: function (target) {
                return $(target).datetimebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datetimebox('setValue', value);
            },
            resize: function (target, width) {
                $(target).datetimebox('resize', width);
            },
            destroy: function (target) {
                $(target).datetimebox('destroy');
            },
        }
    });

});
```

效果图：

**增加：**

![1533568918517](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\72.png)

**修改：**

![1533568978656](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\73.png)

**删除：**

![1533569025738](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\74.png)

**查询：**

![1533569117294](C:\Users\Administrator\Desktop\EasyUI自制教程\DataGrid\pic\75.png)

使用SSM框架+EasyUI DataGrid数据表格实现了基本的增删改查，以及对数据分页，排序功能的实现。