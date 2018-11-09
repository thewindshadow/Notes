## 8.Express

[TOC]

### 8.1 初始Express 

原生的http在某些方面表现的不足以应对我们的开发需求，所以我们就需要使用框架来加快我们的开发效率，框架的目的就是提高效率，让我们的代码高度统一。

在Node中有很多web开发框架，我们这里以学习express为主。

http://expressjs.com/ express官网首页。

![](image/expressInstall.png)

通过以下网址进行访问hello-world页面：

~~~js
http://expressjs.com/en/starter/hello-world.html
~~~

![](image/express-hello-world.png)

### 8.2 HelloWorld

~~~js
// 0.安装

// 1.引入包
var express = require('express');

// 2.创建服务器应用程序
// 		也就是原来的http.createServer();
// var app = express() ==> http.createServer();
var app = express();
app.get('/',function(req,res){
	res.send('Hello Express');
});

// 公开指定目录，这样就可以访问到公共的public目录了
app.use('/public/',express.static('./public/'));

//相当于server.listen();
app.listen(3333,function(){
	console.log('app is running at port 3333');
});
~~~

### 8.3 静态服务

~~~js
// 当以 /public/ 开头的时候，去 ./public/ 目录中找找对应的资源
// 这种方式更容易辨识，推荐这种方式
app.use('/public/', express.static('./public/'))

// 必须是 /a/puiblic目录中的资源具体路径 相当于（第一个参数是第二个参数路径的别名）
app.use('/abc/d/', express.static('./public/'))

// 当省略第一个参数的时候，则可以通过直接省略 /public 的方式来访问
// 这种方式的好处就是可以省略 /public/
app.use(express.static('./public/'))

~~~

### 8.4 Express中如何Get请求参数

在express内置了一个API，可以直接通过下面的方式直接获取get请求的参数。

~~~js
var paramsObj = request.query
~~~

### 8.5 Express中如何获取POST表单请求体数据

在express中没有内置获取表单请求体的API，需要结合第三方插件实现请求体数据。

- 1.安装

  ~~~shell
  npm install --save body-parser
  ~~~

- 2.导入模块

  ~~~js
  var bodyParser = require('body-parser');
  ~~~

- 3.配置中间件（专门用来解析表单post请求体）

  ~~~js
   // 配置body-parser 中间件（插件，专门用于解析post请求体）
   // 只要加入这个配置，则在req请求对象中会多出一个属性：body。
   // 也就是直接可以通过req.body来获得表单post请求体的数据了。
   app.use(bodyParser.urlencoded({extends:false}));
   app.use(bodyParser.json());
  ~~~

- 4.使用

  ~~~js
  app.post('/post',function(req,res) {
  	var comment = req.body;
  	comment.dateTime = getNow();
  	comments.unshift(comment);
  	res.redirect('/');
  });
  ~~~

  ​

