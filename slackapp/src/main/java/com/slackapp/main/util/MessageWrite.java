package com.slackapp.main.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;
import com.slack.api.audit.Actions.File;

@Component
public class MessageWrite {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	@Value("${file.path}")
	String filepath;

	public void messageWriter(Multimap<String, String> content,String channelname,Date date) {
		
		try {
		
			java.io.File f1 =new java.io.File(filepath+date.toString());
			if(!f1.mkdir()) {
				f1.mkdir();
			}
			;
			String newPath=filepath+date.toString()+"/"+channelname;
			java.io.File file=new java.io.File(""+newPath+".txt");
			file.createNewFile();
			PrintWriter printwriter = new PrintWriter(file);
			for(String key:content.keySet()) {
				printwriter.println("Username"+"="+key.toString());
				int count=1;
				Collection<String> messages=content.get(key);
				for(String message:messages) {
					printwriter.println(count+++" "+message.toString());
				}
				printwriter.println("----------------------------------><-----------------------------");
			}
			printwriter.close();

			logger.debug("File write successfully");
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			logger.debug(ioException.getMessage());
		}
	}




}
