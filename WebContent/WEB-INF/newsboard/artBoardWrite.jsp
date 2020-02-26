<%@page import="java.util.regex.Pattern"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.sql.*" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/common/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/common/css/contents.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/BPNR-HtmlDomObjects.js"></script>
<script src="http://www.prototypejs.org/javascripts/prototype.js"></script>
</head>
<style>
     #imgPrev {display:block;width:200px;height:200px;border:1px dashed RGB(211, 211, 211);}
</style>
<body>
<form id="checkFileForm" name="checkFileForm" action="artWrite.do" method="post" enctype="multipart/form-data" onsubmit="return formCheck();">
<!-- Layout -->
<div class="wrap">
	<div class="layout">
		<!-- Title -->
		<div class="page_title">
			<h1><strong><span>등록화면</span></strong></h1>
		</div>

		<!-- Input Area -->
		<div class="input_area">
			<div class="layout">
				<table>
					<colgroup>
						<col width="120px"/>
						<col  />
					</colgroup>
					<tr>
						<th class=""><label for="type">종류</label></th>
						<td><select id="a_gubunboard" name="a_gubunboard">
							<option value="1">서양화</option>
							<option value="2">동양화</option>
						</select></td>
					</tr>
					<tr>
						<th class=""><label for="name">작가</label></th>
						<td><input type="text" id="a_name" name="a_name" ></td>
					</tr>
					<tr>
						<th class=""><label for="title">제목</label></th>
						<td><input type="text" id="a_title" name="a_title"></td>
					</tr>
					<tr>
						<th class=""><label for="year">제작년도</label></th>
						<td><input type="text" id="a_year" name="a_year"></td>
					</tr>
					<tr>
						<th class=""><label for="own">소장</label></th>
						<td><input type="text" id="a_own" name="a_own"></td>
					</tr>
					<tr>
						<th class=""><label for="content">소개</label></th>
						<td colspan="2">
							<textarea rows="15" class="tran" id="a_content" name="a_content" style='width: 85%; height: 200px;'></textarea>
						</td>
					</tr>
					<tr>
						<th><label for="file">파일</label></th>
						<td>
						<div id="fileDiv">
						<input type="file" id="file" name="file" onchange="previewImage(this,'previewId');"/><br/>
						</div>
						<div id='previewId'
							style='display:block;width:200px;height:200px;border:1px dashed RGB(211, 211, 211);'>
						</div>
						<span class="date">&nbsp;&nbsp;*&nbsp;임의로 파일명이 변경될 수 있습니다.</span></td>			
					</tr>				
				</table>       
			</div>
		</div>

		<!-- Button -->
		
		<div class="btn_area tr">
			<input type="submit" value="확인">
			<input type="reset" value="재작성"/>
			<input type="button" value="취소" onclick="moveAction();" />
		</div>
	</div>
</div>
</form>
</body>
<script> 
window.onload = function(){
	document.body.className = "prtlBody urFontBaseFam urScrl body_bg";
}

function check(val){
	if (val == null || val == ""){ return true;} else {return false;}
}
	
	function trim(str){
		 return String(str).replace(/^\s+|\s+$/g, "");
}; 


function formCheck(){

//	var length = document.forms[0].length-1; // submit을 제외한 모든 input태그의 갯수를 가져옴
//	for(var i = 0; i < length; i++){		 // 루프를 돌면서 
//		if(document.forms[0][i].value == null || document.forms[0][i].value == ""){	
//			alert(document.forms[0][i].name + "을/를 입력하세요."); // 경고창을 띄우고
//			document.forms[0][i].focus();			// null인 태그에 포커스를 줌
//			return false;
//		}//end if
//	}//end for

var a_name    = document.forms[0].a_name.value;
var a_title   = document.forms[0].a_title.value;
var a_content = document.forms[0].a_content.value;


if(check(trim(a_name))){
	alert("이름을 입력해 주세요");
	document.forms[0].a_name.focus(); //커서를 타이틀로 옮겨줌
	return false;
	
}

if(a_title==null||a_title==""){
	alert("제목을 입력해 주세요");
	document.forms[0].a_title.focus();
	return false;
}

if(a_content==null||a_content==""){
	alert("내용을 입력해 주세요");
	document.forms[0].a_content.focus();
	return false;
	
}
}

