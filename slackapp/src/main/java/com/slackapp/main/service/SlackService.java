package com.slackapp.main.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;

public interface SlackService {

	Optional<List<Message>> getConversationHistory(String clientId);
	
	Date  converTimeStampToDate(Double timeStamp);
	

}
