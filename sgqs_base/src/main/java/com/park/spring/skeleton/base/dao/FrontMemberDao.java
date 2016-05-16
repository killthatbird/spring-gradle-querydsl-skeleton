package com.park.spring.skeleton.base.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.park.spring.skeleton.base.entity.TFrontMember;

public interface FrontMemberDao extends EntityDao<TFrontMember, Integer> {
	
	/**
	 * 会員の状態を取得する
	 * @param frontMemberKey
	 * @return
	 */
	public Short findByFrontMemberStatusTp(Integer frontMemberKey);
	
	/**
	 * LoginIdを検索する。
	 * @param frontMemberLoginId
	 * @return TFrontMember
	 */
	public TFrontMember findByFrontMemberLoginId(String frontMemberLoginId);
	
	/**
	 * 該当のIDが登録されている件数を検索する
	 * @param frontMemberLoginId
	 * @return
	 */
	public long countByFrontMemberLoginId(String frontMemberLoginId);
	
	/**
	 * 該当の会員キーの登録されている件数を検索する
	 * @param frontMemberKey
	 * @return
	 */
	public long countByFrontMemberKey(Integer frontMemberKey);

	/**
	 * 該当の条件の会員リストを取得する。
	 * @param isApproved
	 * @param searchKey
	 * @param searchValue
	 * @param searchMemberTp
	 * @param frontMemberRegSDt
	 * @param frontMemberRegEDt
	 * @param pageable
	 * @return
	 */
	public Page<TFrontMember> findAll(String searchKey, String searchValue, short[] searchMemberTp, Date frontMemberRegSDt, Date frontMemberRegEDt, Pageable pageable);
	
	/**
	 * 指定登録日の以降のデータを limit件数分取得する
	 * @param frontMemberRegDt
	 * @param limit
	 * @return
	 */
	public List<TFrontMember> findByFrontMemberRegDtAfterList(Date frontMemberRegDt, long limit);
	

	/**
	 * 該当の情報の会員を検索する
	 * @param frontMemberCompany
	 * @param frontMemberEmail
	 * @param frontMemberFirstName
	 * @param frontMemberLastName
	 * @return
	 */
	public TFrontMember findByFrontMember(String frontMemberCompany, String frontMemberEmail, String frontMemberFirstName, String frontMemberLastName);
	

	
	/**
	 * 認証待ちの会員の件数を取得する
	 * @return
	 */
	public long countByFrontMemberApprovalPending(); 
	
	/**
	 * 現在の案件数を計算する
	 * @return 案件数
	 */
	public long countByNomalCurrentMatter();
	
	/**
	 * 会員の承認処理を行う
	 * @param frontMemberKeys 承認する会員のキー
	 * @return 承認された数
	 */
	public long updateIsApprovedByFrontMemberKeys(Integer[] frontMemberKeys);
	
}