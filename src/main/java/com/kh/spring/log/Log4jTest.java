package com.kh.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	private Logger logger = LoggerFactory.getLogger(Log4jTest.class);
	
	public void test() {
		logger.debug("debug로그");
		logger.info("info로그");
		logger.warn("warn로그");
		logger.error("error로그");
//		logger.fatal("fatal로그");
		logger.trace("trace로그");
		
	}
	
	public static void main(String[] args) {
		new Log4jTest().test();
	}
}
