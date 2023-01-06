package com.slackapp.main.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.slackapp.main.util.MailProperties;

@Configuration
public class MailConfig {
	
	@Autowired
	MailProperties mailProperties;

    @Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(mailProperties.getHost());
	     int mailport=Integer.parseInt(mailProperties.getPort());
	    mailSender.setPort(mailport);
	    mailSender.setUsername(mailProperties.getUsername());
	    mailSender.setPassword(mailProperties.getPassword());
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
	
}