### 8.6 使用Express+art-template+Bootstrap 搭建一个简易留言板

 **[官方API文档](http://expressjs.com/en/4x/api.html#req)**

-    1.创建message_board 文件夹，执行`npm init -y`或`npm init `跳过向导/不跳过创建package.json包描述文件。

-    2.通过`npm install --save express`安装 express

-    3.通过`npm install --save art-template`安装art-template

-    4.通过`npm install --save express-art-template`安装express与art-template关联包

-    5.通过`npm install --save body-parser`安装post请求解析工具

-    6.在项目根本下，public/lib/放bootstrap

-    7.编写app.js,放在项目根本下

     ~~~js
        //引入第三方模块
        var express = require('express');
        var bodyParser = require('body-parser');
        //创建app
        var app = express();
        //指定公开访问路径
        app.use('/public/',express.static('./public/'));
        //配置art-template模板引擎
        app.engine('html',require('express-art-template'));
        //app监听指定端口号
        app.listen(3333,function(){
        	console.log('app is running at port 3333');
        });
        /*配置body-parser中间件*/
        app.use(bodyParser.urlencoded({extends:false}));
        app.use(bodyParser.json()); 	

        //定义用于数据仓库
        var comments = [
        	{
        		name:'贾宝玉',
        		message:'你好啊！',
        		dateTime:'2018-11-01 11:11:11'
        	},{
        		name:'林黛玉',
        		message:'你好啊！',
        		dateTime:'2018-11-02 11:11:11'
        	},{
        		name:'薛宝钗',
        		message:'你好啊！',
        		dateTime:'2018-11-03 11:11:11'
        	},{
        		name:'袭人',
        		message:'你好啊！',
        		dateTime:'2018-11-04 11:11:11'
        	},{
        		name:'晴雯',
        		message:'你好啊！',
        		dateTime:'2018-11-05 11:11:11'
        	},
        ];
        //配置访问路由
        app.get('/',function(req,res){

        	res.render('index.html',{
        		comments:comments
        	});
        });

        app.get('/post',function(req,res){
        	res.render('post.html');
        });

        app.post('/post',function(req,res) {
        	//通过req.body的方式获得post方式的表单请求数据。
            var comment = req.body;
        	comment.dateTime = getNow();
        	comments.unshift(comment);
        	res.redirect('/');
        });
        //获得当前时间
        function getNow(){
        	var date = new Date();
        	var year = date.getFullYear();
        	var month = (date.getMonth()+1)>9?(date.getMonth()+1):'0'+(date.getMonth()+1);
        	var day = date.getDate()>9?date.getDate():'0'+date.getDate();
        	var hour = date.getHours()>9?date.getHours():'0'+date.getHours();
        	var minute = date.getMinutes()>9?date.getMinutes():'0'+date.getMinutes();
        	var second = date.getSeconds()>9?date.getSeconds():'0'+date.getSeconds();

        	return year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second;
        }
     ~~~





-    8.编写index.html放在项目根目录下的views文件夹下

     ~~~html
     <!DOCTYPE html>
     <html lang="en">

     <head>
       <meta charset="UTF-8">
       <title>留言本</title>
       <!-- 
         浏览器收到 HTML 响应内容之后，就要开始从上到下依次解析，
         当在解析的过程中，如果发现：
           link
           script
           img
           iframe
           video
           audio
         等带有 src 或者 href（link） 属性标签（具有外链的资源）的时候，浏览器会自动对这些资源发起新的请求。
        -->
        <!-- 
           注意：在服务端中，文件中的路径就不要去写相对路径了。
           因为这个时候所有的资源都是通过 url 标识来获取的
           我的服务器开放了 /public/ 目录
           所以这里的请求路径都写成：/public/xxx
           / 在这里就是 url 根路径的意思。
           浏览器在真正发请求的时候会最终把 http://127.0.0.1:3000 拼上

           不要再想文件路径了，把所有的路径都想象成 url 地址
         -->
       <link rel="stylesheet" href="/public/lib/bootstrap/css/bootstrap.css">
     </head>

     <body>
       <div class="header container">
         <div class="page-header">
           <h1>首页 <small>评论</small></h1>
           <a class="btn btn-success" href="/post">发表留言</a>
         </div>
       </div>
       <div class="comments container">
         <ul class="list-group">
           {{each comments}}
           <li class="list-group-item">{{$value.name}}说：{{$value.message}}<span class="pull-right">{{$value.dateTime}}</span></li>
           {{/each}}
         </ul>
       </div>
     </body>
     </html>
     ~~~

-    9.编写post.html文件，放在项目根路径下的views文件夹下

     ~~~html
              <!DOCTYPE html>
              <html lang="en">

              <head>
                <meta charset="UTF-8">
                <title>Document</title>
                <link rel="stylesheet" href="/public/lib/bootstrap/css/bootstrap.css">
              </head>

              <body>
                <div class="header container">
                  <div class="page-header">
                    <h1><a href="/">首页</a> <small>发表评论</small></h1>
                  </div>
                </div>
                <div class="comments container">
                  <!-- 
                    以前表单是如何提交的？
                    表单中需要提交的表单控件元素必须具有 name 属性
                    表单提交分为：
     1. 默认的提交行为
     2. 表单异步提交

     action 就是表单提交的地址，说白了就是请求的 url 地址
     method 请求方法
         get
         post

      -->
       <form action="/post" method="post">
         <div class="form-group">
           <label for="input_name">你的大名</label>
           <input type="text" class="form-control" required minlength="2" maxlength="10" id="input_name" name="name" placeholder="请写入你的姓名">
         </div>
         <div class="form-group">
           <label for="textarea_message">留言内容</label>
           <textarea class="form-control" name="message" id="textarea_message" cols="30" rows="10" required minlength="5" maxlength="20"></textarea>
         </div>
         <button type="submit" class="btn btn-default">发表</button>
       </form>
     </div>
     </html>
      
     ~~~

-    10.总结

     ~~~js
           // 处理请求（都会自动结束响应）
             res.redirect([status,] path) 用于重定向
             res.render(view [, locals] [, callback])  返回视图
             res.send([body])   返回http响应
             
             
         app.engine('html',require('express-art-template')); // 配置模板引擎。
         
         // 配置body-parser 中间件（插件，专门用于解析post请求体）
         app.use(bodyParser.urlencoded({extends:false}));
         app.use(bodyParser.json());

         //开放公共资源
         app.use('/public/',express.static('./public/'));

         //获得post请求中的请求体中的参数，使用req.body;req.query只能拿到get请求的参数
         app.post('/post',function(req,res){
           var comment = req.body;
           comment.dateTime = getNow();
           comments.unshift(comment);
           res.redirect('/');
         });
     ~~~

-    效果图：

        ![](image/feedback_home.png)

        ![](image/feedback_post.png)

### 8.7 使用Express基于Json文件 实现CRUD

#### 8.7.1 初始化项目

~~~shell
npm init --yes / -y  跳过向导初始化package.json
~~~

#### 8.7.2 安装包

~~~shell
#安装art-template模板
npm install --save art-template
#安装express框架
npm install --save express
#安装在express中整合art-template的整合包
npm install --save express-art-template
#安装解析post请求数据的第三方插件
npm install --save body-parser
#安装bootstrap
npm install --save bootstrap
#安装jquery
npm install --save jquery
~~~

- package.json

  ~~~json
  "dependencies": {
      "art-template": "^4.13.1",
      "body-parser": "^1.18.3",
      "bootstrap": "^4.1.3",
      "express": "^4.16.4",
      "express-art-template": "^1.0.1",
      "jquery": "^3.3.1"
    }
  ~~~

#### 8.7.3 设计url访问接口

~~~js
//get请求，获取所有数据
router.get('/students',function(req,res){});

//get请求，检索数据
router.get('/student',function(req,res){});

//get请求，跳转至添加页面
router.get('/student/add',function(req,res){});

//post请求，添加数据，跳转至列表页
router.post('/student/add',function(req,res){});

//get请求，删除数据，跳转至列表页
router.get('/student/del/:id',function(req,res){});

//get请求，跳转至更新页面，并需要回显数据
router.get('/student/edit',function(req,res){});

//post请求，更新数据，跳转至列表页
router.post('/student/edit',function(req,res){});
~~~

#### 8.7.4 设计数据的操作接口

~~~js
/**
	获取学生列表
*/
exports.find = function(callback){};

/**
	根据Id获取学生信息
*/
exports.findById = function(id,callback){};

/**
	添加保存学生信息
*/
exports.save = function(student,callback){};

/**
	更新学生
*/
exports.update = function(student,callback){};

/**
	删除学生
*/
exports.deleteById = function(id,callback){};

~~~

#### 8.7.5 index.html

~~~html

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/public/img/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/node_modules/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <script type="text/javascript" src="/node_modules/jquery/dist/jquery.js"></script>
  
    <link href="/public/css/dashboard.css" rel="stylesheet">
    <style type="text/css">
        
        .table th, .table td {
        text-align: center;
        vertical-align: middle!important;
        }
    </style>
  </head>

  <body>
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
      <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Company name</a>
      <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#">Sign out</a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link active" href="/students">
                  <span data-feather="home"></span>
                  学生展示 <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file"></span>
                  Orders
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="shopping-cart"></span>
                  Products
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="users"></span>
                  Customers
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="bar-chart-2"></span>
                  Reports
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="layers"></span>
                  Integrations
                </a>
              </li>
            </ul>

            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
              <span>Saved reports</span>
              <a class="d-flex align-items-center text-muted" href="#">
                <span data-feather="plus-circle"></span>
              </a>
            </h6>
            <ul class="nav flex-column mb-2">
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Current month
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Last quarter
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Social engagement
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Year-end sale
                </a>
              </li>
            </ul>
          </div>
        </nav>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
          <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h1 class="h2">杰出青年</h1>
            <div class="btn-toolbar mb-2 mb-md-0">
              <div class="btn-group mr-2">
                <button class="btn btn-sm btn-outline-secondary">Share</button>
                <button class="btn btn-sm btn-outline-secondary">Export</button>
              </div>
              <button class="btn btn-sm btn-outline-secondary dropdown-toggle">
                <span data-feather="calendar"></span>
                This week
              </button>
            </div>
          </div>
          
          <!-- <canvas class="my-4" id="myChart" width="900" height="380"></canvas> -->
          <div class="row placeholders">
            {{ each person }}
            <div class="col-xs-6 col-sm-3 placeholder">
             <!--  <div style="border: 1px solid red;width: 200px; height: 200px; border-radius: 100px;
              background-image:url({{$value.img}}); "> -->
                
               <img src= {{$value.img}} width="200" height="200" class="img-responsive" alt="Generic placeholder thumbnail"> 
              <!-- </div> -->
              
              <h4>{{ $value.name }}</h4>
              <span class="text-muted">{{$value.nickName}}</span>
            </div>
            {{ /each }}
          </div>
          <h2>学生列表</h2>
        

          <form class="form-inline" action="/student">
            <div class="form-group">
              <input type="text" style="margin:5px ;width: 400px"  class="form-control" name="id" 
              placeholder="ID">
            </div>

            <div class="form-inline">
             <button type="submit" class="btn btn-primary" style="width: 100px ;margin:5px">查询</button>
            </div>
          </form><br>
          <a href="/student/add"><button class="btn btn-success">添加学生</button></a>
          <br>
          <br>
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
                <tr>
                  <th>#</th>
                  <th>姓名</th>
                  <th>性别</th>
                  <th>年龄</th>
                  <th>爱好</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                {{if student != null}}
                  <tr>
                    <td>{{student.id}}</td>
                    <td>{{student.name}}</td>
                    {{if student.gender == 0 }}
                    <td>男</td>
                    {{else}}
                    <td>女</td>
                    {{/if}}
                    <td>{{student.age}}</td>
                    <td>{{student.hobbies}}</td>
                    <td>
                        <a href="/student/edit?id={{student.id}}"><button class="btn btn-info">编辑</button></a>
                        <button class="btn btn-danger" onclick="del({{student.id}})">删除</button>
                    </td>
                  </tr>
                {{/if}}

                {{if students != null}}

                {{each students}}
                <tr>
                  <td>{{$value.id}}</td>
                  <td>{{$value.name}}</td>
                  {{if $value.gender == 0 }}
                  <td>男</td>
                  {{else}}
                  <td>女</td>
                  {{/if}}
                  <td>{{$value.age}}</td>
                  <td>{{$value.hobbies}}</td>
                  <td>
                      <a href="/student/edit?id={{$value.id}}"><button class="btn btn-info">编辑</button></a>
                      <button class="btn btn-danger" onclick="del({{$value.id}})">删除</button>
                  </td>
                </tr>
                {{/each}}
                {{/if}}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>

    Icons
    <script src="/public/js/feather.min.js"></script>
    <script>
      feather.replace()
    </script>

    <script>
    // 使用ajax进行数据删除
      function del(id) {
        var res = confirm('确定删除吗？');
        if(!res){
          return;
        }
        $.ajax({
          url:'/student/del/'+id,
          type:'get',
          success:function(data){
            window.location.reload(); //刷新页面
          }
        });
       }

    </script>
  </body>
</html>
~~~

#### 8.7.6 add.html

~~~html

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/public/img/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/node_modules/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/public/css/dashboard.css" rel="stylesheet">
  </head>

  <body>
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
      <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Company name</a>
      <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#">Sign out</a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link active" href="/students">
                  <span data-feather="home"></span>
                  学生展示 <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file"></span>
                  Orders
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="shopping-cart"></span>
                  Products
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="users"></span>
                  Customers
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="bar-chart-2"></span>
                  Reports
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="layers"></span>
                  Integrations
                </a>
              </li>
            </ul>

            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
              <span>Saved reports</span>
              <a class="d-flex align-items-center text-muted" href="#">
                <span data-feather="plus-circle"></span>
              </a>
            </h6>
            <ul class="nav flex-column mb-2">
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Current month
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Last quarter
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Social engagement
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Year-end sale
                </a>
              </li>
            </ul>
          </div>
        </nav>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
        <h1 class="h2">学生信息录入</h1>
         <form class="form-horizontal" action="/student/add" method="post">
			  <div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">姓名</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="" name="name" placeholder="姓名">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="inputPassword3" class="col-sm-2 control-label">年龄</label>
			    <div class="col-sm-10">
			      <input type="number" class="form-control" id="" name="age" placeholder="年龄">
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <div class="checkbox">
			      <label>性别 &nbsp;&nbsp;&nbsp;&nbsp;
			           <label class="radio-inline">
						  <input type="radio" name="gender" id="" value="0"> 男&nbsp;&nbsp;
						</label>
						<label class="radio-inline">
						  <input type="radio" name="gender" id="" value="1" checked="checked"> 女
						</label>
			        </label>
			      </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="inputPassword3" class="col-sm-2 control-label">爱好</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="" name="hobbies" placeholder="爱好">
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" class="btn btn-success">保存人物信息</button>
			    </div>
			  </div>
			</form>
        </main>
      </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="../../../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="../../../../assets/js/vendor/popper.min.js"></script>
    <script src="../../../../dist/js/bootstrap.min.js"></script>

    <!-- Icons -->
    <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
    <script>
      feather.replace()
    </script>

    <!-- Graphs -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
    <script>
      var ctx = document.getElementById("myChart");
      var myChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
          datasets: [{
            data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#007bff',
            borderWidth: 4,
            pointBackgroundColor: '#007bff'
          }]
        },
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: false
              }
            }]
          },
          legend: {
            display: false,
          }
        }
      });
    </script>
  </body>
