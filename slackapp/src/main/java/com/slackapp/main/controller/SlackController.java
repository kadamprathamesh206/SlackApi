package com.slackapp.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;
import com.slackapp.main.serviceImpl.SlackServiceImpl;

@RestController
@RequestMapping("/slack")
public class SlackController {

	@Autowired
	SlackServiceImpl slackServiceImpl;

	@GetMapping("/getconversation")	
	public ResponseEntity<?> conversationHistory(@RequestParam("id") String clientId){

		Optional<List<Message>> response=this.slackServiceImpl.getConversationHistory(clientId);
        
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
//	@GetMapping("/getRetriveMessages")
	
	@GetMapping("/getChannelId")
	public ResponseEntity<?> getChannelID(@RequestParam("channel") String channelName){
		  String channelId=this.slackServiceImpl.findConversation(channelName);
		  return new ResponseEntity<>(channelId,HttpStatus.OK);
	}

}
