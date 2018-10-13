

## js基础

[TOC]

### js中的原始数据

类型有六种：number , string , boolean , null, undefined（未定义）, object.

```js
var num;
console.log(num);//undefined;变量声明了，但是没有初始化（或是函数没有明确返回值，但被接收了）

var num;
console.log(num);//NaN;(不是一个数字)
//如果一个变量的结果是undefined，和一个数字进行运算，结果是NaN,也没有意义。
```

### 使用typeof获取变量的数据类型

```js
//typeof的使用方法(两种)
1.typeof 变量名
2.typeof(变量名)

var num = 11;
var str = "hello";
var bool = true;
var undef;
var nu = null;
var object = new Object();

console.log(typeof num);//number
console.log(typeof(str));//string
console.log(typeof(bool));//boolean
console.log(typeof(undef));//undefined
console.log(typeof(nu));//object
console.log(typeof(object));//object 

//null是表示这个对象的值为空，所以是属于object类型。
```

### js中的进制表示

```js

var num1 = 10;//十进制的10
console.log(num1);//10

var num2 = 010;//八进制的10
console.log(num2);//8

var num3 = 0x10;//十六进制的10
console.log(num3);//16
```

### js中的类型转换

```js
//2.其他类型转数字类型。
console.log(parseInt("12"));//12
console.log(parseInt("12a"));//12
console.log(parseInt("a12"));//NaN
console.log(parseInt("12.98"));//12
console.log(parseInt("13.12"));//13

console.log(parseFloat("12"));//12
console.log(parseFloat("12a"));//12
console.log(parseFloat("a12"));//NaN
console.log(parseFloat("12.98"));//12.98
console.log(parseFloat("13.12"));//13.12

console.log(Number("12"));//12
console.log(Number("12a"));//NaN
console.log(Number("a12"));//NaN
console.log(Number("12.98"));//12.98
console.log(Number("13.12"));//13.12

//其他类型转数字类型---三种方式
1.使用parseInt()转整数
2.使用parseFloat()转小数
3.使用Number()严格转数字


//2.其他类型转字符串类型
//1. toString();
//2. String(var)
//如果变量有意义，使用toString()方式
//如果变量没有意义，使用String()方式。

var num = 10;
console.log(num.toString());

var num2 = 11;
console.log(String(num));


//*********3.其他类型转布尔类型**********
//Boolean(值)

console.log(Boolean(1));//true
console.log(Boolean(0));//false
console.log(Boolean(11));//true
console.log(Boolean(-10));//true
console.log(Boolean("哈哈"));//true
console.log(Boolean(""));//false
console.log(Boolean(null));//false
console.log(Boolean(undefined));//false
```

### number类型

```js
 //数字类型有范围，最大值和最小值
console.log(Number.MAX_VALUE);//1.7976931348623157e+308
console.log(Number.MIN_VALUE);//5e-324
//注意：不要拿小数去验证小数。
//注意：不要用NaN验证是不是NaN。

如何验证结果是不是NaN，使用isNaN()。
判断结果不是一个数字，可以使用isNaN(变量)。

数字类型-->number类型。（无论是整数还是小数都是number类型）
进制的表示：
十进制：10
八进制：010
十六进制：0x001
```

### string类型

```js

//字符串
//获得字符串的长度使用 str.length()
var str = "what are you neng sha ne ";
console.log(str.length);

var str1 = "1234567jiangxishengyichunshiycu";
console.log(str1.length);

转移字符：\\ \t \" 等


//字符串的拼接
var str1 = "您好";
var str2 = "我好";
console.log(str1+str2);
//只要有一个是字符串，其他是数字，那么结果也是拼接，不是相加。

var num1 = "10";
var num2 = 5;
console.log(num1-num2);//5

var num1 = 10;
var num2 = "5";
console.log(num1 - num2);//5
//如果有一个是字符串，另一个不是字符串，使用减号，此时会发生计算。
（浏览器会将字符串转换为数字类型，再进行计算。称为“隐式转换”）。
```

### 布尔类型

```js
布尔类型有两个值：true和false。
console.log(Boolean(1));//true
console.log(Boolean(0));//false
console.log(Boolean(-10));//true
console.log(Boolean(10));//true

变量值如果想为null，必须手动设置。
```

###  表达式

```js

 算数操作符： + - * / %
 算数运算表达式：有算数运算符连接起来的表达式
 一元运算符：这个操作符只需要一个操作数就可以运算的符号。++ --
 二元运算符：这个操作符需要两个操作数就可以运算的符号
 三元运算符 ？：
 复合运算符：+= -= *= /= %=
 var num = 10;
 num += 10;
 复合运算表达式：由复合运算符连接起来的表达式
 关系运算符：>, <, >=, <=, ==(不严格等), ===（严格等）, !=, !==
 
 关系运算表达式：由关系元素符连接起来的表达式（结果是boolean类型）
 逻辑运算符：&&（逻辑与） ||（逻辑或） ！（逻辑非）
 
 逻辑运算表达式：有逻辑运算符连接而成的表达式。
 
 
 //字面量：把一个值直接赋值给一个变量。

//声明变量并初始化。
var num = 10;
var flag = true;
var str = "haha";

```

