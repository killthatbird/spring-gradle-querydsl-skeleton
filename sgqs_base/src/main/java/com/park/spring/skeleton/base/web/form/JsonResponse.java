package com.park.spring.skeleton.base.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.park.spring.skeleton.base.constants.Status;

/**
 * JSONでのレスポンス
 * @author park
 */
public class JsonResponse {
	@Expose private Object model = null;
	@Expose private Status status = null;
	@Expose private String errorMessage = null;
	@Expose private List<ErrorMessage> errorMessageLists = null;
	
	public Object getModel() {
		return model;
	}
	public void setModel(Object model) {
		this.model = model;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<ErrorMessage> getErrorMessageLists() {
		return errorMessageLists;
	}
	
	/**
	 * エラーメッセージから
	 * 国際化対応したメッセージを設定する
	 * @param fieldErrors
	 * @param messageSource
	 * @param locale
	 */
	public void setErrorMessageLists(final List<FieldError> fieldErrors, final MessageSource messageSource, final Locale locale) {
		List<ErrorMessage> errorMessageLists = new ArrayList<ErrorMessage>();
		for (FieldError error : fieldErrors) {
			errorMessageLists.add(new ErrorMessage(error.getField(), messageSource.getMessage(error, locale)));
		}
		this.errorMessageLists = errorMessageLists;
	}
	
	/**
	 * エラーメッセージを設定する
	 * @param field
	 * @param message
	 */
	public void setErrorMessageLists(final String field, final String message) {
		this.errorMessageLists = Lists.newArrayList(new ErrorMessage(field, message));
	}
}
