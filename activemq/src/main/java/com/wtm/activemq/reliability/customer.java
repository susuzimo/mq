package com.wtm.activemq.reliability;

/*
消费端
对消息的确认有4种机制
1、	AUTO_ACKNOWLEDGE = 1    自动确认
2、	CLIENT_ACKNOWLEDGE = 2  客户端手动确认
3、	DUPS_OK_ACKNOWLEDGE = 3 自动批量确认
4、	SESSION_TRANSACTED = 0  事务提交并确认
ACK_MODE描述了Consumer与broker确认消息的方式(时机),比如当消息被Consumer接收之后,Consumer将在何时确认消息。所以ack_mode描述的不是producer于broker之间的关系，而是customer于broker之间的关系。
对于broker而言，只有接收到ACK指令,才会认为消息被正确的接收或者处理成功了,通过ACK，可以在consumer与Broker之间建立一种简单的“担保”机制.
session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
第一个参数:是否支持事务，如果为true，则会忽略第二个参数，自动被jms服务器设置为SESSION_TRANSACTED
 */
public class customer {
    /*
    同步(receive)方法返回message给消息时会立即确认。
    在"异步"(messageListener)方式中,将会首先调用listener.onMessage(message)，
    如果onMessage方法正常结束,消息将会正常确认。
    如果onMessage方法异常，将导致消费者要求ActiveMQ重发消息。
    此外需要注意，消息的重发次数是有限制的，每条消息中都会包含“redeliveryCounter”计数器，
    用来表示此消息已经被重发的次数，如果重发次数达到阀值，将导致broker端认为此消息无法消费,此消息将会被删除或者迁移到"dead letter"通道中。
    因此当我们使用messageListener方式消费消息时，可以在onMessage方法中使用try-catch,这样可以在处理消息出错时记录一些信息，而不是让consumer不断去重发消息；如果你没有使用try-catch,就有可能会因为异常而导致消息重复接收的问题,需要注意onMessage方法中逻辑是否能够兼容对重复消息的判断。
     */


    /* 设置消费者监听器，监听消息*/
    /*
    messageConsumer.setMessageListener(new MessageListener() {
        public void onMessage(Message message) {
            try {
                //do my work
                System.out.println(((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
            //throw new RuntimeException("RuntimeException");
        }
    });
    */




    /*
    客户端手动确认，
    这就意味着AcitveMQ将不会“自作主张”的为你ACK任何消息，
    开发者需要自己择机确认。
    可以用方法： message.acknowledge()，
    或session.acknowledge()；效果一样。
如果忘记调用acknowledge方法，
将会导致当consumer重启后，会接受到重复消息，
因为对于broker而言，那些尚未真正ACK的消息被视为“未消费”。
我们可以在当前消息处理成功之后，立即调用message.acknowledge()方法来"逐个"确认消息，
这样可以尽可能的减少因网络故障而导致消息重发的个数；当然也可以处理多条消息之后，间歇性的调用acknowledge方法来一次确认多条消息，
减少ack的次数来提升consumer的效率，不过需要自行权衡。
实验方法：在模块no-spring包msgreliability下的JmsMsgReliablityConsumerAsyn中将session模式改为Session.CLIENT_ACKNOWLEDGE，，启动两个消费者，发送消息后，可以看到JmsMsgReliablityConsumerAsyn接收了消息但不确认。当JmsMsgReliablityConsumerAsyn重新启动后，会再一次收到同样的消息。加入message.acknowledge()后该现象消失
     */

    /* 设置消费者监听器，监听消息*/
    /*
messageConsumer.setMessageListener(new MessageListener() {
        public void onMessage(Message message) {
            try {
                //do my work
                System.out.println(((TextMessage) message).getText());
                message.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    });
    */

/*
类似于AUTO_ACK确认机制，为自动批量确认而生，
而且具有“延迟”确认的特点，ActiveMQ会根据内部算法，
在收到一定数量的消息自动进行确认。在此模式下，可能会出现重复消息，什么时候？当consumer故障重启后，那些尚未ACK的消息会重新发送过来。
 */




/*
当session使用事务时，就是使用此模式。当决定事务中的消息可以确认时，必须调用session.commit()方法，commit方法将会导致当前session的事务中所有消息立即被确认。在事务开始之后的任何时机调用rollback()，意味着当前事务的结束，事务中所有的消息都将被重发。当然在commit之前抛出异常，也会导致事务的rollback。
 */

/*
try {
     通过连接工厂获取连接
        connection = connectionFactory.createConnection();
     启动连接
        connection.start();
     创建session
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
     创建一个消息队列
        destination = session.createQueue("MsgReliability");
      创建消息消费者
        messageConsumer = session.createConsumer(destination);
        Message message;
        while ((message = messageConsumer.receive()) != null) {
            System.out.println(((TextMessage) message).getText());
        }
        //session.commit();
    } catch (JMSException e) {
        e.printStackTrace();
        //session.rollback();
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
*/

}
