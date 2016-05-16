package com.park.spring.skeleton.front.web.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Shorts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park.spring.skeleton.base.common.MstMasterCode;
import com.park.spring.skeleton.base.constants.Status;
import com.park.spring.skeleton.base.entity.TFrontMember;
import com.park.spring.skeleton.base.exception.PageNotFoundException;
import com.park.spring.skeleton.base.exception.ValidateException;
import com.park.spring.skeleton.base.util.DateUtil;
import com.park.spring.skeleton.base.web.form.JsonResponse;
import com.park.spring.skeleton.front.service.MypageService;
import com.park.spring.skeleton.front.web.form.FrontMember;
import com.park.spring.skeleton.front.web.form.UserDetail;
import com.park.spring.skeleton.front.web.handler.PaginationHandler;

/**
 * 一般会員のMypageページController
 * @author park
 */
@Controller
@RequestMapping(value = "/mypage-normal/")
public class MypageNormalController {
	// **********************************************************************
	// 定数
	// **********************************************************************
	private final static Logger logger = LoggerFactory.getLogger(MypageNormalController.class);
	
	// **********************************************************************
	// メンバ
	// **********************************************************************
	private @Autowired MypageService mypageService;
	private @Autowired MessageSource messageSource;
	
	// **********************************************************************
	// Viewメソッド
	// **********************************************************************
	@PreAuthorize("hasAuthority('" +MstMasterCode.MEM_TYPE_NORMAL+ "')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(Model uiModel, @PageableDefault(direction=Direction.DESC, sort={"frontMemberKey"}) Pageable pageable,
			@RequestParam(value="searchMemberTp", required=false) short[] searchMemberTp,
			@RequestParam(value="searchKey", required=false) String searchKey,
			@RequestParam(value="searchValue", required=false) String searchValue) {
		
		logger.debug("list - searchMemberTp:" + searchMemberTp + "searchKey:" + searchKey + ", searchValue:" + searchValue);
		
		Page<TFrontMember> frontMembers = mypageService.getMemberList(searchKey, searchValue, searchMemberTp, pageable);
		
		if (frontMembers != null) {
			
			String params = "";
			if (!Strings.isNullOrEmpty(searchKey) && !Strings.isNullOrEmpty(searchValue)) {
				params = "?searchKey=" + searchKey + "&searchValue=" + searchValue;
			}
			if (searchMemberTp != null) {
				params += (params.length() > 0 ? "&" : "?") + "searchMemberTp=" + Shorts.join(",", searchMemberTp);
			}
			
			PaginationHandler<TFrontMember> paginationHandler = 
					new PaginationHandler<>(frontMembers, "/mypage-normal/list.html" + params);
			
			uiModel.addAttribute("frontMembers", frontMembers.getContent());
			uiModel.addAttribute("pagination" , paginationHandler);
		}

		return "mypage-normal/list";
	}
	
	
	// **********************************************************************
	// Viewメソッド(Ajax)
	// **********************************************************************
	@RequestMapping(value = "/detail-{frontMemberKey:\\d+}.html", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('" +MstMasterCode.MEM_TYPE_NORMAL+ "')")
	@ResponseBody
	public String detail(Locale locale, Model uiModel, Principal principal,
			@PathVariable int frontMemberKey) {
		logger.debug("detail");
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setStatus(Status.ERROR);
		Gson gson = new GsonBuilder()
	        .setDateFormat(DateUtil.DEFAULT_DATE_FORMAT)
	        .create();
		
		try {
			jsonResponse.setModel(ImmutableMap.of("tFrontMember", mypageService.getMemberDetail(frontMemberKey)));
			jsonResponse.setStatus(Status.SUCCESS);
		} catch (ValidateException ve) {
			jsonResponse.setErrorMessageLists(ve.getField(), messageSource.getMessage(ve.getErrCode(), null, locale));
		}
		
		return gson.toJson(jsonResponse);
		
	}
	
	// **********************************************************************
	// Actionメソッド
	// **********************************************************************
	
	
	// **********************************************************************
	// Actionメソッド(Ajax)
	// **********************************************************************
	@RequestMapping(value = "/memberEditAction.html", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('" +MstMasterCode.MEM_TYPE_NORMAL+ "')")
	@ResponseBody
	public String memberEditAction(Locale locale, 
				@Valid @ModelAttribute("frontMember") FrontMember frontMember,
				BindingResult result, Model uiModel, Principal principal) {
		
		if (frontMember == null || principal == null) {
			throw new PageNotFoundException();
		}
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setStatus(Status.ERROR);
		Gson gson = new Gson();
		
		UserDetail userDetail = (UserDetail)((Authentication) principal).getPrincipal();
		final int frontMemberKey = userDetail.getFrontMemberKey();
		
		logger.info("memberEditAction [" + frontMemberKey + "] " + frontMember.getFrontMemberLoginId());
		
		if (result.hasErrors()) {
			jsonResponse.setErrorMessageLists(result.getFieldErrors(), messageSource, locale);
			return gson.toJson(jsonResponse);
		}
		
		try {
			mypageService.memberEdit(frontMember);
			jsonResponse.setStatus(Status.SUCCESS);
		} catch (ValidateException ve) {
			jsonResponse.setErrorMessageLists(ve.getField(), messageSource.getMessage(ve.getErrCode(), null, locale));
		}
		
		
		return gson.toJson(jsonResponse);
	}
	
	// **********************************************************************
	// InitBinderメソッド (ModelAttribute、RequestMapping初期化)
	// **********************************************************************
	
	
	// **********************************************************************
	// プライベートメソッド
	// **********************************************************************
	
}
