package com.slackapp.main.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.slack.api.model.Conversation;
import com.slackapp.main.serviceImpl.SlackServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.mail.internet.MimeMultipart;

@Service
public class MailUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	@Autowired
	JavaMailSender mailSender;
   
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${mail.address.to}")
	private String to;
	
	@Value("${mail.address.cc}")
    private String cc;

	@Value("${file.path}")
	String path;
	



	public boolean  mailSender(String channelNames,Date date) {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			helper.setFrom(username);
			helper.setTo(to);
			helper.setCc(cc);
			helper.setSubject("slack message data");
			helper.setText("Please check the attachment for your reference.");
			MimeMultipart multipart=new MimeMultipart();
//			for(Conversation channel:channelNames) {
				MimeBodyPart attachment=new MimeBodyPart();
				String fullPath=""+path+""+date+"/"+channelNames+".txt";
				attachment.attachFile(new File(fullPath));
				multipart.addBodyPart(attachment);
//			}
			message.setContent(multipart);
			mailSender.send(message);
			return true;
			
		

		}
		catch(Exception exception) {
			logger.debug(exception.getMessage());
		}
		

         return false;
          
	}




}
