package com.isdb.services.interfaces;

import org.springframework.mail.MailException;

import com.isdb.models.User;

import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
	
	void sendRegistrationEmail(User user) throws MailException, MessagingException;

}