</html>

~~~

#### 8.7.7 edit.html

~~~html

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/public/img/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/node_modules/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/public/css/dashboard.css" rel="stylesheet">
  </head>

  <body>
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
      <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Company name</a>
      <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#">Sign out</a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link active" href="/students">
                  <span data-feather="home"></span>
                  学生展示 <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file"></span>
                  Orders
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="shopping-cart"></span>
                  Products
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="users"></span>
                  Customers
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="bar-chart-2"></span>
                  Reports
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="layers"></span>
                  Integrations
                </a>
              </li>
            </ul>

            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
              <span>Saved reports</span>
              <a class="d-flex align-items-center text-muted" href="#">
                <span data-feather="plus-circle"></span>
              </a>
            </h6>
            <ul class="nav flex-column mb-2">
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Current month
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Last quarter
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Social engagement
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <span data-feather="file-text"></span>
                  Year-end sale
                </a>
              </li>
            </ul>
          </div>
        </nav>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
        <h1 class="h2">学生信息修改</h1>
         <form class="form-horizontal" action="/student/edit" method="post">
          <input hidden="true" type="text" class="form-control" id="" name="id" value="{{student.id}}">
			  <div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">姓名</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="" name="name" value="{{student.name}}">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="inputPassword3" class="col-sm-2 control-label">年龄</label>
			    <div class="col-sm-10">
			      <input type="number" class="form-control" id="" name="age" value="{{student.age}}">
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <div class="checkbox">
            {{if student.gender == 0}}
			      <label>性别 &nbsp;&nbsp;&nbsp;&nbsp;
			           <label class="radio-inline">
						      <input type="radio" name="gender" id="" value="0" checked="checked"> 男&nbsp;&nbsp;
						     </label>
  						<label class="radio-inline">
  						  <input type="radio" name="gender" id="" value="1"> 女
  						</label>
			      </label>
            {{else}}
            <label>性别 &nbsp;&nbsp;&nbsp;&nbsp;
                 <label class="radio-inline">
                  <input type="radio" name="gender" id="" value="0"> 男&nbsp;&nbsp;
                 </label>
              <label class="radio-inline">
                <input type="radio" name="gender" id="" value="1" checked="checked"> 女
              </label>
            </label>
            {{/if}}
			      </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="inputPassword3" class="col-sm-2 control-label">爱好</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="" name="hobbies" value="{{student.hobbies}}">
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" class="btn btn-success">保存修改</button>
			    </div>
			  </div>
			</form>
        </main>
      </div>
    </div>


    <!-- Icons -->
    <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
    <script>
      feather.replace()
    </script>

    <!-- Graphs -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
    <script>
      var ctx = document.getElementById("myChart");
      var myChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
          datasets: [{
            data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#007bff',
            borderWidth: 4,
            pointBackgroundColor: '#007bff'
          }]
        },
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: false
              }
            }]
          },
          legend: {
            display: false,
          }
        }
      });
    </script>
  </body>
