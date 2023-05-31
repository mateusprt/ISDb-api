package com.isdb.exceptions.handlers;

import java.util.Date;
import java.util.List;

public class ResponseException {
	
	public Date timestamp;
	private List<String> message;
	private String details;
	
	
	public ResponseException(List<String> message, String details) {
		super();
		this.timestamp = new Date();
		this.message = message;
		this.details = details;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(List<String> message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	
	
}
