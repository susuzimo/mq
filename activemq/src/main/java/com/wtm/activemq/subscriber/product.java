package com.wtm.activemq.subscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
普通模式
分别运行订阅模式和P2P模式，可以发现，P2P模式缺省把消息进行持久化，而topic模式是没有的
1、	启动两个消费者，启动一个生产者，发送消息，两个消费者都可以收到。
2、	关闭一个消费者，生产者发送消息，活跃的消费者可以收到消息，启动被关闭的消费者，无法收到消息。
3、	关闭所有消费者，生产者发送消息，在ActiveMQ控制台可以看见消息已被接收，关闭再启动ActiveMQ，启动消费者收不到消息。

持久化模式
1、	运行生产者，发布消息，多个消费者可以正常收到。
2、	关闭一个消费者，运行生产者，发布消息后再启动被关闭的消费者，可以收到离线后的消息；
3、	关闭所有消费者，运行生产者，发布消息后，关闭ActiveMQ再启动，启动所有消费者，都可以收到消息。

messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
修改messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
来设置消息本身的持久化属性为非持久化。重复上述实验，可以发现，第1,2点保持不变，
但是第三点，当关闭ActiveMQ再启动，消费者关闭后再启动，是收不到消息的。
说明，即使进行了持久订阅，但是消息本身如果是不持久化的，ActiveMQ关闭再启动，
这些非持久化的消息会丢失，进行持久订阅的消费者也是收不到自身离线期间的消息的。
 */
public class product {
    //默认连接用户名
    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int SENDNUM = 3;
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        connectionFactory
                = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("DurableTopic1");
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            for(int i=0;i<SENDNUM;i++){
                String msg = "发送消息"+i+" "+System.currentTimeMillis();
                TextMessage message = session.createTextMessage(msg);
                System.out.println("发送消息:"+msg);
                messageProducer.send(message);
            }
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
