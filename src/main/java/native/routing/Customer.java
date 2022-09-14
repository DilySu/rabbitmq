package routing;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * Date: 2022-08-11 星期四
 * Time: 10:33
 * Author: Dily_Su
 * Remark: 消费者
 *
 */
public class Customer {

    Runnable runnable = () -> {

        // 1、创建连接工程  // ip port
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            // 2、创建连接Connection
            connection = connectionFactory.newConnection("消费者");

            // 3、获取连接通道Channel
            channel = connection.createChannel();

            // 获取 队列名称
            String queueName = Thread.currentThread().getName();

            // 5、通过通道创建交换机, 声明队列, 绑定关系, 路由key, 发送消息和接收消息
            for (int i = 0; i < 20; i++) {
                channel.basicConsume(queueName, true, (s, message) -> System.out.println(queueName+"接收到的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8)), s -> System.out.println("消息接收失败了"));
            }
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 7、关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static void main(String[] args) {
        new Thread(new Customer().runnable,"queueDemo1").start();
        new Thread(new Customer().runnable,"queueDemo2").start();
        new Thread(new Customer().runnable,"queueDemo3").start();

    }


}
