package frodez.activemq;

import javax.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActiveMQTest {

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	@Test
	public void contextLoads() {
		Destination message = new ActiveMQQueue("message.queue");
		Destination log = new ActiveMQQueue("log.queue");
		jmsTemplate.convertAndSend(message, "生产者发送了消息");
		jmsTemplate.convertAndSend(log, "生产者发送了日志");
	}

}
