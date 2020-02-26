package com.newsboard.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.newsboard.model.NewsBoardBeans;
import com.newsboard.model.NewsBoardGubunModel;
import com.newsboard.service.NewsBoardService;

@Controller
@RequestMapping("/artboard")
public class NewsBoardController {
	
	// DI
	
	private ApplicationContext context = new ClassPathXmlApplicationContext("/com/common/config/applicationContext.xml");
	private NewsBoardService boardService = (NewsBoardService) context.getBean("boardService");
//	private String uploadPath = "D:\\pilsa\\workspace\\ojt\\CyberArtMuseum\\WebContent\\files\\";
//	private String uploadPath = "D:\\pilsa\\workspace\\ojt\\CyberArtMuseum\\WebContent\\files\\";
	private String uploadPath = "D:\\tomcat6.0\\webapps\\CyberArtMuseum\\files\\";
	
	//
	// * User variable
	// article, page variables
	private int currentPage = 1;
	private int showArticleLimit = 15; // change value if want to show more articles by one page
	private int showPageLimit = 10; // change value if want to show more page links
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	
	@RequestMapping("/testList.do")
	public String boardList() {		
		return "/artboard/list";
	}
	
	
	@RequestMapping("/artList.do")
	public ModelAndView boardList(@ModelAttribute("ArtBoardGubunModel") NewsBoardGubunModel artBoardGubunModel, 
			HttpServletRequest request, HttpServletResponse response){
		
		String keyword = null;
		String type = null;
//		int userGubun = bModel.getU_id_gubun();
		// set variables from request parameter
		if(request.getParameter("page") == null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		if(request.getParameter("keyword") != null){
			keyword = request.getParameter(encodeEuc("keyword").trim());
		}
		if(request.getParameter("type") != null){
			type = request.getParameter("type").trim();
		}
		//
		
		// expression article variables value
				startArticleNum = (currentPage - 1) * showArticleLimit + 1;
				endArticleNum = startArticleNum + showArticleLimit -1;
		//
		
		// get boardList and get page html code
		List<NewsBoardBeans> artBoardList = null;
		if(type != null ||keyword != null){
			artBoardList = boardService.searchIarticle(type,keyword, startArticleNum, endArticleNum);
			totalNum = boardService.getIsearchTotalNum(type, keyword);
		}else{
			artBoardList = boardService.getIboardList(startArticleNum, endArticleNum);
			totalNum = boardService.getItotalNum();
		}
//		MemberModel usergubun = memberService.getUserGubun(userGubun);
//		System.out.println("---------------"+usergubun);
		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);
//		ArtBoardGubunModel boardGb = boardService.getBoardGubun(artBoardGubunModel);
//		System.out.println("11111111"+boardGb.getG_idx());
		ModelAndView mav = new ModelAndView();
		mav.addObject("artBoardList", artBoardList);
//		mav.addObject("boardGb", boardGb);
//		mav.addObject("usergubun", usergubun);
		mav.addObject("pageHtml", pageHtml);
		mav.setViewName("artBoardList");
//		mav.setViewName("artboard/artBoardList");
//		mav.setViewName("/board/list");
		
		return mav;
	}
	
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
				pageHtml.append("<span><a href=\"artList.do?page=" + (currentPage-1) + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"artList.do?page=" +i + "\" class=\"page\">" + i + "</a>");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"artList.do?page=" +i + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append(".</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"artList.do?page=" + (currentPage+1) + "\"><다음></a></span>");
			}
		//
		// else: when search
		} else {			
			if(currentPage == 1){
				pageHtml.append("<span>");
			} else {
				pageHtml.append("<span><a href=\"artList.do?page=" + (currentPage-1) + "&type=" + type + "&keyword=" + keyword + "\"><이전></a>&nbsp;&nbsp;");
			}
			
			for(int i = startPage ; i <= lastPage ; i++) {
				if(i == currentPage){
					pageHtml.append(".&nbsp;<strong>");
					pageHtml.append("<a href=\"artList.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
					pageHtml.append("&nbsp;</strong>");
				} else {
					pageHtml.append(".&nbsp;<a href=\"artList.do?page=" +i + "&type=" + type + "&keyword=" + keyword + "\" class=\"page\">" + i + "</a>&nbsp;");
				}
				
			}
			if(currentPage == lastPage){
				pageHtml.append("</span>");
			} else {
				pageHtml.append(".&nbsp;&nbsp;<a href=\"artList.do?page=" + (currentPage+1) + "&type=" + type + "&keyword=" + keyword + "\"><다음></a></span>");
			}
		}
		//		
		return pageHtml;
	}
	
	@RequestMapping("/artView.do")
	public ModelAndView boardView(HttpServletRequest request){
		int idx = Integer.parseInt(request.getParameter("idx"));		
		NewsBoardBeans artview = boardService.getOneIarticle(idx); // get selected article model
		ModelAndView mav = new ModelAndView();
		mav.addObject("artview", artview);
		mav.setViewName("artBoardView");
//		mav.setViewName("artboard/artBoardView");
		return mav;
	}
	
	
	@RequestMapping("/artWrite.do")
	public String boardWrite(@ModelAttribute("ArtBoardBeans") NewsBoardBeans boardModel){		
		return "artBoardWrite";
//		return "artboard/artBoardWrite";
	}
	
	@RequestMapping(value="/artWrite.do", method=RequestMethod.POST)
	public String boardWriteProc(@ModelAttribute("ArtBoardBeans") NewsBoardBeans boardModel, MultipartHttpServletRequest request){
		
		MultipartFile file = request.getFile("file");
		String fileName = file.getOriginalFilename();
		
		File uploadFile = new File(uploadPath + fileName);
		// when file exists as same name
		if(uploadFile.exists()){
			fileName = new Date().getTime() + fileName;
			uploadFile = new File(uploadPath + fileName);
		}
		// save upload file to uploadPath
		try {
			file.transferTo(uploadFile);

		} catch (Exception e) {
			
		}
		boardModel.setA_imagename(fileName);
		
		// new line code change to <br /> tag	
//		String name =  boardModel.getA_name().replaceAll("\r\n", "<br />");
//		System.out.println("11111111111"+name);
//		String title =  boardModel.getA_title();
//		System.out.println("22222222222"+title);
//		String type =  boardModel.getA_type();
//		System.out.println("33333333333"+type);
//		String year =  boardModel.getA_year();
//		System.out.println("44444444444"+year);
//		String own =  boardModel.getA_own();
//		System.out.println("55555555555"+own);
//		String content =  boardModel.getA_content();
//		System.out.println("66666666666"+content);
//		boardModel.setA_name(name);
//		boardModel.setA_title(title);
//		boardModel.setA_type(type);
//		boardModel.setA_year(year);
//		boardModel.setA_own(own);
//		boardModel.setA_content(content);
		
		String content = request.getParameter("a_content").replaceAll("\r\n", "<br />");
		boardModel.setA_content(content);
		boardService.writeIarticle(boardModel);		
		
		return "redirect:artList.do";
	}

	
	@RequestMapping("/artModify.do")
	public ModelAndView boardModify(HttpServletRequest request, HttpSession session){
//		String userId = (String) session.getAttribute("userId");
		int idx = Integer.parseInt(request.getParameter("a_idx"));

		
		NewsBoardBeans board = boardService.getOneIarticle(idx);
		// <br /> tag change to new line code
//		String content = board.getA_content().replaceAll("<br />", "\r\n");
//		board.setA_content(content);
		//
		
		ModelAndView mav = new ModelAndView();
		
//		if(!userId.equals(board.getName())){
			mav.addObject("errCode", "1");	// forbidden connection
			mav.addObject("idx", idx);
//			mav.setViewName("redirect:view.do");
//		} else {
			mav.addObject("board", board);
			System.out.println(board.getA_imagename());
			mav.setViewName("artBoardModify");
//		}		
		
		return mav;
	}
	
	@RequestMapping(value = "/artModify.do", method=RequestMethod.POST)
	public ModelAndView boardModifyProc(@ModelAttribute("ArtBoardBeans") NewsBoardBeans boardModel, MultipartHttpServletRequest request){
		
		String orgFileName = request.getParameter("orgFile");
		MultipartFile newFile = request.getFile("file");
		String newFileName = newFile.getOriginalFilename();
		boardModel.setA_imagename(orgFileName);
		
		// if: when want to change upload file
		if(newFile != null && !newFileName.equals("")){
			if(orgFileName != null || !orgFileName.equals("")){
				// remove uploaded file
				File removeFile = new File(uploadPath  + orgFileName);
				removeFile.delete();
				//
			}
			// create new upload file
			File newUploadFile = new File(uploadPath +newFileName);
			try {
				newFile.transferTo(newUploadFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			boardModel.setA_imagename(newFileName);
		}
		
		//
		// new line code change to <br /> tag
//		String content =  boardModel.getA_content().replaceAll("\r\n", "<br />");		
//		boardModel.setA_content(content);
		//
		
		boardService.modifyIarticle(boardModel);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("idx", boardModel.getA_idx());
		mav.setViewName("redirect:/artboard/artView.do");
		return mav;
	}
	
	@RequestMapping("/artDelete.do")
	public ModelAndView boardDelete(HttpServletRequest request){
//		String userId = (String) session.getAttribute("userId");
		int idx = Integer.parseInt(request.getParameter("a_idx"));
		
//		TestBoardBeans board = boardService.getOneArticle(idx);
		
		ModelAndView mav = new ModelAndView();
		
//		if(!userId.equals(board.getName())){
			mav.addObject("errCode", "1");	// it's forbidden connection
			mav.addObject("idx", idx);
//			mav.setViewName("redirect:view.do");
//		} 
			
				//				
				boardService.deleteIarticle(idx);
				
				mav.setViewName("redirect:artList.do");
			
	
		return mav;
	}
	
	public static String encodeEuc(String string){
	    
		  try{
		   string = new String(string.getBytes("8859_1"),"ksc5601");
		  }catch(UnsupportedEncodingException uee){
		   string = "";
		  }
		  return string;
		 }
	

}
