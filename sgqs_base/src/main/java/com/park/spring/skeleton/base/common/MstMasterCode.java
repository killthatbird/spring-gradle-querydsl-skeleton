package com.park.spring.skeleton.base.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.park.spring.skeleton.base.dao.MstCodeDao;
import com.park.spring.skeleton.base.entity.TMstCode;
import com.park.spring.skeleton.base.util.StringUtil;
import com.park.spring.skeleton.base.util.ValidateUtil;

/**
 * マスタコードを管理する。
 * @author park
 */
@Component("mstMasterCode")
@Scope(org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MstMasterCode {
	// **********************************************************************
	// 定数
	// **********************************************************************
	private final static Logger logger = LoggerFactory.getLogger(MstMasterCode.class);
	
	public final static short MEM_TYPE = 5000;
	public final static short MEM_TYPE_NORMAL = 5001;
	public final static short MEM_TYPE_SPECIAL = 5002;
	
	public final static short MEM_STATUS = 5300;
	public final static short MEM_APPROVAL_PENDING_STATUS = 5301;
	public final static short MEM_NOMAL_STATUS = 5302;
	public final static short MEM_PAUSE_STATUS = 5303;
	public final static short MEM_CANCEL_STATUS = 5304;
	public final static short MEM_FORCED_CANCEL_STATUS = 5305;
	
	
	/*
	
	public final static short MGT_ROLE = 100;
	public final static short MGT_ROLE_TOP = 101;
	public final static short MGT_ROLE_MID = 102;
	public final static short MGT_ROLE_LOW = 103;
	
	public final static short MST_EMAIL = 200;
	
	public final static short MST_EMAIL_MEM_JOIN = 201;
	public final static short MST_EMAIL_MEM_APPROVAL = 202;
	public final static short MST_EMAIL_INQUIRY = 203;
	public final static short MST_EMAIL_COOPERATION_REQUEST = 204;
	public final static short MST_EMAIL_COOPERATION_REQUEST_AFTER = 205;
	public final static short MST_EMAIL_ORDER_PAYMENT_COMPLETION = 206;
	public final static short MST_EMAIL_INTRODUCER_INSERT = 207;
	public final static short MST_EMAIL_MEM_INFO_APPROVAL_YOURSELF = 208;
	public final static short MST_EMAIL_MEM_INFO_APPROVAL_FAILURE = 209;
	public final static short MST_EMAIL_MEM_INFO_APPROVAL_SUCCESS = 210;
	public final static short MST_EMAIL_SELL_PUBLIC_INFO_CHANGE = 211;
	public final static short MST_EMAIL_MEM_PASSWORD_CHANGE = 212;
	public final static short MST_EMAIL_MEM_BASIC_INFO_PUBLIC_CNT_OVER = 213;
	public final static short MST_EMAIL_MEM_CONTRACT_SUCCESS = 214;
	
	
	public final static short MAIL_RECIPIENT = 300;
	public final static short MAIL_RECIPIENT_CUSTOMER_MANAGER = 301;
	public final static short MAIL_RECIPIENT_CUSTOMER = 302;
	public final static short MAIL_RECIPIENT_MANAGER = 303;
	public final static short MAIL_RECIPIENT_NOTHING = 304;
	
	
	
	public final static short PREFERRED_SCHEME = 5100;
	public final static short SALE_REASON = 5200;
	
	
	
	
	public final static short MATTER_TP = 5500;
	public final static short MATTER_BASE_INFO_TP = 5501;
	public final static short MATTER_DETAIL_INFO_TP = 5502;
	public final static short MATTER_CONTRACT_INFO_TP = 5503;

	
	public final static short MATTER_DETAIL_TP = 5600;
	public final static short MATTER_DETAIL_WAITING_TP = 5601;
	public final static short MATTER_DETAIL_CONFIRM_PAYMENT_TP = 5602;
	public final static short MATTER_DETAIL_FAILURE_TP = 5603;
	public final static short MATTER_DETAIL_CLOSED_TP = 5604;
	
	public final static short HELP_TP = 5700;
	public final static short HELP_IN_COMING_TP = 5701;*/
	// **********************************************************************
	// メンバ
	// **********************************************************************
private List<MasterCodeBean> masterCodeBeans = null;  
	
	private @Autowired  MstCodeDao mstCodeDao;
	
	@Transactional
	@PostConstruct
	public void init() {
		
		logger.info("#### MasterCodeComponent initializing ####");
		
		List<TMstCode> tMstCodes = mstCodeDao.findByPublicList();

		masterCodeBeans = new ArrayList<MasterCodeBean>();
		
        tMstCodes.forEach((mstCode) ->
        	//logger.info("----" + mstCode.getMstCodeNm())
        	masterCodeBeans.add(new MasterCodeBean(mstCode.getTMstCodePK().getMstCodeSubKey(), mstCode.getMstCodeNm()))
        );

	}
	
	/**
	 * 該当の地域名リストを suffix 区切りで返す
	 * @param areaKeys
	 * @param suffix
	 * @return
	 */
	public synchronized String getCodeNames(String[] strMstSubKeys, String suffix) {
		if (strMstSubKeys == null || suffix == null) return null;
		
		StringBuffer rtn = new StringBuffer();
		for (String strMstSubKey : strMstSubKeys) {
			
			String codeName = getCodeName(strMstSubKey);
			if (!Strings.isNullOrEmpty(codeName)) {
				rtn.append(codeName)
					.append(suffix);
			}
		}		
		return StringUtil.endStringDelete(rtn.toString(), suffix);
	}
	
	/**
	 * サブキーの名前を返す
	 * @param mstSubKey String
	 * @return
	 */
	public synchronized String getCodeName(String strMstSubKey) {
		if (!ValidateUtil.isNumber(strMstSubKey)) return null;
		
		return getCodeName(Short.parseShort(strMstSubKey));
	}
	
	/**
	 * サブキーの名前を返す
	 * @param mstSubKey short
	 * @return
	 */
	public synchronized String getCodeName(short mstSubKey) {
		Optional<MasterCodeBean> oMasterCodeBean = getMasterCodeBeans().stream()
			.filter(f -> f.getMstSubKey() == mstSubKey)
			.findFirst();
		
		if (!oMasterCodeBean.isPresent()) {
			return "";
		}
		
		return oMasterCodeBean
					.get().getMstNm();
	}
	
	/**
	 * サブキーの説明文を返す
	 * 現在は管理画面のメールテンプレートのみで使われてるため、
	 * ＤＢで検索する仕様になっているが、
	 * 沢山使われるようになるとメモリにあげるべき
	 * @param mstCodeSubKey 
	 * @return
	 */
	public synchronized String getCodeDesc(short mstCodeSubKey) {
		return mstCodeDao.findByCodeDesc(mstCodeSubKey);
	}
	
	/**
	 * マスタキーのサブリストを返す
	 * @param mstSubKey String
	 * @return
	 */
	public synchronized List<MasterCodeBean> getSubCodeList(String strMstMasterKey) {
		if (!ValidateUtil.isNumber(strMstMasterKey)) return null;
		
		return getSubCodeList(Short.parseShort(strMstMasterKey));
	}
	
	/**
	 * マスタキーのサブリストを返す
	 * @param mstSubKey short
	 * @return
	 */
	public synchronized List<MasterCodeBean> getSubCodeList(short mstMasterKey) {
		
		//100単位じゃない場合
		if ((mstMasterKey + 100) % 100 != 0) {
			return new ArrayList<>();
		}
		
		 return getMasterCodeBeans().stream()
				.filter(f -> f.getMstSubKey() > mstMasterKey && f.getMstSubKey() < mstMasterKey + 100)
				.limit(100)
				.collect(Collectors.toList());
	}
	
	
	
	/**
	 * メモリ保持用のBeanクラス
	 * @author park
	 */
	public class MasterCodeBean {
		private short mstSubKey;
		private String mstNm;
		
		public MasterCodeBean(short mstSubKey, String mstNm) {
			this.mstSubKey = mstSubKey;
			this.mstNm = mstNm;
		}
		public short getMstSubKey() {return mstSubKey;}
		public String getMstNm() {return mstNm;}
		
	}
	
	
	// **********************************************************************
	// プライベートメソッド
	// **********************************************************************
	
	/**
	 * masterCodeBeansを返す。
	 * もし、nullの場合は、init処理をする。
	 * @return
	 */
	private synchronized List<MasterCodeBean> getMasterCodeBeans() {
		if (masterCodeBeans == null) {
			init();
		}
		return masterCodeBeans;
	}
}
