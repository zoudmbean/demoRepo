package com.bjc.jms.topic.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 订阅/发布模式  生产者
 * @author Administrator
 *
 */
public class TopicProducer {
	public static void main(String[] args) throws Exception {
		// 1. 创建工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		
		// 2. 创建连接对象
		Connection conn = factory.createConnection();
		conn.start();
		
		// 3. 创建session
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 4. 创建消息队列
		Topic topic = session.createTopic("test-topic");
		
		// 5. 创建生产者
		MessageProducer producer = session.createProducer(topic );
		
		// 6. 创建消息对象
		TextMessage textMessage = session.createTextMessage("你好 topic MQ!");
		
		// 7. 发送消息
		producer.send(textMessage);
		
		// 8. 关闭连接
		producer.close();
		session.close();
		conn.close();
	}
}
