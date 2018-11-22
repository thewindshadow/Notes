---
title: Vue
date: 2018-11-17 12:16:18
tags: vue
categories: vue
---

学习vue的笔记，用于复习和使用时的参考。https://cn.vuejs.org
<!--more-->


## Vue

### Vue起步

#### 安装

~~~shell
npm install --save vue

D:\Node_Vue\Vuejs\day02>npm install -save vue
npm WARN saveError ENOENT: no such file or directory, open 'D:\Node_Vue\Vuejs\da
y02\package.json'
npm notice created a lockfile as package-lock.json. You should commit this file.

npm WARN enoent ENOENT: no such file or directory, open 'D:\Node_Vue\Vuejs\day02
\package.json'
npm WARN day02 No description
npm WARN day02 No repository field.
npm WARN day02 No README data
npm WARN day02 No license field.

+ vue@2.5.17
added 1 package from 1 contributor in 6.572s
~~~

#### Demo

~~~html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <!--
        0.npm install vue
        1.引包
        2.new Vue 得到 Vue实例
            Vue 暂时可以理解成一个模板引擎
    -->
    <div id="app">
        <h1>{{ 1 + 1 }}</h1>
        <h1>{{ 'hello' + 'world'}}</h1>
        <h1>{{message}}</h1>
        <h1>details:{{details()}}</h1>
    </div>

    <script src="node_modules/vue/dist/vue.min.js"></script>
    <script>
        const app = new Vue({
            el:'#app',
            data:{
                message:'Hello Vue'
            },
            methods:{
                details:function(){
                    return '一个非常神奇的框架';
                }
            }
        });
    </script>
</body>
</html>
~~~

![](vue/helloworld.png)



### 数据绑定

#### 文本绑定

~~~html
<div id="app">
    <h1>{{foo}}</h1>
    <p>{{foo}}</p>
</div>

<script>
    var app = new Vue({
        el:'#app',
        data:{
            foo:'Hello'
        },
        methods:{

        }
    });
</script>
~~~

#### 一次性绑定

~~~html
<div id="app">
    <!--初始化之后就不可以修改了-->
    <h1 v-once>{{foo}}</h1>
    <p>{{foo}}</p>
</div>
~~~

#### 输出HTML

~~~html
<div id="app">
    <div>
        {{rowHtml}}
    </div>
    <div v-html="rowHtml"></div>
</div>

<script>
    var app = new Vue({
        el:'#app',
        data:{
            foo:'Hello',
            /*在html字符串中不能使用vue的语法特性，{{foo}}会被处理为字符串，原样输出*/
            rowHtml:'<h1>Hello {{foo}} Vue</h1>'
        },
        methods:{

        }
    });
</script>
~~~

![](vue/seeHtml.png)

####  普通文本绑定与属性绑定

~~~html
<ul>
    <li v-for="item in todos">
        <!-- v-bind只能用于属性
             它的值是一个JavaScript表达式，和{{}}里面的语法一致、
             唯一的区别就是：{{}}用于标签文本绑定，v-bind用于属性文本绑定。
        -->
        <a v-bind:href="'/todos?id='+item.id">{{item.title +  'Vue'}}</a>
        <!--<a v-bind:href="'/todos?id='+item.id">{{item.title}} Vue</a>-->
    </li>
</ul>
<script>
    var app = new Vue({
        el:'#app',
        data:{
            todos:[
                {
                    id:1,
                    title:'aaa'
                },{
                    id:2,
                    title:'bbb'
                },{
                    id:3,
                    title:'ccc'
                }
            ]
        },
        methods:{
        }
    });
</script>
~~~

![](vue/普通文本绑定与属性绑定.png)



#### 使用JavaScript表达式

~~~html
<ul>
    <li v-for="item in todos">
        <a v-bind:href="true?123:345">{{true?123:345}}</a>
    </li>
</ul>

<！--官方示例-->
{{ number + 1 }}

{{ ok ? 'YES' : 'NO' }}

{{ message.split('').reverse().join('') }}

<div v-bind:id="'list-' + id"></div>
~~~

![](vue/javascript表达式.png)

### 指令

#### 简介

~~~html
指令 (Directives) 是带有 v- 前缀的特殊特性。指令特性的值预期是单个 JavaScript 表达式 (v-for 是例外情况，稍后我们再讨论)。指令的职责是，当表达式的值改变时，将其产生的连带影响，响应式地作用于 DOM。回顾我们在介绍中看到的例子：

<p v-if="seen">现在你看到我了</p>
这里，v-if 指令将根据表达式 seen 的值的真假来插入/移除 <p> 元素。
  
v-i 条件渲染
v-for 列表渲染
v-on  注册事件
v-bind 属性绑定
v-once 只绑定一次
v-html 绑定输出html
~~~

#### 参数

~~~html
一些指令能够接收一个“参数”，在指令名称之后以冒号表示。例如，v-bind 指令可以用于响应式地更新 HTML 特性：

