package com.park.spring.skeleton.base.exception;

/**
 * アプリケーションのRuntimeException
 * @author park
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -2005954934386435534L;
	
	private String script;			//表示するJavascript
	
	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message 表示するメッセージ
	 * @param script 表示するJavascript
	 */
	public ApplicationException(String message, String script) {
		super(message);
		this.script = script;
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	public String getScript() {return script;}
}
