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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;
import com.slackapp.main.service.SlackService;
import com.slackapp.main.util.DateUtil;
import com.slackapp.main.util.FileWrite;
import com.slackapp.main.util.MailUtil;




@Service
public class SlackServiceImpl implements SlackService {
	private static final Logger logger = LoggerFactory.getLogger(SlackServiceImpl.class);

	@Value("${spring.slacktoken}")
	String slackToken;
	
	@Value("${file.path}")
	String filepath;

	@Autowired
	MailUtil mailUtil;



	/*
	 * 
	 * getConversationHistory method use for fetch  all the messages of channel 
	 */

	public  boolean  getConversationHistory() {

		DateUtil dateutil=new DateUtil();
		Date date=null;
		Date currentDate=new Date(System.currentTimeMillis());
		Multimap<String, String> listOfmessagesOfUser=ArrayListMultimap.create();
		Optional<List<Message>> conversationHistory=null;
		FileWrite fileWriter=new FileWrite();

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
						date=dateutil.converTimeStampToDate(Double.valueOf(message.getTs()));
						if(date.toString().equals(currentDate.toString())) {
							listOfmessagesOfUser.put(username, message.getText()+" ,Time :"+dateutil.convertTime(Double.valueOf(message.getTs())));
						}
					}
				}
				boolean fileWriterResult=fileWriter.messageWriter(listOfmessagesOfUser,channel.getName(),currentDate,filepath);
				if(fileWriterResult) {
					listOfmessagesOfUser.clear();
					continue;
				}
				else {
					return false;
				}
			}
			catch(Exception exception) {
				logger.error(exception.getMessage());
			}
		}
		return true;



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
			logger.error(exception.getMessage());
		}
		return userWithName;

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
			logger.error(exception.getMessage());
		}
		return result; 

	}


	public boolean sendingEmail() {
		try {
			List<Conversation> channelList=  this.publicChannelList().getChannels();
			Date currentDate=new Date(System.currentTimeMillis());
			boolean mailSendingResult= mailUtil.mailSender(channelList, currentDate);
			if(mailSendingResult) {
				return true;
			}
		}catch(Exception exception) {
			logger.error(exception.getMessage());
		}

		return false;
	}
}



