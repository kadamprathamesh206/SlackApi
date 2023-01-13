package com.slackapp.main.service;


import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;

public interface SlackService {
	
	boolean  getConversationHistory(String channelID);
	
	HashMap<String, String> getUserList();


}
