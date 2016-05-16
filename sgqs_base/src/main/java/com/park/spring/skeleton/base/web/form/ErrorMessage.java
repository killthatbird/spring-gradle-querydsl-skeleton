package com.park.spring.skeleton.base.web.form;

public class ErrorMessage {
	private final String field;
	private final String defaultMessage;
	
	public ErrorMessage(String field, String defaultMessage) {
		this.field = field;
		this.defaultMessage = defaultMessage;
	}

	public String getField() {
		return field;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}