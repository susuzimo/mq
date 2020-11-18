package com.wtm.activemq.wildcard;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class ProducerB {

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
            //destination = session.createQueue("aa.bb.cc.dd");
            destination = session.createTopic("aa.bb.cc.dd");
            messageProducer = session.createProducer(destination);
            for(int i=0;i<SENDNUM;i++){
                String msg = "aa.bb.cc.dd:"+i+" "+System.currentTimeMillis();
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
