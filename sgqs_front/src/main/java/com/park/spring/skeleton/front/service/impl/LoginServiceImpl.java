package com.park.spring.skeleton.front.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.park.spring.skeleton.base.annotation.DataSource;
import com.park.spring.skeleton.base.constants.DataSourceType;
import com.park.spring.skeleton.base.dao.FrontMemberDao;
import com.park.spring.skeleton.base.dao.FrontMemberLoginLogDao;
import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.base.entity.TFrontMemberLoginLog;
import com.park.spring.skeleton.base.util.DateUtil;
import com.park.spring.skeleton.front.service.LoginService;

@Service("loginServiceImpl")
public class LoginServiceImpl implements LoginService {
	
	private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	private @Autowired FrontMemberDao frontMemberDao;
	private @Autowired FrontMemberLoginLogDao frontMemberLoginLogDao;
	
	private final int MAX_FAIL_CNT = 5;
	private final int MAX_TIME = 3;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@DataSource(DataSourceType.MASTER)
	public boolean setLoginFailInfo(String frontMemberLoginId, String remoteAddr)  {
		
		boolean result = false;
		
	
		TFrontMember frontMember = (TFrontMember) frontMemberDao.findByFrontMemberLoginId(frontMemberLoginId);
		logger.debug("frontMember:" + frontMember);
		if (frontMember != null) {
			short failCnt = 0;
			
			//前回の失敗時間から3時間が経ってない場合
			if (frontMember.getFrontMemberLoginFailDt() != null && DateUtil.diffTimes(frontMember.getFrontMemberLoginFailDt(), new Date()) < MAX_TIME) {
				failCnt = frontMember.getFrontMemberLoginFailCnt();
			}
			
			frontMember.setFrontMemberLoginFailCnt(++failCnt);
			frontMember.setFrontMemberLoginFailDt(new Date());
			
			if (failCnt > MAX_FAIL_CNT) {
				// locked user
				frontMember.setFrontMemberIsLocked(true);
			}
			frontMemberDao.update(frontMember);
			
			logger.debug("failCnt:" + failCnt + ", isLocked:" + frontMember.getFrontMemberIsLocked());
			if (frontMember.getFrontMemberIsLocked()) {
				// throw exception
				throw new LockedException("User Account is locked!");
			}
		}
	
		
		return result;
		
	}
	
	
	@Override
	@Transactional
	@DataSource(DataSourceType.MASTER)
	public boolean setLoginSuccess(String frontMemberLoginId) {
		TFrontMember frontMember = (TFrontMember) frontMemberDao.findByFrontMemberLoginId(frontMemberLoginId);
		if (frontMember != null) {
			
			frontMember.setFrontMemberLoginFailCnt((short)0);
			frontMember.setFrontMemberLoginFailDt(null);
			frontMember.setFrontMemberLoginDt(new Date());
			
		}
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@DataSource(DataSourceType.SLAVE)
	@Override
	public void loginFailuerLogWrite(String frontMemberLoginId, String remoteAddr) {
		try {
			//ログ記録
			TFrontMemberLoginLog frontMemberLoginLog = new TFrontMemberLoginLog();
			frontMemberLoginLog.setFrontMemberLoginLogLoginId(frontMemberLoginId);
			frontMemberLoginLog.setFrontMemberLoginLogLoginIp(remoteAddr);
			frontMemberLoginLog.setFrontMemberLoginLogLoginDt(new Date());
			
			frontMemberLoginLogDao.create(frontMemberLoginLog);
		} catch (Exception e) { }
	}
	

}
