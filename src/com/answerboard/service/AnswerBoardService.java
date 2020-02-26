package com.answerboard.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.answerboard.dao.AnswerBoardDao;
import com.answerboard.model.AnswerBoardModel;


public class AnswerBoardService implements AnswerBoardDao {
	
	@Autowired
	private AnswerBoardDao fboardDao;
	
	private SqlMapClientTemplate sqlMapClientTemplate;
	private HashMap<String, Object> valueMap = new HashMap<String, Object>();
		

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<AnswerBoardModel> getFboardList(int startArticleNum, int endArticleNum) {
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("fboard.getFboardList", valueMap);
	}

	@Override
	public AnswerBoardModel getOneArticle(int f_num) {
		return (AnswerBoardModel) sqlMapClientTemplate.queryForObject("fboard.getOneArticle", f_num);
	}

	@Override
	public List<AnswerBoardModel> searchArticle(String type, String keyword, int startArticleNum, int endArticleNum) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		valueMap.put("startArticleNum", startArticleNum);
		valueMap.put("endArticleNum", endArticleNum);
		return sqlMapClientTemplate.queryForList("fboard.searchArticle", valueMap);
	}

	@Override
	public boolean writeArticle(AnswerBoardModel board) {
		
		int newref = this.getRefMax();
		board.setRef(newref);	//sqlMapClientTemplate.insert("fboard.writeArticle", board); ����Ἥ ref���� DB�� �ȵ���
		
		sqlMapClientTemplate.insert("fboard.writeArticle", board);
		
		
		
		System.out.println(newref);
//		return this.writeArticle(board); ���ѷ����߻� ����������������������������
		
		return true;
	}

//	@Override
//	public boolean writeArticle(FBoardModel board, int f_num, int ref) {
//		
//		valueMap.put("f_num", f_num);
//		valueMap.put("newref", ref);
//		
//		sqlMapClientTemplate.queryForList("fboard.writeArticle", valueMap);
//		
//		int newref = this.getRefMax();
//		board.setRef(newref);
//		
////		getSqlSession().insert("newboard.insertBoard", newBoardDTO);
////		int f_num = board.getF_num();
////		return b_no;
//		
//				
//		System.out.println(newref);
////		return this.writeArticle(board); ���ѷ����߻� ����������������������������
//		
//		return true;
//	}

	@Override
	public void updateHitcount(int f_count, int f_num) {
		valueMap.put("f_count", f_count);
		valueMap.put("f_num", f_num);
		sqlMapClientTemplate.update("fboard.updateHitcount", valueMap);		
	}

	@Override
	public void updateRecommendCount(int recommendcount, int fc_num) {
		valueMap.put("recommendcount", recommendcount);
		valueMap.put("fc_num", fc_num);
		sqlMapClientTemplate.update("fboard.updateRecommendcount", valueMap);
		
	}
	@Override
	public int getTotalNum() {
		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getTotalNum");
	}

	@Override
	public int getSearchTotalNum(String type, String keyword) {
		valueMap.put("type", type);
		valueMap.put("keyword", keyword);
		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getSearchTotalNum", valueMap);
	}

	@Override
	public void deleteComment(int fc_num) {
		sqlMapClientTemplate.delete("fboard.deleteComment", fc_num);
	}
	
	@Override
	public void deleteArticle(int f_num) {
		sqlMapClientTemplate.delete("fboard.deleteArticle", f_num);	
	}

	@Override
	public boolean modifyArticle(AnswerBoardModel board) {
		sqlMapClientTemplate.update("fboard.modifyArticle", board);
		return true;
	}
	
//	//���� ����
//	@Override
//	public boolean writeReply(FBoardModel board) {
//		sqlMapClientTemplate.insert("fboard.writeReply", board);		
//		return true;
//	}

	//��� ����
	@Override
//	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={Exception.class})		
	public int insertReplyBoard(AnswerBoardModel fboard){
//		sqlMapClientTemplate.insert("fboard.insertReplyBoard", fboard);

		//���(step)�� �� �����Ѵ� 
		//���� ref(�ֻ����θ�)�� ���� �� �� ���� ����� �� �Խù��� ���� ū���� +1�� �ϸ� �ϳ��� �и��� �Ѵ�.
		this.updateCommunitySetp(fboard);
		
		//���� ����� ����� ���ϰ� �𵨿� �����Ѵ�.
		int restep = this.getStepMax(fboard);		
		//int f_num = fboard.getF_num();
		System.out.println("restep : " + restep);
		
		fboard.setRe_step(restep);
		//fboard.setF_num(f_num);
		sqlMapClientTemplate.insert("fboard.insertReplyBoard", fboard);
		
		System.out.println(restep);
		
		//��� ����
		return restep;	//���ѷ��� , ���� int�� ����������Ѵ�. controller���� int�� �ҷ��ִϱ�
	}

	//insertReplyBoard���� ������ ���� . �Ʒ� 3��
	@Override
	public int getRefMax(){
		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getRefMax");//RefMax ���� �����ͼ� board�� �ȴ���ְ� ������
	}
			
	@Override
	public void updateCommunitySetp(AnswerBoardModel board){
		sqlMapClientTemplate.update("fboard.updateCommunitySetp",board);
	}
	
	@Override
	public int getStepMax(AnswerBoardModel board){
		return (Integer) sqlMapClientTemplate.queryForObject("fboard.getStepMax",board);//board ���� �ʾҾ���
				
	}

	
}