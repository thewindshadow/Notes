HelloWorld



![](RabbitMQ/RabbitMQ.png)



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



1、发送端操作流程

   1）创建连接
   2）创建通道
   3）声明队列
   4）发送消息
2、接收端
   1）创建连接
   2）创建通道
   3）声明队列
   4）监听队列
   5）接收消息
   6）ack回复







生产者

~~~java
package com.ooyhao.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : 阳浩
 * @Date : 2018/12/10
 * @Time : 15:09
 * @Description:
 */
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



消费者

~~~java
package com.ooyhao.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author : 阳浩
 * @Date : 2018/12/10
 * @Time : 15:33
 * @Description:
 */
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





RabbitMQ有以下几种工作模式 ：
1、Work queues
2、Publish/Subscribe
3、Routing
4、Topics
5、Header
6、RPC



1.

work queues与入门程序相比，多了一个消费端，两个消费端共同消费同一个队列中的消息。
应用场景：对于 任务过重或任务较多情况使用工作队列可以提高任务处理的速度。
测试：
1、使用入门程序，启动多个消费者。
2、生产者发送多个消息。
结果：
1、一条消息只会被一个消费者接收；
2、rabbit采用轮询的方式将消息是平均发送给消费者的；
3、消费者在处理完某条消息后，才会收到下一条消息。



2.

发布订阅模式：
1、每个消费者监听自己的队列。
2、生产者将消息发给broker，由交换机将消息转发到绑定此交换机的每个队列，每个绑定交换机的队列都将接收
到消息

**案例：**
用户通知，当用户充值成功或转账完成系统通知用户，通知方式有短信、邮件多种方法 。

**1、生产者**
声明Exchange_fanout_inform交换机。
声明两个队列并且绑定到此交换机，绑定时不需要指定routingkey
发送消息时不需要指定routingkey







