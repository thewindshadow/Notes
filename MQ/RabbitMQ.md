## MQ模型



![](RabbitMQ/RabbitMQ.png)





### 2.收发信息的步骤

~~~xml
生产者：
1.创建连接工厂
ConnectionFactory factory = new ConnectionFactory();
2.通过工厂对象创建连接
Connection connection = factory.newConnection();
3.通过连接对象创建通道
Channel channel = connection.createChannel();
	3.1队列声明
	3.2消息发布

消费者：
1.创建连接工厂
ConnectionFactory factory = new ConnectionFactory();
2.通过工厂对象创建连接
Connection connection = factory.newConnection();
3.通过连接对象创建通道
Channel channel = connection.createChannel();
	3.1队列声明
	3.2创建消费方法
        DefaultConsumer consumer = new DefaultComsumer(channel){
			@Override
			handleDelivery();
        };
	3.3进行监听
~~~

#### 1、发送端操作流程

- 1）创建连接
- 2）创建通道
- 3）声明队列
- 4）发送消息

#### 2、接收端

- 1）创建连接
- 2）创建通道
- 3）声明队列
- 4）监听队列
- 5）接收消息
- 6）ack回复



### 3.简单队列

#### 1.模型 

![](RabbitMQ/1.png)



#### 2.获取连接的工具类

~~~java
public class ConnectionUtils {
    /**
     * 服务器地址
     */
    public static final String HOST = "127.0.0.1";

    /**
     * 服务器端口 AMQP
     */
    public static final Integer PORT = 5672;

    /**
     * 用户名
     */
    public static final String USERNAME = "guest";

    /**
     * 密码
     */
    public static final String PASSWORD = "guest";

    /**
     * 主机访问地址
     */
    public static final String VIRTUALHOST = "/";

    public static Connection getConnection(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUALHOST);

        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
~~~



#### 3.生产者生产消息

~~~java
public class Producer01 {
    //对列名称
    private static final String QUEUE = "helloworld";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            //rabbitmq 默认虚拟机名称为"/"，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost("/");
            //创建与RabbitMQ服务的TCP连接
            connection = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每一个通道代表一个会话任务。
            channel = connection.createChannel();

            /**
             * 声明队列，如果rabbit中没有此队列将自动创建
             * param1: 队列名称
             * param2: 是否持久化
             * param3: 队列是否独占此连接
             * param4: 队列不再使用时是否自动删除此队列
             * param5: 队列参数
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            String message = "helloworld小明："+System.currentTimeMillis();
            /**
             * 消息发布方法
             * param1: Exchange的名称，如果没有指定名称，则使用 Default Exchange
             * param2: routingKey，消息的路由Key，用于Exchange（交换机）将消息转发到指定的消息队列
             * param3: 消息包含的属性
             * param4: 消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认的交换机，每个队列也会绑定那个默认的交换机，
             *	但是不能
             * 绑定显示或是解除绑定
             * 默认的交换机，routingKey等于队列名称
             */
            channel.basicPublish("",QUEUE,null,
                    message.getBytes());
            System.out.println("Send Message is："+message);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (channel != null){
                channel.close();
            }
            if (connection != null){
                connection.close();
            }
        }
    }
}
~~~



#### 4.消费者消费消息

~~~java
public class Consumer01 {
    private static final String QUEUE = "helloworld";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ所在服务器的ip和端口
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE,true,false,false,null);
        //定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * 消费者接受消息调用此方法
             * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
             * @param envelope    消息包的内容，可以从中获取消息id，消息的routingKey，交换机，消息
             *                      和重传标志（收到消息失败后是否需要重新发送）
             * @param properties
             * @param body
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String message = new String(body,"utf-8");
                //是否重传
                boolean isRedeliver = envelope.isRedeliver();
                System.out.println("exchange:"+exchange);
                System.out.println("routingKey:"+routingKey);
                System.out.println("deliveryTag:"+deliveryTag);
                System.out.println("isRedeliver:"+isRedeliver);
                System.out.println("message:"+message);
            }
        };

        /**
         * 监听消息String queue，boolean autoAck，Consumer callback
         * 参数明细：
         * 1.队列名称
         * 2.是否自动回复，设置为true表示消息接收到自动向mq回复接收到了，
         *		mq接收到回复消息会删除消息，设置为false则需要手动回复
         * 3.消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE,true,consumer);
    }
}
~~~

#### 5.简单队列的不足

耦合性高，生产者一一对应消费者（如何我想要有多个消费者消费队列中消息，这时候就不行了），队列名变更，这时候得同事变更。



### 4.work  queues 工作队列模式

#### 1.模型

![](RabbitMQ/python-two.png)

 为什么会出现工作队列

simple队列是一一对应的，而且实际开发，生产者发送消息是毫不费力的，而消费者一般是要跟业务相结合的，消费者接受到消息之后就需要处理，可能需要花费时间，这时候队列就会积压了很多消息



#### 2.生产者

~~~java
public class Send {
    /**
     *               |-- C1
     * p -- Queue -- |
     *               |-- C2
     */
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        Channel channel = connection.createChannel();
        //声明Queue(String queueName, Boolean durable, Boolean exclusive, Boolean autoDelete, Map<String, Object> arguments)
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        for (int i = 0; i < 50; i++) {
            String message = "MQ "+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("send message: "+message);
            Thread.sleep(i*20);
        }
        channel.close();
        connection.close();
    }
}
~~~

