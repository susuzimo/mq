package com.wtm.activemq.delayQueue.service.mq;


import com.google.gson.Gson;
import com.wtm.activemq.delayQueue.model.OrderExp;
import com.wtm.activemq.delayQueue.service.busi.DlyOrderProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *类说明：处理消息队列返回的延时订单
 */
@Service
public class MqConsume implements MessageListener {
	private Logger logger = LoggerFactory.getLogger(MqConsume.class);
	
	@Autowired
	private DlyOrderProcessor processDlyOrder;
	
	public void onMessage(Message message) {
		try {
			String txtMsg = ((TextMessage)message).getText();
			logger.info("接收到消息队列发出消息："+txtMsg);
			Gson gson = new Gson();
			OrderExp order = (OrderExp)gson.fromJson(txtMsg, OrderExp.class);
			processDlyOrder.checkDelayOrder(order);
		} catch (Exception e) {
			logger.error("处理消费异常！",e);
		}
	}

}
