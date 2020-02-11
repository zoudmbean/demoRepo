package com.bjc.jms.queue.consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消费者
 * @author Administrator
 *
 */
public class QueueSummer {

	public static void main(String[] args) throws Exception {
		// 1. 创建连接工厂（连接工厂与activeMQ是通过TCP协议连接的）  客户端连接的端口号是61616
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		// 2. 创建连接
		Connection conn = connectionFactory.createConnection();
		// 3. 启动连接
		conn.start();
		// 4. 创建session会话对象 
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5. 创建队列对象,参数是队列名称
		Queue queue = session.createQueue("test-queue");
		
		// 6. 创建消息消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		
		// 7. 设置监听
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage)message;
					System.out.println("提取的消息：" + textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// 8. 等待键盘输入
		System.in.read();
		
		// 9. 关闭资源
		consumer.close();
		session.close();
		conn.close();
		
	}

}
