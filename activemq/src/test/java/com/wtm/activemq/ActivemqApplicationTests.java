package com.wtm.activemq;

import com.wtm.activemq.springboot.normal.queue.ProducerQueue;
import com.wtm.activemq.springboot.normal.topic.ProducerTopic;
import com.wtm.activemq.springboot.replyto.ProducerR;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jms.Destination;

@SpringBootTest
class ActivemqApplicationTests {


	@Autowired
	private ProducerQueue producerQueue;
	@Autowired
	private ProducerR producerR;
	@Autowired
	private ProducerTopic producerTopic;

	@Test
	public void testQueueNormal() {
		Destination destination
				= new ActiveMQQueue("springboot.queue");
		for(int i=0; i<10; i++){
			producerQueue.sendMessage(destination,
					"NO:"+i+";my name is Mark!!!");
		}
	}

	@Test
	public void testTopicNormal() {
		Destination destination
				= new ActiveMQTopic("springboot.topic");
		for(int i=0; i<3; i++){
			producerTopic.sendMessage(destination,
					"NO:"+i+";James like 13 !!!");
		}
	}

	@Test
	public void testReplyTo() {
		Destination destination
				= new ActiveMQQueue("springboot.replyto.queue");
		for(int i=0; i<10; i++){
			producerR.sendMessage(destination,
					"NO:"+i+";my name is Mark!!!");
		}
	}


}
