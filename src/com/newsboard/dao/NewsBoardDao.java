package com.newsboard.dao;

import java.util.List;

import com.newsboard.model.NewsBoardBeans;
import com.newsboard.model.NewsBoardGubunModel;

public interface NewsBoardDao {
	
	// get all contents in JMBoard table
		List<NewsBoardBeans> getIboardList(int startArticleNum, int showArticleLimit);
		
		// show detail about selected article
		NewsBoardBeans getOneIarticle(int idx);
		
		public NewsBoardGubunModel getBoardGubun(NewsBoardGubunModel artBoardGubunModel);
		// get search result list
		List<NewsBoardBeans> searchIarticle(String type, String keyword, int startArticleNum, int endArticleNum); 
		
		// modify(update) article
		boolean modifyIarticle(NewsBoardBeans board);
		
		// insert article data
		boolean writeIarticle(NewsBoardBeans board);
		
		// update hitcount
		void updateHitcount(int hitcount, int idx);
		
		// get contents count
		int getItotalNum();
		
		// get count of search results
		int getIsearchTotalNum(String type, String keyword);
		
		// delete a article
		void deleteIarticle(int idx);
		
		//DelFile a article
		void DelFileIarticle(String FileName);
		
		List<NewsBoardBeans> getImageList();
		
//		ArtBoardBeans getImageList(ArtBoardBeans artBoardBeans);
		
//		public ArtBoardBeans getImageList(ArtBoardBeans artBoardBeans);
		
//		MemberModel getUserGubun(int userGubun);

}
