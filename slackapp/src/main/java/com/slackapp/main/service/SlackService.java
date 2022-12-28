package com.slackapp.main.service;

import java.util.List;
import java.util.Optional;

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;

public interface SlackService {

	Optional<List<Message>> getConversationHistory(String clientId);
	
	public  String  findConversation(String name);
}
