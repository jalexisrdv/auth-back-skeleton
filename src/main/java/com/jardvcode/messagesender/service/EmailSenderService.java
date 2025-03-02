package com.jardvcode.messagesender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	
	private final static Logger LOG = LoggerFactory.getLogger("messageSenderLog");
	
    private final JavaMailSender sender;
    
    public EmailSenderService(JavaMailSender sender) {
    	this.sender = sender;
    }

    public void send(String to, String subject, String text) {
    	try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            sender.send(message);
    	} catch (Exception e) {
			LOG.info(e.getMessage(), e);
		}
    }
    
}