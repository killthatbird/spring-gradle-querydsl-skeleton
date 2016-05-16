package com.park.spring.skeleton.front.service;


import org.springframework.security.authentication.LockedException;

public interface LoginService {
	/**
	 * ログイン失敗した際の情報を設定する。
	 * @param frontMemberLoginId ログインID
	 * @param remoteAddr ClientのIP
	 * @return
	 * @throws LockedException 連続ログイン失敗によるロック
	 */
	public boolean setLoginFailInfo(String frontMemberLoginId, String remoteAddr) throws LockedException;
	
	/**
	 * ログイン成功などにより、失敗情報をリセットする。
	 * @param frontMemberLoginId リセットするID
	 * @return
	 */
	public boolean setLoginSuccess(String frontMemberLoginId);
	
	
	/**
	 * ログイン失敗ログを書き込む
	 * @param frontMemberLoginId
	 * @param remoteAddr
	 */
	public void loginFailuerLogWrite(String frontMemberLoginId, String remoteAddr);
	
}
