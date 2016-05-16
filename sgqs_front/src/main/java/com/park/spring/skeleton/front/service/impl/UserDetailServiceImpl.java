package com.park.spring.skeleton.front.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.park.spring.skeleton.base.dao.FrontMemberDao;
import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.front.web.form.UserDetail;

/**
 * SpringSecurityでのログイン処理を行う　UserDetailsService
 * @author park
 */
@Service("userDetailsService") 
public class UserDetailServiceImpl implements UserDetailsService {
	
	private @Autowired FrontMemberDao frontMemberDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		TFrontMember frontMember = (TFrontMember) frontMemberDao.findByFrontMemberLoginId(username);
		if (frontMember == null) {
			throw new UsernameNotFoundException("ユーザーIDもしくはパスワードが存在しません。");
		}
		
		return new UserDetail(frontMember);
	}
	

}