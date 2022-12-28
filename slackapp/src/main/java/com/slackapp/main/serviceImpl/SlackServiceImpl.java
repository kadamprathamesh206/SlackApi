package com.slackapp.main.serviceImpl;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.naming.spi.DirStateFactory.Result;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slackapp.main.service.SlackService;
import com.slackapp.main.util.MailConstant;
import com.slackapp.main.util.MailProperties;

import jakarta.mail.Authenticator;
import jakarta.mail.Session;

@Service
public class SlackServiceImpl implements SlackService {

     @Autowired
     MailProperties mailPropersties;
	
	@Autowired
	JavaMailSender mailSender;

	public  Optional<List<Message>> getConversationHistory(String clientId) {

		Multimap<String, String> listOfmessagesOfUser=ArrayListMultimap.create();
		Optional<List<Message>> conversationHistory=null;
		MethodsClient  client = Slack.getInstance().methods();
		var logger = LoggerFactory.getLogger("my-awesome-slack-app");
		try {
			// Call the conversations.history method using the built-in WebClient
			ConversationsHistoryResponse  result = client.conversationsHistory(r -> r
					// The token you used to initialize your app
					.token("xoxp-4506012519077-4521596214497-4553499007351-ce276f3d9945f7940381a059017bfb5f")
					.channel(clientId)
					);
			System.out.println(result);
			conversationHistory = Optional.ofNullable(result.getMessages());
			for(Message message:result.getMessages()) {
				listOfmessagesOfUser.put(message.getUser(), message.getText());

				//						System.out.println(message.getUser()+" "+message.getText());
			}
			
			System.out.println(listOfmessagesOfUser);

			Collection<String> values= listOfmessagesOfUser.get("U04FBHJ6AEM");	
			try {
				SimpleMailMessage mailMessage=new SimpleMailMessage();
				
				System.out.println(mailPropersties);
				mailMessage.setFrom(mailPropersties.getUsername());
				
				
				mailMessage.setTo("moiz.sache@stspl.com");
				mailMessage.setText(values.toString());
				System.out.println(values.toString());
				mailMessage.setSubject("slack message data");
			    mailSender.send(mailMessage);
				
				System.out.println("mail send successfully");

				//	 		Properties properties=System.getProperties();
				//	 	Session session=Session.getInstance(mailProperties,new java.net.Authenticator() {
				//
				//				@Override
				//				protected PasswordAuthentication getPasswordAuthentication() {
				//					// TODO Auto-generated method stub
				//					return new PasswordAuthentication(mailProperties.getUsername(),mailProperties.getPassword());
				//				}



			}catch(Exception exception) {
				System.out.println(exception);
			}


			//			for (Object key : listOfmessagesOfUser.keys()) { 
			//				  System.out.println(key);
			//				 }

			System.out.println(listOfmessagesOfUser);





			// Prnt results
			//            logger.info("{} messages found in {}", conversationHistory.orElse(emptyList()).size(), id);
		} catch (IOException | SlackApiException e) {
			logger.error("error: {}", e.getMessage(), e);
		}

		return conversationHistory;
	}


	public  String  findConversation(String name) {


		String conversationId=null;
		// you can get this instance via ctx.client() in a Bolt app
		MethodsClient  client = Slack.getInstance().methods();

		var logger = LoggerFactory.getLogger("my-awesome-slack-app");
		try {
			// Call the conversations.list method using the built-in WebClient
			ConversationsListResponse  result = client.conversationsList(r -> r
					// The token you used to initialize your app
					.token("xoxp-4506012519077-4521596214497-4553499007351-ce276f3d9945f7940381a059017bfb5f")
					);
			for (Conversation channel : result.getChannels()) {
				if (channel.getId().equals(name)) {
					conversationId = channel.getId();
					// Print result
					logger.info("Found conversation ID: {}", conversationId);
					// Break from for loop
					break;
				}
			}
		} catch (IOException | SlackApiException e) {
			logger.error("error: {}", e.getMessage(), e);
		}
		return conversationId;
	}




}
