package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {

	
	/**
	 * 회원가입 화면
	 * @return
	 */
	@GetMapping("/sign-up-view")
	public String signUpView() {
		// 가운데 레이아웃 조각만 내려주면 전체 레이아웃과 함께 구성된다.
		// 왜냐면 조각이 전체에 껴넣기를 했기 때문에
		return "user/signUp";
	}
	
	/**
	 * 로그인 화면
	 * @return
	 */
	
	@GetMapping("/sign-in-view")
	public String signinView() {
		// 가운데 레이아웃 조각만 내려주면 전체 레이아웃과 함께 구성된다.
		// 왜냐면 조각이 전체에 껴넣기를 했기 때문에
		return "user/signIn";
	}
	
	
	@GetMapping("/sign-out")
	public String signOut(HttpSession session) {
		// session을 비운다.
		session.removeAttribute("userId");
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		
		// redirect 로 로그인 페이지 이동
		return "redirect:/user/sign-in-view";
		
	}
	
	
	
}
