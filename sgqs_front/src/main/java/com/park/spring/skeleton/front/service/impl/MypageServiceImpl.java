package com.park.spring.skeleton.front.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.park.spring.skeleton.base.dao.FrontMemberDao;
import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.base.exception.ApplicationException;
import com.park.spring.skeleton.base.exception.ValidateException;
import com.park.spring.skeleton.base.util.SecurityUtil;
import com.park.spring.skeleton.front.service.MypageService;
import com.park.spring.skeleton.front.web.form.FrontMember;

@Transactional
@Service("mypageServiceImpl") 
public class MypageServiceImpl implements MypageService {
	// **********************************************************************
	// 定数
	// **********************************************************************
	private final static Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
	
	// **********************************************************************
	// メンバ
	// **********************************************************************
	private @Autowired FrontMemberDao frontMemberDao;
	
	// **********************************************************************
	// Override
	// **********************************************************************
	@Override
	public Page<TFrontMember> getMemberList(String searchKey,
			String searchValue, short[] searchMemberTp, Pageable pageable) {
		return frontMemberDao.findAll(searchKey, searchValue, searchMemberTp, null, null, pageable);
	}
	
	@Override
	public TFrontMember getMemberDetail(Integer frontMemberKey) {
		return frontMemberDao.findByPk(frontMemberKey);
	}

