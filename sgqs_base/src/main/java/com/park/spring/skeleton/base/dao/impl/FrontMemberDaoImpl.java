package com.park.spring.skeleton.base.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.primitives.Shorts;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Projections;
import com.park.spring.skeleton.base.common.MstMasterCode;
import com.park.spring.skeleton.base.dao.FrontMemberDao;
import com.park.spring.skeleton.base.entity.QTFrontMember;
import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.base.util.DateUtil;

@Repository
public class FrontMemberDaoImpl extends AbstractQueryDslDao<TFrontMember,QTFrontMember, Integer> implements FrontMemberDao {
	
	public FrontMemberDaoImpl() {
        super(TFrontMember.class, QTFrontMember.tFrontMember);
    }
	
	@Transactional
	@Override
	public TFrontMember findByFrontMemberLoginId(String frontMemberLoginId) {

		return getSelectQuery()
				.where(q.frontMemberLoginId.eq(frontMemberLoginId))
				.uniqueResult(q);
				
	}

	@Override
	public long countByFrontMemberLoginId(String frontMemberLoginId) {
		return getSelectQuery()
				.where(q.frontMemberLoginId.eq(frontMemberLoginId))
				.count();
	}
	

	@Override
	public long countByFrontMemberKey(Integer frontMemberKey) {
		return getSelectQuery()
				.where(q.frontMemberKey.eq(frontMemberKey))
				.count();
	}
	
	@Override
	public Page<TFrontMember> findAll(String searchKey, String searchValue, short[] searchMemberTp, Date frontMemberRegSDt, Date frontMemberRegEDt,
			Pageable pageable) {
		
		BooleanBuilder predicate = new BooleanBuilder();
		
		//日付の検索
		if (frontMemberRegSDt != null || frontMemberRegEDt != null) {
			//開始日を指定してない場合
			if (frontMemberRegSDt == null) {
				frontMemberRegSDt = DateUtil.toDate("1900-01-01");
			}
			
			//終了日を指定してない場合
			if (frontMemberRegEDt == null) {
				frontMemberRegEDt = DateUtil.toDate("9999-01-01");
			}
			
			predicate.and(q.frontMemberRegDt.between(DateUtil.toFirstTime(frontMemberRegSDt), DateUtil.toLastTime(frontMemberRegEDt)));
		}
		
		//キーワード検索
		if (!Strings.isNullOrEmpty(searchKey) && !Strings.isNullOrEmpty(searchValue)) {
			if (searchKey.equals("frontMemberLoginId")) {
				predicate.and(q.frontMemberLoginId.like("%"+searchValue+"%"));
			} else if (searchKey.equals("frontMemberNm")) {
				predicate.and(q.frontMemberName.like("%"+searchValue+"%"));				
			} else if (searchKey.equals("frontEmail")) {
				predicate.and(q.frontMemberEmail.like("%"+searchValue+"%"));				
			} 
		}
		
		if (searchMemberTp != null) {
			predicate.and(q.frontMemberTp.in(Shorts.asList(searchMemberTp)));
		}
		
		return findAll(predicate, pageable);
	}
	
	@Override
	public List<TFrontMember> findByFrontMemberRegDtAfterList(Date frontMemberRegDt, long limit) {
		return getSelectQuery()
				.where(q.frontMemberRegDt.after(frontMemberRegDt))
				.orderBy(q.frontMemberKey.desc())
				.limit(limit)
				.list(Projections.bean(q, q.frontMemberLoginId, q.frontMemberName,q.frontMemberTp, q.frontMemberStatusTp, q.frontMemberRegDt));
	}
	

	@Override
	public long countByFrontMemberApprovalPending() {
		return getSelectQuery()
				.where(q.frontMemberStatusTp.eq(MstMasterCode.MEM_APPROVAL_PENDING_STATUS))
				.count();
	}
	
	@Override
	public long countByNomalCurrentMatter() {
		return getSelectQuery()
				.where(
						q.frontMemberTp.eq(MstMasterCode.MEM_TYPE_NORMAL)
						.and(q.frontMemberStatusTp.eq(MstMasterCode.MEM_NOMAL_STATUS))
					)
				.count();
	}

	

	@Override
	public long updateIsApprovedByFrontMemberKeys(Integer[] frontMemberKeys) {
		return getUpdateQuery()
			.set(q.frontMemberStatusTp, MstMasterCode.MEM_NOMAL_STATUS)
			.set(q.frontMemberApprovalDt, new Date())
			.set(q.frontMemberUpdDt, new Date())
			.where(q.frontMemberKey.in(frontMemberKeys))
			.execute();
	}

	@Override
	public TFrontMember findByFrontMember(String frontMemberCompany,
			String frontMemberEmail, String frontMemberName,
			String frontMemberLastName) {
		return getSelectQuery()
				.where(
					q.frontMemberEmail.eq(frontMemberEmail)
					.and(q.frontMemberName.eq(frontMemberName))
				)
				.uniqueResult(q);
	}


	@Override
	public Short findByFrontMemberStatusTp(Integer frontMemberKey) {
		return getSelectQuery()
				.where(q.frontMemberKey.eq(frontMemberKey))
				.uniqueResult(q.frontMemberStatusTp);
	}

	
	
}
