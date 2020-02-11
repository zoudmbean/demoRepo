package com.bjc.jms.queue.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**生产者
 * @author Administrator
 *
 */
public class QueueProducer {

	public static void main(String[] args) throws Exception {
		// 1. 创建连接工厂（连接工厂与activeMQ是通过TCP协议连接的）  客户端连接的端口号是61616
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		
		// 2. 创建连接
		Connection conn = connectionFactory.createConnection();
		
		// 3. 启动连接
		conn.start();
		
		// 4. 创建session会话对象 
		/*
		 * 参数1：是否启用事务  true 启用   需要手动commit提交事务； false 不起用，不起用也就是一种默认提交事务的行为，
		 * 参数2：消息的确认方式，是一个枚举常量
		 * */
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 5. 创建队列对象,参数是队列名称
		Queue queue = session.createQueue("test-queue");
		
		// 6. 创建消息生产者对象  参数是一个 Destination 的对象  Queue是Destination的子类
		MessageProducer messageProducer = session.createProducer(queue);
		
		// 7. 创建一个消息对象（文本消息）
		TextMessage textMsg = session.createTextMessage("你好，MQ！");
		
		// 8. 发送消息
		messageProducer.send(textMsg);
		
		// 9. 关闭资源
		messageProducer.close();
		session.close();
		conn.close();
		
	}

}