</html>

~~~

#### 8.7.8 app.js

~~~js
//导入核心模块
var fs = require('fs');

var express = require('express');
var bodyParesr = require('body-parser');

var router = require('./router.js');

var app = express();

//开发公共资源
app.use('/public/',express.static('./public/'));
app.use('/node_modules/',express.static('./node_modules/'));

//配置bodyParser中间件（需要在app.use(router)之前）
app.use(bodyParesr.urlencoded({extended:false}));
app.use(bodyParesr.json());

//配置art-template模板引擎
app.engine('html',require('express-art-template'));

app.listen(3333,function(){
	console.log("app is running at port 3333");
});

//将路由模块挂载到app上
app.use(router);
~~~

#### 8.8.9 router.js

~~~js
//导入第三方模块
var express = require('express');
//导入核心模块
var fs = require('fs');

var student = require('./student.js');

var router = express();

router.get('/students',function(req,res){
	student.find(function(error,data){
		if(error){
			console.log(error);
			res.status(500).send('server error');
		}else{
			res.render('index.html',{
				person:person,
				students:data,
			});
		}
	});
});


/*router.get('/students',function(req,res){
    var studentsDB;
	fs.readFile('./db.json', 'utf8', function(err,data){
		studentsData = JSON.parse(data);
		studentsDB = studentsData.students;
		if(err){

		}else{
			res.render('index.html',{
				person:person,
				students:studentsDB,
			});
		}
	});
});
*/

