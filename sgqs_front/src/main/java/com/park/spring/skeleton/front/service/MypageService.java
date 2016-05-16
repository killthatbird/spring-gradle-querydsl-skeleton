package com.park.spring.skeleton.front.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.front.web.form.FrontMember;

public interface MypageService {
	
	/**
	 * リストを取得する
	 * @param searchKey
	 * @param searchValue
	 * @param searchMemberTp
	 * @param pageable
	 * @return Page<TFrontMember>
	 */
	public Page<TFrontMember> getMemberList(String searchKey, String searchValue, short[] searchMemberTp, Pageable pageable);
	
	/**
	 * 会員の詳細情報を取得する
	 * @param frontMemberKey
	 * @return TFrontMember
	 */
	public TFrontMember getMemberDetail(Integer frontMemberKey);
	
	/**
	 * 会員情報を情報・登録する
	 * @param frontMember
	 */
	public void memberEdit(FrontMember frontMember);
	
	
}
