<%@page import="java.util.regex.Pattern"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.sql.*" %>	
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.newsboard.model.NewsBoardBeans" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/common/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/common/css/contents.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/BPNR-HtmlDomObjects.js"></script>
<head>
<style>
*{
	margin:0;
	padding:0;
	font-size:100%;
}

body{
	font:11px 돋움,dotum;
	letter-spacing:-1px;
	color:#666;
	margin-top:20px;
	background:#F6F5F0;
	text-align:center;
}

a{color:#666;text-decoration:none;}
#wrap{width:1000px;margin:0 auto;position:relative;}
#main-content li{
	width:200px;
	height:200px;
	border:solid #CCC;
	border-width:0 1px 1px 0;
	background:#FFF;
	padding:10px;
	text-align:center;
	position:relative;
	float:left;
	display:inline;
	margin:3px;
}


#main-content  a span{position:absolute;left:-9999px;}
#main-content a:hover span{	
	top:-20px;
	left:10px;
	width:202px;
	font-weight:bold;
	background:#FFF;
	color:#999;
	padding:5px 0 5px 0;
	text-transform:uppercase;
	font-size:11px;
	border:1px solid #EEE;
	border-width:0 0 10px 0;
	filter: alpha(opacity=100);
} 

img{border:1px solid #EEE;}
</style>
</head>
<body>
<h1>게시글 리스트</h1>
<form action="list.do" method="post">
<input type="hidden" id="idx" name="idx" value="${board.a_idx }">
<input type="hidden" id="a_idx" name="a_idx" value="${board.rnum }">
<!-- Layout -->
<div class="wrap">
	<div class="layout">
		<!-- Title -->
		<div class="page_title">
			<h1><strong><span> 서양예술</span></strong></h1>
			<a href="<%=request.getContextPath()%>/logout.do">로그아웃</a>
		</div>
		<!-- Button -->
		<div class="btn_area tr">
			<a class="btn" href="artWrite.do" title="글쓰기"><strong><img src="<%=request.getContextPath()%>/common/images/ico/ico_report2.gif">등록</strong></a>
		</div>
<!-- Input Area -->
		<div class="input_area">
			<div class="layout">
				<table>
					<colgroup>
						<col width="92px" />
						<col width="200px" />
						<col />
					</colgroup>
					<tr>
					    <th class=""><label for="ser_project_startdate">검색조건</label></th>
						<td class="tl">
						</td>
						<td class="tl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<select id="type" name="type">
								<option value="a_title">제목</option>
								<option value="a_name">작성자</option>
							</select>
						<input style="width:200px" type="text" id="keyword" name="keyword" value="<%if(request.getParameter("keyword") != null){ out.print(request.getParameter("keyword")); } else { out.print(""); }%>" />
						<input type="submit" value="검색" />	
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="list_area">
	<div class="layout">
			<colgroup>
				<col width="70px"/>
				<col />
				<col width="120px"/>
				<col width="120px"/>
				<col width="80px"/>
				<col width="80px"/>
			</colgroup>
	<div id="wrap"> 
	  <div id="main-content"> 
	  		<div>
				<c:forEach var="board" items="${artBoardList}">
			 		<tr>
						<td>
						<ul>
			    			<li><div id="photo_1"><img src="<%=request.getContextPath()%>/files/${board.a_imagename}" style="width:150px; height:150;" alt="" /><a type="hidden" href="view.do?idx=${board.a_idx}">
			    			<br>${board.a_name }</br><br>${board.a_title }</br></a></div></li>
						</ul>
						</td>
					</tr>
				</c:forEach>
			</div>		
		</div>
	</div>
	</div>
	</div>
		<div class="paginate">
			${pageHtml }
			<!-- 페이징 처리부 끝 -->
		</div>
</form>
</body>
<script> 
window.onload = function(){
	document.body.className = "prtlBody urFontBaseFam urScrl body_bg";
}
</script>
</html>