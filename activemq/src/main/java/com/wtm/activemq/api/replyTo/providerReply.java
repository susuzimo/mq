package com.wtm.activemq.api.replyTo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.jms.*;

public class providerReply {
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
    public static void main(String[] args) throws InterruptedException {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection = null;
        //会话
        Session session;
        //目的地
        Destination destination;
        //生产者
        MessageProducer messageProducer;
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            /* 第一个参数表示是否使用事务，第二次参数表示是否自动确认*/
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("request-response");
            messageProducer = session.createProducer(destination);
            String msg = "发送消息"+System.currentTimeMillis();
            TextMessage message = session.createTextMessage(msg);
            System.out.println("发送消息:"+msg);

            //配置消费者应答
            Destination temporaryQueue = session.createQueue("response");
            //MessageConsumer consumer = session.createConsumer(temporaryQueue);
            message.setJMSReplyTo(temporaryQueue);
            message.setJMSCorrelationID(System.currentTimeMillis()+"");
            messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
