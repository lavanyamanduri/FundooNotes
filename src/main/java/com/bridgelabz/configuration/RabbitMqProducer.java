package com.bridgelabz.configuration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bridgelabz.dto.MailDto;
import com.bridgelabz.utility.MailSendingUtil;


@Component
public class RabbitMqProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MailSendingUtil sendingMail;

	@Value("${rabbit.rabbitmq.exchange}")
	private String exchangeName;

	@Value("${rabbit.rabbitmq.routingKey}")
	private String routingKey;

	public void produceMsg(MailDto message) {
		rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
	}

	@RabbitListener(queues = "${rabbit.rabbitmq.queue}")
	public void rabbitMqlistener(MailDto msg) {
		sendingMail.sendMail(msg);
	}

}

