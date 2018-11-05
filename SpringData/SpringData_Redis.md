# SpringDataRedis

## **Redis 数据结构简介**

Redis可以存储键与5种不同数据结构类型之间的映射，这5种数据结构类型分别为String（字符串）、List（列表）、Set（集合）、Hash（散列）和 Zset（有序集合）。

下面来对这5种数据结构类型作简单的介绍：

| 结构类型 | 结构存储的值                                                 | 结构的读写能力                                               |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| String   | 可以是字符串、整数或者浮点数                                 | 对整个字符串或者字符串的其中一部分执行操作；对象和浮点数执行自增(increment)或者自减(decrement) |
| List     | 一个链表，链表上的每个节点都包含了一个字符串                 | 从链表的两端推入或者弹出元素；根据偏移量对链表进行修剪(trim)；读取单个或者多个元素；根据值来查找或者移除元素 |
| Set      | 包含字符串的无序收集器(unorderedcollection)，并且被包含的每个字符串都是独一无二的、各不相同 | 添加、获取、移除单个元素；检查一个元素是否存在于某个集合中；计算交集、并集、差集；从集合里卖弄随机获取元素 |
| Hash     | 包含键值对的无序散列表                                       | 添加、获取、移除单个键值对；获取所有键值对                   |
| Zset     | 字符串成员(member)与浮点数分值(score)之间的有序映射，元素的排列顺序由分值的大小决定 | 添加、获取、删除单个元素；根据分值范围(range)或者成员来获取元素 |

Redis 5种数据结构的概念大致介绍到这边，下面将结合Spring封装的RedisTemplate来对这5种数据结构的运用进行演示

## **RedisTemplate介绍**

Spring封装了RedisTemplate对象来进行对Redis的各种操作，它支持所有的Redis原生的api。RedisTemplate位于spring-data-redis包下。

### **RedisTemplate在Spring代码中的结构如下：**

```
org.springframework.data.redis.core
Class RedisTemplate<K,V>
java.lang.Object
    org.springframework.data.redis.core.RedisAccessor
        org.springframework.data.redis.core.RedisTemplate<K,V>
```

Type Parameters:
K

- the Redis key type against which the template works (usually a String)
  模板中的Redis key的类型（通常为String）如：RedisTemplate<String, Object>
  注意：**如果没特殊情况，切勿定义成RedisTemplate<Object, Object>**，否则根据里氏替换原则，使用的时候会造成类型错误 。

V

- the Redis value type against which the template works
  模板中的Redis value的类型

### **RedisTemplate中定义了对5种数据结构操作**

```
redisTemplate.opsForValue();//操作字符串
redisTemplate.opsForHash();//操作hash
redisTemplate.opsForList();//操作list
redisTemplate.opsForSet();//操作set
redisTemplate.opsForZSet();//操作有序set
```

### **StringRedisTemplate与RedisTemplate**

- 两者的关系是StringRedisTemplate继承RedisTemplate。

- 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。

- SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。

  StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。

  RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。

### **RedisTemplate配置如下：**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
{
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### **Redis的String数据结构 （推荐使用StringRedisTemplate）**

**注意：如果使用RedisTemplate需要更改序列化方式**

```
RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );
        template.setValueSerializer(stringSerializer );
        template.setHashKeySerializer(stringSerializer );
        template.setHashValueSerializer(stringSerializer );
```

public interface ValueOperations<K,V>
Redis operations for simple (or in Redis terminology 'string') values.
ValueOperations可以对String数据结构进行操作：

- set void set(K key, V value);

  ```
  使用：redisTemplate.opsForValue().set("name","tom");
  结果：redisTemplate.opsForValue().get("name")  输出结果为tom
  ```

- set void set(K key, V value, long timeout, TimeUnit unit);

  ```
  使用：redisTemplate.opsForValue().set("name","tom",10, TimeUnit.SECONDS);
  结果：redisTemplate.opsForValue().get("name")由于设置的是10秒失效，十秒之内查询有结果，十秒之后返回为null
  ```

- set void set(K key, V value, long offset);
  该方法是用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始

  ```
  使用：template.opsForValue().set("key","hello world");
        template.opsForValue().set("key","redis", 6);
        System.out.println("***************"+template.opsForValue().get("key"));
  结果：***************hello redis
  ```

- setIfAbsent Boolean setIfAbsent(K key, V value);

  ```
  使用：System.out.println(template.opsForValue().setIfAbsent("multi1","multi1"));//false  multi1之前已经存在
          System.out.println(template.opsForValue().setIfAbsent("multi111","multi111"));//true  multi111之前不存在
  结果：false
  true
  ```


- multiSet void multiSet(Map<? extends K, ? extends V> m);
  为多个键分别设置它们的值

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Map<String,String> maps = new HashMap<String, String>();
        maps.put("multi1","multi1");
        maps.put("multi2","multi2");
        maps.put("multi3","multi3");
        template.opsForValue().multiSet(maps);
        List<String> keys = new ArrayList<String>();
        keys.add("multi1");
        keys.add("multi2");
        keys.add("multi3");
        System.out.println(template.opsForValue().multiGet(keys));
  结果：[multi1, multi2, multi3]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- multiSetIfAbsent Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m);
  为多个键分别设置它们的值，如果存在则返回false，不存在返回true

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Map<String,String> maps = new HashMap<String, String>();
        maps.put("multi11","multi11");
        maps.put("multi22","multi22");
        maps.put("multi33","multi33");
        Map<String,String> maps2 = new HashMap<String, String>();
        maps2.put("multi1","multi1");
        maps2.put("multi2","multi2");
        maps2.put("multi3","multi3");
        System.out.println(template.opsForValue().multiSetIfAbsent(maps));
        System.out.println(template.opsForValue().multiSetIfAbsent(maps2));
  结果：true
  false
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- get V get(Object key);

  ```
  使用：template.opsForValue().set("key","hello world");
        System.out.println("***************"+template.opsForValue().get("key"));
  结果：***************hello world
  ```

- getAndSet V getAndSet(K key, V value);
  设置键的字符串值并返回其旧值

  ```
  使用：template.opsForValue().set("getSetTest","test");
        System.out.println(template.opsForValue().getAndSet("getSetTest","test2"));
  结果：test
  ```

- multiGet List<V> multiGet(Collection<K> keys);
  为多个键分别取出它们的值

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Map<String,String> maps = new HashMap<String, String>();
        maps.put("multi1","multi1");
        maps.put("multi2","multi2");
        maps.put("multi3","multi3");
        template.opsForValue().multiSet(maps);
        List<String> keys = new ArrayList<String>();
        keys.add("multi1");
        keys.add("multi2");
        keys.add("multi3");
        System.out.println(template.opsForValue().multiGet(keys));
  结果：[multi1, multi2, multi3]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- increment Long increment(K key, long delta);
  支持整数

  ```
  使用：template.opsForValue().increment("increlong",1);
        System.out.println("***************"+template.opsForValue().get("increlong"));
  结果：***************1
  ```

