package com.park.spring.skeleton.base.dao;

import java.util.List;

import com.park.spring.skeleton.base.entity.TMstCode;
import com.park.spring.skeleton.base.entity.TMstCodePK;

public interface MstCodeDao extends EntityDao<TMstCode, TMstCodePK> {
	
	/**
	 * 公開されているマスタ情報を取得する
	 * @return
	 */
	public List<TMstCode> findByPublicList();
	
	
	/**
	 * サブキーの説明文を取得する
	 * @param mstCodeSubKey
	 * @return
	 */
	public String findByCodeDesc(short mstCodeSubKey);
}
