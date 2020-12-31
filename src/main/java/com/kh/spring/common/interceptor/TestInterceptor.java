package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TestInterceptor implements AsyncHandlerInterceptor{
	
	private Logger logger = LoggerFactory.getLogger(TestInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// DispatcherServlet이 Controller를 호출하기 전에 수행
		
		logger.debug("===========START==========");
		logger.debug(request.getRequestURI());
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Controller에서 DispatcherServlet으로 리턴되는 순간 진행
		logger.debug("------------view-----------");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 최종 결과를 생성하는 일을 포함한 모든 작업이 완료된 후
		logger.debug("============END==========");
	}
}