router.get('/student',function(req,res){
	var id = req.query.id;
	// 
	if(id == null || id == '' || id.trim() == '' ){
		student.find(function(error,data){
			if(error){
				console.log(error);
				res.status(500).send('server error');
			}else{
				res.render('index.html',{
					person:person,
					students:data
				});
			}
		});
	}else{
		student.findById(parseInt(id),function(error,data){
		if(error){
			console.log(error);
			res.status(500).send('server error');
		}else{
			res.render('index.html',{
				person:person,
				student:data,
			});
		}
	});
	}
	
});


router.get('/student/add',function(req,res){
	res.render('add.html');
});


router.post('/student/add',function(req,res){

	var stu = req.body;
	student.save(stu,function(error){
		if(error){
			console.log(error);
			res.status(500).send('server error');
		}else{
			res.redirect('/students');
		}
	});
	
	
});
/*router.post('/student/add',function(req,res){
	var studentsDB;
	fs.readFile('./db.json', 'utf8', function(err,data){
		studentsData = JSON.parse(data);
		studentsDB = studentsData.students;
		if(err){

		}else{
			var student = req.body;
			student.id = 10;
			studentsDB.unshift(student);
			var stu = {};
			stu.students = studentsDB;
			fs.writeFile('./db.json',JSON.stringify(stu),'utf8',function(err){
				if(err){

				}else{

				}
			});

			res.redirect('/students');
		}
	});
});*/