### 分支结构

```js
/**
 *
 * 页面弹框输入年龄，判断是否为成年人，如果成年输出可以看电影了，如果不成年输出回家写作业
 *
 */
var age = prompt("请输入您的年龄！");
if (isNaN(age)) {
    console.log("您输入的不是一个数字！");
} else {
    if (age > 18) {
        console.log("可以看电影了！");
    } else {
        console.log("看什么看，回家写作业去！");
    }
}



switch-case
注意问题：
    1.default后面的break可以省略。
    2.default也可以省略。
    3.case后面的break也可以省略，但是需要注意可能会出现问题。(没有break不会自动停止，只有遇到break或右大括号才会停止。)。
    4.比较时相当于===。既要比较值，也要比较类型。
```

### 循环结构

```js
/**
 * 写一个九九乘法表
 */
document.write("<table border='1' cellpadding='0' cellspacing='0'>");
for(var i = 1; i <=9; i++){
    document.write("<tr>")
    for(var j = 1; j <= i; j++){
        document.write("<td>"+j+"*"+i+"="+(i*j)+"</td>");
    }
    document.write("</tr>")
}
        document.write("</table>");
```

### 数组

```js
数组：一组数有序的数据。
数组的作用：可以一次性存储多个数据。
数组的定义：
	1.通过构造函数创建数组
		var 数组名 = new Array();
		var arr = new Array();
		注意：数组的名字如果直接输出，那么直接就可以把数组中的数据显示出来，如果没有数据，就看不到数据
		var arr = new Array(长度);//如果数组中没有数据，但是定义了长度，数组中的每个值都是
undefined;

		var arr = new Array(10,20,30,40,50);//给数组赋值。
//如果通过构造函数创建数组时，括号中只有一个数字，则该数字是指明数组的长度，如果数组中有多个数据，则是
//给数组进行赋值，数组的长度就是数据的个数
		
	2.通过字面量的方式创建数组
		var 数组名 = [];//空数组
		var arr = [10,20,30,40];
		//arr.length获得数组的长度。


JS数组中存储的数据类型可以不一致。（通常存储的数据类型是一样的）
注意：Js中的数组的长度是可以改变的。

清空数组可以：
1.循环删除数组中的元素
2.将数组的长度length的长度之间变成为0
```

### 排序

```js
var arr = [10,2,6,24,72,3,2,6,4,72,45,21,2345,65,32];
//以下是选择排序法
for(var i = 0; i < arr.length-1; i++){
    for(var j = i+1; j < arr.length; j++){
        if(arr[i]>arr[j]){
            var temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
//使用冒泡排序
for(var i = 0; i < arr.length-1; i++){
    for(var j = 0; j <arr.length-1-i; j++){
        if(arr[j]>arr[j+1]){
            var temp = arr[j];
            arr[j] = arr[j+1];
            arr[j+1] = temp;
        }
    }
}
console.log(arr);

```

### 全局变量与局部变量

```
JS中没有块级作用域一说。在块级作用域{}中定义的变量在块级作用域以外也可以访问。
全局变量：生命的变量时使用var生命的，那么这个变量就是全局变量，全局变量可以在页面的任何位置使用。
(除了函数以外，其他的任何位置定义的变量都是全局变量)
全局变量，如果页面不关闭，那么就不会释放，就会占空间，消耗内存。
局部变量：在函数内部定义的变量是局部变量，外面不能使用。
隐式全局变量：不使用var定义的变量。
全局变量不可以被删除，隐式全局变量可以被删除。（使用var定义的变量不可以被删除，不适用var定义的变量可以被删除）
全局作用域：全局变量的使用范围
局部作用域：局部变量的使用范围

变量的查找范围是从小到大的。
```



### 预解析

```js
预解析：提交解析代码；
预解析做了什么事：
1.把变量的声明提前了，提前到了当前所在的作用域的最上面。
2.函数的声明也会被提前了，提前到当前所在的作用域的最上面。
预解析中，变量的提升，只会在当前的作用域中提升，提升到当前的作用域的最上面
函数中的变量，只会提前到函数的作用域中的最前面，不会出去
预解析的过程：
1.把变量的声明提升到当前作用域的最前面，只会提升声明，不会提升赋值。
2.把函数的声明提升到当前作用域的最前面，只会提升声明，不会提升赋值。
3.先提升var，再提升function

```



### 函数

