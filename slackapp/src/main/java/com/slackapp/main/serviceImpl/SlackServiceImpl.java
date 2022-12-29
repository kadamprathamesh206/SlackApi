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
import com.slack.api.methods.response.admin.users.AdminUsersListResponse.User;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slackapp.main.service.SlackService;

import com.slackapp.main.util.MailProperties;
import com.slackapp.main.util.MailUtil;

import jakarta.mail.Authenticator;
import jakarta.mail.Session;

@Service
public class SlackServiceImpl implements SlackService {

	@Value("${spring.slacktoken}")
	String slackToken;

	@Autowired
	MailProperties mailPropersties;


	@Autowired
	MailUtil mailUtil;

	public  Optional<List<Message>> getConversationHistory(String clientId) {

		Multimap<String, String> listOfmessagesOfUser=ArrayListMultimap.create();
		Optional<List<Message>> conversationHistory=null;
		MethodsClient  client = Slack.getInstance().methods();
		var logger = LoggerFactory.getLogger("my-awesome-slack-app");
		try {
			// Call the conversations.history method using the built-in WebClient
			ConversationsHistoryResponse  result = client.conversationsHistory(r -> r
					// The token you used to initialize your app
					.token(slackToken)
					.channel(clientId)
					);
			System.out.println(result);
			conversationHistory = Optional.ofNullable(result.getMessages());
			System.out.println(result.getMessages());
			
			
			for(Message message:result.getMessages()) {
				listOfmessagesOfUser.put(message.getUser(), message.getText()+","+message.getTs());
				
			}
			System.out.println(listOfmessagesOfUser);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		HashMap<String,String> userWithEmailList=this.getUserList();
		for(Object username : listOfmessagesOfUser.keySet()) {
			if(userWithEmailList.containsKey(username)) {
				Collection<String> messages= listOfmessagesOfUser.get((String) username);
				mailUtil.mailSender(mailPropersties, userWithEmailList, messages, username);
			}
		}
		return conversationHistory;


	}



	

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
			//			for(com.slack.api.model.User user:users) {
			//				System.out.println(user.getProfile().getEmail());
			//				if(user.getProfile().getEmail()==null){
			//					continue;
			//				}else {
			//					userWithEmail.put(user.getId(), user.getProfile().getEmail());
			//                    continue;
			//				}
			int value=0;
			while(users.size()>value) {
				System.out.println(users.get(value).getProfile().getEmail());
				if(users.get(value).getProfile().getEmail()!=null) {
					userWithEmail.put(users.get(value).getId(), users.get(value).getProfile().getEmail());
				}
				value++;
			}
			System.out.println(userWithEmail);


		}catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
		return userWithEmail;

	}



	@Override
	public java.util.Date converTimeStampToDate(Double timeStamp) {
		Date dayAndDate = new Date(  (long) (timeStamp * 1000));


		System.out.println(dayAndDate);
		//		System.out.println(dayAndDate.getDate()+" "+dayAndDate.getMonth()+" "+dayAndDate.getDay());
		return dayAndDate;
	}







}