#### 3.消费者1

~~~java
public class Receive1 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("[1] get message :"+ new String(body,"utf-8"));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[1] over");
                }
            }
        };

        boolean autoAck = true;
        //监听消息String queue，boolean autoAck，Consumer callback
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
~~~



#### 4.消费者2

~~~java
public class Receive2 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("[2] get message :"+ new String(body,"utf-8"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[2] over");
                }
            }
        };

        boolean autoAck = true;
        //监听消息String queue，boolean autoAck，Consumer callback
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
~~~



#### 5.现象

消费者1和消费者2处理的消息是一样的。

消费者1：偶数

消费者2：奇数

这种方式叫做**轮询分发**（roun-robin）结果就是：不管谁忙活着谁清闲 都不会多给一个消息任务

任务消息总是平均分配。（你一个我一个）



### 5.公平分发 fair depatch

#### 1.说明

使用公平分发，必须关闭自动应答ack,改为手动

#### 2.生产者

~~~java
public class Send {
    /**
     *               |-- C1
     * p -- Queue -- |
     *               |-- C2
     */
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        Channel channel = connection.createChannel();
        //声明Queue(String queueName, Boolean durable, Boolean exclusive, Boolean autoDelete, Map<String, Object> arguments)
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        /**
         * 每个消费者 发送确认消息消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         *
         * 限制发送给同一个消费者不得超过一条消息
         */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        for (int i = 0; i < 50; i++) {
            String message = "MQ "+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("send message: "+message);
            Thread.sleep(i*20);
        }
        channel.close();
        connection.close();
    }
}
~~~

#### 3.消费者1

~~~java
public class Receive1 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("[1] get message :"+ new String(body,"utf-8"));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动回执应答
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("[1] over");
                }
            }
        };
        boolean autoAck = false;//非自动应答
        //监听消息String queue，boolean autoAck，Consumer callback
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
~~~

#### 4.消费者2

~~~java
public class Receive2 {
    public static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("[2] get message :"+ new String(body,"utf-8"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动回执应答
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("[2] over");
                }
            }
        };
        boolean autoAck = false;
        //监听消息String queue，boolean autoAck，Consumer callback
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);
    }
}
~~~

#### 5.与work queues的差别之处

##### 5.1 生产者

![](RabbitMQ/workQueue&workFairs差别0.png)



##### 5.2 消费者

![](RabbitMQ/workQueue&workFairs差别.png)



#### 6.现象

消费者2处理的消息比消费者1多（能者多劳，公平分发）





### 6.消息应答 与 消息持久化

#### 6.1消息应答

自动确认模式：

​	RabbitMQ一旦将消息分发非消费之后，就会从内存中删除这个消息

现象：

​	这种情况下，如果杀死（kill）当前正在执行的消费者，就会丢失正在执行的消息。

```java
boolean autoAck = true;//自动应答
channel.basicConsume(QUEUE_NAME,autoAck,consumer);
```



手动确认模式：

~~~java
boolean autoAck = false;//非自动应答
//监听消息String queue，boolean autoAck，Consumer callback
channel.basicConsume(QUEUE_NAME,autoAck,consumer);
~~~

如果有一个消费者挂掉，就会交付给其他消费者。RabbitMQ支持消息应答，消费者发送一个消息应答，告诉RabbitMQ这个消息我已经处理完成，RabbitMQ可以将这个消息从**内存**中删除了。