router.get('/student/del/:id',function(req,res){
	var id = req.params.id;
	student.deleteById(id,function(error){
		if(error){
			console.log(error);
			res.status(500).send('server error');
			return;
		}
		res.redirect('/students');
	});
});
/*router.get('/student/del/:id',function(req,res){
	//拿到传递过来的id
	var id = req.params.id;
	console.log(id);
	var studentsDB;
	//读取json文件
	fs.readFile('./db.json', 'utf8', function(err,data){
		studentsData = JSON.parse(data);
		studentsDB = studentsData.students;
		if(err){

		}else{
			// console.log(studentsDB);
			var studentsWrite = [];
			studentsDB.forEach(function(ele,index,arr){
				if(ele.id != id){
					console.log('id:'+ele.id);
					studentsWrite.push(ele);
					
				}
			});
			var stu = {};
			stu.students = studentsWrite;
			fs.writeFile('./db.json',JSON.stringify(stu),'utf8',function(error){
				if(error){
				}else{
					res.redirect('/students');
					console.log("hahahha");
				}
			});
		}
	});
});*/

router.get('/student/edit',function(req,res){
	console.log(req.query.id);
	var id = req.query.id;
	student.findById(id,function(error,data){
		if(error){
			console.log(error);
			res.status(500).send('server error');
		}else{
			res.render('edit.html',{
				student:data
			});
		}
	});

});


