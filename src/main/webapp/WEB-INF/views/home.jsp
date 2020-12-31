<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<style>#tb{margin: auto; width: 700px;}</style>
</head>
<body>
	<c:import url="common/menubar.jsp"></c:import>
	
	<script>
		$(function(){
			var msg = '${msg}';
			if(msg != ""){
				alert(msg);
			}
		});
	</script>
	
	<h1>게시글 TOP5 목록</h1>
	<table id="tb" border=1 align="center">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>내용</th>
				<th>날짜</th>
				<th>조회수</th>
				<th>첨부파일</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	
	<script>
		function topList(){
			$.ajax({
				url: 'topList.bo',
				success: function(data){
					console.log(data);
					
					$tableBody = $('#tb tbody');
					$tableBody.html('');
					
					for(var i in data){
						var $tr = $('<tr>');
						var $bId = $('<td>').text(data[i].bId);
						var $bTitle = $('<td>').text(data[i].bTitle);
						var $bWriter = $('<td>').text(data[i].bWriter);
						var $bContent = $('<td>').text(data[i].bContent);
						var $bCreateDate = $('<td>').text(data[i].bCreateDate);
						var $bCount = $('<td>').text(data[i].bCount);
						var $bFile = $('<td>').text('');
						
						if(data[i].originalFileName != null) {
							$bFile = $('<td>').text("O");
						}
						
						$tr.append($bId);
						$tr.append($bTitle);
						$tr.append($bWriter);
						$tr.append($bContent);
						$tr.append($bCreateDate);
						$tr.append($bCount);
						$tr.append($bFile);
						
						$tableBody.append($tr);
					}
				}
			});
		}
		
		$(function(){
			topList();
			setInterval(function(){
				topList();
			}, 5000);
		});
	</script>
</body>
</html>