package com.kh.spring.member.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes("loginUser")
@Controller   
public class MemberController {
	
	@Autowired  //---> MemberService에서 만든 객체를 여기로 가져다 쓰기 위해서!(의존성 주입)
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	/******** 파라미터 전송 받는 방법 *******/
	// 1.HttpServletRequest를 통해 전송받기
//	@RequestMapping("login.me")
//	public void login(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		String pwd = request.getParameter("pwd");
//		
//		System.out.println("id: " + id);
//		System.out.println("pwd : " + pwd);
//	}
	
	// 2. @RequestParam 어노테이션 방식
//	@RequestMapping("login.me")
//	public void login(@RequestParam("id") String id, @RequestParam("pwd") String pwd) {	
//		System.out.println("id: " + id);
//		System.out.println("pwd : " + pwd);
//	}
	
	// 3. @RequestParam 어노테이션 생략
//	@RequestMapping("login.me")
//	public void login(String id, String pwd) {	
//		System.out.println("id: " + id);
//		System.out.println("pwd : " + pwd);
//	}
	
	// 4. @ModelAttribute 어노테이션  --->> 단!!!!!! 기본생성자 + setter 무조건 필요!!!!!!!!!!!!
//	@RequestMapping("login.me")
//	public void login(@ModelAttribute Member m) {	
//		System.out.println("id: " + m.getId());
//		System.out.println("pwd : " + m.getPwd());
//	}
	
	// 5. @ModelAttribute 어노테이션 생략
//	@RequestMapping("login.me")
//	public void login(Member m) {	
//		System.out.println("id: " + m.getId());
//		System.out.println("pwd : " + m.getPwd());	
		
	//	MemberService mService = new MemberServiceImpl();  
//		System.out.println(mService.hashCode());
		// 늘 하던대로의 MemberService service = new MemberService() -->객체안의 객체 --> 결합도가 높은 코드 -->좋지 않음 해결법은?
		// 1. 클래스 명 바꿔보기
		// 2. 해시코드 출력 (계속 같은 해시코드 나오도록 할꺼임)
		
//		Member loginUser = mService.memberLogin(m);
//	}
	
	/******** 요청 후 전달하고자 하는 데이터가 있을 경우 *********/
	// 1. Model객체 사용 : 맵 형식(key, value)
//	@RequestMapping("login.me")
//	public String login(Member m, HttpSession session, Model model) {	
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			session.setAttribute("loginUser", loginUser);
//			return "redirect:home.do";
//		} else {
////			request.setAttribute("message", "로그인에 실패"); -->이제 이걸 Model로 보내겠다
//			model.addAttribute("message", "로그인에 실패하였습니다.");
//			return "../common/errorPage";
//		}
//	}
	
	// 2. ModelAndView객체 사용
	// Model(맵형식: key와 value) + View(RequestDispatcher처럼 forward할 페이지 정보 담음)
//	@RequestMapping("login.me")
//	public ModelAndView login(Member m, HttpSession session, ModelAndView mv) {	
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			session.setAttribute("loginUser", loginUser);
//			mv.setViewName("redirect:home.do");
//		} else {
////			model.addAttribute("message", "로그인에 실패하였습니다.");
//			mv.addObject("message", "로그인에 실패하였습니다.");
////			return "../common/errorPage";
//			mv.setViewName("../common/errorPage");
//		}		
//		return mv;
//	}

	
	// 로그아웃 컨트롤러1
//	@RequestMapping("logout.me")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		
//		return "redirect:home.do";
//	}
	
//	// 3. session에 저장할 때 @SessionAttributes 사용  (암호화 전)
//	// model에 attribute가 추가될 때 자동으로 키 값을 찾아 세션에 등록 ***********************8
//	@RequestMapping("login.me")
//	public String login(Member m, Model model) {	
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			model.addAttribute("loginUser", loginUser); 
//			//---->model에 loginUser 해줘서 맨 위에 sessionAttributes 할수 있는거임~~
//			return "redirect:home.do";
//		} else {
////			model.addAttribute("message", "로그인에 실패하였습니다.");
////			return "../common/errorPage";
//			throw new MemberException("로그인에 실패했습니다.");
//		}		
//	}
	
	// 로그아웃 컨트롤러2
	@RequestMapping("logout.me")
	public String logout(SessionStatus status) {
		status.setComplete();
		
		return "redirect:home.do";
	}
	
