package com.newsboard.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
public class DelFile1Controller {
	
	File path1 = new File("");
	
//	private String uploadPath1 = "D:\\pilsa\\workspace\\ojt\\CyberArtMuseum\\WebContent\\files\\";
	private String uploadPath1 = "D:\\tomcat6.0\\webapps\\CyberArtMuseum\\files\\";
			
		
		@RequestMapping("/delfile.do")
		public ModelAndView delfilego(HttpServletRequest request)
		{
			//웹상 idx의 번호에 맞게 가져온 "Filename" 정보를 다시 자바로 가져올때 선언한 것이다.
			String del_filename =(String)  request.getParameter("df");
			System.out.println("sadasdasd"+del_filename);
			//파일명이 들어오는지 확인 여부 
			System.out.println("fileName1 =" +del_filename);
			
			File del_file = new File(uploadPath1+del_filename);
			
			del_file.delete();
			ModelAndView mav = new ModelAndView();
			
			mav.setViewName("/artboard/artBoardModify");
			//BoardModel board = boardService.getOneArticle(); // get selected article model
			
			//"board"는 jsp단에 선언된 board, cboard는 우리가 수정해서 넣을 값.
			//mav.addObject("board", board);
			return mav;
		}

}
