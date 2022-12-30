package com.slackapp.main.util;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.slackapp.main.serviceImpl.SlackServiceImpl;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

@Service
public class MailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	@Autowired
	JavaMailSender mailSender;
	

	
	public void mailSender(MailProperties mailPropersties,Date date) {

		try {

			  MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			helper.setFrom(mailPropersties.getUsername());
			helper.setTo(mailPropersties.getTo());
			helper.setCc(mailPropersties.getCc());
			helper.setSubject("slack message data");
			helper.setText("");
			FileSystemResource file = new FileSystemResource("D:\\SlackMessages\\"+date+".txt");
			helper.addAttachment(file.getFilename(), file);
			mailSender.send(message);
		    logger.debug("mail sent successfully");
			
		}
		catch(Exception exception) {
			logger.debug(exception.getMessage());
		}



	}
	   
	  
	 

}
