package com.slackapp.main.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;

@Component
public class MessageWrite {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	@Value("${file.path}")
	String filepath;
	
	public void messageWriter(Multimap<String, Multimap<String, Date>> content,Date date) {
		Path fileName = Path.of(
				""+filepath+""+date+".txt");
		try {
			Files.writeString(fileName, content.toString());
			logger.debug("File write successfully");
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			logger.debug(ioException.getMessage());
		}
	}
	
	
	

}
