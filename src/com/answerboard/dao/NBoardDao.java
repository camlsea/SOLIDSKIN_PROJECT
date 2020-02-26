package com.answerboard.dao;

import java.util.List;

import com.answerboard.model.NewsBoardModel;

public interface NBoardDao {
	// get all contents in JMBoard table
	List<NewsBoardModel> getBoardList(int startArticleNum, int showArticleLimit);
	
	// show detail about selected article
	NewsBoardModel getOneArticle(int n_num);
	
	// get search result list
	List<NewsBoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum); 
	
	// modify(update) article
	boolean modifyArticle(NewsBoardModel board);
	
	// insert article data
	boolean writeArticle(NewsBoardModel board);
	
	// update hitcount
	void updateHitcount(int count, int n_num);
	
	// update recommendcount
	void updateRecommendCount(int recommendcount, int n_num);
	
	// get contents count
	int getTotalNum();
	
	// get count of search results
	int getSearchTotalNum(String type, String keyword);
	
	// delete a comment
	void deleteComment(int n_num);
	
	// delete a article
	void deleteArticle(int n_num);
}