```js
1、函数的定义：
function 函数名字（）{
    函数体；
}
2、函数的调用
	函数名字（）；

3、 函数需要先定义再使用。
    函数名需要遵循驼峰命名法。
    函数不能重名，否则会被覆盖。（后面的函数会把前面的函数覆盖）
    形参的个数可以与实参个数不同。
    函数没有明确的返回值，但是被接收了，此时结果为undefined。（没有返回值：函数没有return，或函数有return,但是return后面没有任何内容）；
    
    
4、函数的形参定义时是不需要写关键字var。
5、return关键字表示将结果返回。如果只有return，表示结束函数。

如果输出的是函数名而不是函数名()，则输出的是函数的内容（函数代码）。

6、命名函数与匿名函数
命名函数：如果函数有名字，则称为命名函数
匿名函数：如果函数没有名字，则称为匿名函数。
/**
 * arguments对象
 */
//函数表达式：把一个函数赋值给一个变量，此时就形成了函数表达式。
function fun(){
    var sum = 0;
    for(var i = 0; i < arguments.length; i++){
        sum += arguments[i];
    }
    return sum;
}
console.log(fun(1,2,4,5));//12
console.log(fun(1,2,4,5,5,6,6));//29


//注意：
//函数声明
function f1(){
    console.log("Hello Java");
}
f1();//Hello JavaScript

function f1(){
    console.log("Hello JavaScript");
}
f1();//Hello JavaScript

//函数表达式
var f2 = function (){
    console.log("java");
};
f2();//java
f2 = function (){
    console.log("JavaScript");
};
f2();//JavaScript

//函数的自调用;（声明的同时，函数直接调用，一次性的）
(function(){
    console.log("这是函数的自调用！");
})();
```

### arguments对象（伪数组）

```js
/**
* arguments对象
*/
function fun(){
    var sum = 0;
    for(var i = 0; i < arguments.length; i++){
        sum += arguments[i];
    }
    return sum;
}
console.log(fun(1,2,4,5));//12
console.log(fun(1,2,4,5,5,6,6));//29

```

### 对象

```js
//* 创建对象的三种方式。
//* 1.调用系统的构造函数创建对象。
var obj = new Object();
Object()就是系统的构造函数。

//* 2.自定义构造函数创建对象
 //自定义构造函数创建对象
/**
以下代码做的几件事：
var person = new Person("ooyhao",22);
1.在内存中开辟空间，存储创建的新的对象。
2.把this设置为当前的对象
3.设置对象的属性和方法的值
4.把this对象返回
*/
function Person(name,age){
    this.name = name;
    this.age = age;
    this.study = function(){
        console.log("学习");
    }
}
var person = new Person("ooyhao",22);
person.name = "yanghao";
console.log(person.name);
console.log(person.age);    
person.study();
//字面量的方式创建对象（一次性的对象：无法向构造函数那样进行参数传递进行赋值）。
var obj = {
    name:"ooyhao",
    age:3,
    eat:function(){
        console.log("没钱吃!");
    },
    showInfo:function(){
        console.log(this.name+","+this.age);
    }
};
//        obj.name = "yanghao";
console.log(obj.name);
console.log(obj.age);

obj.eat();
obj.showInfo();

Instanceof关键字 
如何获取该变量（对象）是不是属于该类型的？
语法：
变量 instanceof 类型的名字。返回布尔类型，true就是这种类型，false就不是这种类型。


this 
在当前的对象的方法中，可以使用this关键字代表当前对象。


Js是一门什么样的语言：
1.是一门解释性语言
2.是一门脚本语言
3.是一门弱类型的语言，声明变量都用var
4.是一门基于对象的语言
5.是一门动态类型的语言：
	1.代码（变量）只有执行到这个位置的时候，才知道这个变量中到底存储的是什么，如果是对象，就有对象
    的属性和方法，如果是变量就是变量的作用、
    2.对象没有什么，只要点了，通过点语法，那么就可以为对象添加属性和方法。 
```

### 设置对象的属性或方法的另一种方式

```js
/**
 * 设置和获取属性的另一种方式
 */
function Person(name, age){
    this.name = name;
    this.age = age;
    this.play = function(){
        console.log(this.name+"喜欢玩游戏");
    }
}
var person = new Person("小明",22);

person["name"] = "小红";
console.log(person["name"]);
console.log(person["age"]);
person["play"]();
```

### JSON格式的数据

```js
JSON格式的数据：一般都是成对的，是键值对。
JSON格式的数据实际上就是格式化后的一组字符串的数据
json也是一个对象，数据都是成对的，一般json格式的数据无论是键还是值都是用双引号括起来的。
var json = {
    "name":"ooyhao",
    "age":"12"
};

//使用for-in 循环遍历对象
var json = {
    "name":"ooyhao",
    "age":"22",
    "sex":"男" 
};
//使用for-in循环遍历json对象。
for(var key in json){
    //key 是一个变量，这个变量中存储的是该对象的所有的属性的名字
    console.log(json[key]);
}
```

### 简单类型和复杂数据类型

```js
值类型的值在哪一块空间中存储？栈中存储。
引用类型的值在哪一块空间中存储？栈（地址）和堆（对象）中存储。

值类型之间的传递，传递的是值。
引用类型之间的传递，传递的是地址（引用）
```

### Math对象

