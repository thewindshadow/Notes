
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





## 



















### 8.2 使用Express+art-template+Bootstrap 搭建一个简易留言板

 **[官方API文档](http://expressjs.com/en/4x/api.html#req)**

- 1.创建message_board 文件夹，执行`npm init -y`或`npm init `跳过向导/不跳过创建package.json包描述文件。

- 2.通过`npm install --save express`安装 express

- 3.通过`npm install --save art-template`安装art-template

- 4.通过`npm install --save express-art-template`安装express与art-template关联包

- 5.通过`npm install --save body-parser`安装post请求解析工具

- 6.在项目根本下，public/lib/放bootstrap

- 7.编写app.js,放在项目根本下

  ~~~js
  
  // 引入express框架
  var express = require('express');
  // 引入post参数解析
  var bodyParser = require('body-parser');
  // 创建应用服务器
  var app = express();
  
  // 监听端口号
  app.listen(3333,function(){
    console.log('App is running at port 3333');
  });
  
  //开放公共资源
  app.use('/public/',express.static('./public/'));
  
  
  // -----------------------------------------
  // Express框架，配置使用art-template模板引擎
  // 第一个参数，表示当渲染以.html文件结尾的时候，使用art-template模板引擎。
  // express-art-template 是专门用来在Express中把art-template模板引擎整合到express中。
  // 虽然外面不需要引用 art-template,但是也必须安装 。
  // 原因在于：express-art-template 依赖了 art-template;
  // ----------------------------------------
  app.engine('html',require('express-art-template'));
  
  
  // -----------------------------------------
  // 配置body-parser 中间件（插件，专门用于解析post请求体）
  // -----------------------------------------
  app.use(bodyParser.urlencoded({extends:false}));
  app.use(bodyParser.json());
  
  
  // -----------------------------------------
  // 处理请求
  // res.redirect([status,] path) 用于重定向
  // res.render(view [, locals] [, callback])  返回视图
  // res.send([body])   返回http响应
  // -----------------------------------------
  app.get('/',function(req,res){
    res.render('index.html',{
      comments:comments
    });
  });
  
  app.get('/post',function(req,res){
    res.render('post.html');
  });
  
  app.post('/post',function(req,res){
    var comment = req.body;
    comment.dateTime = getNow();
    comments.unshift(comment);
    res.redirect('/');
  });
  
  
  function getNow(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    var time = 
      (year)+'-'
      +((month>9)?month:('0'+month))+'-'
      +((day>9)?day:('0'+day))+' '
      +((hour>9)?hour:('0'+hour))+':'
      +((minute>9)?minute:('0'+minute))+':'
      +((second>9)?second:('0'+second));
        return time;
  }
  
  
  
  // ----------------------------------------
  // 模拟数据
  // ----------------------------------------
  
  var comments = [
    {
      name:'张三1',
      message:'今天天气真好!',
      dateTime:'2018-11-08 11:11:11'
    },{
      name:'张三2',
      message:'今天天气真好!',
      dateTime:'2018-11-08 11:11:11'
    },{
      name:'张三3',
      message:'今天天气真好!',
      dateTime:'2018-11-08 11:11:11'
    },{
      name:'张三4',
      message:'今天天气真好!',
      dateTime:'2018-11-08 11:11:11'
    },{
      name:'张三5',
      message:'今天天气真好!',
      dateTime:'2018-11-08 11:11:11'
    },
  ];
  
  
  ~~~

- 8.编写index.html放在项目根目录下的views文件夹下

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

- 9.编写post.html文件，放在项目根路径下的views文件夹下

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
  </body>
  
  </html>
  ~~~

- 10.总结

  ~~~js
  // 处理请求
  res.redirect([status,] path) 用于重定向
  res.render(view [, locals] [, callback])  返回视图
  res.send([body])   返回http响应
  
  
  
  ~~~

  





 



 

## 9.1修改代码自动重启

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