(message acknowledgment)消息应答模式（Ack）是打开的， false。

如果RabbitMQ挂了，消息仍然会丢失。



#### 6.2消息持久化

```java
/**
 * 参数：
 *  queue:队列的名称
 *  durable:能否持久化
 *  exclusive:是否独占连接
 *  autoDelete:是否自动删除
 *  arguments:参数
 */
boolean durable = true;
channel.queueDeclare(QUEUE_NAME,durable,false,false,null);

```

我们将程序中的boolean durable = true; 改为false是不可以的，会报错。尽管代码是正确的，但是该队列应该声明定义好了，就不可以再进行修改了。（RabbitMQ不允许用不同的参数重新定义一个已经存在的队列（可以先删除再创建））

### 7.Exchange(交换机，转发器)

一方面是接收生产者的消息，另一方面是向队列推送消息。

匿名转发：“”;

#### 7.1 Fanout Exchange(不处理路由键)

![](RabbitMQ/fanout_exchange_1.png)

只需要将生产者与exchange进行bind，就会把exchange中的信息转发到与exchange绑定的所有Queue中。

~~~xml
任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上。

1.可以理解为路由表的模式

2.这种模式不需要RouteKey

3.这种模式需要提前将Exchange与Queue进行绑定，一个Exchange可以绑定多个Queue，一个Queue可以同多个Exchange进行绑定。

4.如果接受到消息的Exchange没有与任何Queue绑定，则消息会被抛弃。
~~~



#### 7.2 Direct Exchange	 处理路径键

![](RabbitMQ/direct_exchange.png)

~~~xml
任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。

1.一般情况可以使用rabbitMQ自带的Exchange：”"(该Exchange的名字为空字符串，下文称其为default Exchange)。

2.这种模式下不需要将Exchange进行任何绑定(binding)操作

3.消息传递时需要一个“RouteKey”，可以简单的理解为要发送到的队列名字。

4.如果vhost中不存在RouteKey中指定的队列名，则该消息会被抛弃。
~~~



#### 7.3	Topic Exchange 

![](RabbitMQ/topic_exchange.png)



~~~xml
任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上

1.这种模式较为复杂，简单来说，就是每个队列都有其关心的主题，所有的消息都带有一个“标题”(RouteKey)，Exchange会将消息转发到所有关注主题能与RouteKey模糊匹配的队列。

2.这种模式需要RouteKey，也许要提前绑定Exchange与Queue。

3.在进行绑定时，要提供一个该队列关心的主题，如“#.log.#”表示该队列关心所有涉及log的消息(一个RouteKey为”MQ.log.error”的消息会被转发到该队列)。

4.“#”表示0个或若干个关键字，“*”表示一个关键字。如“log.*”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配。

5.同样，如果Exchange没有发现能够与RouteKey匹配的Queue，则会抛弃此消息。
~~~



**性能排序：fanout > direct >> topic。比例大约为11：10：6**


### 8.订阅模式Publish/Subscribe(fanout)

#### 1.模型

![](RabbitMQ/python-three.png)



#### 2.解读

1. 一个生产者，多个消费者。
2. 每一个消费者都有自己对应的队列。
3. 生产者没有直接把消息发送到队列 而是发送到了交换机 （转发器exchange）
4. 每个队列都要绑定到交换机上
5. 生产者发送的消息经过交换机到达队列 ，就能实现一个消息就可以被多个消费者消费。

~~~xml
发布订阅模式：
1、每个消费者监听自己的队列。
2、生产者将消息发给broker，由交换机将消息转发到绑定此交换机的每个队列，每个绑定交换机的队列都将接收
到消息
~~~



注册 --> 邮件 --> 短信

#### 3.发送者

