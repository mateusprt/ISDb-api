package com.isdb.utils;

public class EmailUtils {
	
	public static String defaultRegisterAcccountSubject() {
		return "Bem vindo ao ISDb";
	};
	
	public static String defaultRegisterAcccountMessage(String confirmationToken) {
		String url = "http://localhost:8080/api/v1/auth/confirmation/" + confirmationToken;
		return "<h1>Seja bem vindo ao ISDb</h1><p>Para acessar sua conta é preciso confirmá-la antes clicando <a href=\"" + url + "\">aqui</a></p>";
	}
	
}
