package com.wtm.activemq.subscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
需要设置客户端id：connection.setClientID("Mark");   33
消息的destination变为 Topic   38
消费者类型变为TopicSubscriber  26
消费者创建时变为session.createDurableSubscriber(destination,"任意名字，代表订阅名 ");  40
 */
public class customerA {
    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接
        Session session;//会话 接受或者发送消息的线程
        TopicSubscriber messageConsumer;//消息的消费者
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(customerA.USERNAME,
                customerA.PASSWORD, customerA.BROKEURL);
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            connection.setClientID("mark");
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("DurableTopic1");
            //创建消息消费者
            messageConsumer = session.createDurableSubscriber(destination,"xiangxue");
            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        System.out.println("Accept msg : "
                                +((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
