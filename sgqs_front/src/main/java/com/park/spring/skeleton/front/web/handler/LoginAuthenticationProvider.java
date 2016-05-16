package com.park.spring.skeleton.front.web.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.park.spring.skeleton.front.service.LoginService;

/**
 * Spring Securityで利用しているDaoAuthenticationProviderを継承し、
 * ログイン成功・失敗時の処理を行う
 * @author park
 */
@Component("authenticationProvider")
public class LoginAuthenticationProvider extends DaoAuthenticationProvider {
	private final static Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class);
	
	@Autowired LoginService loginService;
	
	@Autowired @Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {
			//ログイン
			Authentication auth = super.authenticate(authentication);
			
			/*
			 * ログインに成功した場合は
			 * ログイン失敗情報をリセットする。
			 */
			loginService.setLoginSuccess(authentication.getName());
			return auth;
			
		} catch (BadCredentialsException e) {	
			logger.debug("BadCredentialsException login failure");
			
			//invalid login, update to user_attempts
			try {
				WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
				String ip = details.getRemoteAddress();
				
				loginService.setLoginFailInfo(authentication.getName(), ip);
				loginService.loginFailuerLogWrite(authentication.getName(), ip);
			} catch (LockedException le){
				throw le;
			}
			throw e;
			
		}

	}
	
}