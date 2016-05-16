package com.park.spring.skeleton.base.exception;

public class UnAuthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = 5292997991831308518L;
	
	private String prevUrl;		//前のページのURL
	private String errCode;		//エラーコード(表示するメッセージ　messages_ja.properties)
	
	public UnAuthorizedException() {
		super();
	}

	public UnAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthorizedException(String message) {
		super(message);
	}

	public UnAuthorizedException(Throwable cause) {
		super(cause);
	}

	public String getPrevUrl() {return prevUrl;}
	public String getErrCode() {return errCode;}
}
