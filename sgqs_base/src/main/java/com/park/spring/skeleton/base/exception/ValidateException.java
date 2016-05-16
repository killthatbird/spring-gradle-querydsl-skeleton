package com.park.spring.skeleton.base.exception;

/**
 * 間違った値の場合発生するException
 * 基本的にJSR-303で対応するが
 * しきれない場合はServiceから発生させる。
 * @author park
 *
 */
public class ValidateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String field;			//フィールド名　(DBカラム名)
	private String errCode;			//エラーコード(表示するメッセージ　messages_ja.properties)
	private Object[] errorArgs;		//メッセージに表示するコード
	private String script;			//表示するJavascript
/*	
	public ValidateException() {
		super();
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(String message) {
		super(message);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}
*/	
	/**
	 * 
	 * @param field フィールド名　(カラム名)
	 * @param errCode エラーコード(表示するメッセージ　messages_ja.properties)
	 */
	public ValidateException(String field, String errCode) {
		this.errCode = errCode;
		this.field = field;
	}
	
	/**
	 * Javascriptをエラー画面に表示するためのException]
	 * @message エラーメッセージ
	 * @param errCode エラーコード(表示するメッセージ　messages_ja.properties)
	 * @param script 表示するJavascript
	 */
	public ValidateException(String message, String errCode, String script) {
		super(message);
		this.errCode = errCode;
		this.script = script;
	}
	
	
	
	/**
	 * 
	 * @param field フィールド名　(カラム名)
	 * @param errCode エラーコード(表示するメッセージ　messages_ja.properties)
	 * @param errorArgs エラーメッセージのコードリスト
	 */
	public ValidateException(String field, String errCode, Object[] errorArgs) {
		this.errCode = errCode;
		this.field = field;
		this.errorArgs = errorArgs;
	}
	
	public String getErrCode() {return errCode;}
 	public String getField() {return field;}
	public Object[] getErrorArgs() {return errorArgs;}
	public String getScript() {return script;}
	
}
