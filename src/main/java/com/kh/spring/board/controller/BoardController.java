package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.common.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;
	
	@RequestMapping("blist.bo")	//-->> int형을 안쓰는 이유 : 기본자료형(int)에 절대 null이 들어갈 수 없기 떄문
								//null도 들어갈 수 있는 래퍼클래스(Integer)로 써줘야 함
	public ModelAndView boardList(@RequestParam(value="page", required=false) Integer page, ModelAndView mv) {
										         //      ㄴ-->page가 없을수도 있어~~
		int currentPage = 1;
		if(page != null) {
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if(list != null) {
			mv.addObject("list", list);
			mv.addObject("pi", pi);
			mv.setViewName("boardListView");
		} else {
			throw new BoardException("게시글 전체 조회에 실패했습니다.");
		}		
		return mv;
	}
	
	@RequestMapping("binsertView.bo")
	public String boardInsertForm() {
		return "boardInsertForm";
	}
	
	@RequestMapping("binsert.bo")
	public String boardInsert(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
		
//		System.out.println(b);
//		System.out.println(uploadFile);
		
		// 첨부파일이 있고 없고는 MultipartFile의 객체가 null이냐 아니냐로 따지는것이 아니라
		// MultipartFile 안에 있는 메소드 getOriginFilename()으로 판별 가능
		// 첨부 파일이 있을 때 : 파일 실제 이름
		// 첨부 파일이 없을 때 :
		
		// 첨부 파일이 있다면
		if(uploadFile != null && uploadFile.isEmpty()) {
			String renameFileName = saveFile(uploadFile, request);
			
			if(renameFileName != null) {
				b.setRenameFileName(renameFileName);
				b.setOriginalFileName(uploadFile.getOriginalFilename());
			}
		}
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new BoardException("게시물 등록에 실패하였습니다.");
		}
	}
	
	public String saveFile(MultipartFile file, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		// 웹 서버 contextPath를 불러와 폴더의 경로를 받아옴. (webapp 하위의 resources 폴더)
		
		String savePath = root + "\\buploadFiles";
		
		// 혹시라도 폴더 안만들어져 있음 만들어라~
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		// 이름바꾸기 + 뒤에 확장자도 같이 가져오기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String originFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(new Date(System.currentTimeMillis())) 
								+ "." + originFileName.substring(originFileName.lastIndexOf(".") + 1);
		
		String renamePath = folder + "\\" + renameFileName;
		
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return renameFileName;
	}
}
