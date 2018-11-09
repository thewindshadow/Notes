
## 8.Express

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

​	







 



 

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








