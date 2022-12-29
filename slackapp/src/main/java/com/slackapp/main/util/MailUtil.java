package com.slackapp.main.util;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MailUtil {
	@Autowired
	JavaMailSender mailSender;
	
	public void mailSender(MailProperties mailPropersties,HashMap<String, String> userWithEmail,Collection<String> values,Object username) {

		try {
			SimpleMailMessage mailMessage=new SimpleMailMessage();
			System.out.println(mailPropersties);
			mailMessage.setFrom(mailPropersties.getUsername());
			mailMessage.setTo((String) userWithEmail.get(username));
			mailMessage.setText(values.toString());
			System.out.println(values.toString());
			mailMessage.setSubject("slack message data");
			mailSender.send(mailMessage);
			System.out.println("mail send successfully");
		}
		catch(Exception exception) {
			System.out.println(exception);
		}



	}
	   
	  
	 

}