~~~java
public class Send {
    public static final String QUEUE_NAME = "test_queue_fanout";
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        //通过工具类获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        Channel channel = connection.createChannel();
        /**
         * 声明交换机
         * 参数：
         * exchange:exchange的名字
         * type:exchange的类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//分发
        //发送消息
        String message = "hello publish/subscribe";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
        System.out.println("send message : "+message);
        channel.close();
        connection.close();
    }
}
~~~



#### 4.exchange图示

![](RabbitMQ/fanout_exchange.png)

**消息去哪里了？？**丢失了，因为交换机没有存储的能力，在RabbitMQ里面只有队列有存储能力。因为此时没有把交换机和相应的队列进行绑定，所以数据就丢失了。



#### 5.发送者

~~~java
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {

        //通过工具类获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        Channel channel = connection.createChannel();
        //声明交换机
        /**
         * 参数：
         * exchange:exchange的名字
         * type:exchange的类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//分发
        //发送消息
        String message = "hello publish/subscribe";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
        System.out.println("send message : "+message);
        channel.close();
        connection.close();
    }
}
~~~



#### 6.消费者1

~~~java
public class Receive1 {
    public static final String QUEUE_NAME = "test_queue_fanout_email";
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[1] receive message:"+new String(body,"utf-8"));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[1] over");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~

#### 7.消费者2

~~~java
public class Receive2 {
    public static final String QUEUE_NAME = "test_queue_fanout_sms";
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[2] receive message:"+new String(body,"utf-8"));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[2] over");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~



#### 8.管理界面

![](RabbitMQ/fanout_exchange_bind.png)







### 9.Routing(direct)

#### 1.路由模型

![](RabbitMQ/routing.png)



#### 2.生产者

~~~java
public class Send {
    //exchange name
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    //routing key
    public static final String ROUTING_KEY = "error";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得连接
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //消息
        String message = "hello direct!"+ROUTING_KEY;
        //发布消息
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,null,message.getBytes());
        System.out.println("send message:"+message);
        //资源释放
        channel.close();
        connection.close();
    }
}
~~~



#### 3.消费者1

~~~java
public class Receive1 {
    //queue name
    public static final String QUEUE_NAME = "test_queue_direct_1";
    //exchange name
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    //routing key
    public static final String ROUTING_KEY = "error";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得Connection
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        DefaultConsumer consumer = new MyDefaultConsumer(channel,"1");
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~



#### 4.消费者2

~~~java
public class Receive2 {
    //queue name
    public static final String QUEUE_NAME = "test_queue_direct_2";
    //exchange name
    public static final String EXCHANGE_NAME = "test_exchange_direct";
    //routing key
    public static final String ROUTING_KEY1 = "info";
    public static final String ROUTING_KEY2 = "error";
    public static final String ROUTING_KEY3 = "warning";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得Connection
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY1);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY2);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY3);
        DefaultConsumer consumer = new MyDefaultConsumer(channel,"2");
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~



5.MyDefaultConsumer

~~~java
public class MyDefaultConsumer extends DefaultConsumer {
    private Channel channel = null;
    private String name = "";
    public MyDefaultConsumer(Channel channel,String name) {
        super(channel);
        this.channel = channel;
        this.name = name;
    }
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println(name+" receive message: "+new String(body,"utf-8"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(name+" over");
            channel.basicAck(envelope.getDeliveryTag(),false);
        }
    }
}
~~~



#### 5.Exchanges图示

![](RabbitMQ/exchanges_direct.png)



### 10.Topic(topic)

#### 1.模型

![](RabbitMQ/exchange_topic.png)

~~~xml
.“#”表示0个或若干个关键字，“”表示一个关键字。如“log.”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配。

Goods.insert |
Goods.update | ==> Goods.# 
Goods.delete |

~~~



#### 2.生产者

~~~java
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException {
    //获得连接
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String message = "商品。。。";
        //发布消息
        channel.basicPublish(EXCHANGE_NAME,"goods.delete",null,message.getBytes());
        System.out.println("topic send message:"+message);
        channel.close();
        connection.close();
    }
}
~~~



#### 3.消费者1

~~~java
public class Receive1 {
    //queue name
    public static final String QUEUE_NAME = "test_queue_topic_1";
    //exchange name
    public static final String EXCHANGE_NAME = "test_exchange_topic";
    //routing key
    public static final String ROUTING_KEY = "goods.add";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得Connection
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        DefaultConsumer consumer = new MyDefaultConsumer(channel,"topic1");
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~



#### 4.消费者2

~~~java
public class Receive2 {
    //queue name
    public static final String QUEUE_NAME = "test_queue_topic_2";
    //exchange name
    public static final String EXCHANGE_NAME = "test_exchange_topic";
    //routing key
    public static final String ROUTING_KEY = "goods.#";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得Connection
        Connection connection = ConnectionUtils.getConnection();
        //创建Channel
        final Channel channel = connection.createChannel();
        //声明Queue
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.basicQos(1);
        //绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        DefaultConsumer consumer = new MyDefaultConsumer(channel,"topic2");
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
~~~



### 11.RabbitMQ的消息确认机制