	@Transactional
	@Override
	public void memberEdit(FrontMember frontMember) {
		
		if (frontMember.getFrontMemberKey() == null) {
			//insert
			String password = frontMember.getFrontMemberLoginPassword();
			String loginId = frontMember.getFrontMemberLoginId();
			if (password == null) {
				throw new ValidateException("frontMemberLoginPassword", "javax.validation.constraints.NotNull.message");
			}
			if (loginId == null) {
				throw new ValidateException("frontMemberLoginId", "javax.validation.constraints.NotNull.message");
			}
			if (frontMemberDao.findByFrontMemberLoginId(loginId) != null){
				throw new ValidateException("frontMemberLoginId", "validation.duplication.message");
			}
			
			TFrontMember tFrontMember = new TFrontMember();
			tFrontMember.setFrontMemberIsLocked(frontMember.getFrontMemberIsLocked());
			tFrontMember.setFrontMemberLoginId(loginId);
			
			String encPass = SecurityUtil.shaPasswordEncoder(tFrontMember.getFrontMemberLoginId(), password);
			if (encPass != null && SecurityUtil.isShaPasswordType(encPass, tFrontMember.getFrontMemberLoginId(), password)) {
				tFrontMember.setFrontMemberLoginPassword(encPass);
			} else {
				logger.error("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember);
				throw new ApplicationException("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember, "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
			}
			
			tFrontMember.setFrontMemberName(frontMember.getFrontMemberName());
			tFrontMember.setFrontMemberStatusTp(frontMember.getFrontMemberStatusTp());
			tFrontMember.setFrontMemberTp(frontMember.getFrontMemberTp());
			tFrontMember.setFrontMemberRegDt(new Date());
			tFrontMember.setFrontMemberUpdDt(new Date());
			frontMemberDao.create(tFrontMember);
			
		} else {
			//update
			
			TFrontMember tFrontMember = getMemberDetail(frontMember.getFrontMemberKey());
			if (tFrontMember.getFrontMemberIsLocked() && !frontMember.getFrontMemberIsLocked()) {
				tFrontMember.setFrontMemberLoginFailCnt((short)0);
			}
			tFrontMember.setFrontMemberIsLocked(frontMember.getFrontMemberIsLocked());
			tFrontMember.setFrontMemberName(frontMember.getFrontMemberName());
			tFrontMember.setFrontMemberStatusTp(frontMember.getFrontMemberStatusTp());
			tFrontMember.setFrontMemberTp(frontMember.getFrontMemberTp());
			tFrontMember.setFrontMemberUpdDt(new Date());
			
			String password = frontMember.getFrontMemberLoginPassword();
			if (password != null) {
				String encPass = SecurityUtil.shaPasswordEncoder(tFrontMember.getFrontMemberLoginId(), password);
				
				if (encPass != null && SecurityUtil.isShaPasswordType(encPass, tFrontMember.getFrontMemberLoginId(), password)) {
					tFrontMember.setFrontMemberLoginPassword(encPass);
				} else {
					logger.error("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember);
					throw new ApplicationException("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember, "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
				}
			}
			
			frontMemberDao.update(tFrontMember);
		}
	}

	

	
	/*
	@Override
	public Page<TFrontMember> getAuthMemberList(String searchKey,
			String searchValue, short[] searchMemberTp, Pageable pageable) {
		return frontMemberDao.findAll(false, searchKey, searchValue, searchMemberTp, null, null, pageable);
	}

	@Override
	public Page<TFrontMember> getMemberBuyerList(String searchKey,
			String searchValue, Date frontMemberRegSDt, Date frontMemberRegEDt, Pageable pageable) {
		return frontMemberDao.findAll(true, searchKey, searchValue, new short[]{MstMasterCode.MEM_TYPE_BUY}, frontMemberRegSDt, frontMemberRegEDt, pageable);
	}


	@Override
	public Page<TFrontSeller> getMemberSellerList(String searchKey,
			String searchValue, Date frontSellerRegSDt, Date frontSellerRegEDt, Pageable pageable) {
		return frontSellerDao.findAll(true, searchKey, searchValue, frontSellerRegSDt, frontSellerRegEDt, pageable);
	}
	
	@Override
	public List<TFrontMember> getNewMemberList(int newDayCnt, int newCnt) {
		return frontMemberDao.findByFrontMemberRegDtAfterList(DateUtil.minusDay(new Date(), newDayCnt), newCnt);
	}
	

	@Override
	public Page<TFrontMemberMatter> getFrontMemberMatters(String searchKey,
			String searchValue, Short frontMemberMatterTp, Short frontMemberMatterDetailTp, 
			Date frontMemberMatterContractUpdSDt, Date frontMemberMatterContractUpdEDt, Pageable pageable) {
		return frontMemberMatterDao.findAllMatterList(searchKey, searchValue, frontMemberMatterTp, frontMemberMatterDetailTp, frontMemberMatterContractUpdSDt, frontMemberMatterContractUpdEDt, pageable);
	}
	
	
	@Override
	public long getAuthMemberCnt() {
		return frontMemberDao.countByFrontMemberApprovalPending();
	}
	
	
	@Override
	public void setMemberApprove(Integer[] frontMemberKeys) {
		//frontMemberDao.updateIsApprovedByFrontMemberKeys(frontMemberKeys);
		
		Date currDate = new Date();
		TMgtBasicInfo mgtBasicInfo = mgtBasicInfoDao.findByMgtBasicInfoIsEnabled();
		
		for (Integer frontMemberKey : frontMemberKeys) {
			
			TFrontMember frontMember = frontMemberDao.findByPk(frontMemberKey);
			if (frontMember == null) continue;
			
			final String password = SecurityUtil.getRandomString(8);
			String encPass = SecurityUtil.shaPasswordEncoder(frontMember.getFrontMemberLoginId(), password);
			if (encPass != null && SecurityUtil.isShaPasswordType(encPass, frontMember.getFrontMemberLoginId(), password)) {
				frontMember.setFrontMemberLoginPassword(encPass);
				frontMember.setFrontMemberLoginDeCryptPassword(password);
			} else {
				logger.error("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember);
				throw new ApplicationException("SecurityUtil.shaPasswordEncoder error frontMember : " + frontMember, "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
			}
			
			frontMember.setFrontMemberStatusTp(MstMasterCode.MEM_NOMAL_STATUS);
			frontMember.setFrontMemberApprovalDt(currDate);
			frontMember.setFrontMemberUpdDt(currDate);
			
			
			if (Objects.equal(frontMember.getFrontMemberTp(), MstMasterCode.MEM_TYPE_BUY)) {
				
				TFrontBuyer frontBuyer = frontMember.getTFrontBuyer();
				if (frontBuyer == null) continue;
				
				//メール送信(承認・有無)
				try {
					mailManager.type(MstMasterCode.MST_EMAIL_MEM_APPROVAL)
								.to(frontBuyer.getFrontBuyerEmail())
								.toNm(frontBuyer.getFrontBuyerLastName() + " " + frontBuyer.getFrontBuyerFirstName())
								.send(frontMember);
					
				} catch (ApplicationException e) {
					logger.error("mail sender exception ", e);
				}
				
			} else {
				
				TFrontSeller frontSeller = frontMember.getTFrontSeller();
				if (frontSeller == null) continue;
				
				frontSeller.setFrontSellerBasicInfoPublicCnt(mgtBasicInfo.getMgtBasicInfoMemBasicInfoPublicCnt());
				frontSeller.setFrontSellerEnableDt(DateUtil.addDay(currDate, mgtBasicInfo.getMgtBasicInfoMemEnableDay()));
				frontSeller.setFrontSellerEnableStartDt(currDate);
				//メール送信(承認・有無)
				try {
					mailManager.type(MstMasterCode.MST_EMAIL_MEM_APPROVAL)
								.to(frontSeller.getFrontSellerEmail())
								.toNm(frontSeller.getFrontSellerLastName() + " " + frontSeller.getFrontSellerFirstName())
								.send(frontMember);
					
				} catch (ApplicationException e) {
					logger.error("mail sender exception ", e);
				}
		
			}
			
			//パスワードを消す
			frontMember.setFrontMemberLoginDeCryptPassword(null);
		}
	}


	@Override
	public TFrontMember getMemberDetail(Integer frontMemberKey) {
		if (frontMemberKey == null) return null;
		
		TFrontMember frontMember = frontMemberDao.findByPk(frontMemberKey);
		if (frontMember != null) {
			if (Objects.equal(frontMember.getFrontMemberTp(), MstMasterCode.MEM_TYPE_BUY)) {
				frontMember.setTFrontBuyer(frontBuyerDao.findByPk(frontMemberKey));
			} else {
				frontMember.setTFrontSeller(frontSellerDao.findByPk(frontMemberKey));
			}
		}
		return frontMember;
	}
	
	@Override
	public TFrontMemberMatterBuyer getMatterBuyerDetail(Integer frontBuyerKey,
			Integer frontSellerKey, Short frontMemberMatterMemberTp) {
		return frontMemberMatterBuyerDao.findByPk(new TFrontMemberMatterPK(frontBuyerKey, frontSellerKey, frontMemberMatterMemberTp));
	}

	@Override
	public TFrontMemberMatterSeller getMatterSellerDetail(
			Integer frontBuyerKey, Integer frontSellerKey,
			Short frontMemberMatterMemberTp) {
		return frontMemberMatterSellerDao.findByPk(new TFrontMemberMatterPK(frontBuyerKey, frontSellerKey, frontMemberMatterMemberTp));
	}
	

	@Override
	public TFrontMemberMatter getMatterDetail(Integer frontBuyerKey,
			Integer frontSellerKey, Short frontMemberMatterMemberTp) {
		
		return frontMemberMatterDao.findByMatterDetail(frontBuyerKey, frontSellerKey, frontMemberMatterMemberTp);
	}
	

	@Override
	public void setMatter(TFrontMemberMatter frontMemberMatter) {
		frontMemberMatterDao.updateByMatterInfo(frontMemberMatter);
	}
	
	
	@Override
	public TFrontSeller getSellerPublicInfo(Integer frontSellerKey) {
		return frontSellerDao.findByPublicInfo(frontSellerKey);
	}
	
	
	@Override
	public void setSellerPublicInfo(SellerPublicInfoForm sellerPublicInfoForm) {
		frontSellerDao.updateByPublicInfo(sellerPublicInfoForm.getFrontSellerKey(), sellerPublicInfoForm.getFrontSellerBasicInfoPublicCnt(), sellerPublicInfoForm.getFrontSellerEnableDt());
		
		if (sellerPublicInfoForm.getIsSenderMail()) {
			TFrontSeller frontSeller = frontSellerDao.findByPk(sellerPublicInfoForm.getFrontSellerKey());
			try {
				if (frontSeller != null) {
					mailManager.type(MstMasterCode.MST_EMAIL_SELL_PUBLIC_INFO_CHANGE)
								.to(frontSeller.getFrontSellerEmail())
								.toNm(frontSeller.getFrontSellerLastName() + " " + frontSeller.getFrontSellerFirstName())
								.send(frontSeller);
				}
			} catch (ApplicationException e) {
				logger.error("mail sender exception ", e);
			}
		}
	}
	

	@Override
	public void update(TFrontBuyer paraFrontBuyer) {
		
		Date currDate = new Date();
		
		//GET DATA
		TMgtBasicInfo mgtBasicInfo = mgtBasicInfoDao.findByMgtBasicInfoIsEnabled();
		TFrontMember frontMember = getMemberDetail(paraFrontBuyer.getFrontBuyerKey());
		
		if (frontMember == null) {
			throw new ApplicationException("frontMember data not found frontBuyerKey:" + paraFrontBuyer.getFrontBuyerKey(), "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		TFrontBuyer frontBuyer = frontMember.getTFrontBuyer();
		if (frontBuyer == null) {
			throw new ApplicationException("frontBuyer data not found frontBuyerKey:" + paraFrontBuyer.getFrontBuyerKey(), "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		frontBuyerCompanyAreaDao.deleteByFrontBuyer(frontBuyer);
		*//********************
		 * 入力値・各種チェック
		 ********************//*
		if (mgtBasicInfo == null) {
			throw new ApplicationException("base-info not setting", "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		//ファイルチェック　&　紹介者チェック
		if (Strings.nullToEmpty(paraFrontBuyer.getSelectCodeFile()).equals("introducerNum")) {
			long cnt = mgtIntroducerDao.countByMgtIntroducerNum(paraFrontBuyer.getFrontBuyerIntroducerNum());
			if (cnt == 0) {
				throw new ValidateException("frontBuyerIntroducerNum","validation.introducer.not.found");
			}
			
			paraFrontBuyer.setFrontBuyerCommercialRegistrationFileName(null);
			paraFrontBuyer.setFrontBuyerFinancialStatementsFileName(null);
		} else if (Strings.nullToEmpty(paraFrontBuyer.getSelectCodeFile()).equals("fileUpload")) {
			if (!FileUtil.isFile(globals.getUploadTempPath() + paraFrontBuyer.getFrontBuyerCommercialRegistrationFileName())) {
				throw new ValidateException("frontBuyerCommercialRegistrationFileName","validation.upload.error.message");
			}
			if (!FileUtil.isFile(globals.getUploadTempPath() + paraFrontBuyer.getFrontBuyerFinancialStatementsFileName())) {
				throw new ValidateException("frontBuyerFinancialStatementsFileName","validation.upload.error.message");
			}
			
			paraFrontBuyer.setFrontBuyerIntroducerNum(null);
		} 
		
		
		*//********************
		 * データ設定
		 ********************//*
		if (Strings.nullToEmpty(paraFrontBuyer.getSelectCodeFile()).equals("fileUpload")) {
			//ファイルコピー & 削除
			newFileChange(paraFrontBuyer.getFrontBuyerCommercialRegistrationFileName(), frontBuyer.getFrontBuyerCommercialRegistrationFileName());
			newFileChange(paraFrontBuyer.getFrontBuyerFinancialStatementsFileName(), frontBuyer.getFrontBuyerFinancialStatementsFileName());
		
		} 
		
		frontBuyer.setFrontBuyerFirstName(paraFrontBuyer.getFrontBuyerFirstName());
		frontBuyer.setFrontBuyerFirstNameKana(paraFrontBuyer.getFrontBuyerFirstNameKana());
		
		frontBuyer.setFrontBuyerLastName(paraFrontBuyer.getFrontBuyerLastName());
		frontBuyer.setFrontBuyerLastNameKana(paraFrontBuyer.getFrontBuyerLastNameKana());
		
		frontBuyer.setFrontBuyerPosition(paraFrontBuyer.getFrontBuyerPosition());
		
		frontBuyer.setFrontBuyerCompanyTel(paraFrontBuyer.getFrontBuyerCompanyTel());
		frontBuyer.setFrontBuyerCompanyZipCode(paraFrontBuyer.getFrontBuyerCompanyZipCode());
		frontBuyer.setFrontBuyerCompanyAddress(paraFrontBuyer.getFrontBuyerCompanyAddress());
		frontBuyer.setFrontBuyerOfficeTel(paraFrontBuyer.getFrontBuyerOfficeTel());
		frontBuyer.setFrontBuyerOfficeZipCode(paraFrontBuyer.getFrontBuyerOfficeZipCode());
		frontBuyer.setFrontBuyerOfficeAddress(paraFrontBuyer.getFrontBuyerOfficeAddress());
		
		frontBuyer.setFrontBuyerCompany(paraFrontBuyer.getFrontBuyerCompany());
		frontBuyer.setFrontBuyerBusinessTypeMain(paraFrontBuyer.getFrontBuyerBusinessTypeMain());
		frontBuyer.setFrontBuyerBusinessTypeSub1(paraFrontBuyer.getFrontBuyerBusinessTypeSub1());
		frontBuyer.setFrontBuyerBusinessTypeSub2(paraFrontBuyer.getFrontBuyerBusinessTypeSub2());
		
		frontBuyer.setFrontBuyerEmail(paraFrontBuyer.getFrontBuyerEmail());
		
		frontBuyer.setFrontBuyerUrl(paraFrontBuyer.getFrontBuyerUrl());
		
		frontBuyer.setFrontBuyerIntroducerNum(paraFrontBuyer.getFrontBuyerIntroducerNum());
		frontBuyer.setFrontBuyerFinancialStatementsFileName(paraFrontBuyer.getFrontBuyerFinancialStatementsFileName());
		frontBuyer.setFrontBuyerCommercialRegistrationFileName(paraFrontBuyer.getFrontBuyerCommercialRegistrationFileName());
		
		frontBuyer.setFrontBuyerNetSales(paraFrontBuyer.getFrontBuyerNetSales());
		
		frontBuyer.setFrontBuyerEmployeeCnt(paraFrontBuyer.getFrontBuyerEmployeeCnt());
		frontBuyer.setFrontBuyerFoundedYm(paraFrontBuyer.getFrontBuyerFoundedYm());
		frontBuyer.setFrontBuyerOfficesCnt(paraFrontBuyer.getFrontBuyerOfficesCnt());
		
		
		frontBuyer.setFrontBuyerRemarks(paraFrontBuyer.getFrontBuyerRemarks());
		frontBuyer.setFrontBuyerBusinessOutline(paraFrontBuyer.getFrontBuyerBusinessOutline());
		
		if (paraFrontBuyer.getTFrontBuyerCompanyAreaList() != null) {
			paraFrontBuyer.getTFrontBuyerCompanyAreaList().removeIf(frontBuyerCompanyArea -> frontBuyerCompanyArea.getTFrontBuyerCompanyAreaPK() == null ||
					frontBuyerCompanyArea.getTFrontBuyerCompanyAreaPK().getFrontBuyerCompanyAreaKey() <= 0);
			paraFrontBuyer.getTFrontBuyerCompanyAreaList().stream()
					.forEach(frontBuyerCompanyArea -> frontBuyerCompanyArea.getTFrontBuyerCompanyAreaPK().setFrontBuyerKey(paraFrontBuyer.getFrontBuyerKey()));
		}
		
		frontBuyer.setTFrontBuyerCompanyAreaList(paraFrontBuyer.getTFrontBuyerCompanyAreaList());
		frontBuyer.setFrontBuyerUpdDt(currDate);
		
		//FrontMember
		frontMember.setFrontMemberCompany(paraFrontBuyer.getFrontBuyerCompany());
		frontMember.setFrontMemberFirstNm(paraFrontBuyer.getFrontBuyerFirstName());
		frontMember.setFrontMemberLastNm(paraFrontBuyer.getFrontBuyerLastName());
		frontMember.setFrontMemberPosition(paraFrontBuyer.getFrontBuyerPosition());
		frontMember.setFrontMemberEmail(paraFrontBuyer.getFrontBuyerEmail());
		frontMember.setFrontMemberOfficeTel(paraFrontBuyer.getFrontBuyerOfficeTel());
	}


	@Override
	public void update(TFrontSeller paraFrontSeller) {
		
		Date currDate = new Date();
		
		//GET DATA
		TMgtBasicInfo mgtBasicInfo = mgtBasicInfoDao.findByMgtBasicInfoIsEnabled();
		TFrontMember frontMember = getMemberDetail(paraFrontSeller.getFrontSellerKey());
		
		if (frontMember == null) {
			throw new ApplicationException("frontMember data not found frontSellerKey:" + paraFrontSeller.getFrontSellerKey(), "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		TFrontSeller frontSeller = frontMember.getTFrontSeller();
		if (frontSeller == null) {
			throw new ApplicationException("frontSeller data not found frontSellerKey:" + paraFrontSeller.getFrontSellerKey(), "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		frontSellerCompanyAreaDao.deleteByFrontSeller(frontSeller);
		frontSellerPreferredSchemeDao.deleteByFrontSeller(frontSeller);
		frontSellerSaleReasonDao.deleteByFrontSeller(frontSeller);
		
		*//********************
		 * 入力値・各種チェック
		 ********************//*
		if (mgtBasicInfo == null) {
			throw new ApplicationException("base-info not setting", "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		//ファイルチェック　&　紹介者チェック
		if (Strings.nullToEmpty(paraFrontSeller.getSelectCodeFile()).equals("introducerNum")) {
			long cnt = mgtIntroducerDao.countByMgtIntroducerNum(paraFrontSeller.getFrontSellerIntroducerNum());
			if (cnt == 0) {
				throw new ValidateException("frontSellerIntroducerNum","validation.introducer.not.found");
			}
			
			paraFrontSeller.setFrontSellerCommercialRegistrationFileName(null);
			paraFrontSeller.setFrontSellerFinancialStatementsFileName(null);
		} else if (Strings.nullToEmpty(paraFrontSeller.getSelectCodeFile()).equals("fileUpload")) {
			if (!FileUtil.isFile(globals.getUploadTempPath() + paraFrontSeller.getFrontSellerCommercialRegistrationFileName())) {
				throw new ValidateException("frontSellerCommercialRegistrationFileName","validation.upload.error.message");
			}
			if (!FileUtil.isFile(globals.getUploadTempPath() + paraFrontSeller.getFrontSellerFinancialStatementsFileName())) {
				throw new ValidateException("frontSellerFinancialStatementsFileName","validation.upload.error.message");
			}
			
			paraFrontSeller.setFrontSellerIntroducerNum(null);
		} 
		
		
		
		*//********************
		 * データ設定
		 ********************//*
		if (Strings.nullToEmpty(paraFrontSeller.getSelectCodeFile()).equals("fileUpload")) {
			//ファイルコピー & 削除
			newFileChange(paraFrontSeller.getFrontSellerCommercialRegistrationFileName(), frontSeller.getFrontSellerCommercialRegistrationFileName());
			newFileChange(paraFrontSeller.getFrontSellerFinancialStatementsFileName(), frontSeller.getFrontSellerFinancialStatementsFileName());
		} 
		
		frontSeller.setFrontSellerFirstName(paraFrontSeller.getFrontSellerFirstName());
		frontSeller.setFrontSellerFirstNameKana(paraFrontSeller.getFrontSellerFirstNameKana());
		frontSeller.setFrontSellerLastName(paraFrontSeller.getFrontSellerLastName());
		frontSeller.setFrontSellerLastNameKana(paraFrontSeller.getFrontSellerLastNameKana());
		
		frontSeller.setFrontSellerPosition(paraFrontSeller.getFrontSellerPosition());
		
		frontSeller.setFrontSellerCompanyTel(paraFrontSeller.getFrontSellerCompanyTel());
		frontSeller.setFrontSellerCompanyZipCode(paraFrontSeller.getFrontSellerCompanyZipCode());
		frontSeller.setFrontSellerCompanyAddress(paraFrontSeller.getFrontSellerCompanyAddress());
		frontSeller.setFrontSellerOfficeTel(paraFrontSeller.getFrontSellerOfficeTel());
		frontSeller.setFrontSellerOfficeZipCode(paraFrontSeller.getFrontSellerOfficeZipCode());
		frontSeller.setFrontSellerOfficeAddress(paraFrontSeller.getFrontSellerOfficeAddress());
		
		frontSeller.setFrontSellerCompany(paraFrontSeller.getFrontSellerCompany());
		
		frontSeller.setFrontSellerEmail(paraFrontSeller.getFrontSellerEmail());
		
		frontSeller.setFrontSellerUrl(paraFrontSeller.getFrontSellerUrl());
		
		frontSeller.setFrontSellerIntroducerNum(paraFrontSeller.getFrontSellerIntroducerNum());
		frontSeller.setFrontSellerCommercialRegistrationFileName(paraFrontSeller.getFrontSellerCommercialRegistrationFileName());
		frontSeller.setFrontSellerFinancialStatementsFileName(paraFrontSeller.getFrontSellerFinancialStatementsFileName());
		

		frontSeller.setFrontSellerBusinessTypeMain(paraFrontSeller.getFrontSellerBusinessTypeMain());
		frontSeller.setFrontSellerBusinessTypeSub1(paraFrontSeller.getFrontSellerBusinessTypeSub1());
		frontSeller.setFrontSellerBusinessTypeSub2(paraFrontSeller.getFrontSellerBusinessTypeSub2());
		
		frontSeller.setFrontSellerEmployeeCnt(paraFrontSeller.getFrontSellerEmployeeCnt());
		
		frontSeller.setFrontSellerFoundedYm(paraFrontSeller.getFrontSellerFoundedYm());
		frontSeller.setFrontSellerIntellectualProperty(paraFrontSeller.getFrontSellerIntellectualProperty());
		
		
		frontSeller.setFrontSellerManufacturingEquip(paraFrontSeller.getFrontSellerManufacturingEquip());
		frontSeller.setFrontSellerNetAssets(paraFrontSeller.getFrontSellerNetAssets());
		frontSeller.setFrontSellerNetSales(paraFrontSeller.getFrontSellerNetSales());
		frontSeller.setFrontSellerOfficesCnt(paraFrontSeller.getFrontSellerOfficesCnt());
		frontSeller.setFrontSellerOparatingIncome(paraFrontSeller.getFrontSellerOparatingIncome());
		
		frontSeller.setFrontSellerPreferredPrice(paraFrontSeller.getFrontSellerPreferredPrice());
		frontSeller.setFrontSellerRealEstate(paraFrontSeller.getFrontSellerRealEstate());
		
		frontSeller.setFrontSellerTotalLiabilities(paraFrontSeller.getFrontSellerTotalLiabilities());
		
		
		frontSeller.setFrontSellerBusinessOutline(paraFrontSeller.getFrontSellerBusinessOutline());
		frontSeller.setFrontSellerRemarks(paraFrontSeller.getFrontSellerRemarks());
		
		frontSeller.setFrontSellerSpecialAbility(paraFrontSeller.getFrontSellerSpecialAbility());
		frontSeller.setFrontSellerLicenses(paraFrontSeller.getFrontSellerLicenses());
		
		frontSeller.setFrontSellerTrienniumNetSales(paraFrontSeller.getFrontSellerTrienniumNetSales());
		frontSeller.setFrontSellerTrienniumOparatingIncome(paraFrontSeller.getFrontSellerTrienniumOparatingIncome());
		frontSeller.setFrontSellerRecentLoanValue(paraFrontSeller.getFrontSellerRecentLoanValue());
		frontSeller.setFrontSellerRecentNetAssets(paraFrontSeller.getFrontSellerRecentNetAssets());
		
		
		if (paraFrontSeller.getTFrontSellerCompanyAreaList() != null) {
			//TFrontSellerCompanyArea
			paraFrontSeller.getTFrontSellerCompanyAreaList().removeIf(frontSellerCompanyArea -> frontSellerCompanyArea.getTFrontSellerCompanyAreaPK() == null ||
																frontSellerCompanyArea.getTFrontSellerCompanyAreaPK().getFrontSellerCompanyAreaKey() == null);
			paraFrontSeller.getTFrontSellerCompanyAreaList().stream()
						.forEach(frontSellerCompanyArea -> frontSellerCompanyArea.getTFrontSellerCompanyAreaPK().setFrontSellerKey(paraFrontSeller.getFrontSellerKey()));
		}
		
		if (paraFrontSeller.getTFrontSellerPreferredSchemeList() != null) {
			//TFrontSellerPreferredScheme
			paraFrontSeller.getTFrontSellerPreferredSchemeList().removeIf(frontSellerPreferredScheme -> frontSellerPreferredScheme.getTFrontSellerPreferredSchemePK() == null ||
																frontSellerPreferredScheme.getTFrontSellerPreferredSchemePK().getFrontSellerPreferredSchemeTp() == null);
			paraFrontSeller.getTFrontSellerPreferredSchemeList().stream()
						.forEach(frontSellerPreferredScheme -> frontSellerPreferredScheme.getTFrontSellerPreferredSchemePK().setFrontSellerKey(paraFrontSeller.getFrontSellerKey()));
		}
		
		if (paraFrontSeller.getTFrontSellerSaleReasonList() != null) {
			//TFrontSellerSaleReason
			paraFrontSeller.getTFrontSellerSaleReasonList().removeIf(frontSellerSaleReason -> frontSellerSaleReason.getTFrontSellerSaleReasonPK() == null ||
																	frontSellerSaleReason.getTFrontSellerSaleReasonPK().getFrontSellerSaleReasonTp() == null);
			paraFrontSeller.getTFrontSellerSaleReasonList().stream()
						.forEach(frontSellerSaleReason -> frontSellerSaleReason.getTFrontSellerSaleReasonPK().setFrontSellerKey(paraFrontSeller.getFrontSellerKey()));
		}
		
		frontSeller.setTFrontSellerCompanyAreaList(paraFrontSeller.getTFrontSellerCompanyAreaList());
		frontSeller.setTFrontSellerPreferredSchemeList(paraFrontSeller.getTFrontSellerPreferredSchemeList());
		frontSeller.setTFrontSellerSaleReasonList(paraFrontSeller.getTFrontSellerSaleReasonList());
		
		frontSeller.setFrontSellerUpdDt(currDate);
		
		//FrontMember
		frontMember.setFrontMemberCompany(paraFrontSeller.getFrontSellerCompany());
		frontMember.setFrontMemberFirstNm(paraFrontSeller.getFrontSellerFirstName());
		frontMember.setFrontMemberLastNm(paraFrontSeller.getFrontSellerLastName());
		frontMember.setFrontMemberPosition(paraFrontSeller.getFrontSellerPosition());
		frontMember.setFrontMemberEmail(paraFrontSeller.getFrontSellerEmail());
		frontMember.setFrontMemberOfficeTel(paraFrontSeller.getFrontSellerOfficeTel());
	}
	
	@Override
	public void memInfoUpdate(TFrontMember paraFrontMember, boolean isSenderMail) {
		if (paraFrontMember == null || paraFrontMember.getFrontMemberKey() == null) {
			throw new ApplicationException("parameter or key is null ", "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		TFrontMember frontMember = frontMemberDao.findByPk(paraFrontMember.getFrontMemberKey());
		if (frontMember == null) {
			throw new ApplicationException("frontMember is not found ", "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
		}
		
		boolean isUpdate = false;
		
		if (!Objects.equal(paraFrontMember.getFrontMemberIsLocked(), frontMember.getFrontMemberIsLocked())) {
			frontMember.setFrontMemberIsLocked(paraFrontMember.getFrontMemberIsLocked());
			frontMember.setFrontMemberLoginFailCnt((short)0);
			frontMember.setFrontMemberLoginFailDt(null);
			logger.debug("setFrontMemberIsLocked ");
			isUpdate = true;
		}
		
		if (!Objects.equal(paraFrontMember.getFrontMemberStatusTp(), frontMember.getFrontMemberStatusTp())) {
			frontMember.setFrontMemberStatusTp(paraFrontMember.getFrontMemberStatusTp());
			logger.debug("setFrontMemberStatusTp ");
			isUpdate = true;
		}
		
		
		if (!Strings.isNullOrEmpty(paraFrontMember.getFrontMemberLoginPassword())) {
			
			String encPass = SecurityUtil.shaPasswordEncoder(frontMember.getFrontMemberLoginId(), paraFrontMember.getFrontMemberLoginPassword());
			if (encPass != null && SecurityUtil.isShaPasswordType(encPass, frontMember.getFrontMemberLoginId(), paraFrontMember.getFrontMemberLoginPassword())) {
				frontMember.setFrontMemberLoginDeCryptPassword(paraFrontMember.getFrontMemberLoginPassword());
				frontMember.setFrontMemberLoginPassword(encPass);
				
				logger.debug("setFrontMemberLoginPassword");
				
				isUpdate = true;
				
				if (isSenderMail) {
					//メール送信(パスワード変更完了)
					try {
						mailManager.type(MstMasterCode.MST_EMAIL_MEM_PASSWORD_CHANGE)
									.to(frontMember.getFrontMemberEmail())
									.toNm(frontMember.getFrontMemberLastNm() + " " + frontMember.getFrontMemberFirstNm())
									.send(frontMember);
						
					} catch (ApplicationException e) {
						logger.error("mail sender exception ", e);
					}
				}
			} else {
				logger.error("SecurityUtil.shaPasswordEncoder error paraFrontMember : " + paraFrontMember);
				throw new ApplicationException("SecurityUtil.shaPasswordEncoder error paraFrontMember ", "window.parent.viewErrorMessage('alertDialogDefaultMessage');");
			}
		}
		
		if (isUpdate) {
			frontMember.setFrontMemberUpdDt(new Date());
		}
	}*/
	
	// **********************************************************************
	// プライベートメソッド
	// **********************************************************************
	


	
	
}
