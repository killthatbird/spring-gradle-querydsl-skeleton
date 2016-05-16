package com.park.spring.skeleton.base.util;

import java.security.SecureRandom;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Security関連　Util
 * @author park
 */
public class SecurityUtil {
	public static final int STRENGTH = 256;
	public static final int ITERATIONS = 5;
	
	/**
	 * パスワードを暗号化する
	 * 復号化不可能
	 * @param salt
	 * @param password
	 * @return null or encoder pwd
	 */
	public static String shaPasswordEncoder(String salt, String password) {
		if (salt == null || password == null) {
			return null;
		}
		
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(STRENGTH);
		shaPasswordEncoder.setIterations(ITERATIONS);
		String encPass = shaPasswordEncoder.encodePassword(password, salt);
		
		return encPass;
	}	
	
	/**
	 * パスワードが問題ないのかチェックする。
	 * @param encPass
	 * @param salt
	 * @param password
	 * @return true, false
	 */
	public static boolean isShaPasswordType(String encPass, String salt, String password) {
		if (encPass == null || salt == null || password == null) {
			return false;
		}
		
		boolean isPasswordValid = false;
		try {
			ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(STRENGTH);
			shaPasswordEncoder.setIterations(ITERATIONS);
			
			isPasswordValid = shaPasswordEncoder.isPasswordValid(encPass,password, salt);
		} catch (Exception ex) {
			//無視し、失敗処理する。
		}
		
		return isPasswordValid;
	}

	/**
	 * ログインしているユーザのIDを取得する
	 * @return
	 */
	public static String getLoginUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	
	/**
	 * ログインしているユーザのROLEを取得する
	 * @return
	 */
	public static String getLoginUserRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<? extends GrantedAuthority> optAuthoritie  = auth.getAuthorities().stream().findFirst();
		if (optAuthoritie.isPresent()) {
			return optAuthoritie.get().getAuthority();
		} else {
			return "";
		}
	}
	
	/**
	 * ランダムな英数字からなる指定サイズの文字列を返す。
	 * 
	 * @param size
	 *            文字列のサイズ
	 * @return 生成された文字列
	 */
	public static String getRandomString(final int size) {

		SecureRandom random = new SecureRandom();
		char[] pass = new char[size];

		for (int k = 0; k < pass.length; k++) {
			switch (random.nextInt(3)) {
			case 0: // 'a' - 'z'
				pass[k] = (char) (97 + random.nextInt(26));
				break;
			case 1: // 'A' - 'Z'
				pass[k] = (char) (65 + random.nextInt(26));
				break;
			case 2: // '0' - '9'
				pass[k] = (char) (48 + random.nextInt(10));
				break;
			default:
				pass[k] = 'a';
			}
		}
		// 文字の配列を文字列に変換
		return new String(pass);

	}
}