- increment Double increment(K key, double delta);
  也支持浮点数

  ```
  使用：template.opsForValue().increment("increlong",1.2);
        System.out.println("***************"+template.opsForValue().get("increlong"));
  结果：***************2.2
  ```

- append Integer append(K key, String value);
  如果key已经存在并且是一个字符串，则该命令将该值追加到字符串的末尾。如果键不存在，则它被创建并设置为空字符串，因此APPEND在这种特殊情况下将类似于SET。

  ```
  使用：template.opsForValue().append("appendTest","Hello");
        System.out.println(template.opsForValue().get("appendTest"));
        template.opsForValue().append("appendTest","world");
        System.out.println(template.opsForValue().get("appendTest"));
  结果：Hello
        Helloworld
  ```

- get String get(K key, long start, long end);
  截取key所对应的value字符串

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：appendTest对应的value为Helloworld
  System.out.println("*********"+template.opsForValue().get("appendTest",0,5));
  结果：*********Hellow
  使用：System.out.println("*********"+template.opsForValue().get("appendTest",0,-1));
  结果：*********Helloworld
  使用：System.out.println("*********"+template.opsForValue().get("appendTest",-3,-1));
  结果：*********rld
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- size Long size(K key);
  返回key所对应的value值得长度

  ```
  使用：template.opsForValue().set("key","hello world");
    System.out.println("***************"+template.opsForValue().size("key"));
  结果：***************11
  ```

- setBit Boolean setBit(K key, long offset, boolean value);
  对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
  key键对应的值value对应的ascii码,在offset的位置(从左向右数)变为value

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：template.opsForValue().set("bitTest","a");
        // 'a' 的ASCII码是 97。转换为二进制是：01100001
        // 'b' 的ASCII码是 98  转换为二进制是：01100010
        // 'c' 的ASCII码是 99  转换为二进制是：01100011
        //因为二进制只有0和1，在setbit中true为1，false为0，因此我要变为'b'的话第六位设置为1，第七位设置为0
        template.opsForValue().setBit("bitTest",6, true);
        template.opsForValue().setBit("bitTest",7, false);
        System.out.println(template.opsForValue().get("bitTest"));
  结果：b
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- getBit Boolean getBit(K key, long offset);
  获取键对应值的ascii码的在offset处位值

  ```
  使用：System.out.println(template.opsForValue().getBit("bitTest",7));
  结果：false
  ```

### **Redis的List数据结构**

**这边我们把RedisTemplate序列化方式改回之前的**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
       template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

public interface ListOperations<K,V>
Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素导列表的头部（左边）或者尾部（右边）
ListOperations专门操作list列表：

- List<V> range(K key, long start, long end);
  返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素

  ```
  使用：System.out.println(template.opsForList().range("list",0,-1));
  结果:[c#, c++, python, java, c#, c#]
  ```

- void trim(K key, long start, long end);
  修剪现有列表，使其只包含指定的指定范围的元素，起始和停止都是基于0的索引

  ```
  使用：System.out.println(template.opsForList().range("list",0,-1));
  template.opsForList().trim("list",1,-1);//裁剪第一个元素
  System.out.println(template.opsForList().range("list",0,-1));
  结果:[c#, c++, python, java, c#, c#]
  [c++, python, java, c#, c#]
  ```

- Long size(K key);
  返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。

  ```
  使用：System.out.println(template.opsForList().size("list"));
  结果:6
  ```

- Long leftPush(K key, V value);
  将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：template.opsForList().leftPush("list","java");
        template.opsForList().leftPush("list","python");
        template.opsForList().leftPush("list","c++");
  结果:返回的结果为推送操作后的列表的长度
  1
  2
  3
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long leftPushAll(K key, V... values);
  批量把一个数组插入到列表中

  ```
  使用：String[] stringarrays = new String[]{"1","2","3"};
        template.opsForList().leftPushAll("listarray",stringarrays);
        System.out.println(template.opsForList().range("listarray",0,-1));
  结果:[3, 2, 1]
  ```

- Long leftPushAll(K key, Collection<V> values);
  批量把一个集合插入到列表中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：List<Object> strings = new ArrayList<Object>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        template.opsForList().leftPushAll("listcollection4", strings);
        System.out.println(template.opsForList().range("listcollection4",0,-1));
  结果:[3, 2, 1]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long leftPushIfPresent(K key, V value);
  只有存在key对应的列表才能将这个value值插入到key所对应的列表中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForList().leftPushIfPresent("leftPushIfPresent","aa"));
        System.out.println(template.opsForList().leftPushIfPresent("leftPushIfPresent","bb"));
  ==========分割线===========
  System.out.println(template.opsForList().leftPush("leftPushIfPresent","aa"));
        System.out.println(template.opsForList().leftPushIfPresent("leftPushIfPresent","bb"));
  结果:
  0
  0
  ==========分割线===========
  1
  2
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long leftPush(K key, V pivot, V value);
  把value值放到key对应列表中pivot值的左面，如果pivot值存在的话

  ```
  使用：template.opsForList().leftPush("list","java","oc");
        System.out.print(template.opsForList().range("list",0,-1));
  结果：[c++, python, oc, java, c#, c#]
  ```

- Long rightPush(K key, V value);
  将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：template.opsForList().rightPush("listRight","java");
        template.opsForList().rightPush("listRight","python");
        template.opsForList().rightPush("listRight","c++");
  结果:
  1
  2
  3
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long rightPushAll(K key, V... values);

  ```
  使用：String[] stringarrays = new String[]{"1","2","3"};
        template.opsForList().rightPushAll("listarrayright",stringarrays);
        System.out.println(template.opsForList().range("listarrayright",0,-1));
  结果:[1, 2, 3]
  ```

- Long rightPushAll(K key, Collection<V> values);

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：List<Object> strings = new ArrayList<Object>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        template.opsForList().rightPushAll("listcollectionright", strings);
        System.out.println(template.opsForList().range("listcollectionright",0,-1));
  结果:[1, 2, 3]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long rightPushIfPresent(K key, V value);
  只有存在key对应的列表才能将这个value值插入到key所对应的列表中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForList().rightPushIfPresent("rightPushIfPresent","aa"));
        System.out.println(template.opsForList().rightPushIfPresent("rightPushIfPresent","bb"));
        System.out.println("==========分割线===========");
        System.out.println(template.opsForList().rightPush("rightPushIfPresent","aa"));
        System.out.println(template.opsForList().rightPushIfPresent("rightPushIfPresent","bb"));
  结果:0
  0
  ==========分割线===========
  1
  2
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long rightPush(K key, V pivot, V value);
  把value值放到key对应列表中pivot值的右面，如果pivot值存在的话

  ```
  使用：System.out.println(template.opsForList().range("listRight",0,-1));
        template.opsForList().rightPush("listRight","python","oc");
        System.out.println(template.opsForList().range("listRight",0,-1));
  结果:[java, python, c++]
  [java, python, oc, c++]
  ```

