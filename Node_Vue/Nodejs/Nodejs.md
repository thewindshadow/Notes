# Nodejs

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
      package.json 必须在包的顶层目录下；  
     二进制文件应该在 bin 目录下；  
      JavaScript 代码应该在 lib 目录下；  
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













































