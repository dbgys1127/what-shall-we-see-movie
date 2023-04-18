<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>

<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}

</style>
<body>
<h2>문의</h2>
<div class="container text-center" style="border: 1px solid;">
    <c:forEach var="inquiry" items="${pageData.data}">    
        <div class="row" style="margin-top: 15px;">
            <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                <a href="/inquiry/detail?inquiryId=${inquiry.inquiryId}">${inquiry.inquiryTitle}</a>
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
    </c:forEach>    
    <button type="button" onclick="location.href='/inquiry/add-inquiry-form';" class="btn btn-dark" style="text-align: center;">문의 추가</button>
</div>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/inquiry?page=${pageData.startPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/inquiry?page=${num}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/inquiry?page=${pageData.endPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>