```js
Math 是一个内置对象，它具有数学常数和函数的属性和方法，不是一个函数对象。
描述：与其他全局对象不同的是，Math不是一个构造器，Math的所有属性和方法都是静态的。

Math.PI
Math.E

Math.abs()函数的行为
传入一个非数字形式的字符串或者 undefined/empty 变量，将返回 NaN。传入 null 将返回 0。
Math.abs('-1');     // 1
Math.abs(-2);       // 2
Math.abs(null);     // 0
Math.abs("string"); // NaN
Math.abs();         // NaN

例子：使用 Math.ceil
下例展示了 Math.ceil() 的使用：向上取整。
Math.ceil(.95);    // 1
Math.ceil(4);      // 4
Math.ceil(7.004);  // 8
Math.ceil(-0.95);  // -0
Math.ceil(-4);     // -4
Math.ceil(-7.004); // -7


Math.floor
描述
由于 floor 是 Math 的一个静态方法，你总是应该像这样使用它 Math.floor()，而不是作为你创建的一个Math对象的一种方法（Math不是一个构造函数）。

例子：使用 Math.floor
Math.floor( 45.95); 
// 45 
Math.floor( 45.05); 
// 45 
Math.floor( 4 ); 
// 4 
Math.floor(-45.05); 
// -46 
Math.floor(-45.95); 
// -46


Math.max();一组数中的最大值
Math.max(10, 20);   //  20
Math.min();一组数中的最小值
Math.min(10, 20);   //  10

Math.pow()
function raisePower(x,y) {
   return Math.pow(x,y)
}
如果 x 是 7 ，且 y 是 2，则 raisePower 函数返回 49 （7 的 2 次幂）。


Math.sqrt 求一个数的平方根  
Math.sqrt(9); // 3
Math.sqrt(2); // 1.414213562373095

Math.sqrt(1);  // 1
Math.sqrt(0);  // 0
Math.sqrt(-1); // NaN


Math.random() 
函数返回一个浮点,  伪随机数在范围[0，1)，也就是说，从0（包括0）往上，但是不包括1（排除1），然后您可以缩放到所需的范围。实现将初始种子选择到随机数生成算法;它不能被用户选择或重置。他不能被用户选择或重置。
             
console.log(Math.random()); //0.6185734075381502

/**
 * 使用Math对象随机产生一个十六进制的颜色值
 */
function getColor(){
    var color = "#"
    var num = ["0","1","2","3","4","5","6","7","8","9",
        "a","b","c","d","e","f"];
    for(var i = 0; i < 6; i++){
        var randomNum = parseInt(Math.random()*16);
        color += num[randomNum];
    }
    return color;
}
console.log(getColor());

window.onload = function(){
    document.getElementById("dv").style.backgroundColor = getColor();
}
                   
```

### Date对象

```js
<script>
    /**
     * 创建Date对象
     */
    var date1 = new Date();
    console.log(date1);
    var date2 = new Date(1996,03,24);//创建的对象是四月
    console.log(date2);
    var date3 = new Date("1996-03-24");
    console.log(date3);
    var date4 = new Date("1996/03/24");
    console.log(date4);

    console.log(Date.now());//返回当前时间的毫秒值。
	
 //如果浏览器不支持H5，则使用以下方式
    var now = + new Date(); //调用Date对象的valueOf();
    console.log(now);

	
    var date = new Date();
    console.log(date.getYear());
    console.log(date.getFullYear());
    console.log(date.getMonth());//获取月份，从0开始。
    console.log(date.getDate());
    console.log(date.getHours());
    console.log(date.getMinutes());
    console.log(date.getSeconds());
    console.log(date.getDay());//获取星期，从0开始。
		
    console.log(date.toDateString());//Sun Jul 08 2018
    console.log(date.toLocaleDateString());//2018/7/8

    console.log(date.toTimeString());//19:58:48 GMT+0800 (中国标准时间)
    console.log(date.toLocaleTimeString());//下午7:58:48
</script>


//格式化后的指定格式的日期和时间
function getCurrentDate(date){

    var week = ["日","一","二","三","四","五","六"];
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    month = month<=9 ? "0"+month:month;
    var day = date.getDate();
    day = day <=9 ? "0"+day : day;
    var hours = date.getHours();
    hours = hours <=9 ? "0"+hours : hours;
    var minutes = date.getMinutes();
    minutes = minutes <=9 ? "0"+minutes : minutes;
    var seconds = date.getSeconds();
    seconds = seconds <=9 ? "0"+seconds : seconds;
    var weekDay = week[date.getDay()];
    return year+"年"
            +month+"月"
            +day+"日 "
            +hours+":"
            +minutes+":"
            +seconds+" "
            +"星期"+weekDay;
}


```

### String对象

