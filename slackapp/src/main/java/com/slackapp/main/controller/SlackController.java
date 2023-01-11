package com.slackapp.main.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;
import com.slackapp.main.serviceImpl.SlackServiceImpl;

@RestController
@RequestMapping("/slack")
public class SlackController {
	private static final Logger logger = LoggerFactory.getLogger(SlackController.class);


	@Value("${mail.isMail}")
	boolean isEmail;

	@Autowired
	SlackServiceImpl slackServiceImpl;

	@GetMapping("/getconversation")	
	public ResponseEntity<?> conversationHistory(){
		try {
			boolean fileWriterResult=this.slackServiceImpl.getConversationHistory();
			if(fileWriterResult) {
				if(isEmail) {
					boolean mailSendResult=this.slackServiceImpl.sendingEmail();
					if(mailSendResult) {
						return new ResponseEntity<>("Email Sent Successfully",HttpStatus.OK);
					}else {
						return new ResponseEntity<>("facing message while sending email",HttpStatus.BAD_REQUEST);
					}
				}else {
					return new ResponseEntity<>("File Write Successfully",HttpStatus.OK);
				}
			}
		}
		catch(Exception exception) {
            logger.error(exception.getMessage());
		}
		return new ResponseEntity<>("Having some issue while fetching conversation history",HttpStatus.BAD_REQUEST);
	}


	//	@GetMapping("/sendingEmail")
	//	public ResponseEntity<?> getDateFromTimeStamp(){
	//		boolean result=   this.slackServiceImpl.sendingEmail();
	//		if(result) {
	//			return new ResponseEntity<>("Email Sent Successfully",HttpStatus.OK);
	//		}
	//
	//		return new ResponseEntity<>("facing message while sending email",HttpStatus.BAD_REQUEST);
	//	}

}