<a v-bind:href="url">...</a>
在这里 href 是参数，告知 v-bind 指令将该元素的 href 特性与表达式 url 的值绑定。

另一个例子是 v-on 指令，它用于监听 DOM 事件：

<a v-on:click="doSomething">...</a>
在这里参数是监听的事件名。我们也会更详细地讨论事件处理。
~~~

#### 修饰符

~~~html
修饰符 (Modifiers) 是以半角句号 . 指明的特殊后缀，用于指出一个指令应该以特殊方式绑定。例如，.prevent 修饰符告诉 v-on 指令对于触发的事件调用 event.preventDefault()：

<form v-on:submit.prevent="onSubmit">...</form>
在接下来对 v-on 和 v-for 等功能的探索中，你会看到修饰符的其它例子。
~~~

~~~html
<!DOCTYPE html>
<html xmlns:v-on="http://www.w3.org/1999/xhtml">
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <script src="node_modules/vue/dist/vue.min.js"></script>

</head>
<body>
    <div id="app">
        <!--
            v-i 条件渲染
            v-for 列表渲染
            v-on  注册事件
            v-bind 属性绑定
            v-once 只绑定一次
            v-html 绑定输出html
        -->
        <p v-if="seen">你看不见我</p>
        <!--<a v-on:click="handleGoBaidu" href="http://www.baidu.com">去百度</a>-->
        <a v-on:click.prevent="handleGoBaidu" href="http://www.baidu.com">去百度</a>
    </div>
    <script>

        var app = new Vue({
            el:'#app',//css选择器
            data:{
                seen:true
            },
            methods:{
                handleGoBaidu:function(e){
                    /*
                    * 阻止默认的a标签的跳转事件。（阻止默认事件行为）
                    * 即：如果没有e.preventDefault();弹框之后，会跳转到百度。
                    * 但是使用e.preventDefault();可以阻止默认事件，即只会弹框不会进行链接跳转。
                    * 也可以使用v-on:click.prevent=""
                    * */
                    e.preventDefault();
                    window.alert('去百度');
                }
            }
        });
    </script>
</body>
</html>
~~~

#### 缩写

~~~html
v- 前缀作为一种视觉提示，用来识别模板中 Vue 特定的特性。当你在使用 Vue.js 为现有标签添加动态行为 (dynamic behavior) 时，v- 前缀很有帮助，然而，对于一些频繁用到的指令来说，就会感到使用繁琐。同时，在构建由 Vue.js 管理所有模板的单页面应用程序 (SPA - single page application) 时，v- 前缀也变得没那么重要了。因此，Vue.js 为 v-bind 和 v-on 这两个最常用的指令，提供了特定简写：

v-bind 缩写

    <!-- 完整语法 -->
    <a v-bind:href="url">...</a>
    
    <!-- 缩写 -->
    <a :href="url">...</a>

v-on 缩写

    <!-- 完整语法 -->
    <a v-on:click="doSomething">...</a>
    
    <!-- 缩写 -->
    <a @click="doSomething">...</a>

它们看起来可能与普通的 HTML 略有不同，但 : 与 @ 对于特性名来说都是合法字符，在所有支持 Vue.js 的浏览器都能被正确地解析。而且，它们不会出现在最终渲染的标记中。缩写语法是完全可选的，但随着你更深入地了解它们的作用，你会庆幸拥有它们。
~~~

### Class 与 Style 绑定

#### 绑定HTML Class

~~~html
<!DOCTYPE html>
<html xmlns:v-bind="http://www.w3.org/1999/xhtml">
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <script src="node_modules/vue/dist/vue.min.js"></script>
    <style>
        .active {
            color: red;
        }
    </style>
</head>
<body>
    <div id="app">
        <h1 v-bind:class="{active:isActive}" v-bind:style="{backgroundColor:'yellow'}" >Hello Vue</h1>
    </div>
    <script>
        var app = new Vue({
            el:'#app',
            data:{
                isActive:true,
                bgc:'gold'
            },
            methods:{
            }
        });
    </script>
</body>
</html>
~~~

#### 绑定内联样式

~~~html
<div id="app">
	<h1 :class="{active:isActive}" :style="{backgroundColor:bgc}" >Hello Vue</h1>
</div>
<script>
    var app = new Vue({
        el:'#app',
        data:{
            isActive:true,
            bgc:'gold'
        },
        methods:{

        }
    });
</script>
~~~

注意：v-bind:class == :class

#### 数组语法

~~~html
v-bind:style 的数组语法可以将多个样式对象应用到同一个元素上：
<div v-bind:style="[baseStyles, overridingStyles]"></div>
~~~

### http-server使用教程 hs -o

~~~html
01.npm install http-server -g全局安装
02.在要打开的项目文件夹处打开命令窗口，输入 hs -o回车
03.将对应的网址复制到浏览器打开
	注意点：一次只能运行一个项目，当要运行另外一个项目时要先结束前面项目的服务在命令窗口按住ctrl+c即可，再到另外一			个项目文件运行服务
