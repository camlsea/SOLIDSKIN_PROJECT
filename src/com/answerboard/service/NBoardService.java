package com.answerboard.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.answerboard.dao.NBoardDao;
import com.answerboard.model.NewsBoardModel;


public class NBoardService implements NBoardDao {
	private SqlMapClientTemplate sqlMapClientTemplate;
	private HashMap<String, Object> valueMap = new HashMap<String, Object>();
		

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<NewsBoardModel> getBoardList(int startArticleNum, int endArticleNum) {
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.getBoardList", valueMap);
	}

	@Override
	public NewsBoardModel getOneArticle(int n_num) {
		return (NewsBoardModel) sqlMapClientTemplate.queryForObject("board.getOneArticle", n_num);
	}

	@Override
	public List<NewsBoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.searchArticle", valueMap);
	}

	@Override
	public boolean writeArticle(NewsBoardModel board) {
		sqlMapClientTemplate.insert("board.writeArticle", board);		
		return true;
	}

	@Override
	public void updateHitcount(int n_count, int n_num) {
		valueMap.put("n_count", n_count);
		valueMap.put("n_num", n_num);
		sqlMapClientTemplate.update("board.updateHitcount", valueMap);		
	}

	@Override
	public void updateRecommendCount(int recommendcount, int nc_num) {
		valueMap.put("recommendcount", recommendcount);
		valueMap.put("nc_num", nc_num);
		sqlMapClientTemplate.update("board.updateRecommendcount", valueMap);
		
	}
	@Override
	public int getTotalNum() {
		return (Integer) sqlMapClientTemplate.queryForObject("board.getTotalNum");
	}

	@Override
	public int getSearchTotalNum(String type, String keyword) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		return (Integer) sqlMapClientTemplate.queryForObject("board.getSearchTotalNum", valueMap);
	}

	@Override
	public void deleteComment(int nc_num) {
		sqlMapClientTemplate.delete("board.deleteComment", nc_num);
	}
	
	@Override
	public void deleteArticle(int n_num) {
		sqlMapClientTemplate.delete("board.deleteArticle", n_num);	
	}


	@Override
	public boolean modifyArticle(NewsBoardModel board) {
		sqlMapClientTemplate.update("board.modifyArticle", board);
		return true;
	}


}
