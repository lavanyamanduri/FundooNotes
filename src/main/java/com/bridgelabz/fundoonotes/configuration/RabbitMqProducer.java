package com.bridgelabz.fundoonotes.configuration;

/*
 *  author : Lavanya Manduri
 */

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.MailDto;
import com.bridgelabz.fundoonotes.utility.MailSendingUtil;

@Component
public class RabbitMqProducer {
	
	/* Configuration for RabbitMq Producer */

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

