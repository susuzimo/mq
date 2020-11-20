package com.wtm.activemq.delayQueue.service.mq;


import com.google.gson.Gson;
import com.wtm.activemq.delayQueue.model.OrderExp;
import com.wtm.activemq.delayQueue.service.delay.IDelayOrder;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 功能
 * 性能
 * 可用性
 * 伸缩性
 * 扩展性
 *类说明：消息队列的实现
 *  *
 修改配置文件(activemq.xml)，增加延迟和定时投递支持
 <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true">
 需要把几个描述消息定时调度方式的参数作为属性添加到消息，broker端的调度器就会按照我们想要的行为去处理消息。

 */
@Service
@Qualifier("mq")
public class MqProducer implements IDelayOrder {
	
	private Logger logger = LoggerFactory.getLogger(MqProducer.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;	
    
    /**
     *类说明：创建消息的类
     */
    private static class CreateMessage implements MessageCreator{
    	
    	private OrderExp order;
    	private long expireTime;
    	
		public CreateMessage(OrderExp order, long expireTime) {
			super();
			this.order = order;
			this.expireTime = expireTime;
		}

		public Message createMessage(Session session) throws JMSException {
			Gson gson = new Gson();
			String txtMsg = gson.toJson(order);
			Message message = session.createTextMessage(txtMsg);
			/*
			1：AMQ_SCHEDULED_DELAY ：延迟投递的时间
			2：AMQ_SCHEDULED_PERIOD ：重复投递的时间间隔
			3：AMQ_SCHEDULED_REPEAT：重复投递次数
			4：AMQ_SCHEDULED_CRON：Cron表达式
			 */
			message.setLongProperty(
					ScheduledMessage.AMQ_SCHEDULED_DELAY, expireTime);
			return message;
		}
    }

	public void orderDelay(OrderExp order, long expireTime) {
		logger.info("订单[超时时长："+expireTime+"秒] 将被发送给消息队列，详情："+order);
		jmsTemplate.send("order.delay", new CreateMessage(order,expireTime*1000));
		
	}
    
}
