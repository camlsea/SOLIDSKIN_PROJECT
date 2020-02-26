package com.answerboard.dao;
//package com.ojt.bbs.dao;
//
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//
//
//import org.springframework.orm.ibatis.SqlMapClientTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.ojt.bbs.model.FBoardCommentModel;
//import com.ojt.bbs.model.FBoardModel;
//
//
//public class FBoardDaoImpl implements FBoardDao {
//
//	private SqlMapClientTemplate sqlMapClientTemplate;
//	private HashMap<String, Object> valueMap = new HashMap<String, Object>();
//	
//	@Override
//	public int getRefMax() throws Exception {
//		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getRefMax");
//	}
//	
//	
//	
//	@Override
//	public void updateCommunitySetp(FBoardModel board) throws Exception {
//		sqlMapClientTemplate.update("fboard.updateCommunitySetp",board);
//	}
//	
//	@Override
//	public int  getStepMax(FBoardModel board) throws Exception {
//		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getStepMax");
//				
//	}
//	
//	//����
//	@Override
//	public int insertReplyBoard(FBoardModel board) throws Exception {
////		logger.info("insertReplyBoard invoked...");
//		sqlMapClientTemplate.insert("fboard.insertReplyBoard", board);
//		int f_num = board.getF_num();
//		return f_num;		
//	}
//
//
//
//}
//
