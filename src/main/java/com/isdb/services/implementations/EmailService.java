package com.isdb.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.isdb.models.User;
import com.isdb.services.interfaces.EmailServiceInterface;
import com.isdb.utils.EmailUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailServiceInterface {
	
	@Value("${application.mail.default-remetent}")
	private String defaultRemetent;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendRegistrationEmail(User user) throws MailException, MessagingException {
		
		String subject = EmailUtils.defaultRegisterAcccountSubject();
		String messageOnHTMLFormat = EmailUtils.defaultRegisterAcccountMessage(user.getConfirmationToken());
		String mailTo = user.getEmail();
		
		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		mailMessage.setSubject(subject);
		
		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
		
		//SimpleMailMessage mailMessage = new SimpleMailMessage();
		helper.setFrom(defaultRemetent);
		helper.setSubject(subject);
		helper.setText(messageOnHTMLFormat, true);
		helper.setTo(mailTo);
		javaMailSender.send(mailMessage);
		
	}

}
