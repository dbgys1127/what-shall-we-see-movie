<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#inquiry").addClass("now-click");
    });
</script>
<body>
<h2> 문의 상세화면 </h2>
<table class="table" >
    <!-- 표 헤더 -->
        <thead class="table-dark">
            <th>작성자</th>
            <th>작성일</th>
            <th>처리 상태</th>
            <th>처리 체크</th>
        </thead>
            <tr>
                <!-- 표안에 보여질 관리자 정보 -->
                <td>${inquiry.createdBy}</td>
                <td>
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${inquiry.inquiryStatus}</td>
<form action="/admin/inquiry/answer?inquiryId=${inquiry.inquiryId}" method="post">
                <td><input type="checkbox" name="inquiryStatus" <c:if test="${inquiry.inquiryStatus eq '처리'}">checked</c:if> /></td>
            </tr>
</table>
<div class="row">
    <h2 class="bg-dark"> ${inquiry.inquiryTitle} </h2>
    <p class="bg-white">${inquiry.inquiryDescription}</p>
</div>
<div class="row">
    <textarea class="form-control" name="answerDescription" placeholder="답변을 등록하세요."></textarea>
    <button type="submit" class="btn btn-dark">답변등록</button>
</div>
</form>
<div class="row">
    <c:forEach var="answer" items="${inquiry.answers}">    
        <tr><td>${answer.answerDescription}</td></tr>
        <tr>
            <td>${answer.createdBy}</td>
            <td>
                <fmt:parseDate value="${answer.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </td>
        </tr>
        <tr>
            <td>
                <button type="submit">수정</button>
            </td>
            <td>
                <form action="/admin/inquiry/answer/delete?inquiryId=${inquiry.inquiryId}&answerId=${answer.answerId}" method="post">
                    <button type="submit">삭제</button>
                </form>
            </td>
        </tr>
    </c:forEach>  
</div>

</body>
</html>