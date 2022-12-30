package com.slackapp.main.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailProperties {
    
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private String port;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;
	
	@Value("${mail.address.to}")
	private String to;
	
	@Value("${mail.address.cc}")
    private String cc;
	
	public MailProperties(String host, String port, String username, String password, String auth, String to,
			String cc) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.auth = auth;
		this.to = to;
		this.cc = cc;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public MailProperties(String host, String port, String username, String password, String auth) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.auth = auth;
	}

	public MailProperties() {
		super();
		// TODO Auto-generated constructor stub
	}
}
