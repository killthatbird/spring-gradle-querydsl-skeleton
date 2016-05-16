package com.park.spring.skeleton.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.park.spring.skeleton.base.dao.MstCodeDao;
import com.park.spring.skeleton.base.entity.QTMstCode;
import com.park.spring.skeleton.base.entity.TMstCode;
import com.park.spring.skeleton.base.entity.TMstCodePK;

@Repository("mstCodeDao")
public class MstCodeDaoImpl extends AbstractQueryDslDao<TMstCode, QTMstCode,  TMstCodePK>  implements MstCodeDao {
	
	public MstCodeDaoImpl() {
        super(TMstCode.class, QTMstCode.tMstCode);
    }

	
	@Transactional
	@Override
	public List<TMstCode> findByPublicList() {
		
		return getSelectQuery()
			.where(q.mstCodeIsEnabled.isTrue())
			.orderBy(q.mstCodePriority.asc())
			.list(q);
		
	}


	@Override
	public String findByCodeDesc(short mstCodeSubKey) {
		return getSelectQuery()
				.where(q.tMstCodePK.mstCodeSubKey.eq(mstCodeSubKey))
				.uniqueResult(q.mstCodeDesc);
	}

}