- void set(K key, long index, V value);
  在列表中index的位置设置value值

  ```
  使用：System.out.println(template.opsForList().range("listRight",0,-1));
        template.opsForList().set("listRight",1,"setValue");
        System.out.println(template.opsForList().range("listRight",0,-1));
  结果:[java, python, oc, c++]
  [java, setValue, oc, c++]
  ```

- Long remove(K key, long count, Object value);
  从存储在键中的列表中删除等于值的元素的第一个计数事件。
  计数参数以下列方式影响操作：
  count> 0：删除等于从头到尾移动的值的元素。
  count <0：删除等于从尾到头移动的值的元素。
  count = 0：删除等于value的所有元素。

  ```
  使用：System.out.println(template.opsForList().range("listRight",0,-1));
        template.opsForList().remove("listRight",1,"setValue");//将删除列表中存储的列表中第一次次出现的“setValue”。
        System.out.println(template.opsForList().range("listRight",0,-1));
  结果:[java, setValue, oc, c++]
  [java, oc, c++]
  ```

- V index(K key, long index);
  根据下表获取列表中的值，下标是从0开始的

  ```
  使用：System.out.println(template.opsForList().range("listRight",0,-1));
  System.out.println(template.opsForList().index("listRight",2));
  结果:[java, oc, c++]
  c++
  ```

