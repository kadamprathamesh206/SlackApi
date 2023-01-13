package com.slackapp.main.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.slackapp.main.serviceImpl.SlackServiceImpl;

//@Service
//public class Sheduler {
//  
//	@Autowired SlackServiceImpl slackserviceImpl;
//	
//	@Scheduled(cron = "0 9 16 * * ?")
//	public void sheduling() {
//		slackserviceImpl.getConversationHistory();
//	}
//}
