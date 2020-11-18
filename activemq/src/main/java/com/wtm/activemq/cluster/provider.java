package com.wtm.activemq.cluster;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
需求：
1. 消息接收方和发送方都是集群。 
2. 同一个消息的接收方可能有多个集群进行消息的处理。
3. 不同集群对于同一条消息的处理不能相互干扰

解决方法
虚拟主题
    生产者destination = session.createTopic("VirtualTopic.vtgroup");    VirtualTopic.name
    消费者destination = session.createQueue("Consumer.A.VirtualTopic.vtgroup");Consumer.name.VirtualTopic.productName
    customA1和customA2因为一起监听Consumer.A.VirtualTopic.vtgroup  所有平分消息
    customB只要自己监听Consumer.B.VirtualTopic.vtgroup所以收到全部的消息
组合Destinations
 */
public class provider {
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
    private static final int SENDNUM = 10;

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
            destination = session.createTopic("VirtualTopic.vtgroup");
            messageProducer = session.createProducer(destination);
            for(int i=0;i<SENDNUM;i++){
                String msg = "vtgroup "+i+" "+System.currentTimeMillis();
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