function fileCheckGo(){

var ob = document.checkFileForm;
var fileObj = document.getElementById("file");
var strCheckValue = "";
strCheckValue = ((ob.file.value).substring((ob.file.value).lastIndexOf(".")+1)).toUpperCase();
if(!(strCheckValue == "GIF" || strCheckValue == "JPG" || strCheckValue == "JPEG" || strCheckValue == "BMP" || strCheckValue == "PNG")){
	alert("파일형식(gif,jpg,bmp)을 확인해 주십시요!");
	return;
}else{
       if (navigator.userAgent.indexOf('MSIE') < 0) {  //ie가 아닐때.
        if (typeof FileReader !== "undefined")
        var fileSize = fileObj.files[0].size;
       }
    else if (navigator.userAgent.indexOf('MSIE') > 0 && navigator.appVersion.indexOf('MSIE 8.') > 0) {
     //IE8
     var fso = new ActiveXObject("Scripting.FileSystemObject");
     var f = fso.GetFile(fileObj.value);
      fileSize = f.size;
     f = null;
     fso = null;
    }
    else if(navigator.userAgent.indexOf('MSIE') > 0 && navigator.appVersion.indexOf('MSIE 7.') > 0) {
     //IE7
     var fso = new ActiveXObject("Scripting.FileSystemObject");
     var f = fso.GetFile(fileObj.value);
     var fileSize = f.size;
     f = null;
     fso = null;
     }
    else {  //ie 7 미만. - ie 6
     var img = new Image();
     img.dynsrc = fileObj.value;
     fileSize = img.fileSize;
    }
   // alert(fileSize);
    if (fileSize / 50000 > 10) //10kb 초과면!
    {
     alert("파일 용량이 초과 하였습니다.");
     if(fileSize == null || fileSize <=0){
    	 fileValueReset(); 
     }else{
	     fileObj.value = "";
	     return false;
     }
    }else{
     return true;	
    }
}
}


function previewImage(targetObj, previewId) {

var preview = document.getElementById(previewId); //div id   
var ua = window.navigator.userAgent;
fileCheckGo();
if (ua.indexOf("MSIE") > -1) {//ie일때

    targetObj.select();

  try {
      var src = document.selection.createRange().text; // get file full path 
      var ie_preview_error = document
              .getElementById("ie_preview_error_" + previewId);

      if (ie_preview_error) {
          preview.removeChild(ie_preview_error); //error가 있으면 delete
      }

      var img = document.getElementById(previewId); //이미지가 뿌려질 곳 

      img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
              + src + "', sizingMethod='scale')"; //이미지 로딩, sizingMethod는 div에 맞춰서 사이즈를 자동조절 하는 역할
  } catch (e) {
      if (!document.getElementById("ie_preview_error_" + previewId)) {
          var info = document.createElement("<p>");
          info.id = "ie_preview_error_" + previewId;
          info.innerHTML = "a";
          preview.insertBefore(info, null);
      }
  }
} else { //ie가 아닐때
    var files = targetObj.files;
    for ( var i = 0; i < files.length; i++) {

        var file = files[i];

        var imageType = /image.*/; //이미지 파일일경우만.. 뿌려준다.
        if (!file.type.match(imageType))
            continue;

        var prevImg = document.getElementById("prev_" + previewId); //이전에 미리보기가 있다면 삭제
        if (prevImg) {
            preview.removeChild(prevImg);
        }

        var img = document.createElement("img"); //크롬은 div에 이미지가 뿌려지지 않는다. 그래서 자식Element를 만든다.
        img.id = "prev_" + previewId;
        img.classList.add("obj");
        img.file = file;
        img.style.width = '50px'; //기본설정된 div의 안에 뿌려지는 효과를 주기 위해서 div크기와 같은 크기를 지정해준다.
        img.style.height = '50px';
        
        preview.appendChild(img);

    if (window.FileReader) { // FireFox, Chrome, Opera 확인.
       var reader = new FileReader();
       reader.onloadend = (function(aImg) {
           return function(e) {
               aImg.src = e.target.result;
           };
     	})(img);
 		 reader.readAsDataURL(file);
		} else { // safari is not supported FileReader
 	 //alert('not supported FileReader');
	  if (!document.getElementById("sfr_preview_error_"
          + previewId)) {
          var info = document.createElement("p");
          info.id = "sfr_preview_error_" + previewId;
          info.innerHTML = "not supported FileReader";
          preview.insertBefore(info, null);
     }
 	}
  }
 }
}

	//첨부파일부분 리셋!!
	function fileValueReset(){
		var fileDiv = document.getElementById("file");
		fileDiv.innerHTML = "<input name=\"file\" type=\"file\" size=\"20\" onchange=\"previewImage();\">";
	}
	function moveAction(){
		
		location.href = "list.do";
		
	}
</script>
</html>