package com.wtm.activemq.deadQueue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
/*
死信队列
重试策略属于ActiveMQ的部分，所以有部分connectionFactory，connection的声明等等不能使用接口，必须使用ActiveMQ的实现
 active.xml
 <destinationPolicy>
      <policyMap>
          <policyEntries>
              <policyEntry topic=">" producerFlowControl="true">
                  <deadLetterStrategy>
			          <individualDeadLetterStrategy   queuePrefix="DLQ." useQueueForQueueMessages="true" />
			        </deadLetterStrategy>
               <pendingMessageLimitStrategy>
                    <constantPendingMessageLimitStrategy limit="1000"/>
               </pendingMessageLimitStrategy>
               </policyEntry>
                <policyEntry queue=">" producerFlowControl="true" memoryLimit="1mb">
                 	 <deadLetterStrategy>
		            	<individualDeadLetterStrategy   queuePrefix="DLQ." useQueueForQueueMessages="true" />
		          	</deadLetterStrategy>
               </policyEntry>
           </policyEntries>
        </policyMap>
 </destinationPolicy>
 */

public class product {
    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int SENDNUM = 1;
    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory;
        ActiveMQConnection connection = null;
        Session session;
        ActiveMQDestination destination;
        MessageProducer messageProducer;
        connectionFactory
                = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
        try {
            connection = (ActiveMQConnection) connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination = (ActiveMQDestination) session.createQueue("TestDlq2");
            //destination = (ActiveMQDestination) session.createTopic("TestDlq1");
            messageProducer = session.createProducer(destination);
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