router.post('/student/edit',function(req,res){
	// console.log(req.body);

	var stu = req.body;
	student.update(stu,function(error){
		if(error){
			console.log(error);
			res.status(500).send('server error');
		}else{
			res.redirect('/students');
		}
	});


});




var person = [
	{
		name:'贾宝玉',
		img:'/public/img/贾宝玉.jpg',
		nickName:'JiaBaoYu'
	},{
		name:'林黛玉',
		img:'/public/img/Lindaiyu.jpg',
		nickName:'LinDaiYu'
	},{
		name:'薛宝钗',
		img:'/public/img/薛宝钗.jpg',
		nickName:'XueBaoChai'
	}
];

module.exports = router;
~~~

#### 8.8.10 student.js

~~~js
/*
	student.js
	数据操作的模块。
	职责：操作文件中的数据，只进行数据处理，不关心具体的业务。
*/

// --------------------- 
//   封装异步API.
// ---------------------


//导入文件处理的模块
var fs = require('fs');

//文件统一路径处理
var dbPath = './db.json';


/**
	获取学生列表
*/
exports.find = function(callback){
	fs.readFile(dbPath,function(error,data){
		if(error){
			return callback(error);
		}
		callback(null,JSON.parse(data).students);
	});
};



/**
	根据Id获取学生信息
*/
exports.findById = function(id,callback){
	var student = null;
	/*利用已经写好的find函数*/
	this.find(function(error,data){
		if(error){
			return callback(error);
		}
		data.forEach(function(ele,index,arr){
			if(parseInt(ele.id) === parseInt(id)){
				student = ele;
			}
		});
		callback(null,student);
	});
};


/**
	添加保存学生信息
*/

