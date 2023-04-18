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
<h2> 댓글 목록 </h2>
<br>
<div class="container text-center" style="border: 1px solid;">
    <c:forEach var="comment" items="${pageData.data}">    
        <div class="row" style="margin-top: 15px;">
            <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                ${comment.commentDetail}
            </div>
        </div>
        <div class="row">
            <div class="col-md-1 flex-grow-1 me-2">
                <a href="/movie/detail?movieTitle=${comment.movieTitle}">${comment.movieTitle}</a>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <c:out value="신고내역:${comment.claimCount}회"></c:out>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <form action="/movie/comment/patch?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                    <button type="submit" class="btn btn-dark">수정</button>
                </form>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <form action="/movie/comment/delete?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                    <button type="submit" class="btn btn-dark">삭제</button>
                </form>
            </div>
        </div>
    </c:forEach>    
</div>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/mypage/comment?page=${pageData.startPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/mypage/comment?page=${num}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/mypage/comment?page=${pageData.endPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>