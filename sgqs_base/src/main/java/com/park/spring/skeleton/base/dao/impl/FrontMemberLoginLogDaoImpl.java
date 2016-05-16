package com.park.spring.skeleton.base.dao.impl;


import org.springframework.stereotype.Repository;

import com.park.spring.skeleton.base.dao.FrontMemberLoginLogDao;
import com.park.spring.skeleton.base.entity.QTFrontMemberLoginLog;
import com.park.spring.skeleton.base.entity.TFrontMemberLoginLog;

@Repository("frontMemberLoginLogDao")
public class FrontMemberLoginLogDaoImpl extends AbstractQueryDslDao<TFrontMemberLoginLog, QTFrontMemberLoginLog, Integer>  implements FrontMemberLoginLogDao {
	
	public FrontMemberLoginLogDaoImpl() {
        super(TFrontMemberLoginLog.class, QTFrontMemberLoginLog.tFrontMemberLoginLog);
    }

}
