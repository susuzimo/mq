package com.wtm.activemq.reliability;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
生产端
send方法
在生产者端，我们会使用send()方法向ActiveMQ发送消息，默认情况下，持久化消息以同步方式发送，send()方法会被阻塞，直到broker发送一个确认消息给生产者，这个确认消息表示broker已经成功接收到消息，并且持久化消息已经把消息保存到二级存储中。

事务消息
事务中消息（无论是否持久化），会进行异步发送，send()方法不会被阻塞。但是commit 方法会被阻塞，直到收到确认消息，表示broker已经成功接收到消息，并且持久化消息已经把消息保存到二级存储中。

总结
非持久化又不在事务中的消息，可能会有消息的丢失。为保证消息可以被ActiveMQ收到，我们应该采用事务消息或持久化消息。
 */
public class product {

    /*默认连接用户名*/
    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;
    /* 默认连接密码*/
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;
    /* 默认连接地址*/
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final int SENDNUM = 4;
    public static void main(String[] args) {
        /* 连接工厂*/
        ConnectionFactory connectionFactory;
        /* 连接*/
        Connection connection = null;
        /* 会话*/
        Session session;
        /* 消息的目的地*/
        Destination destination;
        /* 消息的生产者*/
        MessageProducer messageProducer;
        /* 实例化连接工厂*/
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
                BROKEURL);
        try {
            /* 通过连接工厂获取连接*/
            connection = connectionFactory.createConnection();
            /* 启动连接*/
            connection.start();
            /* 创建session
             * 第一个参数表示是否使用事务，第二次参数表示是否自动确认*/
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            /* 创建一个消息队列*/
            //topic
            destination = session.createQueue("MsgReliability");
            /* 创建消息生产者*/
            messageProducer = session.createProducer(destination);
            /* 循环发送消息*/
            for (int i = 0; i < SENDNUM; i++) {
                String msg = "发送消息" + i + " " + System.currentTimeMillis();
                TextMessage textMessage = session.createTextMessage(msg);
                System.out.println("标准用法:" + msg);
                messageProducer.send(textMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
