

### Combobox教程

**第一种Class加载方式**



```jsp
<select class="easyui-combobox" style="width:150px;">
    <option value="1">aaa</option>
    <option value="2">bbb</option>
    <option value="3">ccc</option>
    <option value="4">ddd</option>
</select>
```

效果图：

![1533653721245](C:\Users\Administrator\Desktop\EasyUI自制教程\Combobox\pic\01.png)

**第二种JS加载方式：**

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
    <title>Combobox下拉框</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/easyui/themes/color.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/Combobox.js"></script>
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
   <%-- <select class="easyui-combobox" style="width:150px;">
        <option value="1">aaa</option>
        <option value="2">bbb</option>
        <option value="3">ccc</option>
        <option value="4">ddd</option>
    </select>--%>
    <input id="box" name="box">
    <button onclick="myClear();">清除下拉框中的值</button>
    <button onclick="myReloadData();">重新加载数据</button>
    <button onclick="mySetValue();">手动设置值</button>
    <button onclick="mySetValues();">手动设置多个值</button>
    <button onclick="mySelect();">选择</button>
    <button onclick="myUnSelect();">取消选择</button>
</body>
</html>

```

JS文件

```js
$(function () {
    $("#box").combobox({
        //基础数据值名称绑定到改下拉列表框，默认是value。
        valueField:"id",
        //基础数字段名称绑定到该下拉列表框，默认为text。
        textField:"user",
        //指定分组字段
        // groupField:"id",
        groupFormatter:function (group) {
            //返回格式化后的分组文本，以显示分组项
        },

        //通过URL加载远程数据
        url:"getUsers.action",
        //请求的方式
        method:"post",
        //用户输入时就会实时将输入的数据以参数名为q的形式发送到后台服务器上。
        mode:"local",
        // mode:"remote",
        /*filter:function (q,row) {
            //设置长度大于9的时候输入查询时可以显示
          /!*if(row.user.length>9){
              return true;
          }
          return false;*!/
        },*/
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) >= 0;
        },
        /*格式化行显示的样式*/
        formatter:function (row) {
            return "["+row.id+"] "+"["+row.user+"]";
        },
        loadFilter:function (data) {
            //在数据加载前加数据进行过滤或修改
            for(var d = 0; d < data.length; d++){
                if(data[d].user.length<=9){
                    data[d].user = "---";
                }
            }
            // console.log(data);
            return data;
        },
        loader:function (param,success,error) {

        },
       /* data: [{
                id: 0,
                user: "yanghao0",
            },
            {
                id: 1,
                user: "yanghao11",
            },
            {
                id: 2,
                user: "yanghao222",
            },{
                id: 3,
                user: "yanghao3333",
            },{
                id: 4,
                user: "yanghao44444",
            },],*/
        onBeforeLoad:function () {
            // alert("在加载前执行...");
        },
        onLoadSuccess:function (data) {
            // alert("加载成功时执行...");

        },
        onLoadError:function () {
            // alert("加载失败时执行...");
        },
        onSelect:function (data) {
            //data 表示返回选中的行
            // alert("选中某项时执行...");
            // console.log(data);
            //getData方法是用来获取所有的数据。
            // console.log($("#box").combobox("getData"));
            console.log(data);
        },

        onUnselect:function (data) {
            //data返回取消选中的行
            // alert("取消选中时执行...");
            // console.log(data);
        }
    });
    /*$("#box").combobox("loadData",[
        {
            id:0,
            user:"yanghao0",
        },
        {
            id:1,
            user:"yanghao1",
        },
        {
            id:2,
            user:"yanghao2",
        },
    ]);*/

});
function myClear() {
    $("#box").combobox("clear");
}
function myReloadData() {
    $("#box").combobox("reload","getUsers.action");
}
function mySetValue() {
    $("#box").combobox("setValue","2");
}
function mySetValues() {
    $("#box").combobox("setValues","2,3");
}
function mySelect() {
    $("#box").combobox("select","2");
}
function myUnSelect() {
    $("#box").combobox("unselect","2");
}
```

**Controller文件：**

```java
package com.ooyhao.controller;

import com.ooyhao.pojo.User;
import com.ooyhao.service.ComboboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author ooyhao
 */
@Controller
public class ComboboxController {

    @Autowired
    private ComboboxService comboboxService;

    @RequestMapping(value = "/initCombobox")
    public String index(){
        return "Combobox";
    }

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public List<User> getUsersCombobox(){
        List<User> maps = comboboxService.getUsersCombobox();
        System.out.println(maps);
        return maps;
    }
}

```

这里介绍EasyUI的第二个高频出现的组件，下拉框组件，在后台管理的过程中，下拉框可以说中奖率高达百分之九十多。

**效果图：**

![1533960068022](C:\Users\Administrator\Desktop\EasyUI自制教程\Combobox\pic\02.png)