```js
String 是一个对象。
字符串可以看成是字符组成的数组，但是js中是没有字符的。
在js中可以使用单引号或双引号括起来。

字符串的特性：
1.不可变性（字符串的值是不可以改变的）字符串的值之所以看起来是改变的，那是因为指向改变了，并不是真的值改变了。


String的属性和方法-->详解和实例

/**
 * String对象
 */
var str = " hello ";
var strObj = new String(" hello ");
console.log(typeof str);//string
console.log(typeof(strObj));//object



var string = "what are you doing ?";
//==========属性
//获得字符串长度
console.log("length:"+string.length);//20

//==========方法
//去除首尾的空白符
console.log("trim:"+str.trim());//hello
//获取指定位置上的字符
console.log("charAt:"+string.charAt(1));//h
//获取指定位置上的字符所对应的编码
console.log("charCodeAt:"+string.charCodeAt(1));//104
//连接两个字符串
console.log("concat:"+string.concat("Test"));//what are you doing ?Test
//判断字符串是否包含另一个字符串
console.log("includes:"+string.includes("you"));//true
//判断字符串是否以另一个字符串结尾
console.log("endsWith:"+string.endsWith("?"));//true
console.log("startsWith:"+string.startsWith("what"));//true
//返回指定字符串在该字符串的指定位置开始，第一次出现的位置，无则返回-1
console.log("indexOf:"+string.indexOf("o"));//10
console.log("indexOf:"+string.indexOf("A"));//-1
//返回指定字符串在该字符串的指定位置开始，最后一次出现的位置，无则返回-1
console.log("lastIndexOf:"+string.lastIndexOf('o'));//14
console.log("lastIndexOf:"+string.lastIndexOf('A'));//-1
//匹配正则表达式
console.log("123456abcdefg".match(/^[a-z0-9_-]{6,18}$/));
//格式化数据
console.log("\u1E9B\u0323".normalize("NFC"));
console.log("\u1E9B\u0323".normalize("NFD"));
console.log("\u1E9B\u0323".normalize('NFKC'));
//使用指定字符串将该字符串填充到指定长度
console.log('abc'.padEnd(10));          // "abc       "
console.log('abc'.padEnd(10, "foo"));   // "abcfoofoof"
console.log('abc'.padEnd(6, "123456")); // "abc123"
console.log('abc'.padEnd(1));// "abc"
//使用指定字符串将该字符串填充到指定长度
console.log('abc'.padStart(10));         // "       abc"
console.log('abc'.padStart(10, "foo"));  // "foofoofabc"
console.log('abc'.padStart(6,"123465")); // "123abc"
console.log('abc'.padStart(8, "0"));     // "00000abc"
console.log('abc'.padStart(1));          // "abc"
//将字符串进行重复N遍
console.log(string.repeat(2));//what are you doing ?what are you doing ?
//将字符串中的指定字符串替换成指定字符串
console.log("hello java".replace("java","JavaScript"));//hello JavaScript
//查询指定字符串（或正则表达式）在该字符串中的位置，无则返回-1
console.log("hello JavaScript".search("JavaScript"));//6

var newStr = "I want be better !";
//获取子字符串
console.log(newStr.slice(2,5));//wan
console.log(newStr.slice(2,-2));//want be better
console.log(newStr.slice(2,newStr.length));//want be better !
console.log(newStr.slice(2,newStr.length+1));//want be better !
console.log(newStr.slice(-3));//r !
console.log(newStr.slice(2,-4));//want be bett

var strArr = newStr.split(" ");
for(var i = 0; i < strArr.length; i++){
    console.log(strArr[i]);
}
//I
//want
//be
//better
//!

//截取字符串（从下标为2的位置开始，截取5个）
console.log("hello world".substr(2,5));//llo w
//截取字符串（从下标为2的位置开始，截取到下标为5的位置结束，不包括5）
console.log("hello world".substring(2,5));//llo
//根据任何特定于语言环境的案例映射,将制定的字符串的字符全部转化为指定大小写
console.log("HELLO WORLD12".toLocaleLowerCase());//hello world
console.log("hello world12".toLocaleUpperCase());//HELLO WORLD
//将制定的字符串的字符全部转化为指定大小写
console.log("HELLO WORLD12".toLowerCase());//hello world
console.log("hello world12".toUpperCase());//HELLO WORLD

var strObject = new String("hello");
console.log(typeof strObject);//object
console.log(typeof strObject.toString());//string


var newString = "  HELLO  ";
console.log("-"+newString.trim()+"-");//-HELLO-
console.log("-"+newString.trimEnd()+"-");//-  HELLO-
console.log("-"+newString.trimStart()+"-");//-HELLO  -


```

### Array对象

