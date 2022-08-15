package work.fair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Date: 2022-08-11 星期四
 * Time: 10:33
 * Author: Dily_Su
 * Remark: 消费者
 */
public class Work1 {
    public static void main(String[] args) {


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

            // 4、通过通道创建交换机, 声明队列, 绑定关系, 路由key, 发送消息和接收消息
            channel.basicQos(1); // 设置指标，规定一次取出多少消息
            Channel finalChannel = channel;
            channel.basicConsume(
                    "queueDemo",
                    false, // autoAck  关闭自动应答,进行手动应答
                    (s, message) -> {
                        System.out.println("Work1-接收到的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
                        // 手动应答
                        finalChannel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    },
                    s -> System.out.println("消息接收失败了")
            );
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 6、关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
