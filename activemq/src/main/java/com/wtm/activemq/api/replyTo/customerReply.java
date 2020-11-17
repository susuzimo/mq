package com.wtm.activemq.api.replyTo;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class customerReply {
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
        MessageConsumer consumer;
        MessageProducer messageProducer;
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            //是否开启事务,自动确认
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("request-response");
            consumer = session.createConsumer(destination);
            Message message;
            while ((message=consumer.receive())!=null){
                System.out.println(message.getJMSReplyTo()+"reply-to");
                //如果reply不为空需要应答
                if(message.getJMSReplyTo()!=null){
                    //根据创建的临时队列
                    destination = session.createQueue(message.getJMSReplyTo().toString().substring(8));
                    System.out.println(message.getJMSReplyTo().toString().substring(8)+"------");
                    messageProducer = session.createProducer(destination);
                    String msg = "回应消息"+message.getJMSCorrelationID();
                    TextMessage replyMessage = session.createTextMessage(msg);
                    messageProducer.send(replyMessage);
                    System.out.println(msg);
                }
                System.out.println(message);
            }
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
