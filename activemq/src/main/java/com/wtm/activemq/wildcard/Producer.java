package com.wtm.activemq.wildcard;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
Wildcards 用来支持联合的名字分层体系（federatednamehierarchies）。它不是JMS 规范的一部分，而是ActiveMQ 的扩展。ActiveMQ 支持以下三种
wildcards：
• "." 用于作为路径上名字间的分隔符。
• "*" 用于匹配路径上的任何名字。
• ">" 用于递归地匹配任何以这个名字开始的destination。
*：匹配当前层的内容
>：任何一层的都能匹配
 */
public class Producer {

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
            destination = session.createTopic("aa.bb.cc.ff");
            messageProducer = session.createProducer(destination);
            for(int i=0;i<SENDNUM;i++){
                String msg = "aa.bb.cc.ff"+i+" "+System.currentTimeMillis();
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
