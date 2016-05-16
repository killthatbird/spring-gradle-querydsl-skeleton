package com.park.spring.skeleton.front.web.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.park.spring.skeleton.base.common.MstMasterCode;
import com.park.spring.skeleton.base.entity.TFrontMember;

/**
 * ログインしたユーザー情報をセッションに保持する
 * @author park
 *
 */
public class UserDetail extends User {
	private static final long serialVersionUID = -5331943472789415468L;
	
	private final int		frontMemberKey;
	private final String 	frontMemberLoginId;			//ログインID
	private final String 	frontMemberName;			//お名前
	private final short 	frontMemberTp;				//会員区分
	
	public UserDetail(TFrontMember frontMember) {
		super(frontMember.getFrontMemberLoginId(), frontMember.getFrontMemberLoginPassword(),
				Objects.equal(frontMember.getFrontMemberStatusTp(), MstMasterCode.MEM_NOMAL_STATUS), true, true, !frontMember.getFrontMemberIsLocked(), 
				getGrantedAuthority(frontMember.getFrontMemberTp()+""));
		
		
		frontMemberKey = frontMember.getFrontMemberKey();
		frontMemberLoginId = frontMember.getFrontMemberLoginId();
		frontMemberName = Strings.nullToEmpty(frontMember.getFrontMemberName());
		frontMemberTp = frontMember.getFrontMemberTp();
	}
	
	private static  Collection<GrantedAuthority> getGrantedAuthority(String roles) {
		if (roles == null) return null;
		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority(roles));
		return grantedAuthorities;
	}

	
	public int getFrontMemberKey() {return frontMemberKey;}
	public String getFrontMemberLoginId() {return frontMemberLoginId;}
	public String getFrontMemberName() {return frontMemberName;}
	public short getFrontMemberTp() {return frontMemberTp;}
}