- V leftPop(K key);
  弹出最左边的元素，弹出之后该值在列表中将不复存在

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForList().range("list",0,-1));
        System.out.println(template.opsForList().leftPop("list"));
        System.out.println(template.opsForList().range("list",0,-1));
  结果:
  [c++, python, oc, java, c#, c#]
  c++
  [python, oc, java, c#, c#]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- V leftPop(K key, long timeout, TimeUnit unit);
  移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

  ```
  使用：用法与 leftPop(K key);一样
  ```

- V rightPop(K key);
  弹出最右边的元素，弹出之后该值在列表中将不复存在

  ```
  使用：    System.out.println(template.opsForList().range("list",0,-1));
        System.out.println(template.opsForList().rightPop("list"));
        System.out.println(template.opsForList().range("list",0,-1));
  结果:[python, oc, java, c#, c#]
  c#
  [python, oc, java, c#]
  ```

- V rightPop(K key, long timeout, TimeUnit unit);
  移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

  ```
  使用：用法与 rightPop(K key);一样
  ```

- V rightPopAndLeftPush(K sourceKey, K destinationKey);
  用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForList().range("list",0,-1));
  template.opsForList().rightPopAndLeftPush("list","rightPopAndLeftPush");
    System.out.println(template.opsForList().range("list",0,-1));
    System.out.println(template.opsForList().range("rightPopAndLeftPush",0,-1));
  结果:[oc, java,c#]
  [oc, java]
  [c#]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit);
  用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

  ```
  使用：用法与rightPopAndLeftPush(K sourceKey, K destinationKey)一样
  ```

### **Redis的Hash数据机构**

Redis的散列可以让用户将多个键值对存储到一个Redis键里面。
public interface HashOperations<H,HK,HV>
HashOperations提供一系列方法操作hash：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
初始数据:
//template.opsForHash().put("redisHash","name","tom");
        //template.opsForHash().put("redisHash","age",26);
        //template.opsForHash().put("redisHash","class","6");

//Map<String,Object> testMap = new HashMap();
        //testMap.put("name","jack");
        //testMap.put("age",27);
        //testMap.put("class","1");
        //template.opsForHash().putAll("redisHash1",testMap);
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long delete(H key, Object... hashKeys);
  删除给定的哈希hashKeys

  ```
  使用：System.out.println(template.opsForHash().delete("redisHash","name"));
        System.out.println(template.opsForHash().entries("redisHash"));
  结果：1
  {class=6, age=28.1}
  ```

- Boolean hasKey(H key, Object hashKey);
  确定哈希hashKey是否存在

  ```
  使用：System.out.println(template.opsForHash().hasKey("redisHash","age"));
        System.out.println(template.opsForHash().hasKey("redisHash","ttt"));
  结果：true
  false
  ```

- HV get(H key, Object hashKey);
  从键中的哈希获取给定hashKey的值

  ```
  使用：System.out.println(template.opsForHash().get("redisHash","age"));
  结果：26
  ```

- List<HV> multiGet(H key, Collection<HK> hashKeys);
  从哈希中获取给定hashKey的值

  ```
  使用：List<Object> kes = new ArrayList<Object>();
        kes.add("name");
        kes.add("age");
        System.out.println(template.opsForHash().multiGet("redisHash",kes));
  结果：[jack, 28.1]
  ```

- Long increment(H key, HK hashKey, long delta);
  通过给定的delta增加散列hashKey的值（整型）

  ```
  使用：System.out.println(template.opsForHash().get("redisHash","age"));
    System.out.println(template.opsForHash().increment("redisHash","age",1));
  结果：26
  27
  ```

- Double increment(H key, HK hashKey, double delta);
  通过给定的delta增加散列hashKey的值（浮点数）

  ```
  使用：System.out.println(template.opsForHash().get("redisHash","age"));
    System.out.println(template.opsForHash().increment("redisHash","age",1.1));
  结果：27
  28.1
  ```

- Set<HK> keys(H key);
  获取key所对应的散列表的key

  ```
  使用：System.out.println(template.opsForHash().keys("redisHash1"));
  //redisHash1所对应的散列表为{class=1, name=jack, age=27}
  结果：[name, class, age]
  ```

- Long size(H key);
  获取key所对应的散列表的大小个数

  ```
  使用：System.out.println(template.opsForHash().size("redisHash1"));
  //redisHash1所对应的散列表为{class=1, name=jack, age=27}
  结果：3
  ```

- void putAll(H key, Map<? extends HK, ? extends HV> m);
  使用m中提供的多个散列字段设置到key对应的散列表中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Map<String,Object> testMap = new HashMap();
        testMap.put("name","jack");
        testMap.put("age",27);
        testMap.put("class","1");
        template.opsForHash().putAll("redisHash1",testMap);
        System.out.println(template.opsForHash().entries("redisHash1"));
  结果：{class=1, name=jack, age=27}
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- void put(H key, HK hashKey, HV value);
  设置散列hashKey的值

  ```
  使用：template.opsForHash().put("redisHash","name","tom");
        template.opsForHash().put("redisHash","age",26);
        template.opsForHash().put("redisHash","class","6");
  System.out.println(template.opsForHash().entries("redisHash"));
  结果：{age=26, class="6", name=tom}
  ```

- Boolean putIfAbsent(H key, HK hashKey, HV value);
  仅当hashKey不存在时才设置散列hashKey的值。

  ```
  使用：System.out.println(template.opsForHash().putIfAbsent("redisHash","age",30));
  System.out.println(template.opsForHash().putIfAbsent("redisHash","kkk","kkk"));
  结果：false
  true
  ```

- List<HV> values(H key);
  获取整个哈希存储的值根据密钥

  ```
  使用：System.out.println(template.opsForHash().values("redisHash"));
  结果：[tom, 26, 6]
  ```

- Map<HK, HV> entries(H key);
  获取整个哈希存储根据密钥

  ```
  使用：System.out.println(template.opsForHash().entries("redisHash"));
  结果：{age=26, class="6", name=tom}
  ```

- Cursor<Map.Entry<HK, HV>> scan(H key, ScanOptions options);
  使用Cursor在key的hash中迭代，相当于迭代器。

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Cursor<Map.Entry<Object, Object>> curosr = template.opsForHash().scan("redisHash", ScanOptions.ScanOptions.NONE);
        while(curosr.hasNext()){
            Map.Entry<Object, Object> entry = curosr.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
  结果：age:28.1
  class:6
  kkk:kkk
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### **Redis的Set数据结构**

Redis的Set是string类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。
Redis 中 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。
public interface SetOperations<K,V>
SetOperations提供了对无序集合的一系列操作：

- Long add(K key, V... values);
  无序集合中添加元素，返回添加个数
  也可以直接在add里面添加多个值 如：template.opsForSet().add("setTest","aaa","bbb")

  ```
  使用：String[] strarrays = new String[]{"strarr1","sgtarr2"};
        System.out.println(template.opsForSet().add("setTest", strarrays));
  结果：2
  ```

- Long remove(K key, Object... values);
  移除集合中一个或多个成员

  ```
  使用：String[] strarrays = new String[]{"strarr1","sgtarr2"};
  System.out.println(template.opsForSet().remove("setTest",strarrays));
  结果：2
  ```

- V pop(K key);
  移除并返回集合中的一个随机元素

  ```
  使用：System.out.println(template.opsForSet().pop("setTest"));
  System.out.println(template.opsForSet().members("setTest"));
  结果：bbb
  [aaa, ccc]
  ```

- Boolean move(K key, V value, K destKey);
  将 member 元素从 source 集合移动到 destination 集合

  ```
  使用：template.opsForSet().move("setTest","aaa","setTest2");
        System.out.println(template.opsForSet().members("setTest"));
        System.out.println(template.opsForSet().members("setTest2"));
  结果：[ccc]
  [aaa]
  ```

- Long size(K key);
  无序集合的大小长度

  ```
  使用：System.out.println(template.opsForSet().size("setTest"));
  结果：1
  ```

- Boolean isMember(K key, Object o);
  判断 member 元素是否是集合 key 的成员

  ```
  使用：System.out.println(template.opsForSet().isMember("setTest","ccc"));
        System.out.println(template.opsForSet().isMember("setTest","asd"));
  结果：true
  false
  ```

- Set<V> intersect(K key, K otherKey);
  key对应的无序集合与otherKey对应的无序集合求交集

  ```
  使用：System.out.println(template.opsForSet().members("setTest"));
        System.out.println(template.opsForSet().members("setTest2"));
        System.out.println(template.opsForSet().intersect("setTest","setTest2"));
  结果：[aaa, ccc]
  [aaa]
  [aaa]
  ```

- Set<V> intersect(K key, Collection<K> otherKeys);
  key对应的无序集合与多个otherKey对应的无序集合求交集

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForSet().members("setTest"));
          System.out.println(template.opsForSet().members("setTest2"));
          System.out.println(template.opsForSet().members("setTest3"));
          List<String> strlist = new ArrayList<String>();
          strlist.add("setTest2");
          strlist.add("setTest3");
          System.out.println(template.opsForSet().intersect("setTest",strlist));
  结果：[aaa, ccc]
  [aaa]
  [ccc, aaa]
  [aaa]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)


- Long intersectAndStore(K key, K otherKey, K destKey);
  key无序集合与otherkey无序集合的交集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
  System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
  System.out.println(template.opsForSet().intersectAndStore("setTest","setTest2","destKey1"));
  System.out.println(template.opsForSet().members("destKey1"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  2
  [aaa, ccc]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long intersectAndStore(K key, Collection<K> otherKeys, K destKey);
  key对应的无序集合与多个otherKey对应的无序集合求交集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println("setTest3:" + template.opsForSet().members("setTest3"));
        List<String> strlist = new ArrayList<String>();
        strlist.add("setTest2");
        strlist.add("setTest3");
        System.out.println(template.opsForSet().intersectAndStore("setTest",strlist,"destKey2"));
        System.out.println(template.opsForSet().members("destKey2"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  setTest3:[ccc, aaa]
  2
  [aaa, ccc]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> union(K key, K otherKey);
  key无序集合与otherKey无序集合的并集

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println(template.opsForSet().union("setTest","setTest2"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  [ccc, aaa, ddd, bbb]
  ```

- Set<V> union(K key, Collection<K> otherKeys);
  key无序集合与多个otherKey无序集合的并集

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println("setTest3:" + template.opsForSet().members("setTest3"));
        List<String> strlist = new ArrayList<String>();
        strlist.add("setTest2");
        strlist.add("setTest3");
        System.out.println(template.opsForSet().union("setTest",strlist));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  setTest3:[xxx, ccc, aaa]
  [ddd, xxx, bbb, aaa, ccc]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long unionAndStore(K key, K otherKey, K destKey);
  key无序集合与otherkey无序集合的并集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println(template.opsForSet().unionAndStore("setTest","setTest2","unionAndStoreTest1"));
        System.out.println("unionAndStoreTest1:" + template.opsForSet().members("unionAndStoreTest1"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  4
  unionAndStoreTest1:[ccc, aaa, ddd, bbb]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long unionAndStore(K key, Collection<K> otherKeys, K destKey);
  key无序集合与多个otherkey无序集合的并集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println("setTest3:" + template.opsForSet().members("setTest3"));
        List<String> strlist = new ArrayList<String>();
        strlist.add("setTest2");
        strlist.add("setTest3");
        System.out.println(template.opsForSet().unionAndStore("setTest",strlist,"unionAndStoreTest2"));
        System.out.println("unionAndStoreTest2:" + template.opsForSet().members("unionAndStoreTest2"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  setTest3:[xxx, ccc, aaa]
  5
  unionAndStoreTest2:[ddd, xxx, bbb, aaa, ccc]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> difference(K key, K otherKey);
  key无序集合与otherKey无序集合的差集

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println(template.opsForSet().difference("setTest","setTest2"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  [bbb, ddd]
  ```

- Set<V> difference(K key, Collection<K> otherKeys);
  key无序集合与多个otherKey无序集合的差集

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println("setTest3:" + template.opsForSet().members("setTest3"));
        List<String> strlist = new ArrayList<String>();
        strlist.add("setTest2");
        strlist.add("setTest3");
        System.out.println(template.opsForSet().difference("setTest",strlist));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  setTest3:[xxx, ccc, aaa]
  [bbb, ddd]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long differenceAndStore(K key, K otherKey, K destKey);
  key无序集合与otherkey无序集合的差集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println(template.opsForSet().differenceAndStore("setTest","setTest2","differenceAndStore1"));
        System.out.println("differenceAndStore1:" + template.opsForSet().members("differenceAndStore1"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  2
  differenceAndStore1:[bbb, ddd]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long differenceAndStore(K key, Collection<K> otherKeys, K destKey);
  key无序集合与多个otherkey无序集合的差集存储到destKey无序集合中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
        System.out.println("setTest3:" + template.opsForSet().members("setTest3"));
        List<String> strlist = new ArrayList<String>();
        strlist.add("setTest2");
        strlist.add("setTest3");
        System.out.println(template.opsForSet().differenceAndStore("setTest",strlist,"differenceAndStore2"));
        System.out.println("differenceAndStore2:" + template.opsForSet().members("differenceAndStore2"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTest2:[ccc, aaa]
  setTest3:[xxx, ccc, aaa]
  2
  differenceAndStore2:[bbb, ddd]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> members(K key);
  返回集合中的所有成员

  ```
  使用：System.out.println(template.opsForSet().members("setTest"));
  结果：[ddd, bbb, aaa, ccc]
  ```

- V randomMember(K key);
  随机获取key无序集合中的一个元素

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println("setTest:" + template.opsForSet().members("setTest"));
        System.out.println("setTestrandomMember:" + template.opsForSet().randomMember("setTest"));
        System.out.println("setTestrandomMember:" + template.opsForSet().randomMember("setTest"));
        System.out.println("setTestrandomMember:" + template.opsForSet().randomMember("setTest"));
        System.out.println("setTestrandomMember:" + template.opsForSet().randomMember("setTest"));
  结果：setTest:[ddd, bbb, aaa, ccc]
  setTestrandomMember:aaa
  setTestrandomMember:bbb
  setTestrandomMember:aaa
  setTestrandomMember:ddd
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> distinctRandomMembers(K key, long count);
  获取多个key无序集合中的元素（去重），count表示个数

  ```
  使用：System.out.println("randomMembers:" + template.opsForSet().distinctRandomMembers("setTest",5));
  结果：randomMembers:[aaa, bbb, ddd, ccc]
  ```

- List<V> randomMembers(K key, long count);
  获取多个key无序集合中的元素，count表示个数

  ```
  使用：System.out.println("randomMembers:" + template.opsForSet().randomMembers("setTest",5));
  结果：randomMembers:[ccc, ddd, ddd, ddd, aaa]
  ```

- Cursor<V> scan(K key, ScanOptions options);
  遍历set

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：    Cursor<Object> curosr = template.opsForSet().scan("setTest", ScanOptions.NONE);
        while(curosr.hasNext()){
            System.out.println(curosr.next());
        }
  结果：ddd
  bbb
  aaa
  ccc
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### **Redis的ZSet数据结构**

Redis有序集合和无序集合一样也是string类型元素的集合,且不允许重复的成员。
不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
有序集合的成员是唯一的,但分数(score)却可以重复。
public interface ZSetOperations<K,V>
ZSetOperations提供了一系列方法对有序集合进行操作：

- Boolean add(K key, V value, double score);
  新增一个有序集合，存在的话为false，不存在的话为true

  ```
  使用：System.out.println(template.opsForZSet().add("zset1","zset-1",1.0));
  结果：true
  ```

- Long add(K key, Set<TypedTuple<V>> tuples);
  新增一个有序集合

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<Object>("zset-6",9.9);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        System.out.println(template.opsForZSet().add("zset1",tuples));
        System.out.println(template.opsForZSet().range("zset1",0,-1));
  结果：[zset-1, zset-2, zset-3, zset-4, zset-5, zset-6]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long remove(K key, Object... values);
  从有序集合中移除一个或者多个元素

  ```
  使用：System.out.println(template.opsForZSet().range("zset1",0,-1));
        System.out.println(template.opsForZSet().remove("zset1","zset-6"));
        System.out.println(template.opsForZSet().range("zset1",0,-1));
  结果：[zset-1, zset-2, zset-3, zset-4, zset-5, zset-6]
  1
  [zset-1, zset-2, zset-3, zset-4, zset-5]
  ```

- Double incrementScore(K key, V value, double delta);
  增加元素的score值，并返回增加后的值

  ```
  使用：System.out.println(template.opsForZSet().incrementScore("zset1","zset-1",1.1));  //原为1.1
  结果：2.2
  ```

- Long rank(K key, Object o);
  返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().range("zset1",0,-1));
        System.out.println(template.opsForZSet().rank("zset1","zset-2"));
  结果：[zset-2, zset-1, zset-3, zset-4, zset-5]
  0   //表明排名第一
  ```

- Long reverseRank(K key, Object o);
  返回有序集中指定成员的排名，其中有序集成员按分数值递减(从大到小)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().range("zset1",0,-1));
        System.out.println(template.opsForZSet().reverseRank("zset1","zset-2"));
  结果：[zset-2, zset-1, zset-3, zset-4, zset-5]
  4 //递减之后排到第五位去了
  ```

- Set<V> range(K key, long start, long end);
  通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().range("zset1",0,-1));
  结果：[zset-2, zset-1, zset-3, zset-4, zset-5]
  ```

- Set<TypedTuple<V>> rangeWithScores(K key, long start, long end);
  通过索引区间返回有序集合成指定区间内的成员对象，其中有序集成员按分数值递增(从小到大)顺序排列

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("zset1",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-2score:1.2
  value:zset-1score:2.2
  value:zset-3score:2.3
  value:zset-4score:6.6
  value:zset-5score:9.6
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> rangeByScore(K key, double min, double max);
  通过分数返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().rangeByScore("zset1",0,5));
  结果：[zset-2, zset-1, zset-3]
  ```

- Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max);
  通过分数返回有序集合指定区间内的成员对象，其中有序集成员按分数值递增(从小到大)顺序排列

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeByScoreWithScores("zset1",0,5);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-2score:1.2
  value:zset-1score:2.2
  value:zset-3score:2.3
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> rangeByScore(K key, double min, double max, long offset, long count);
  通过分数返回有序集合指定区间内的成员，并在索引范围内，其中有序集成员按分数值递增(从小到大)顺序排列

  ```
  使用：    System.out.println(template.opsForZSet().rangeByScore("zset1",0,5));
    System.out.println(template.opsForZSet().rangeByScore("zset1",0,5,1,2));
  结果：[zset-2, zset-1, zset-3]
  [zset-1, zset-3]
  ```

- Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max, long offset, long count);
  通过分数返回有序集合指定区间内的成员对象，并在索引范围内，其中有序集成员按分数值递增(从小到大)顺序排列

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeByScoreWithScores("zset1",0,5,1,2);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-1score:2.2
  value:zset-3score:2.3
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> reverseRange(K key, long start, long end);
  通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递减(从大到小)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().reverseRange("zset1",0,-1));
  结果：[zset-5, zset-4, zset-3, zset-1, zset-2]
  ```

- Set<TypedTuple<V>> reverseRangeWithScores(K key, long start, long end);
  通过索引区间返回有序集合成指定区间内的成员对象，其中有序集成员按分数值递减(从大到小)顺序排列

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().reverseRangeWithScores("zset1",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-5score:9.6
  value:zset-4score:6.6
  value:zset-3score:2.3
  value:zset-1score:2.2
  value:zset-2score:1.2
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Set<V> reverseRangeByScore(K key, double min, double max);

  ```
  使用：与rangeByScore调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列
  ```

- Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max);

  ```
  使用：与rangeByScoreWithScores调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列
  ```

- Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count);

  ```
  使用：与rangeByScore调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列
  ```

- Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max, long offset, long count);

  ```
  使用：与rangeByScoreWithScores调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列
  ```

- Long count(K key, double min, double max);
  通过分数返回有序集合指定区间内的成员个数

  ```
  使用：System.out.println(template.opsForZSet().rangeByScore("zset1",0,5));
        System.out.println(template.opsForZSet().count("zset1",0,5));
  结果：[zset-2, zset-1, zset-3]
  3
  ```

- Long size(K key);
  获取有序集合的成员数，内部调用的就是zCard方法

  ```
  使用：System.out.println(template.opsForZSet().size("zset1"));
  结果：6
  ```

- Long zCard(K key);
  获取有序集合的成员数

  ```
  使用：System.out.println(template.opsForZSet().zCard("zset1"));
  结果：6
  ```

- Double score(K key, Object o);
  获取指定成员的score值

  ```
  使用：System.out.println(template.opsForZSet().score("zset1","zset-1"));
  结果：2.2
  ```

- Long removeRange(K key, long start, long end);
  移除指定索引位置的成员，其中有序集成员按分数值递增(从小到大)顺序排列

  ```
  使用：System.out.println(template.opsForZSet().range("zset2",0,-1));
        System.out.println(template.opsForZSet().removeRange("zset2",1,2));
        System.out.println(template.opsForZSet().range("zset2",0,-1));
  结果：[zset-1, zset-2, zset-3, zset-4]
  2
  [zset-1, zset-4]
  ```

- Long removeRangeByScore(K key, double min, double max);
  根据指定的score值得范围来移除成员

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：//System.out.println(template.opsForZSet().add("zset2","zset-1",1.1));
        //System.out.println(template.opsForZSet().add("zset2","zset-2",1.2));
        //System.out.println(template.opsForZSet().add("zset2","zset-3",2.3));
        //System.out.println(template.opsForZSet().add("zset2","zset-4",6.6));
  System.out.println(template.opsForZSet().range("zset2",0,-1));
  System.out.println(template.opsForZSet().removeRangeByScore("zset2",2,3));
    System.out.println(template.opsForZSet().range("zset2",0,-1));
  结果：[zset-1, zset-2, zset-3,zset-4]
  1
  [zset-1, zset-2, zset-4]
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long unionAndStore(K key, K otherKey, K destKey);
  计算给定的一个有序集的并集，并存储在新的 destKey中，key相同的话会把score值相加

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForZSet().add("zzset1","zset-1",1.0));
        System.out.println(template.opsForZSet().add("zzset1","zset-2",2.0));
        System.out.println(template.opsForZSet().add("zzset1","zset-3",3.0));
        System.out.println(template.opsForZSet().add("zzset1","zset-4",6.0));

        System.out.println(template.opsForZSet().add("zzset2","zset-1",1.0));
        System.out.println(template.opsForZSet().add("zzset2","zset-2",2.0));
        System.out.println(template.opsForZSet().add("zzset2","zset-3",3.0));
        System.out.println(template.opsForZSet().add("zzset2","zset-4",6.0));
        System.out.println(template.opsForZSet().add("zzset2","zset-5",7.0));
        System.out.println(template.opsForZSet().unionAndStore("zzset1","zzset2","destZset11"));

        Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("destZset11",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-1score:2.0
  value:zset-2score:4.0
  value:zset-3score:6.0
  value:zset-5score:7.0
  value:zset-4score:12.0
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long unionAndStore(K key, Collection<K> otherKeys, K destKey);
  计算给定的多个有序集的并集，并存储在新的 destKey中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：//System.out.println(template.opsForZSet().add("zzset1","zset-1",1.0));
        //System.out.println(template.opsForZSet().add("zzset1","zset-2",2.0));
        //System.out.println(template.opsForZSet().add("zzset1","zset-3",3.0));
        //System.out.println(template.opsForZSet().add("zzset1","zset-4",6.0));
        //
        //System.out.println(template.opsForZSet().add("zzset2","zset-1",1.0));
        //System.out.println(template.opsForZSet().add("zzset2","zset-2",2.0));
        //System.out.println(template.opsForZSet().add("zzset2","zset-3",3.0));
        //System.out.println(template.opsForZSet().add("zzset2","zset-4",6.0));
        //System.out.println(template.opsForZSet().add("zzset2","zset-5",7.0));

        System.out.println(template.opsForZSet().add("zzset3","zset-1",1.0));
        System.out.println(template.opsForZSet().add("zzset3","zset-2",2.0));
        System.out.println(template.opsForZSet().add("zzset3","zset-3",3.0));
        System.out.println(template.opsForZSet().add("zzset3","zset-4",6.0));
        System.out.println(template.opsForZSet().add("zzset3","zset-5",7.0));

        List<String> stringList = new ArrayList<String>();
        stringList.add("zzset2");
        stringList.add("zzset3");
        System.out.println(template.opsForZSet().unionAndStore("zzset1",stringList,"destZset22"));

        Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("destZset22",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-1score:3.0
  value:zset-2score:6.0
  value:zset-3score:9.0
  value:zset-5score:14.0
  value:zset-4score:18.0
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long intersectAndStore(K key, K otherKey, K destKey);
  计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：System.out.println(template.opsForZSet().intersectAndStore("zzset1","zzset2","destZset33"));

        Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("destZset33",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-1score:2.0
  value:zset-2score:4.0
  value:zset-3score:6.0
  value:zset-4score:12.0
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Long intersectAndStore(K key, Collection<K> otherKeys, K destKey);
  计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：List<String> stringList = new ArrayList<String>();
        stringList.add("zzset2");
        stringList.add("zzset3");
        System.out.println(template.opsForZSet().intersectAndStore("zzset1",stringList,"destZset44"));

        Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("destZset44",0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
  结果：value:zset-1score:3.0
  value:zset-2score:6.0
  value:zset-3score:9.0
  value:zset-4score:18.0
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

- Cursor<TypedTuple<V>> scan(K key, ScanOptions options);
  遍历zset

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  ```
  使用：    Cursor<ZSetOperations.TypedTuple<Object>> cursor = template.opsForZSet().scan("zzset1", ScanOptions.NONE);
        while (cursor.hasNext()){
            ZSetOperations.TypedTuple<Object> item = cursor.next();
            System.out.println(item.getValue() + ":" + item.getScore());
        }
  结果：zset-1:1.0
  zset-2:2.0
  zset-3:3.0
  zset-4:6.0
  ```

  [![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

注：TimeUnit是java.util.concurrent包下面的一个类，表示给定单元粒度的时间段
常用的颗粒度

```java
TimeUnit.DAYS //天
TimeUnit.HOURS //小时
TimeUnit.MINUTES //分钟
TimeUnit.SECONDS //秒
TimeUnit.MILLISECONDS //毫秒
```



## testOpsForValue

看源码



~~~java
@Test
    public void testOpsForValue(){
        //设置String的value值
//        stringRedisTemplate.opsForValue().set("ooyhao","ouYang");
        ValueOperations<String, String> ops = 
                stringRedisTemplate.opsForValue();
        //在指定key值所对应的value上追加内容
//        ops.append("ooyhao","1996");
        //获取String的key所对应的value、
//        String name = ops.get("ooyhao");
        //设置值
//        ops.set("number","1");
//        在指定value值加1（++1）
//        ops.increment("number");
//        在指定value值减1（--1）
//        ops.decrement("number");
        //设置新的value，返回原来的value
//        String set = ops.getAndSet("ooyhao", "Hello Redis");
        ops.set("setRangeValue","123");
        //设置值，（在原有value的offset位置开始覆盖。（offset >= 0））
        ops.set("setRangeValue","123456",0);
        //删除指定key值。
        ops.getOperations().delete("setRangeValue");
        String value = ops.get("setRangeValue");
        System.out.println(value);
    }
~~~



## testOpsForHash

~~~java
@Test
    public void testOpsForHash(){
        HashOperations<String, Object, Object> ops
                = stringRedisTemplate.opsForHash();
//        Map<String,Object> map = new HashMap<>();
//        map.put("name","ouYang");
//        map.put("age","22");
        //一次存储一个map数据
//        ops.putAll("student",map);
        //将指定key的数据一次性取出
//        Map<Object, Object> student = ops.entries("student");
//        for(Map.Entry entry : student.entrySet()){
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
        //取出指定key下的指定hashKey的hashValue
//        String name = ops.get("student","name").toString();
//        System.out.println(name);
        //设置指定key下的指定hashKey的hashValue
//        ops.put("student","sex","男");
//        System.out.println(ops.get("student","sex"));
        //判断指定key的hashKey是否存在
//        System.out.println(ops.hasKey("student","stuNo"));
        //获得指定key下的所有hashKey
        Set<Object> studentKeys = ops.keys("student");
        System.out.println(studentKeys);
        //获得指定key下的所有hashValue
        List<Object> studentValues = ops.values("student");
        System.out.println(studentValues);
        //报错，由于sex不是一个integer（exception info : ERR hash value is not an integer）
//        ops.increment("student","sex",1);
//        ops.increment("student","age",1);
//        对指定key下的hashKey的hashValue进行增/减
//        ops.increment("student","age",-1);
//        System.out.println(ops.get("student","age"));
//        ops.put("student","testKey1","1");
//        ops.put("student","testKey2","2");
//        System.out.println(ops.get("student","testKey1"));
//        System.out.println(ops.get("student","testKey2"));
        //批量删除指定key下的hashKey
//        ops.delete("student","testKey1","testKey2");
        //获得指定key下hashKey的hashValue的长度
//        Long length = ops.lengthOfValue("student", "age");
//        System.out.println(ops.get("student","age"));
//        System.out.println(length);
//        List list = new ArrayList();
//        list.add("name");
//        list.add("age");
//        list.add("sex");
        //批量取出指定key的hashKeys的hashValues
//        List multiGet = ops.multiGet("student", list);
//        System.out.println(multiGet);
        //设置hashValue,如果指定key下的hashValue不存在。
//        System.out.println(ops.putIfAbsent("student","studentNo1","N007"));
//        System.out.println(ops.putIfAbsent("student","age","20"));
        //获得指定key下的hashKey-hashValue的个数
//        Long size = ops.size("student");
//        System.out.println(size);
    }
~~~



## testOpsForList

~~~java
@Test
    public void testOpsForList(){
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        //从右边压入一个value。
//        ops.rightPush("user","ouYang");
        //直译：设置。其实个人觉得是覆盖，即只能覆盖已经存在的索引（既不创建key，也不创建key下不存在的index的value）
//        ops.set("user",0,"ouYang1");
        //求大小
//        System.out.println(ops.size("user"));
        //从左边压入一个value
//        ops.leftPush("user","男");
        //获得指定范围的数据(集合)
//        List<String> user = ops.range("user", 0, -1);
//        System.out.println(user);//[ 男, ouYang1]
        //获得指定下标的值。类似：list.get(index)
//        String user = ops.index("user", 1);
        //返回的list的长度
//        Long user = ops.leftPush("user", "");
        //左边压入多个
//        ops.leftPushAll("user","1","2","3","");
        List<String> range = ops.range("stu", 0, -1);
//        //截取子集合，subList。改变本身
//        ops.trim("user",0,3);
//        range = ops.range("user", 0, -1);
        //从左开始，压入到第一个pivot值的左边。如果不存在，则不压入。
//        ops.leftPush("user","2","6");
        //如果key存在，则左边压入
//        Long user = ops.leftPushIfPresent("user1", "7");
        //从左边开始，删除指定个数的指定value，不足个数，则只是全部删除。
//        Long user = ops.remove("user", 3, "6");
//        List<String> range1 = ops.range("stu", 0, -1);
//        //取出sourceKey的右边一个value，压入到destinationKey的左边。
//        String s = ops.rightPopAndLeftPush("user", "stu");
//
//        range = ops.range("user", 0, -1);
//        range1 = ops.range("stu", 0, -1);
//        System.out.println(range);
//        System.out.println(range1);
        //java.lang.IllegalArgumentException: non null key required
//        ops.leftPush(null, "we");
//        List<String> aLong = ops.range("null", 0, -1);
//        System.out.println(aLong);

//        ops.leftPop("user",1000, TimeUnit.MILLISECONDS);
        ops.leftPop("stu",5, TimeUnit.SECONDS);
        range = ops.range("stu", 0, -1);
        System.out.println(range);
    }
~~~

## testOpsForSet

~~~java
@Test
    public void testOpsForSet(){
        SetOperations<String, String> ops = stringRedisTemplate.opsForSet();
        //添加数据，与java Set一样，不能重复
//        Long aLong = ops.add("customer", "A", "B", "C","D");
//        Long aLong1 = ops.add("boss", "A", "C", "E","F");
        Long aLong1 = ops.add("manager", "A", "C", "W","Y");
        //判断指定值是否是key的成员，即key中是否存在o
//        Boolean isMember = ops.isMember("customer","c1");
        //获得所有成员
        Set<String> customer = ops.members("customer");
        Set<String> boss = ops.members("boss");
        Set<String> manager = ops.members("manager");
        System.out.println("customer:"+customer);
        System.out.println("boss    :"+boss);
        System.out.println("manager :"+manager);
        //左边弹出一个成员
//        String pop = ops.pop("customer");
        //左边弹出count个成员
//        List<String> strings = ops.pop("customer", 2);
        //移除批量元素
//        ops.remove("customer","c1","b3","b2");

        //返回的是customer中与boss中不同的。
//        Set<String> stringSet = ops.difference("customer", "boss");
        //返回的是boss中与customer中不同的。
//        Set<String> stringSet = ops.difference("boss", "customer");

        List<String> list = new ArrayList<>();
        list.add("boss");
        list.add("manager");
        //与多个List相比，返回customer中不同于其他集合的值
//        Set<String> stringSet = ops.difference("customer", list);
        //先把customer-boss，剩余相差的元素在添加到boss中，会把之前的元素清除
//        Long aLong = ops.differenceAndStore("customer", "boss", "boss");
        //先把customer-list，剩下的元素添加到boss中，会把之前的元素清除
//        Long aLong = ops.differenceAndStore("customer", list, "boss");
        //随机从key集合中取出count个元素，并且不重复。
//        Set<String> strings = ops.distinctRandomMembers("customer", 2);
        //取出customer与manager的交集
//        Set<String> intersect = ops.intersect("customer", "manager");
        //customer与多个集合求交集
//        Set<String> intersect = ops.intersect("customer", list);
        //求customer与boss求交集，将结果存到boss中
//        ops.intersectAndStore("customer","boss","boss");
        //求customer与list求交集，将结果存到boss中
//        ops.intersectAndStore("customer",list,"boss");
        //随机取出一个元素
        //String randomMember = ops.randomMember("customer");
        //随机取出count元素,可重复
//        List<String> strings = ops.randomMembers("customer", 5);
        //求大小
//        Long size = ops.size("customer");
        //弹出一个元素，即移除并返回,并不一定按顺序
//        String pop = ops.pop("customer");
        //弹出count个元素，即移除并返回
//        List<String> pop = ops.pop("manager", 2);
        //求customer与manager并集
//        Set<String> union = ops.union("customer", "manager");
        //求customer与list中的key求并集
//        Set<String> union = ops.union("customer", list);
        //求customer与boss的交集，将结果存入到boss
//        ops.unionAndStore("customer","boss","boss");
        //求customer与list中key求交集，将结果存入到boss
//        ops.unionAndStore("customer",list,"boss");
//        System.out.println(union);

        customer = ops.members("customer");
        boss = ops.members("boss");
        manager = ops.members("manager");
        System.out.println("customer:"+customer);
        System.out.println("boss    :"+boss);
        System.out.println("manager :"+manager);

    }
~~~



## testOpsForZSet

~~~java
@Test
    public void testOpsForZSet(){

        ZSetOperations<String, String> ops = stringRedisTemplate.opsForZSet();

        //添加单个value
        ops.add("player","tom",2.1);
        ops.add("player","jack",3.3);
        ops.add("player","rose",1.3);
        ops.add("player","lily",1.5);
        ops.add("doctor","tom",2);
        ops.add("doctor","jack",3.2);
        ops.add("doctor","rose",1.3);
        ops.add("doctor","lily",1.5);
        ops.add("teacher","tom",2);
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        DefaultTypedTuple<String> typedTuple1 = new DefaultTypedTuple<>("jack",3.2);
        DefaultTypedTuple<String> typedTuple2 = new DefaultTypedTuple<>("rose",1.3);
        DefaultTypedTuple<String> typedTuple3 = new DefaultTypedTuple<>("lily",2.5);
        set.add(typedTuple1);
        set.add(typedTuple2);
        set.add(typedTuple3);
        //批量添加，具有分数的value
        ops.add("teacher",set);
        //统计分数在指定区间的value的个数
//        Long count = ops.count("teacher", 2, 4);
        //添加指定key的value的分数（权重）[正(加)/负(减)]
//        Double aDouble = ops.incrementScore("teacher", "tom", 0.3);

        List<String> list = new ArrayList<>();
        list.add("player");
        list.add("doctor");
        //与上面set一样(交集)
//        System.out.println(ops.range("doctor",0,-1));
        //交集，只与value比较，不使用score
//        Long aLong1 = ops.intersectAndStore("teacher", "player", "teacher");
//        Long aLong = ops.intersectAndStore("teacher", list, "doctor");

        //获得指定value的排名
//        System.out.println("rank:"+ops.rank("teacher","rose"));

//        ops.remove("teacher",values);//values可变参数
//        ops.removeRange("teacher",start,end);//通过下标删
//        ops.removeRangeByScore("teacher",min,max);//通过分数删

        //size与zCard一致
//        Long size = ops.size("teacher");
//        Long zCard = ops.zCard("teacher");
        //返回指定范围的value，带有分数
//        Set<ZSetOperations.TypedTuple<String>> tuples
//                = ops.rangeByScoreWithScores("teacher", 0, 10);

        //获取指定分数范围筛选后，从第offset个开始，返回count个，包含score
//        Set<ZSetOperations.TypedTuple<String>> tuples =
//                ops.rangeByScoreWithScores("teacher", 5, 10, 0, 2);
//        for(ZSetOperations.TypedTuple<String> typedTuple : tuples){
//            System.out.println(typedTuple.getValue()+"/"+typedTuple.getScore());
//        }

        Set<ZSetOperations.TypedTuple<String>> teacher
                = ops.rangeWithScores("player", 0, -1);
        for(ZSetOperations.TypedTuple<String> tuple : teacher){
            System.out.println(tuple.getValue()+"--"+tuple.getScore());
        }



        //将排名与对称位置的调换(原有元素不变)
//        Long aLong = ops.reverseRank("teacher", "tom");  // 1 -- 2
//        Long rose = ops.reverseRank("teacher", "rose");  // 0 -- 3
        //返回排名调换之后的set，原有元素排名不变，根据分数
//        Set<String> stringSet = ops.reverseRangeByScore("teacher", 1, 3);
        //返回排名调换之后的set，原有元素排名不变，根据索引
//        Set<String> stringSet = ops.reverseRange("teacher", 2, 4);
        //其他类似
        //求并集，其他类似set
        Long aLong = ops.unionAndStore("teacher", "player", "player");
        System.out.println(aLong);
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        //首先使用分数进行排序。
        //使用gt,gte,lt,lte,是按照字母顺序排。
//        range.gt("jack"); //>
//        range.gte("j");
//        range.lt("t");
//        range.lte("tom");
        Set<String> strings = ops.rangeByLex("player", range);
        System.out.println(strings);

        //返回指定分数范围的value值。不包含score
//        Set<String> stringSet = ops.rangeByScore("teacher", 2, 4);

        //获取指定范围的value
//        Set<String> teacher = ops.range("doctor", 0, -1);
    }
~~~

