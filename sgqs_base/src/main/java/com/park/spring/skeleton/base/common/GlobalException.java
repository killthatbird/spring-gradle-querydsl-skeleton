package com.park.spring.skeleton.base.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.park.spring.skeleton.base.constants.Status;
import com.park.spring.skeleton.base.exception.ApplicationException;
import com.park.spring.skeleton.base.exception.PageNotFoundException;
import com.park.spring.skeleton.base.exception.ValidateException;
import com.park.spring.skeleton.base.web.form.JsonResponse;

/**
 * 例外のハンドラクラス。
 * ControllerでキャッチしてないException処理
 * @author park
 *
 */
public class GlobalException implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception ex) {

		logger.error(" X-Requested-With : " + request.getHeader("X-Requested-With"), ex);
		
		if (Strings.nullToEmpty(request.getHeader("X-Requested-With")).equals("XMLHttpRequest")) {
			//Ajaxからの接続の場合
			response.setCharacterEncoding("UTF-8");
			
			JsonResponse jsonResponse = new JsonResponse();
			jsonResponse.setStatus(Status.ERROR);
			
			Gson gson = new Gson();
			
			PrintWriter writer = null;
			try {
				String errorMessage = "";
				if (ex != null) {
					if (ex instanceof PageNotFoundException) {
						errorMessage = "ページが存在しないか間違った接続です。";
					} else if (ex instanceof AccessDeniedException) {
						errorMessage = "該当のページには接続できません。";
					} else if (ex instanceof ApplicationException) {
						errorMessage = "エラーにより処理出来ませんでした。";
					} else {
						errorMessage = "エラーにより処理出来ませんでした。";
					}
				} else {
					errorMessage = "エラーにより処理出来ませんでした。";
				}
				
				jsonResponse.setErrorMessage(errorMessage);
				
				writer = response.getWriter();
				writer.write(gson.toJson(jsonResponse));  
				writer.flush();  
			} catch (IOException e) {  
			} finally {
				writer.close();
			}
			
			return null;
		}
		
		ModelAndView mav = new ModelAndView();

		// 表示するメッセージをセットします。
		mav.addObject("message", ex.getMessage());
		mav.addObject("datetime", new Date());
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        
		// 遷移先を指定します。
		if (ex instanceof PageNotFoundException ||
				ex instanceof HttpRequestMethodNotSupportedException) {
			mav.setViewName("/error/notfound-error");
		} else if (ex instanceof AccessDeniedException) {
			mav.setViewName("redirect:/?security");
		} else {
			mav.setViewName("/error/error");
			
			String script = "";
			if (ex instanceof ValidateException) {
				script = ((ValidateException) ex).getScript();
			} else if (ex instanceof ApplicationException) {
				script = ((ApplicationException) ex).getScript();
			}
			
			if (!Strings.isNullOrEmpty(script)) {
				mav.addObject("script", script);
			}
		}
		
		return mav;

	}


}