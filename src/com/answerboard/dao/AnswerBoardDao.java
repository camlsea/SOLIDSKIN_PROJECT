package com.answerboard.dao;

import java.util.List;

import com.answerboard.model.AnswerBoardModel;

public interface AnswerBoardDao {
	// get all contents in JMBoard table
	List<AnswerBoardModel> getFboardList(int startArticleNum, int showArticleLimit);
	
	// show detail about selected article
	AnswerBoardModel getOneArticle(int f_num);
	
	// get search result list
	List<AnswerBoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum); 
	
	// modify(update) article
	boolean modifyArticle(AnswerBoardModel board);
	
	// insert article data
	boolean writeArticle(AnswerBoardModel board);
	
//	boolean writeArticle(FBoardModel board, int f_num, int ref);
	
	// update hitcount
	void updateHitcount(int count, int f_num);
	
	// update recommendcount
	void updateRecommendCount(int recommendcount, int f_num);
	
	// get contents count
	int getTotalNum();
	
	// get count of search results
	int getSearchTotalNum(String type, String keyword);
	
	// delete a comment
	void deleteComment(int f_num);
	
	// delete a article
	void deleteArticle(int f_num);

	//insert reply
//	boolean writeReply(FBoardModel board);

	//	public int insertReplyBoard(FBoardModel board);

	
	//아래 4개 public 뺐음
	//최상위 부모글 번호생성
	int getRefMax();
		
	//같은 ref(최상위부모값)을 가진 것 중 현재 답글을 달 게시물의 순번보다 큰것을 +1로하며 하나씩 밀리게 한다.	
	void updateCommunitySetp(AnswerBoardModel board);
		
	//현재 답글의 순번을 생성
	int  getStepMax(AnswerBoardModel board);
	//리플 저장
	int insertReplyBoard(AnswerBoardModel board);

	
	
	}
