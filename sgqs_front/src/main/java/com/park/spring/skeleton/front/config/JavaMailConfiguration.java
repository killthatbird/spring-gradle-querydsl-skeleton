package com.park.spring.skeleton.front.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource({ "classpath:config.properties" })
public class JavaMailConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(env.getProperty("java.mail.host"));
		javaMailSender.setPort(JavaMailSenderImpl.DEFAULT_PORT);
		javaMailSender.setUsername(env.getProperty("java.mail.username"));
		javaMailSender.setPassword(env.getProperty("java.mail.password"));
		javaMailSender.setJavaMailProperties(new Properties() {
			private static final long serialVersionUID = 2612266498763921686L;
			{
			setProperty("mail.smtp.auth", env.getProperty("java.mail.smtp.auth"));
			setProperty("mail.smtp.starttls.enable", env.getProperty("java.mail.smtp.starttls.enable"));
		}});
		
		return javaMailSender;
	}
}
