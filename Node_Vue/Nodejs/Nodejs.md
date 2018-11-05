# Nodejs

## 说在前面的：

### 简介

- Node.js® is a JavaScript runtime built on [Chrome's V8 JavaScript engine](https://developers.google.com/v8/).
  - Node.js不是一门语言
  - Node.js不是库，也不是框架
  - Node.js是一个JavaScript运行环境
  - 简单点来说就是node.js可以解析和执行JavaScript代码。
  - 以前只有浏览器可以解析和执行JavaScript代码
  - 也就是说现在的JavaScript可以完全脱离浏览器运行，一切都归功于node.js
  - Event-driven 事件驱动，
  - non-blocking i/o模型 非阻塞的io模型
  - npm是世界上最大的开源库生态系统
  - 绝大多数JavaScript相关的包都存放在了npm上
  - 构建于Chrome的v8引擎之上
    - 代码只是具有特定格式的字符串而已
    - 引擎可以认识它，引擎可以帮你解析和执行
    - google chrome的v8引擎是目前公认的解析和执行JavaScript代码最快的
    - Node.js的作者把google Chrome的v8引擎移植出来，开发一个独立的JavaScript运行环境。


- 浏览器中的JavaScript
  - ecmascript
    - 基本语法
  - BOM
  - DOM
- Node.js中的JavaScript
  - 没有BOM，DOM
  - EcmaCript
  - 在node这个js执行环境中为JavaScript提供来一些服务器级别的操作API
    - 文件读写
    - 网络服务的构建
    - 网络通信
    - http服务器 

### Node.js能做什么

- web 服务端后台
- 命令行工具
  - npm（node）
  - git(c 语言)
  - hexo（node）
- 对于前段工程师来讲，接触node最多的是它的命令行工具
  - 自己写的少，主要是用第三方
  - webpack
  - gulp

### 一些资源

- 《深入浅出Node.js》 朴灵
  - 偏理论
- 《Node.js权威指南》
  - API讲解
  - 没有实战
- Node入门：[www.nodebeginner.org/index-zh-cn.html](www.nodebeginner.org/index-zh-cn.html)

## 起步

### 安装Node

- 官网`nodejs.org`安装。一路`next`
- 使用`node --version`或`node -v`看版本，
- 如果有版本号，则表示安装成功

### HelloWorld

在目录写一段示例代码：

```
var foo = 'bar'
console.log(foo);
```

进入当前目录下，使用 `node +文件名.js`运行





## 起步案例

### 读文件

~~~js
var fs = require("fs");

//异步回调方式读取文件
fs.readFile("./data/read.txt",function (error,data) {
    if(error){
        console.log(error);
    }else{
        console.log(data.toString());
    }
});
console.log("end");

        /*
            end
            你好，nodejs
            测试nodejs读取文件
        */

//使用同步方式读取文件
var fs = require("fs");
var data = fs.readFileSync("./data/read.txt","utf-8");
console.log(data);
console.log("end");
        /*
            你好，nodejs
            测试nodejs读取文件
            end
        */
~~~

### 写文件

~~~js
var fs = require("fs");
fs.writeFile("./data/write.txt","你好，我是nodejs，这是测试nodejs写文件",function (error) {
    if(error){
        console.log("写文件失败！"+error);
    }else{
        console.log("写文件成功！");
    }
});
~~~

### http请求

~~~js

var http = require("http");

var server = http.createServer();

server.listen("3333",function () {
    console.log("http服务器创建成功，正在监听3333端口");
});

server.on("request",function (req,res) {

    var products =[{
        name:"xiaomi2",
        price:"1999"
    },{
        name:"xiaomi5",
        price:"2999"
    },{
        name:"xiaomi8",
        price:"3999"
    }
    ];

    if(req.url === "/products"){
        res.end(JSON.stringify(products));
    }else{
        res.end("404 File Not Found!");
    }

});
~~~



## 事件

~~~js
Node.js 所有的异步 I/O 操作在完成时都会发送一个事件到事件队列。在开发者看来，事件由 EventEmitter 对象提供。前面提到的 fs.readFile 和 http.createServer 的回调函数都是通过 EventEmitter  来实现的。下面我们一个简单的例子说明 EventEmitter 的用法： 

//导入events模块
var events = require("events");

//使用events模块对象new出一个EventEmitter对象
var eventEmitter = new events.EventEmitter();

//使用eventEmitter绑定一个事件（监听一个事件）
eventEmitter.on("some_events",function () {
    console.log("事件执行了");
});

console.log(new Date());
//触发事件
setTimeout(function () {
    eventEmitter.emit("some_events");
    console.log(new Date());
},2000);

~~~

**可以看出**：

- 需要使用`require`将`events`核心模块引入

- 使用`eventEmitter.on();`进行事件绑定
- 使用`eventEmitter.emit();`进行事件触发



### 事件方法

~~~js
var events = require("events");

var eventEmitter = new events.EventEmitter();

//定义两个用于回调的函数
var listener1 = function(){
    console.log("监听器listener1执行");
};

var listener2 = function(){
    console.log("监听器listener2执行");
};


//绑定connection事件，处理函数listener1
eventEmitter.addListener("connection",listener1);
//绑定connection事件，处理函数listener2
eventEmitter.on("connection",listener2);

var eventListeners = eventEmitter.listenerCount("connection");
console.log(eventListeners+"个监听器监听事件");

//处理connection事件

eventEmitter.emit("connection");

//移除绑定事件listener1函数
eventEmitter.removeListener("connection",listener1);
console.log("listener1不再监听");

eventEmitter.emit("connection");
eventListeners = eventEmitter.listenerCount("connection");
console.log(eventListeners+"个监听器监听事件");

console.log("程序执行完毕！");
~~~



### 方法

| 序号   | 方法 & 描述                                  |
| ---- | ---------------------------------------- |
| 1    | **addListener(event, listener)**  为指定事件添加一个监听器到监听器数组的尾部。 |
| 2    | **on(event, listener)** 为指定事件注册一个监听器，接受一个字符串 event 和一个回调函数。 `server.on('connection', function (stream) {   console.log('someone connected!'); });` |
| 3    | **once(event, listener)** 为指定事件注册一个单次监听器，即 监听器最多只会触发一次，触发后立刻解除该监听器。 `server.once('connection', function (stream) {   console.log('Ah, we have our first user!'); });` |
| 4    | **removeListener(event, listener)** 移除指定事件的某个监听器，监听器必须是该事件已经注册过的监听器。它接受两个参数，第一个是事件名称，第二个是回调函数名称。 `var callback = function(stream) {   console.log('someone connected!'); }; server.on('connection', callback); // ... server.removeListener('connection', callback);` |
| 5    | **removeAllListeners([event])** 移除所有事件的所有监听器， 如果指定事件，则移除指定事件的所有监听器。 |
| 6    | **setMaxListeners(n)** 默认情况下， EventEmitters 如果你添加的监听器超过 10 个就会输出警告信息。 setMaxListeners 函数用于提高监听器的默认限制的数量。 |
| 7    | **listeners(event)** 返回指定事件的监听器数组。       |
| 8    | **emit(event, [arg1], [arg2], [...])** 按参数的顺序执行每个监听器，如果事件有注册监听返回 true，否则返回 false。 |

### 类方法

| 序号   | 方法 & 描述                                  |
| ---- | ---------------------------------------- |
| 1    | **listenerCount(emitter, event)** 返回指定事件的监听器数量。 |

```
events.EventEmitter.listenerCount(emitter, eventName) //已废弃，不推荐
events.emitter.listenerCount(eventName) //推荐
```

### 事件

| 序号   | 事件 & 描述                                  |
| ---- | ---------------------------------------- |
| 1    | **newListener**   **event** - 字符串，事件名称 **listener** - 处理事件函数  该事件在添加新监听器时被触发。 |
| 2    | **removeListener**   **event** - 字符串，事件名称 **listener** - 处理事件函数  从指定监听器数组中删除一个监听器。需要注意的是，此操作将会改变处于被删监听器之后的那些监听器的索引 |

### error事件

~~~xml
error 事件

EventEmitter 定义了一个特殊的事件 error，它包含了错误的语义，我们在遇到 异常的时候通常会触发 error 
事件。
当 error 被触发时，EventEmitter 规定如果没有响 应的监听器，Node.js 会把它当作异常，退出程序并输出错误信息。
我们一般要为会触发 error 事件的对象设置监听器，避免遇到错误后整个程序崩溃。例如：
~~~

```js
var events = require("events");
var eventEmitter = new events.EventEmitter();
eventEmitter.emit("error");
```

运行时会显示以下错误： 

~~~js
events.js:173
    throw err; // Unhandled 'error' event
    ^

Error [ERR_UNHANDLED_ERROR]: Unhandled error.
    at EventEmitter.emit (events.js:171:17)
    at Object.<anonymous> (D:\Node_Vue\Node\day01\events\04-error.js:4:14)
    at Module._compile (internal/modules/cjs/loader.js:707:30)
    at Object.Module._extensions..js (internal/modules/cjs/loader.js:718:10)
    at Module.load (internal/modules/cjs/loader.js:605:32)
    at tryModuleLoad (internal/modules/cjs/loader.js:544:12)
    at Function.Module._load (internal/modules/cjs/loader.js:536:3)
    at Function.Module.runMain (internal/modules/cjs/loader.js:760:12)
    at startup (internal/bootstrap/node.js:308:19)
    at bootstrapNodeJSCore (internal/bootstrap/node.js:878:3)
~~~





## 模块和包

###  什么是模块

~~~xml
 在 Node.js 中，创建一个模块非常简单，因为一个文件就是一个模块，我们要关注的问题仅仅在于如何在其他文件中获取这个模块。Node.js 提供了 exports 和 require 两个对象，其中 exports 是模块公开的接口，require 用于从外部获取一个模块的接口，即所获取模块的 exports 对象。 
~~~

module.js

~~~js
var name;

exports.setName = function (thyName) {
    name = thyName;
};

exports.sayHello = function(){
    console.log("Hello "+name);
};
~~~

getmodule.js

~~~js
var myModule = require("./module");
myModule.setName("BYVoids");
myModule.sayHello();
~~~

注意：

~~~xml
在以上示例中，module.js 通过 exports 对象把 setName 和 sayHello 作为模块的访问接口，在 getmodule.js 中通过 require('./module') 加载这个模块，然后就可以直接访问 module.js 中 exports 对象的成员函数了。
~~~

- 1.使用**exports**对象定义接口
- 2.通过**require**来加载模块

~~~xml

 注意：
	模块接口的唯一变化是使用 module.exports = Hello 代替了 exports.Hello=Hello。在外部引用该模块时，其接口对象就是要输出的 Hello 对象本身，而不是原先的exports。
    事实上，exports 本身仅仅是一个普通的空对象，即 {}，它专门用来声明接口，本质上是通过它为模块闭包的内部建立了一个有限的访问接口。因为它没有任何特殊的地方，所以可以用其他东西来代替，譬如我们上面例子中的 Hello 对象。

   不可以通过对  exports 直接赋值代替对  module.exports 赋值。exports 实际上只是一个和 module.exports 指向同一个对象的变量，它本身会在模块执行结束后释放，但  module 不会，因此只能通过指定
module.exports 来改变访问接口。

~~~

### 创建包 

~~~xml
	包是在模块基础上更深一步的抽象，Node.js 的包类似于 C/C++ 的函数库或者 Java/.Net 的类库。它将某个独立的功能封装起来，用于发布、更新、依赖管理和版本控制。Node.js 根据 CommonJS 规范实现了包机制，开发了 npm来解决包的发布和获取需求。 Node.js 的包是一个目录，其中包含一个 JSON 格式的包说明文件 package.json。严格符合 CommonJS 规范的包应该具备以下特征： 
     package.json 必须在包的顶层目录下；  
     二进制文件应该在 bin 目录下；  
     JavaScript 代码应该在 lib 目录下；  
     文档应该在 doc 目录下；  
     单元测试应该在 test 目录下。 
Node.js 对包的要求并没有这么严格，只要顶层目录下有 package.json，并符合一些规范即可。当然为了提高兼容性，我们还是建议你在制作包的时候，严格遵守 CommonJS 规范。  
1. 作为文件夹的模块 
模块与文件是一一对应的。文件不仅可以是 JavaScript 代码或二进制代码，还可以是一个文件夹。最简单的包，就是一个作为文件夹的模块。下面我们来看一个例子，建立一个叫做 somepackage 的文件夹，在其中创建 index.js，内容如下： 
//somepackage/index.js 
 
exports.hello = function() { 
  console.log('Hello.'); 
}; 
然后在 somepackage 之外建立 getpackage.js，内容如下： 
//getpackage.js 
 
var somePackage = require('./somepackage'); 
 
somePackage.hello(); 
运行 node getpackage.js，控制台将输出结果 Hello.。 
我们使用这种方法可以把文件夹封装为一个模块，即所谓的包。包通常是一些模块的集
合，在模块的基础上提供了更高层的抽象，相当于提供了一些固定接口的函数库。通过定制 
package.json，我们可以创建更复杂、更完善、更符合规范的包用于发布。 

2. package.json 
在前面例子中的 somepackage 文件夹下，我们创建一个叫做 package.json 的文件，内容如下所示： 
{ 
  "main" : "./lib/interface.js" 
} 
然后将 index.js 重命名为 interface.js 并放入 lib 子文件夹下。以同样的方式再次调用这个包，依然可以正常使用。 
Node.js 在调用某个包时，会首先检查包中 package.json 文件的 main 字段，将其作为
包的接口模块，如果 package.json 或  main 字段不存在，会尝试寻找 index.js 或 index.node 作为包的接口。 
package.json 是 CommonJS 规定的用来描述包的文件，完全符合规范的 package.json 文件应该含有以下字段。 
	  name：包的名称，必须是唯一的，由小写英文字母、数字和下划线组成，不能包含空格。 
      description：包的简要说明。 
      version：符合语义化版本识别①规范的版本字符串。 
      keywords：关键字数组，通常用于搜索。 
      maintainers：维护者数组，每个元素要包含  name、email （可选）、web （可选）字段。 
      contributors：贡献者数组，格式与maintainers相同。包的作者应该是贡献者数组的第一个元素。 
      bugs：提交bug的地址，可以是网址或者电子邮件地址。 
      licenses：许可证数组，每个元素要包含  type （许可证的名称）和 url （链接到许可证文本的地址）字段。 
      repositories：仓库托管地址数组，每个元素要包含  type  （仓库的类型，如 git ）、url （仓库的地址）和 path （相对于仓库的路径，可选）字段。 
	  dependencies：包的依赖，一个关联数组，由包名称和版本号组成。	
~~~

## 解决编码问题

~~~js
var http = require("http");
var server = http.createServer();
server.listen("3333",function () {
    console.log("http服务器创建成功，正在监听3333端口号：");
});
server.on("request",function (req,res) {
    //在服务器默认发送的数据是utf-8，但是浏览器不知道是utf-8。
    //浏览器在不知道服务器响应内容的编码情况下，会按照当前系统的默认编码去解析，
	//中文操作系统，默认是gbk
	//解决方法：正确的告诉浏览器我给你发送的内容是什么编码。
    res.setHeader("Content-Type","text/plain;charset=utf-8");
	res.end("Hello 世界");
});
~~~

结果图：

![](image/char.png)









