	@RequestMapping("enrollView.me")
	public String enrollView() {
		logger.debug("회원 등록 페이지");
		
		return "memberJoin";
	}
	
	@RequestMapping("minsert.me")
	public String memberInsert(@ModelAttribute Member m, @RequestParam("post") String post,
														 @RequestParam("address1") String address1,
														 @RequestParam("address2") String address2) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		//////비번 암호화 하기//////
		// JSP/Servlet : SHA-512
		// Spring : Bcrypt 암호화방식
		
		String encPwd = bcrypt.encode(m.getPwd());
//		System.out.println(encPwd);
		m.setPwd(encPwd);
		
		int result = mService.insertMember(m);
		
		if(result > 0) {
			return "redirect:home.do";
		} else {
			throw new MemberException("회원가입에 실패했습니다.");
		}
	}
	
	//회원가입 후 로그인 (암호화 후)
	@RequestMapping("login.me")
	public String login(Member m, Model model) {
		//////////-----입력한 비번 , db에 있는 비번  비교!
		/////*** 현재 loginUser에는 DB의 값이, Member m 에는 view에서 받아온 값 들어있음~!!!! ***     
		Member loginUser = mService.memberLogin(m);
	      
		if(bcrypt.matches(m.getPwd(), loginUser.getPwd())) {
		// 두 개를 비교해서 맞는지 확인한 후 맞으면 true 다르면 false
			model.addAttribute("loginUser", loginUser);
			logger.info(loginUser.getId());
		    return "redirect:home.do";
		}else {
	//	   model.addAttribute("message", "로그인에 실패하였습니다.");
	//	   return "../common/errorPage";
		   throw new MemberException("로그인에 실패했습니다.");
		}
	}
	
	// 탈퇴하기
	@RequestMapping("mdelete.me")
	public String deleteMember(@RequestParam("id") String id, SessionStatus status) {
		int result = mService.deleteMember(id);
		
		if(result > 0) {
			status.setComplete();
			return "redirect:home.do";
		} else {
			throw new MemberException("회원탈퇴에 실패하였습니다.");
		}
	}
	
	// 마이페이지
	@RequestMapping("myinfo.me")
	public String myinfo(Member m, Model model) {
		return "mypage";
	}
	
	// 내정보 수정폼으로
	@RequestMapping("mupdateView.me")
	public String updateFormView() {
		return "memberUpdateForm";
	}
	
	// 내정보 수정하기
	@RequestMapping("mupdate.me")
	public String updateMe(@ModelAttribute Member m, @RequestParam("post") String post,
									 				 @RequestParam("address1") String address1,
									 				 @RequestParam("address2") String address2, Model model) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.updateMember(m);
		
		if(result > 0) {
			model.addAttribute("loginUser", m); //---->여기가 막혔던 부분!!!!!
			return "mypage";
		} else {
			throw new MemberException("회원정보 수정에 실패");
			
		}
	}
	
	// 비번 수정폼으로
	@RequestMapping("mpwdUpdateView.me")
	public String updatePwdFormView() {
		return "memberPwdUpdateForm";
	}
	
	// 비번 수정하기
	@RequestMapping("mPwdUpdate.me")
	public String updatePwd(@RequestParam("pwd") String pwd, @RequestParam("newPwd1") String newPwd, HttpSession session) {
		
		Member m = mService.memberLogin((Member)session.getAttribute("loginUser"));
		
		//기존 비번이 맞으면 보내줄꺼야
		if(bcrypt.matches(pwd, m.getPwd())) {
			String encNewPwd = bcrypt.encode(newPwd);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", m.getId());
			map.put("newPwd", encNewPwd);
			
			int result = mService.updatePwd(map);
			
			if(result > 0) {
				return "mypage";
			} else {
				throw new MemberException("비밀번호 변경 실패");
			}
			
		} else {
			System.out.println("왜 이동안해");
			throw new MemberException("기존 비밀번호가 틀렸습니다.");
		}
	}
	
	@RequestMapping("dupid.me")
	public void idDuplicateCheck(@RequestParam("id") String id, HttpServletResponse response) {
		boolean isUsable = mService.checkIdDup(id) == 0 ? true : false;
		
		try {
			response.getWriter().print(isUsable); //isUsable 보내주기
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
