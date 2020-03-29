package com.sb.integration.config;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailConfig {

	public void sendTextMail(String subject, String body, String to, Boolean isTextMessage) {
		Properties properties = new Properties();
		final Properties prop = new Properties();
		InputStream is = null;
		try {
			;
			is = this.getClass().getResourceAsStream("/mail.properties");
			prop.load(is);

			properties.setProperty("mail.smtp.host", prop.getProperty("mail.smtp.host"));
			properties.setProperty("mail.smtp.socketFactory.class", prop.getProperty("mail.smtp.socketFactory.class"));
			properties.setProperty("mail.smtp.socketFactory.fallback",
					prop.getProperty("mail.smtp.socketFactory.fallback"));
			properties.setProperty("mail.smtp.port", prop.getProperty("mail.smtp.port"));
			properties.setProperty("mail.smtp.socketFactory.port", prop.getProperty("mail.smtp.socketFactory.port"));
			properties.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));
			properties.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
			properties.put("mail.debug", prop.getProperty("mail.debug"));
			properties.put("mail.store.protocol", prop.getProperty("mail.store.protocol"));
			properties.put("mail.transport.protocol", prop.getProperty("mail.transport.protocol"));
			properties.put("mail.debug.auth", prop.getProperty("mail.debug.auth"));
			properties.setProperty("mail.pop3.socketFactory.fallback",
					prop.getProperty("mail.pop3.socketFactory.fallback"));
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prop.getProperty("mail.username"), prop.getProperty("mail.pass"));
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prop.getProperty("mail.from")));
			message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			
			if(isTextMessage){
				message.setText(body);
			}else{
				//message.setContent(body, "text/html");
				message.setText(body, "UTF-8", "html");
			}
			
			Transport.send(message);
			
			System.out.println("Mail send....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
