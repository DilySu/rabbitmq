package simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Date: 2022-08-11 星期四
 * Time: 10:33
 * Author: Dily_Su
 * Remark: 生产者
 * <p>
 * 所有的中间件技术都是基于 tcp/ip 协议基础上构建的新型协议规范, 只不过 rabbitmq 遵循的是 amqp
 */
public class Producer {

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
            connection = connectionFactory.newConnection("生产者");

            // 3、获取连接通道Channel
            channel = connection.createChannel();

            // 4、通过通道创建交换机, 声明队列, 绑定关系, 路由key, 发送消息和接收消息
            String queueName = "queueDemo";
            /**
             * @params1 队列名称
             * @params2 是否要持久化，所谓之持久化消息是否存盘，非持久化消息是否存盘？
             * @params3 排他性，是否独占独立
             * @params4 是否自动删除，随着最后一个消息消费后是否把队列自动删除
             * @params5 携带附属参数
             */
            channel.queueDeclare(queueName, false, false, false, null);

            // 5、准备消息内容
            String msg = "hello";

            // 6、发送消息给队列queue
            /**
             * @params1 交换机
             * @params2 消息队列、路由key
             * @params3 消息的状态控制
             * @params4 消息内容
             */
            channel.basicPublish("", queueName, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7、关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 8、关闭连接
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