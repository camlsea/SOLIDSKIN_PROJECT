package com.answerboard.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.answerboard.model.AnswerBoardModel;
import com.answerboard.service.AnswerBoardService;

@Controller
@RequestMapping("/fboard")
public class AnswerBoardController {
	// DI
	private ApplicationContext context = new ClassPathXmlApplicationContext("/com/common/config/applicationContext.xml");
	private AnswerBoardService boardService = (AnswerBoardService) context.getBean("answerboardService");
	//
	// * User variable
	// article, page variables
	private int currentPage = 1;
	private int showArticleLimit = 10; // change value if want to show more articles by one page
	private int showPageLimit = 10; // change value if want to show more page links
	private int startArticleNum = 0;
	private int endArticleNum = 0;
	private int totalNum = 0;
	//
	// file upload path
//	private String uploadPath = "C:\\Users\\yunhee\\Documents\\JAVA01\\BoardTest\\WebContent\\files";
	//
	//
	
	@RequestMapping("/freeList.do")
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response){
		
		String type = null;
		String keyword = null;		
		
		// set variables from request parameter
		if(request.getParameter("page") == null || request.getParameter("page").trim().isEmpty() || request.getParameter("page").equals("0")) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
//		
		if(request.getParameter("type") != null){
			type = request.getParameter("type").trim();
		}
		
		if(request.getParameter("keyword") != null){
			keyword = request.getParameter(encodeEuc("keyword").trim());
		}
		//
		
		// expression article variables value
		startArticleNum = (currentPage - 1) * showArticleLimit + 1;
		endArticleNum = startArticleNum + showArticleLimit -1;
		//
		
		// get boardList and get page html code
		List<AnswerBoardModel> boardList;
		if(type != null && keyword != null){
			boardList = boardService.searchArticle(type, keyword, startArticleNum, endArticleNum);
			totalNum = boardService.getSearchTotalNum(type, keyword);

		} else {
			
			boardList = boardService.getFboardList(startArticleNum, endArticleNum);
			
			totalNum = boardService.getTotalNum();
		}
		StringBuffer pageHtml = getPageHtml(currentPage, totalNum, showArticleLimit, showPageLimit, type, keyword);
		//
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("boardList", boardList);
		mav.addObject("pageHtml", pageHtml);
		mav.setViewName("freeList");
		
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
	
	@RequestMapping("/freeView.do")
	public ModelAndView boardView(HttpServletRequest request){
		int f_num = Integer.parseInt(request.getParameter("f_num"));		
		AnswerBoardModel board = boardService.getOneArticle(f_num); // get selected article model
		boardService.updateHitcount(board.getF_count()+1, f_num); // update hitcount
		
		
		ModelAndView mav = new ModelAndView();
		
		
		
		mav.addObject("board", board);
		mav.setViewName("freeView");
		return mav;
	}
	
	@RequestMapping("/freeWrite.do")
	public String boardWrite(@ModelAttribute("FBoardModel") AnswerBoardModel fboardModel){
		
		return "freeWrite";
	}
	
	@RequestMapping(value="/freeWrite.do", method=RequestMethod.POST)
	public String boardWriteProc(@ModelAttribute("FBoardModel") AnswerBoardModel fboardModel, HttpServletRequest request){
//
//		int ref =  fboardModel.getRef();
//		int f_num = fboardModel.getF_num();
//		
//		fboardModel.setRef(ref);
//		fboardModel.setF_num(f_num);
		
		String content = fboardModel.getF_content().replaceAll("<br />", "\r\n");
		fboardModel.setF_content(content);
		
		boardService.writeArticle(fboardModel);	
			
		
			
			
			
			return "redirect:freeList.do";
		}

	
	@RequestMapping("/freeModify.do")
	public ModelAndView boardModify(HttpServletRequest request, HttpSession session){
//		String userId = (String) session.getAttribute("userId");
		int f_num = Integer.parseInt(request.getParameter("f_num"));
		
		AnswerBoardModel board = boardService.getOneArticle(f_num);
		// <br /> tag change to new line code
		String content = board.getF_content().replaceAll("<br />", "\r\n");
		board.setF_content(content);
		//
		
		ModelAndView mav = new ModelAndView();
		
//		if(!userId.equals(board.getWriter())){
			mav.addObject("errCode", "1");	// forbidden connection
			mav.addObject("f_num", f_num);
//			mav.setViewName("redirect:view.do");
//		} else {
			mav.addObject("board", board);
			mav.setViewName("freeModify");
//		}		
		
		return mav;
	}
	
	@RequestMapping(value = "/freeModify.do", method=RequestMethod.POST)
	public ModelAndView boardModifyProc(@ModelAttribute("FBoardModel") AnswerBoardModel fboardModel){
		

//		String orgFileName = request.getParameter("orgFile");
//		MultipartFile newFile = request.getFile("newFile");
//		String newFileName = newFile.getOriginalFilename();
//		
//		boardModel.setFileName(orgFileName);
//		
//		// if: when want to change upload file
//		if(newFile != null && !newFileName.equals("")){
//			if(orgFileName != null || !orgFileName.equals("")){
//				// remove uploaded file
//				File removeFile = new File(uploadPath  + orgFileName);
//				removeFile.delete();
//				//
//			}
//			// create new upload file
//			File newUploadFile = new File(uploadPath +newFileName);
//			try {
//				newFile.transferTo(newUploadFile);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			//
//			boardModel.setFileName(newFileName);
//		}
		//
		// new line code change to <br /> tag
		String content =  fboardModel.getF_content().replaceAll("\r\n", "<br />");		
		fboardModel.setF_content(content);
		//
		
		boardService.modifyArticle(fboardModel);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("f_num", fboardModel.getF_num());
		mav.setViewName("redirect:/fboard/freeView.do");
		return mav;
	}
//}	
	@RequestMapping("/freeDelete.do")
	public ModelAndView boardDelete(HttpServletRequest request, HttpSession session){
//		String userId = (String) session.getAttribute("userId");
		int idx = Integer.parseInt(request.getParameter("f_num"));
		
		AnswerBoardModel board = boardService.getOneArticle(idx);
		
		ModelAndView mav = new ModelAndView();
		
//		if(!userId.equals(board.getWriter())){
			mav.addObject("errCode", "1");	// it's forbidden connection
			mav.addObject("idx", idx);
//			mav.setViewName("redirect:view.do");
		//} else {
//			List<BoardCommentModel> commentList = boardService.getCommentList(idx); // check comments
//			if(commentList.size() > 0){
//				mav.addObject("errCode", "2"); // can't delete because a article has comments
//				mav.addObject("idx", idx);
//				mav.setViewName("redirect:view.do");
//			} else {
//				// if: when the article has upload file - remove that
//				if(board.getFileName() != null){
//					File removeFile = new File(uploadPath + board.getFileName());
//					removeFile.delete();
//				}
				//				
				boardService.deleteArticle(idx);
				
				mav.setViewName("redirect:freeList.do");

		return mav;
	}

	
	@RequestMapping("/freeCommentDelete.do")
	public ModelAndView commendDelete(HttpServletRequest request, HttpSession session){
		int fc_num = Integer.parseInt(request.getParameter("fc_num"));
		int linkedArticleNum = Integer.parseInt(request.getParameter("linkedArticleNum"));
		
//		String userId = (String) session.getAttribute("userId");
//		BoardCommentModel comment = boardService.getOneComment(idx);
		
		ModelAndView mav = new ModelAndView();
		
//		if(!userId.equals(comment.getWriterId())){
			mav.addObject("errCode", "1");
//		} else {
			boardService.deleteComment(fc_num);
//	}	
				
		mav.addObject("f_num", linkedArticleNum); // move back to the article
		mav.setViewName("redirect:freeView.do");
		
		return mav;
	}

	
////	@RequestMapping("/recommend.do")
////	public ModelAndView updateRecommendcount(HttpServletRequest request, HttpSession session){
////		int idx = Integer.parseInt(request.getParameter("idx"));
////		String userId = (String) session.getAttribute("userId");
////		BoardModel board = boardService.getOneArticle(idx);
////		
////		ModelAndView mav = new ModelAndView();
////		
////		if(userId.equals(board.getWriterId())){
////			mav.addObject("errCode", "1");
////		} else {
////			boardService.updateRecommendCount(board.getRecommendcount()+1, idx);
////		}
////		
////		mav.addObject("idx", idx);
////		mav.setViewName("redirect:/board/view.do");
////		
////		return mav;
////	}
//}

	

	
//	@RequestMapping("/commentModify.do")
//	public ModelAndView commentModifyProc(@ModelAttribute("CommentModel") BoardCommentModel commentModel){
//		// new line code change to <br /> tag
//		String content = commentModel.getContent().replaceAll("\r\n", "<br />");
//		commentModel.setContent(content);
//		//
//		boardService.modifyComment(commentModel);
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("idx", commentModel.getLinkedArticleNum());
//		mav.setViewName("redirect:view.do");
//		
//		return mav;
//	}
//	
	
	
	@RequestMapping(value="/freeWriteReply.do", method=RequestMethod.GET)
	public ModelAndView replyBoard(HttpServletRequest request, HttpServletResponse response) {
		int f_num = Integer.parseInt(request.getParameter("f_num"));	
		
		System.out.println("replyBoard fnum : " + request.getParameter("f_num"));
		
		ModelAndView mav = new ModelAndView();
//		FBoardModel board = boardService.getOneArticle(f_num);
		if(request.getParameter("f_num") != null && !request.getParameter("f_num") .equals("")) {
			try {		
				AnswerBoardModel board = boardService.getOneArticle(f_num);
				String content = board.getF_content();
				
				System.out.println("!! content : " + content);
				
				content = content.replaceAll ("\"", "'");
				board.setF_content(content);
				
				System.out.println("board.fnum : " + board.getF_num());
				
				mav.addObject("board", board);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
//			
//			mav.addObject("board", board);
//			mav.addObject("commentList", commentList);
//			mav.setViewName("/fboard/view");
//			return mav;
		}
//		mav.addObject("board", board);

		mav.setViewName("freeWriteReply");
		return mav;
	}
	
	@RequestMapping(value="/freeWriteReply.do", method=RequestMethod.POST)
	public void insertReplyBoard(HttpServletRequest request, HttpServletResponse response, AnswerBoardModel board, BindingResult errors) {
		if(errors.hasErrors()) {
			//logger.info("errors..");
		}		
		try {		
			int f_num = 0;	
			System.out.println("1111 : " + request.getParameter("f_num"));
			if(request.getParameter("f_num") != null && !request.getParameter("f_num").equals("")) {
				System.out.println("2222");
				String content = request.getParameter("f_content");//content�� �߸���� null �������
				
				System.out.println("3333");
				
				board.setF_content(content);
				
				System.out.println("3-3 f_num : " + board.getF_num());
				
				System.out.println("4-1 f_num : " + request.getParameter("f_num"));
				System.out.println("4444 ref : " + request.getParameter("ref"));
				
				System.out.println("4444 board ref : " + board.getRef());
				
				System.out.println("4444 board step : " + board.getRe_step());
				
				f_num = boardService.insertReplyBoard(board);
				
				System.out.println("5-1 f_num : " + board.getF_num());
				
				System.out.println("5555");
				System.out.println(content);
			}
			
			System.out.println("6666");
//			response.sendRedirect("/CyberArtMuseum/fboard/view.do?f_num="+f_num);	//f_num���� �ƹ��ų� ������ 
			response.sendRedirect("/CyberArtMuseum/fboard/freeList.do");
			System.out.println("7777");
			System.out.println("f_num" + f_num);
		}catch(Exception e) {
			//logger.info("insert Fail...");
			e.printStackTrace();
		}
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