```js
 var arr = new Array(1,2,3,4);
//.length
//获取数组的长度
console.log(arr.length);
//Array.isArray(对象);
//判断对象是不是数组类型、
var i = 10;
console.log(Array.isArray(i));//false
console.log(i instanceof Array);//false
console.log(Array.isArray(arr));//true
console.log(arr instanceof Array);//true
//.concat():拼接数组,组合成一个新的数组
var arr1 = [1,2,3];
var arr2 = ['a','b','c'];
console.log(arr1.concat(arr2));
console.log(arr1);//[1,2,3]
console.log(arr2);//['a','b','c']
/**
 * .every(回调函数):返回值是布尔类型，函数作为参数使用。
 *	 回调函数：
 * 		ele : 元素的值（可选）
 * 		index ：索引的值（可选）
 * 		c ：谁调用了这个方法，那么c就是谁（可选，一般不使用）
 */
arr.every(function (ele,index,c){
    console.log(ele);
    return ele > 0;
});
/**
 * filter(function(ele,index,thisArg){}):创建一个新数组，过滤出符合要求的元素，将其组成一个新的数组。
 * （每一个元素都需要执行一个回调函数）
 * 三个参数：
 * ele:数组中的每一个元素
 * index:数组元素的下标
 * thisArg:调用该方法的数组对象
 */
var arrFilter = [20,30,40,50,60,70,80];
var newArrFilter = arrFilter.filter(function(ele,index){
    return ele > 30;
});
console.log("filter:"+newArrFilter);

/**
 *
 * .push(ele...):将一个或多个元素追加到数组的末尾，并返回数组的大小
 * .pop():删除并返回最后一个元素
 * .shift():删除并返回第一个元素
 * .unshift(ele...):将一个或多个元素添加到开头，并返回数组的大小
 */
var arr = [10,20,30,40,50];
//console.log(arr.push(100,200));//7
//console.log(arr);//[10, 20, 30, 40, 50, 100]
//console.log(arr.pop());//50
//console.log(arr);//[10, 20, 30, 40]
//console.log(arr.shift());//10
//console.log(arr);//[20, 30, 40, 50]
console.log(arr.unshift(99));//6
console.log(arr);


/**
 *
 * .forEach(function(ele,index,array));遍历数组
 *      三个参数：
 *      1.ele 数组中的每一元素
 *      2.index 数组中的元素的下标
 *      3.array 使用该方法的数组
 * .indexOf(ele): 从左开始查询，返回ele的位置，无则返回-1
 * .lastIndexOf(ele):从右边开始查询，返回ele的位置，无则返回-1
 * .join(ele);//返回一个字符串，在每一个元素中间插入制定元素，组成一个字符串
 * .map(callback(){}):遍历每一个元素，每一个元素都执行一次callback函数。返回一个新数组。
 * .reverse():将数组中的元素进行反转。返回反转后的数组。
 * .sort(compareFun(ele1,ele2){}):对数组进行排序，compareFun进行排序规则定义
 * .slice():截取数组中的一部分，返回截取后的新数组，包左不包右。（浅拷贝）
 * .splice(index,num,ele（可选）):从指定位置（index）开始，删除N（num）个元素，
 *             将元素ele插入到指定位置（index），一般用于删除数组中的元素或替换元素，或者是插入元素
 *
 *
 */
var arr = [10,20,30,40,50,20,30,90];
arr.forEach(function (ele,index,array) {
    console.log(ele+"-"+index+"-"+array);
});

console.log(arr.indexOf(20));
console.log(arr.indexOf(100));
console.log(arr.lastIndexOf(20));//5
console.log(arr.lastIndexOf(30));//6
console.log(arr.lastIndexOf(200));//-1

var arr1 = ["a","b","c","d"];
var str = arr1.join("|");
console.log(str);//a|b|c|d （字符串）

var arrMap = [1,4,9,16];
var newArr = arrMap.map(Math.sqrt);
console.log(newArr);//[1, 2, 3, 4]

var arr = [10,20,30,40,50];
console.log(arr.reverse());

var arr2 = [1,6,3,3,7,9,0,2];
console.log(arr2.sort());
console.log(arr2.sort(function (ele1,ele2) {
//            return ele1 < ele2;//降序
    return ele1 > ele2;//升序
}));

var chsArr = ["aa","bb","cc","dd","ee","ff"];
console.log(chsArr.slice(1,4));// ["bb", "cc", "dd"]

//        chsArr.splice(2,0,"ooyhao");
//        console.log(chsArr);//["aa", "bb", "ooyhao", "cc", "dd", "ee", "ff"]
chsArr.splice(2,2,"yanghao");
console.log(chsArr);//["aa", "bb", "yanghao", "ee", "ff"]
```

### 基本包装类型

```js
/**
 * 普通变量不能直接调用属性和方法
 * 对象可以直接调用属性和方法
 *
 * 基本包装类型：本身是基本类型。但是在执行代码的过程中，如果这种类型的变量调用了属性或方法，
 *          那么这种类型就不再是基本类型了，而是基本包装类，这个变量也不是普通的变量了，
 *          而是基本包装类型了
 *
 * */

var num = 10;//基本包装类型
//num.split();//报错
console.log(num.toString());//对象，基本包装类型
var str = "hello";//str 普通变量
var newStr = str.replace("ll","HH");// str 对象，基本包装类型。
console.log(newStr);

var bool = new Boolean(false);//bool是对象
var result = bool && true;
//如果是一个对象&&true，则结果为true，
//如果是true&&对象，则结果是对象
console.log(result);//true

var num1 = 10;//基本类型
var num2 = Number("10");//基本类型（类型转换）
var num3 = new Number("10");//基本包装类型（对象）
```



