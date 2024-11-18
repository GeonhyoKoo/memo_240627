package com.memo.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request , HttpServletResponse response
			, Object handler) throws IOException {
		
		// 요청 url path
		String uri =  request.getRequestURI();
		
		log.info("[@@@@@@@@@@@@@@@@@@@ preHandle] uri:{}" , uri);
		
		// 로그인 여부 -> 세션
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		// /post로 시작하고 비로그인 -> 로그인 페이지로 이동 , 컨트롤러 수행 방지
		if (uri.startsWith("/post") && userId == null) {
			// redirect
			response.sendRedirect("/user/sign-in-view");
			
			// 원래 가려고 했었던 controller 수행 방지
			return false;
		}
		
		
		// /user로 시작하고 로그인 -> 글 목록 페이지로 이동, 컨트롤러 수행 방지
		if (uri.startsWith("/user") && userId != null) {
			//redirect
			response.sendRedirect("/post/post-list-view");
			// 원래 가려고 했었던 controller 수행 방지
			return false;
		}
		
		
		return true;
	}
	
	
	
	@Override
	public void postHandle(HttpServletRequest request , HttpServletResponse response
			, Object handler, ModelAndView mav) {
		
		log.info("[#################### postHandle]");
		
		// view , model 이 있다는건 html이 해석되기(렌더링) 전의 상황
		
		
	}
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request , HttpServletResponse response
			, Object handler, Exception ex) {
		
		log.info("[%%%%%%%%%%%%%%%%%%% afterCompletion]");
		
		// html 렌더링 끝난 상태
		
	}
	
	
	
}