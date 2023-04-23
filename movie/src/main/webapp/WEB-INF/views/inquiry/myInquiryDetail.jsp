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
      $("#h-inquiry").addClass("now-click");
    });
</script>
<body>
<h2> 문의 상세화면 </h2>
<div class="container" style="border: 1px solid;">
    문의
    <div class="row" style="margin-top: 15px;">
        <div class="col-md-12 flex-grow-1 me-2">
            ${inquiry.inquiryTitle}
        </div>
    </div>
    <div class="row" style="margin-top: 15px;">
        <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
            ${inquiry.inquiryDescription}
        </div>
    </div>
    <div class="row">
        <div class="col-md-1 flex-grow-1 me-2">
            <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
            <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
        </div>
        <div class="col-md-1 flex-grow-1 me-2">
            <c:out value="${inquiry.inquiryStatus}"></c:out>
        </div>
        <div class="col-md-1 flex-grow-1 me-2">
            <form action="/inquiry/patch?inquiryId=${inquiry.inquiryId}" method="post">
                <button type="submit" class="btn btn-dark">수정</button>
            </form>
        </div>
        <div class="col-md-1 flex-grow-1 me-2">
            <form action="/inquiry/delete?inquiryId=${inquiry.inquiryId}" method="post">
                <button type="submit" class="btn btn-dark">삭제</button>
            </form>
        </div>
    </div>
</div>

<div class="container" style="border: 1px solid;">
    답변
    <c:forEach var="answer" items="${inquiry.answers}">   
        <div class="row">
            <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                ${answer.answerDescription}
            </div>
        </div>
        <div class="row">
            <div class="col-md-1 flex-grow-1 me-2">
                ${answer.createdBy}
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <fmt:parseDate value="${answer.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
        </div>
    </c:forEach>  
</div>
</body>
</html>