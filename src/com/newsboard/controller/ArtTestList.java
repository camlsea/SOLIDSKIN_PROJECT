package com.newsboard.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.answerboard.model.AnswerBoardModel;
import com.answerboard.model.NewsBoardModel;
import com.answerboard.service.AnswerBoardService;
import com.answerboard.service.NBoardService;
import com.newsboard.model.NewsBoardBeans;
import com.newsboard.service.NewsBoardService;


@Controller
@RequestMapping("/testboard")
public class ArtTestList {
	
	private ApplicationContext context = new ClassPathXmlApplicationContext("/com/common/config/applicationContext.xml");
	private NBoardService boardService = (NBoardService) context.getBean("nboardService");
	private AnswerBoardService boardService2 = (AnswerBoardService) context.getBean("fboardService");
	private NewsBoardService boardService3 = (NewsBoardService) context.getBean("boardService");

	private int currentPage = 1;
	private int showArticleLimit = 10; // change value if want to show more articles by one page
	private int showPageLimit = 10; // change value if want to show more page links
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	private int totalNum2 = 0;
	
	@RequestMapping("/list.do")
	public ModelAndView boardList(@ModelAttribute("ArtBoardBeans") NewsBoardBeans boardModel,HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView mav = new ModelAndView();
		String type = null;
		String keyword = null;
		
		// set variables from request parameter
		if(request.getParameter("page") == null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		if(request.getParameter("type") != null){
			type = request.getParameter("type").trim();
		}
		
		if(request.getParameter("keyword") != null){
			keyword = request.getParameter("keyword").trim();
		}
		//
		
		// expression article variables value
		startArticleNum = (currentPage - 1) * showArticleLimit + 1;
		endArticleNum = startArticleNum + showArticleLimit -1;
		//
		
		// get boardList and get page html code
		List<NewsBoardModel> boardList;
		List<AnswerBoardModel> boardList2 = null;
		NewsBoardBeans boardList3;
//		boardList3 = boardService3.getImageList(boardModel);
		if(type != null && keyword != null){
			boardList = boardService.searchArticle(type, keyword, startArticleNum, endArticleNum);
			boardList2 = boardService2.searchArticle(type, keyword, startArticleNum, endArticleNum);
			totalNum = boardService.getSearchTotalNum(type, keyword);
			totalNum2 = boardService2.getSearchTotalNum(type, keyword);
     	} else {
			boardList = boardService.getBoardList(startArticleNum, endArticleNum);
			boardList2 = boardService2.getFboardList(startArticleNum, endArticleNum);
//			System.out.println("sssssss"+boardList3.getA_idx());
			totalNum = boardService.getTotalNum();
			totalNum2 = boardService2.getTotalNum();
		}
//		boardList3 = boardService3.getImageList();
		StringBuffer pageHtml2 = getPageHtml2(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);

		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);//
		
//		boardList3 = (ArtBoardBeans)boardService3.getImageList(boardModel);
		mav.addObject("boardList", boardList);
		mav.addObject("boardList2", boardList2);
//		mav.addObject("boardList3", boardList3);
		mav.addObject("pageHtml", pageHtml);
		mav.addObject("pageHtml2", pageHtml2);
		mav.setViewName("artboard/list");
		
		return mav;
	}

	
	// A method for Creating page html code
	private StringBuffer getPageHtml2(int currentPage, int totalNum, int showArticleLimit, int showPageLimit, String type, String keyword) {
		StringBuffer pageHtml = new StringBuffer();
		int startPage = 0;
		int lastPage = 0;
		
		// expression page variables
		startPage = ((currentPage-1) / showPageLimit) * showPageLimit + 1;
		lastPage = startPage + showPageLimit - 1;
		
		if(lastPage > totalNum / showArticleLimit) {
			lastPage = (totalNum / showArticleLimit) + 1;
		}
		//
		
		// create page html code
		// if: when no search	
		if(type == null && keyword == null){			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage-1) + "\"><다음></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" +i + "\" class=\"page\">" + i + "</a>");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" +i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append(".</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "\"><이전></a></span>");
			}
		//
		// else: when search
		} else {			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"list.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><다음></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"list.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"list.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"list.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><이전></a></span>");
			}
		}
		//		
		return pageHtml;
	}
	
////	@RequestMapping("/list.do")
//	public ModelAndView boardList2(HttpServletRequest request, HttpServletResponse response){
//		
//		String type = null;
//		String keyword = null;		
//		
//		// set variables from request parameter
//		if(request.getParameter("page") == null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
//			currentPage = 1;
//		} else {
//			currentPage = Integer.parseInt(request.getParameter("page"));
//		}
////		
//		if(request.getParameter("type") != null){
//			type = request.getParameter("type").trim();
//		}
//		
//		if(request.getParameter("keyword") != null){
//			keyword = request.getParameter("keyword").trim();
//		}
//		//
//		
//		// expression article variables value
//		startArticleNum = (currentPage - 1) * showArticleLimit + 1;
//		endArticleNum = startArticleNum + showArticleLimit -1;
//		//
//		
//		// get boardList and get page html code
//		List<FBoardModel> boardList;
//		if(type != null && keyword != null){
//			boardList = boardService2.searchArticle(type, keyword, startArticleNum, endArticleNum);
//			totalNum = boardService.getSearchTotalNum(type, keyword);
//
//		} else {
//			
//			boardList = boardService2.getFboardList(startArticleNum, endArticleNum);
//			
//			totalNum = boardService.getTotalNum();
//		}
//		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);
//		//
//		
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("boardList", boardList);
//		mav.addObject("pageHtml", pageHtml);
//		mav.setViewName("artboard/list");
//		
//		return mav;
//	
//	}
	
	// A method for Creating page html code
	private StringBuffer getPageHtml(int currentPage, int totalNum, int showArticleLimit, int showPageLimit, String type, String keyword) {
		StringBuffer pageHtml = new StringBuffer();
		int startPage = 0;
		int lastPage = 0;
		
		// expression page variables
		startPage = ((currentPage-1) / showPageLimit) * showPageLimit + 1;
		lastPage = startPage + showPageLimit - 1;
		
		if(lastPage > totalNum / showArticleLimit) {
			lastPage = (totalNum / showArticleLimit) + 1;
		}
		//
		
		// create page html code
		// if: when no search	
		if(type == null && keyword == null){			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"freeList.do?page=" + (currentPage-1) + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"freeList.do?page=" +i + "\" class=\"page\">" + i + "</a>");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"freeList.do?page=" +i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append(".</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"freeList.do?page=" + (currentPage+1) + "\"><다음></a></span>");
			}
		//
		// else: when search
		} else {			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"freeList.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"freeList.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"freeList.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"freeList.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><다음></a></span>");
			}
		}
		//		
		return pageHtml;
	}
	
	@RequestMapping("/list22.do")
	public ModelAndView boardListTest(@ModelAttribute("ArtBoardBeans") NewsBoardBeans boardModel,HttpServletRequest request, HttpServletResponse response){
		
		List<NewsBoardBeans> boardList3;
		
		boardList3  = boardService3.getImageList();
		
		System.out.println("aaaa"+boardList3);
//		boardList3 = boardService3.getImageList();

		ModelAndView mav = new ModelAndView();
//		boardList3 = (ArtBoardBeans)boardService3.getImageList(boardModel);
		mav.addObject("boardList3", boardList3);
		mav.setViewName("artboard/test");
		
		return mav;
	}
	
}
