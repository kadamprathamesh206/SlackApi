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

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;
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

	public  String getConversationHistory() {
		Date date=null;
		Date currentDate=new Date(System.currentTimeMillis());
		Multimap<String, String> listOfmessagesOfUser=ArrayListMultimap.create();
		Optional<List<Message>> conversationHistory=null;

		MethodsClient  client = Slack.getInstance().methods();
		ConversationsListResponse channelList=publicChannelList();
		HashMap<String,String> userWithEmailList=this.getUserList();                                  
		for(Conversation channel:channelList.getChannels()) {
			System.out.println(channel.getName());
			var logger = LoggerFactory.getLogger("my-awesome-slack-app");
			try {
				// Call the conversations.history method using the built-in WebClient
				ConversationsHistoryResponse  result = client.conversationsHistory(r -> r
						// The token you used to initialize your app
						.token(slackToken)
						.channel(channel.getId())
						);
				for(Message message:result.getMessages()) {
					if(userWithEmailList.containsKey(message.getUser())) {
						String username= userWithEmailList.get(message.getUser());
						date=this.converTimeStampToDate(Double.valueOf(message.getTs()));
						if(date.toString().equals(currentDate.toString())) {
							listOfmessagesOfUser.put(username, message.getText()+" ,Time :"+this.convertTime(Double.valueOf(message.getTs())));
						}
					}
				}
				this.messageWrite.messageWriter(listOfmessagesOfUser,channel.getName(),currentDate);
				listOfmessagesOfUser.clear();
			}
			catch(Exception exception) {
				logger.debug(exception.getMessage());
			}
		}
		String message=	mailUtil.mailSender(mailPropersties ,channelList.getChannels(),currentDate);

		return message;
	}



	/*
	 * 
	 *  getUserList method use for fetching all the users from slack
	 * 
	 * 
	 */
	public HashMap<String,String> getUserList() {
		HashMap<String,String> userWithName=new HashMap<>();
		UsersListResponse userListResponse;
		MethodsClient  client = Slack.getInstance().methods();
		try {
			UsersListResponse userList= client.usersList(r -> r
					// The token you used to initialize your app
					.token(slackToken)
					);
			List<com.slack.api.model.User> users=   userList.getMembers();
			for(User user:users) {
				if(user.getProfile().getRealName()!=null) {
					userWithName.put(user.getId(), user.getProfile().getRealName());
				}
			}
			System.out.println(userWithName);


		}catch(Exception exception) {
			logger.debug(exception.getMessage());
		}
		return userWithName;

	}

	/*
	 *  convertime method use for convert timestamp to util date 
	 *  
	 */

	public String convertTime(Double string)
	{
		java.util.Date dateWithTime = new java.util.Date( (long) (string * 1000));
		return dateWithTime.getHours()+" "+dateWithTime.getMinutes()+" "+dateWithTime.getSeconds();
	}

	/*
	 * 
	 *   converTimeStampToDate method use for convert timestamp to sql date
	 * 
	 */

	@Override
	public Date converTimeStampToDate(Double timeStamp) {
		Date dayAndDate = new Date(  (long) (timeStamp * 1000));
		System.out.println(dayAndDate);
		return dayAndDate;
	}


	/*
	 *  
	 * publicChannelList method used for fetch the public channel from channel
	 * 
	 * 
	 */
	public ConversationsListResponse publicChannelList() {
		MethodsClient client = Slack.getInstance().methods();
		var logger = LoggerFactory.getLogger("my-awesome-slack-app");
		ConversationsListResponse result=new ConversationsListResponse();
		try {
			// Call the conversations.list method using the built-in WebClient
			result = client.conversationsList(r -> r
					// The token you used to initialize your app
					.token(slackToken)
					);

		}catch(Exception exception) {
			logger.debug(exception.getMessage());
		}
		return result; 

	}
}



