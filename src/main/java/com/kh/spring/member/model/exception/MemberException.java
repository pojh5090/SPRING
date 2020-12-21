package com.kh.spring.member.model.exception;

public class MemberException extends RuntimeException {
	//RuntimeException이라서 따로 예외처리 할 필요가 없어짐!
	
	public MemberException() {}
	public MemberException(String msg) {
		super(msg);
	}
}
