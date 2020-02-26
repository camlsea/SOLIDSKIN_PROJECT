package com.newsboard.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.newsboard.dao.NewsBoardDao;
import com.newsboard.model.NewsBoardBeans;
import com.newsboard.model.NewsBoardGubunModel;

public class NewsBoardService implements NewsBoardDao {
	
	private SqlMapClientTemplate sqlMapClientTemplate;
	private HashMap<String, Object> valueMap = new HashMap<String, Object>();
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<NewsBoardBeans> getIboardList(int startArticleNum,
			int endArticleNum) {
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.getIboardList", valueMap);
	}

	@Override
	public NewsBoardBeans getOneIarticle(int idx) {
		return (NewsBoardBeans) sqlMapClientTemplate.queryForObject("board.getOneIarticle", idx);
	}
	
	@Override
	public NewsBoardGubunModel getBoardGubun(NewsBoardGubunModel artBoardGubunModel) {
		// TODO Auto-generated method stub
		return (NewsBoardGubunModel) sqlMapClientTemplate.queryForObject("board.getBoardGubun", artBoardGubunModel);
	}

	@Override
	public List<NewsBoardBeans> searchIarticle(String type, String keyword,
			int startArticleNum, int endArticleNum) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("board.searchIarticle", valueMap);
	}

	@Override
	public boolean modifyIarticle(NewsBoardBeans board) {
		sqlMapClientTemplate.update("board.modifyIarticle", board);
		return true;
	}

	@Override
	public boolean writeIarticle(NewsBoardBeans board) {
		sqlMapClientTemplate.insert("board.writeIarticle", board);		
		return true;
	}

	@Override
	public void updateHitcount(int a_count, int idx) {
		valueMap.put("hitcount", a_count);
		valueMap.put("idx", idx);
		sqlMapClientTemplate.update("board.updateHitcount", valueMap);		
	}

	@Override
	public int getItotalNum() {
		return (Integer) sqlMapClientTemplate.queryForObject("board.getItotalNum");
	}

	@Override
	public int getIsearchTotalNum(String type, String keyword) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		return (Integer) sqlMapClientTemplate.queryForObject("board.getIsearchTotalNum", valueMap);
	}

	@Override
	public void deleteIarticle(int idx) {
		sqlMapClientTemplate.delete("board.deleteIarticle", idx);	
	}

	@Override
	public void DelFileIarticle(String FileName) {
		sqlMapClientTemplate.update("board.DelFileIarticle", FileName);
		
	}
	@Override
	public List<NewsBoardBeans> getImageList(){
//		valueMap.put("idx", a_idx);
//		valueMap.put("imagenname", a_imagename);
			return sqlMapClientTemplate.queryForList("board.getImageList");
	}
	
//	@Override
//	public ArtBoardBeans getImageList(ArtBoardBeans artBoardBeans) {
//		return (ArtBoardBeans) sqlMapClientTemplate.queryForObject("board.getImageList", artBoardBeans);
//	}
	
//	public ArtBoardBeans getImageList(ArtBoardBeans artBoardBeans){
//		return (ArtBoardBeans) sqlMapClientTemplate.queryForObject("board.getImageList", artBoardBeans);
//	}
	
//	public MemberModel getUserGubun(int userGubun) {
//		return (MemberModel) sqlMapClientTemplate.queryForObject("board.getUserGubun", userGubun);
//	}

}
