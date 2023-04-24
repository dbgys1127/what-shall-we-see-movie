<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<h2> 찜한 영화 목록 </h2>
<br>
<c:forEach var="movie" items="${pageData.data}" varStatus="loop">
    <c:if test="${loop.index % 5 == 0}">
    <div class="row">
    </c:if>
        <div class="col-md">
            <div class="card" style="width: 140px;">
                <div style="width: 138px; height: 200px;">
                    <a href="/movie/detail?movieTitle=${movie.movieTitle}">
                        <img src="${movie.moviePoster}" class="card-img-top" style="width: 100%; height: 100%;">
                    </a>
                </div>
            <div class="card-header" style="text-align: center; height: 40px;">
                <a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a>
            </div>
            <ul class="list-group list-group-flush" style="font-size: 11px;">
                <li class="list-group-item">평균 시청횟수 : ${movie.avgSawCount}</li>
            </ul>
            </div>
        </div>
    <c:if test="${(loop.index + 1) % 5 == 0 || loop.last}">
    </div>
    </c:if>
    <br>
</c:forEach>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/mypage/want-movie?page=${pageData.startPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/mypage/want-movie?page=${num}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/mypage/want-movie?page=${pageData.endPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>