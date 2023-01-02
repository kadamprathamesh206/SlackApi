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

@Component
public class MessageWrite {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	@Value("${file.path}")
	String filepath;

	public void messageWriter(Multimap<String, String> content,Date date) {
		//		Path fileName = Path.of(
		//				""+filepath+""+date+".txt");
		try {
			//			Files.writeString(fileName, content.toString());
			//			  FileWriter name = new FileWriter(""+filepath+""+date+".txt");
			//			     BufferedWriter out = new BufferedWriter(name);
			//			      do {                 
			//			        a=x.nextLine();//scanner x grabs next line and sets it string a
			//			        out.write(a);//writes a to file
			//			      } while(((Object) content.hasNext());
			//			      out.close();//closees file
			//			   } catch (IOException ioe){
			//			      System.out.println("file writer error");
			//			  }
			FileWriter fw = new FileWriter(""+filepath+""+date+".txt",true);
			PrintWriter out = new PrintWriter(fw);
		
			for(String key:content.keySet()) {
				out.println("Username"+"="+key.toString());
			
				int count=1;
			
				Collection<String> messages=content.get(key);
				for(String message:messages) {
                  out.println(count+++" "+message.toString());

				}
				out.println("----------------------------------><-----------------------------");

			}
			out.close();

			logger.debug("File write successfully");
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			logger.debug(ioException.getMessage());
		}
	}




}
