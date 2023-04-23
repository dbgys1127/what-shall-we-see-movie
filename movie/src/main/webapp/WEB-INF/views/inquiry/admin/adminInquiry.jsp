<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<style>
    .pagination li{display:inline-block;}
    .btn-group li{
        display: inline-block;
        text-align: right;
    }
    .active a{color:red;}
</style>
<body>
<h2> 문의 내역 </h2>
<br>
    <!-- 정렬 기준 -->
    <div class="d-flex justify-content-end">
        <div class="btn-group btn-group-sm btn-group-right">
            <li><a href="/admin/inquiry?page=1&sort=createdAt" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'createdAt' ? 'active':''}">문의 일자순</a></li>
            <li><a href="/admin/inquiry?page=1&sort=inquiryStatus" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'avgSawCount' ? 'active':''}">처리 상태</a></li>
        </div>
    </div>
<br>
<div class="container text-center" style="border: 1px solid;">
    <c:forEach var="inquiry" items="${pageData.data}">    
        <div class="row" style="margin-top: 15px;">
            <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                <a href="/admin/inquiry/detail?inquiryId=${inquiry.inquiryId}">${inquiry.inquiryTitle}</a>
            </div>
        </div>
        <div class="row">
            <div class="col-md-1 flex-grow-1 me-2">
                ${inquiry.createdBy}
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <a href="/movie/detail?movieTitle=${comment.movieTitle}">${comment.movieTitle}</a>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                ${inquiry.inquiryStatus}
            </div>
        </div>
    </c:forEach>    
</div>
<br>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/admin/inquiry?page=${pageData.startPage-1}&sort=${pageData.sort}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/admin/inquiry?page=${num}&sort=${pageData.sort}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/admin/inquiry?page=${pageData.endPage+1}&sort=${pageData.sort}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>