### const,var,let区别

```js
1.const定义的变量不可以修改，而且必须初始化。
2.var定义的变量可以修改，如果不初始化会输出undefined，不会报错。
3.let是块级作用域，函数内部使用let定义后，对函数外部无影响。
```





## 正则表达式全集

| 字符         | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| \            | 将下一个字符标记为一个特殊字符、或一个原义字符、或一个向后引用、或一个八进制转义符。例如，“`n`”匹配字符“`n`”。“`\n`”匹配一个换行符。串行“`\\`”匹配“`\`”而“`\(`”则匹配“`(`”。 |
| ^            | 匹配输入字符串的开始位置。如果设置了RegExp对象的Multiline属性，^也匹配“`\n`”或“`\r`”之后的位置。 |
| $            | 匹配输入字符串的结束位置。如果设置了RegExp对象的Multiline属性，$也匹配“`\n`”或“`\r`”之前的位置。 |
| *            | 匹配前面的子表达式零次或多次。例如，zo*能匹配“`z`”以及“`zoo`”。*等价于{0,}。 |
| +            | 匹配前面的子表达式一次或多次。例如，“`zo+`”能匹配“`zo`”以及“`zoo`”，但不能匹配“`z`”。+等价于{1,}。 |
| ?            | 匹配前面的子表达式零次或一次。例如，“`do(es)?`”可以匹配“`does`”或“`does`”中的“`do`”。?等价于{0,1}。 |
| {n}          | n是一个非负整数。匹配确定的n次。例如，“`o{2}`”不能匹配“`Bob`”中的“`o`”，但是能匹配“`food`”中的两个o。 |
| {n,}         | n是一个非负整数。至少匹配n次。例如，“`o{2,}`”不能匹配“`Bob`”中的“`o`”，但能匹配“`foooood`”中的所有o。“`o{1,}`”等价于“`o+`”。“`o{0,}`”则等价于“`o*`”。 |
| {n,m}        | m和n均为非负整数，其中n<=m。最少匹配n次且最多匹配m次。例如，“`o{1,3}`”将匹配“`fooooood`”中的前三个o。“`o{0,1}`”等价于“`o?`”。请注意在逗号和两个数之间不能有空格。 |
| ?            | 当该字符紧跟在任何一个其他限制符（*,+,?，{n}，{n,}，{n,m}）后面时，匹配模式是非贪婪的。非贪婪模式尽可能少的匹配所搜索的字符串，而默认的贪婪模式则尽可能多的匹配所搜索的字符串。例如，对于字符串“`oooo`”，“`o+?`”将匹配单个“`o`”，而“`o+`”将匹配所有“`o`”。 |
| .            | 匹配除“`\``n`”之外的任何单个字符。要匹配包括“`\``n`”在内的任何字符，请使用像“`(.|\n)`”的模式。 |
| (pattern)    | 匹配pattern并获取这一匹配。所获取的匹配可以从产生的Matches集合得到，在VBScript中使用SubMatches集合，在JScript中则使用$0…$9属性。要匹配圆括号字符，请使用“`\(`”或“`\)`”。 |
| (?:pattern)  | 匹配pattern但不获取匹配结果，也就是说这是一个非获取匹配，不进行存储供以后使用。这在使用或字符“`(|)`”来组合一个模式的各个部分是很有用。例如“`industr(?:y|ies)`”就是一个比“`industry|industries`”更简略的表达式。 |
| (?=pattern)  | 正向肯定预查，在任何匹配pattern的字符串开始处匹配查找字符串。这是一个非获取匹配，也就是说，该匹配不需要获取供以后使用。例如，“`Windows(?=95|98|NT|2000)`”能匹配“`Windows2000`”中的“`Windows`”，但不能匹配“`Windows3.1`”中的“`Windows`”。预查不消耗字符，也就是说，在一个匹配发生后，在最后一次匹配之后立即开始下一次匹配的搜索，而不是从包含预查的字符之后开始。 |
| (?!pattern)  | 正向否定预查，在任何不匹配pattern的字符串开始处匹配查找字符串。这是一个非获取匹配，也就是说，该匹配不需要获取供以后使用。例如“`Windows(?!95|98|NT|2000)`”能匹配“`Windows3.1`”中的“`Windows`”，但不能匹配“`Windows2000`”中的“`Windows`”。预查不消耗字符，也就是说，在一个匹配发生后，在最后一次匹配之后立即开始下一次匹配的搜索，而不是从包含预查的字符之后开始 |
| (?<=pattern) | 反向肯定预查，与正向肯定预查类拟，只是方向相反。例如，“`(?<=95|98|NT|2000)Windows`”能匹配“`2000Windows`”中的“`Windows`”，但不能匹配“`3.1Windows`”中的“`Windows`”。 |
| (?<!pattern) | 反向否定预查，与正向否定预查类拟，只是方向相反。例如“`(?<!95|98|NT|2000)Windows`”能匹配“`3.1Windows`”中的“`Windows`”，但不能匹配“`2000Windows`”中的“`Windows`”。 |
| x\|y         | 匹配x或y。例如，“`z|food`”能匹配“`z`”或“`food`”。“`(z|f)ood`”则匹配“`zood`”或“`food`”。 |
| [xyz]        | 字符集合。匹配所包含的任意一个字符。例如，“`[abc]`”可以匹配“`plain`”中的“`a`”。 |
| [^xyz]       | 负值字符集合。匹配未包含的任意字符。例如，“`[^abc]`”可以匹配“`plain`”中的“`p`”。 |
| [a-z]        | 字符范围。匹配指定范围内的任意字符。例如，“`[a-z]`”可以匹配“`a`”到“`z`”范围内的任意小写字母字符。 |
| [^a-z]       | 负值字符范围。匹配任何不在指定范围内的任意字符。例如，“`[^a-z]`”可以匹配任何不在“`a`”到“`z`”范围内的任意字符。 |
| \b           | 匹配一个单词边界，也就是指单词和空格间的位置。例如，“`er\b`”可以匹配“`never`”中的“`er`”，但不能匹配“`verb`”中的“`er`”。 |
| \B           | 匹配非单词边界。“`er\B`”能匹配“`verb`”中的“`er`”，但不能匹配“`never`”中的“`er`”。 |
| \cx          | 匹配由x指明的控制字符。例如，\cM匹配一个Control-M或回车符。x的值必须为A-Z或a-z之一。否则，将c视为一个原义的“`c`”字符。 |
| \d           | 匹配一个数字字符。等价于[0-9]。                              |
| \D           | 匹配一个非数字字符。等价于[^0-9]。                           |
| \f           | 匹配一个换页符。等价于\x0c和\cL。                            |
| \n           | 匹配一个换行符。等价于\x0a和\cJ。                            |
| \r           | 匹配一个回车符。等价于\x0d和\cM。                            |
| \s           | 匹配任何空白字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。 |
| \S           | 匹配任何非空白字符。等价于[^ \f\n\r\t\v]。                   |
| \t           | 匹配一个制表符。等价于\x09和\cI。                            |
| \v           | 匹配一个垂直制表符。等价于\x0b和\cK。                        |
| \w           | 匹配包括下划线的任何单词字符。等价于“`[A-Za-z0-9_]`”。       |
| \W           | 匹配任何非单词字符。等价于“`[^A-Za-z0-9_]`”。                |
| \xn          | 匹配n，其中n为十六进制转义值。十六进制转义值必须为确定的两个数字长。例如，“`\x41`”匹配“`A`”。“`\x041`”则等价于“`\x04&1`”。正则表达式中可以使用ASCII编码。. |
| \num         | 匹配num，其中num是一个正整数。对所获取的匹配的引用。例如，“`(.)\1`”匹配两个连续的相同字符。 |
| \n           | 标识一个八进制转义值或一个向后引用。如果\n之前至少n个获取的子表达式，则n为向后引用。否则，如果n为八进制数字（0-7），则n为一个八进制转义值。 |
| \nm          | 标识一个八进制转义值或一个向后引用。如果\nm之前至少有nm个获得子表达式，则nm为向后引用。如果\nm之前至少有n个获取，则n为一个后跟文字m的向后引用。如果前面的条件都不满足，若n和m均为八进制数字（0-7），则\nm将匹配八进制转义值nm。 |
| \nml         | 如果n为八进制数字（0-3），且m和l均为八进制数字（0-7），则匹配八进制转义值nml。 |
| \un          | 匹配n，其中n是一个用四个十六进制数字表示的Unicode字符。例如，\u00A9匹配版权符号（©）。 |

 

## 常用正则表达式

| 用户名                  | /^[a-z0-9_-]{3,16}$/                                         |
| ----------------------- | ------------------------------------------------------------ |
| 密码                    | /^[a-z0-9_-]{6,18}$/                                         |
| 十六进制值              | /^#?([a-f0-9]{6}\|[a-f0-9]{3})$/                             |
| 电子邮箱                | /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/ /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/ |
| URL                     | /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/ |
| IP 地址                 | /((2[0-4]\d\|25[0-5]\|[01]?\d\d?)\.){3}(2[0-4]\d\|25[0-5]\|[01]?\d\d?)/ /^(?:(?:25[0-5]\|2[0-4][0-9]\|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]\|2[0-4][0-9]\|[01]?[0-9][0-9]?)$/ |
| HTML 标签               | /^<([a-z]+)([^<]+)*(?:>(.*)<\/\1>\|\s+\/>)$/                 |
| 删除代码\\注释          | (?<!http:\|\S)//.*$                                          |
| Unicode编码中的汉字范围 | /^[\u2E80-\u9FFF]+$/                                         |