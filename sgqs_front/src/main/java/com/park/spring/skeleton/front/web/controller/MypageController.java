package com.park.spring.skeleton.front.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.park.spring.skeleton.base.common.MstMasterCode;
import com.park.spring.skeleton.base.exception.PageNotFoundException;
import com.park.spring.skeleton.base.util.SecurityUtil;

/**
 * ログインした後、会員のTYPEによって
 * 次のページを決める
 * @author park
 */
@Controller
@RequestMapping(value = "/mypage/")
public class MypageController {
	// **********************************************************************
	// 定数
	// **********************************************************************
	private final static Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	// **********************************************************************
	// メンバ
	// **********************************************************************
	
	// **********************************************************************
	// Viewメソッド
	// **********************************************************************
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('" +MstMasterCode.MEM_TYPE_NORMAL+ "') or hasAuthority('" +MstMasterCode.MEM_TYPE_SPECIAL+ "')")
	public String index(Model model) {
		
		short role = Short.parseShort(SecurityUtil.getLoginUserRole());
		
		logger.info("index role:" + role);
		
		if (role == MstMasterCode.MEM_TYPE_NORMAL) {
			return "redirect:/mypage-normal/";
		} else if (role == MstMasterCode.MEM_TYPE_SPECIAL) {
			return "redirect:/mypage-special/";
		} else {
			throw new PageNotFoundException();
		}
	}
	
	// **********************************************************************
	// Actionメソッド
	// **********************************************************************
	
	
	// **********************************************************************
	// プライベートメソッド
	// **********************************************************************
	
}