04.遇到端口被占用的情况，可以用指令：hs -o -p 新端口号，来修改端口。
~~~

### 表单输入绑定

#### 基本语法

~~~html
你可以用 v-model 指令在表单 <input>、<textarea> 及 <select> 元素上创建双向数据绑定。它会根据控件类型自动选取正确的方法来更新元素。尽管有些神奇，但 v-model 本质上不过是语法糖。它负责监听用户的输入事件以更新数据，并对一些极端场景进行一些特殊处理。
  
(v-model用于表单控件)
<!--不能这样使用-->
<h1 v-model="message"></h1>
~~~

#### 文本

```html
<div id="app">
    <!--<input type="text" v-bind:value="message"/>-->
    <input type="text" v-model="message" />{{message}}
</div>
```

#### 多行文本

~~~html
<span>Multiline message is:</span>
<p style="white-space: pre-line;">{{ message }}</p>
<br>
<textarea v-model="message" placeholder="add multiple lines"></textarea>
~~~

#### 复选框

~~~html
<!DOCTYPE html>
<html xmlns:v-bind="http://www.w3.org/1999/xhtml">
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <script src="node_modules/vue/dist/vue.min.js"></script>
    <style>
        .box{
            width: 200px;
            height: 200px;
            background-color: yellow;
        }
    </style>
</head>
<body>
<div id="app">
    <!--复选框-->
    <input type="checkbox" v-bind:checked="checked"/><!--受数据，但不影响数据-->
    <input type="checkbox" v-model="checked"/>{{checked}}<!--双向绑定-->
  	<!--两种方式-->
    <!--<div v-bind:class="{box:checked}"></div>-->
    <div v-if="checked" class="box"></div>
</div>
<script>
    var app = new Vue({
        el:'#app',
        data:{
            checked:true
        }
    });
</script>
</body>
</html>
~~~

![](vue/checkbox.png)

#### 单选按钮

~~~html
<div id="app">
    <input type="radio" id="one"  v-model="picked" value="One"/>One
    <!--<label for="one">One</label>-->
    <br/>
    <input type="radio" id="two" v-model="picked" value="Two" />Two
    <!--<label for="two">Two</label>-->
    <br/>
    <span>Picked:{{picked}}</span>
</div>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            picked:''
        },
        methods: {}
    });
</script>
~~~

![](vue/radio.png)

#### 单选下拉框

~~~html
<div id="app">
    <select v-model="selected">
        <!--disable 设置为不可选-->
        <option disabled value="">==请选择==</option>
        <option value="1">A</option>
        <option value="2">B</option>
        <option value="3">C</option>
    </select>
    <span>Selected:{{selected}}</span>
</div>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            selected:''
        },
        methods: {}
    });
</script>
~~~

![](vue/select.png)

#### 复选下拉框

~~~html
<!--多选框-->
<select v-model="selected" multiple >
    <!--disable 设置为不可选-->
    <option disabled value="">==请选择==</option>
    <option value="1">A</option>
    <option value="2">B</option>
    <option value="3">C</option>
</select>
<span>Selected:{{selected}}</span>
~~~

![](vue/multipleSelected.png)

### 值绑定

对于单选按钮，复选按钮及选择框的选项，`v-model`绑定的值通常是静态字符串（对于复选框也可以是布尔值）：

~~~html
<div id="app">
    <input type="radio" v-model="picked" value="a" />
    <input type="checkbox" v-model="toggle" />

    <select v-model="selected">
        <option value="abc">ABC</option>
        <option value="def">DEF</option>
    </select>
</div>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            picked:'abc',
            toggle:false,
            selected:'def'
        },
        methods: {}
    });
</script>
~~~

但是有时我们可能想把值绑定到Vue实例的一个动态属性，这是可以用`v-bind`实例，并且这个属性的值可以不是字符串。

#### 复选框

~~~html
<div id="app">
    <input
            type="checkbox"
            v-model="toggle"
            true-value="yes"
            false-value="no"
            />
       toggle: {{toggle}}
</div>
<script>
    var app = new Vue({
        el: '#app',
        data: {
            toggle:''
        },
        methods: {}
    });
</script>
~~~

~~~js
// 当选中时
vm.toggle === 'yes'
// 当没有选中时
vm.toggle === 'no'
~~~

![](vue/toggle.png)

**注意：**

这里的 `true-value` 和 `false-value` 特性并不会影响输入控件的 `value` 特性，因为浏览器在提交表单时并不会包含未被选中的复选框。如果要确保表单中这两个值中的一个能够被提交，(比如“yes”或“no”)，请换用单选按钮。



#### 单选按钮

~~~html
<input type="radio" v-model="pick" v-bind:value="a"> {{pick}}/{{a}}
<script>
    var app = new Vue({
        el: '#app',
        data: {
            pick:'23',
            a:'32'
        },
        methods: {}
    });
</script>
~~~

![](vue/值绑定_radio.png)



















