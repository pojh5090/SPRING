package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.spring.member.model.vo.Member;

public class BoardEnterInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			request.setAttribute("msg", "로그인 후 이용하세요.");
			request.getRequestDispatcher("WEB-INF/views/home.jsp").forward(request, response);
			return false;  //컨트롤러 넘어가기 전에 처리해야 하기 때문에
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