exports.save = function(student,callback){
	this.find(function(error,data){
		if(error){
			return callback(error);
		}
		student.id = parseInt(data[0].id) + 1;
		data.unshift(student);
		var stu = {};
		stu.students = data;
		fs.writeFile(dbPath,JSON.stringify(stu),'utf8',function(error){
			if(error){
				callback(error);
			}else{
				callback(null);
			}
		});
	});
};


/**
	更新学生
*/

exports.update = function(student,callback){

	this.find(function(error,data){
		if(error){
			return callback(error);
		}

		student.id = parseInt(student.id);

		//通过es6中的find函数查找
		// https://www.cnblogs.com/kongxianghai/p/7527526.html
		var res = data.find(function(item){
			return item.id === student.id;
		});
		//遍历传递进来的对象，更新属性
		
		for(var key in student){
			res[key] = student[key];
		}

		/*var index = data.findIndex(function(item){
			return parseInt(item.id) === parseInt(student.id);
		});
		data[index] = student;*/
		// console.log(index);
		var stu = {};
		stu.students = data;
		fs.writeFile(dbPath,JSON.stringify(stu),'utf8',function(error){
			if(error){
				return callback(error);
			}
			callback(null);
		});
	});

};



/**
	删除学生
*/
exports.deleteById = function(id,callback){
	//利用find查询文件中的数据
	this.find(function(error,data){
		if(error){
			return callback(error);
		}

		//方法一
		/*var studentsDB = [];
		var stu = {};
		data.forEach(function(ele,index,arr){
			if(parseInt(ele.id) !== parseInt(id)){
				studentsDB.push(ele);
			}
		});
		stu.students = studentsDB;*/
		//方法二
		var index = data.findIndex(function(item){
			return item.id === parseInt(id);
		});
		// 从指定下标开始，删除n个。
		data.splice(index,1);//从index开始，删除1个元素
		var fileData = JSON.stringify({
			students:data
		});
		fs.writeFile(dbPath,fileData,'utf8', function(error){
			if(error){
				return callback(error);
			}
			return callback(null);
		});		
	});
};
~~~

#### 8.8.11 db.json

~~~json
{
	"students": [{
			"name": "香菱",
			"age": "18",
			"gender": "1",
			"hobbies": "吟诗赏月",
			"id": 12
		},
		{
			"name": "贾迎春",
			"age": "20",
			"gender": "1",
			"hobbies": "吟诗作画",
			"id": 11
		},
		{
			"name": "王熙凤",
			"age": "30",
			"gender": "1",
			"hobbies": "听戏",
			"id": 10
		},
		{
			"id": 1,
			"name": "贾宝玉",
			"gender": 0,
			"age": 18,
			"hobbies": "写诗"
		},
		{
			"id": 2,
			"name": "林黛玉",
			"gender": 1,
			"age": 18,
			"hobbies": "写诗"
		},
		{
			"id": 3,
			"name": "薛宝钗",
			"gender": 1,
			"age": 19,
			"hobbies": "写诗"
		},
		{
			"id": 4,
			"name": "袭人",
			"gender": 1,
			"age": 17,
			"hobbies": "写诗"
		},
		{
			"id": 5,
			"name": "晴雯",
			"gender": 1,
			"age": 17,
			"hobbies": "写诗"
		}
	]
}
~~~

#### 8.8.12 项目结构图

![](image/custruct.png)

#### 8.8.13 项目效果图

![](image/list.png)

![](image/add.png)

![](image/update.png)





 



 

## 9 修改代码自动重启

我们这里可以使用一个第三方命令行工具，`nodemon`来帮我们解决频繁修改代码重启服务器问题。

`nodemon`是一个基于nodejs开发，我们需要独立安装。

~~~shell
npm install --global nodemon
~~~

安装完之后，使用nodemon就可以：

~~~shell
node app.js
V
nodemon app.js
~~~

只要是通过`nodemon app.js`启动服务，则它会监视我们的文件的变化，当文件发生变化的时候，会自动帮我们重启服务器。

![](image/auto_restart.png)









​             








