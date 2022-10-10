package com.etz.authorisationserver.services;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.etz.authorisationserver.exception.AuthServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderService {

	  @Value("${mail.smtp}")
	  public String SMTP_HOST_NAME; //can be your host server smtp@yourdomain.com

	  @Value("${mail.username}")
	  private String SMTP_AUTH_USER; //your login username/email

	  @Value("${mail.password}")
	  private String SMTP_AUTH_PWD; //password/secret

	  @Value("${mail.sender}")
	  private String sender;

	  int resendCounter = 5;

	  private  Message message;

	  public void sendEmail(String to, String subject, String msg) {
	    // Recipient's email ID needs to be mentioned.

	    // Sender's email ID needs to be mentioned

	    final String username = SMTP_AUTH_USER;
	    final String password = SMTP_AUTH_PWD;

	    // Assuming you are sending email through relay.jangosmtp.net
	    String host = SMTP_HOST_NAME;

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", "25");

	    // Get the Session object.
	    Session session = Session.getInstance(
	      props,
	      new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	          return new PasswordAuthentication(username, password);
	        }
	      }
	    );

	    try {
	      // Create a default MimeMessage object.
	      message = new MimeMessage(session);

	      // Set From: header field of the header.
	      message.setFrom(new InternetAddress(sender));

	      // Set To: header field of the header.
	      message.setRecipients(
	        Message.RecipientType.TO,
	        InternetAddress.parse(to)
	      );

	      // message.addRecipients(
	      //   Message.RecipientType.CC,
	      //   InternetAddress.parse("abc@gmail.com,abc@yahoo.com")
	      // );

	      // Set Subject: header field
	      message.setSubject(subject);

	      // Create the message part
	      BodyPart messageBodyPart = new MimeBodyPart();

	      // Now set the actual message
	      messageBodyPart.setContent(msg, "text/html");

	      // Create a multipar message
	      Multipart multipart = new MimeMultipart();

	      // Set text message part
	      multipart.addBodyPart(messageBodyPart);

	      //            // Part two is attachment
	      //            messageBodyPart = new MimeBodyPart();
	      //            String filename = Context.;
	      //            DataSource source = new FileDataSource(filename);
	      //            messageBodyPart.setDataHandler(new DataHandler(source));
	      //            messageBodyPart.setFileName(filename);
	      //            multipart.addBodyPart(messageBodyPart);n              // Send the complete message parts
	      message.setContent(multipart);

	      Thread thread = new Thread(
				  () -> {
					try {
					  // Send message
					  Transport.send(message);
					  log.info("Sent message successfully....");
					} catch (Exception e) {
					  e.printStackTrace();
					  sendEmail(to, subject, msg);

					  if (resendCounter != 0) {
						sendEmail(to, subject, msg);
						resendCounter--;
					  }
					  if (resendCounter == 0) {
						log.info("Attempted to send 5 Times but it failed....");
					  }
					}
				  }
		  );

	      thread.start();
	    } catch (Exception e) {
	      throw new AuthServiceException(e.getMessage());
	    }
	  }
}
