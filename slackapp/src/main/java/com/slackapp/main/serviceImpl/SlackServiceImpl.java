package com.slackapp.main.serviceImpl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.PasswordAuthentication;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.naming.spi.DirStateFactory.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.admin.users.AdminUsersListResponse.User;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slackapp.main.service.SlackService;

import com.slackapp.main.util.MailProperties;
import com.slackapp.main.util.MailUtil;
import com.slackapp.main.util.MessageWrite;

import jakarta.mail.Authenticator;
import jakarta.mail.Session;

@Service
public class SlackServiceImpl implements SlackService {
	private static final Logger logger = LoggerFactory.getLogger(SlackServiceImpl.class);

	@Value("${spring.slacktoken}")
	String slackToken;
	
	
	@Value("${slack.channel}")
	String channel;

	@Autowired
	MailProperties mailPropersties;

	@Autowired
	MessageWrite messageWrite;


	@Autowired
	MailUtil mailUtil;
	
	
	/*
	 * 
	 * getConversationHistory method use for fetch  all the messages of channel 
	 */

	public  Optional<List<Message>> getConversationHistory() {
		Date date=null;
		Date currentDate=new Date(System.currentTimeMillis());
		Multimap<String, String> listOfmessagesOfUser=ArrayListMultimap.create();
		Optional<List<Message>> conversationHistory=null;
		MethodsClient  client = Slack.getInstance().methods();
		var logger = LoggerFactory.getLogger("my-awesome-slack-app");
		try {
			// Call the conversations.history method using the built-in WebClient
			ConversationsHistoryResponse  result = client.conversationsHistory(r -> r
					// The token you used to initialize your app
					.token(slackToken)
					.channel(channel)
					);
			System.out.println(result);
			conversationHistory = Optional.ofNullable(result.getMessages());
			System.out.println(result.getMessages());

			HashMap<String,String> userWithEmailList=this.getUserList();
			for(Message message:result.getMessages()) {
				if(userWithEmailList.containsKey(message.getUser())) {

					String username= userWithEmailList.get(message.getUser());
					//Multimap<String, Date> messageWithTime=ArrayListMultimap.create();
					date=this.converTimeStampToDate(Double.valueOf(message.getTs()));
					
					if(date.toString().equals(currentDate.toString())) {
						listOfmessagesOfUser.put(username, message.getText());
						
						
					}
					
				}
			}
			this.messageWrite.messageWriter(listOfmessagesOfUser,currentDate);
			mailUtil.mailSender(mailPropersties ,currentDate);

			System.out.println(listOfmessagesOfUser);
		}
		catch(Exception exception) {
			logger.debug(exception.getMessage());
		}
		return conversationHistory;


	}



    /*
     * 
     *  getUserList method use for fetching all the users from slack
     * 
     * 
     */
	public HashMap<String,String> getUserList() {
		HashMap<String,String> userWithEmail=new HashMap<>();
		UsersListResponse userListResponse;
		MethodsClient  client = Slack.getInstance().methods();
		try {
			UsersListResponse userList= client.usersList(r -> r
					// The token you used to initialize your app
					.token(slackToken)
					);
			List<com.slack.api.model.User> users=   userList.getMembers();
			int value=0;
			while(users.size()>value) {
				System.out.println(users.get(value).getProfile().getEmail());
				if(users.get(value).getProfile().getRealName()!=null) {
					userWithEmail.put(users.get(value).getId(), users.get(value).getProfile().getRealName());
				}
				value++;
			}
			System.out.println(userWithEmail);


		}catch(Exception exception) {
			logger.debug(exception.getMessage());
		}
		return userWithEmail;

	}

  

	@Override
	public Date converTimeStampToDate(Double timeStamp) {
		Date dayAndDate = new Date(  (long) (timeStamp * 1000));
		System.out.println(dayAndDate);
		return dayAndDate;
	}


	@Scheduled(cron = "0 39 14 * * ?")
	public void sheduling() {
		getConversationHistory();
	}







}
