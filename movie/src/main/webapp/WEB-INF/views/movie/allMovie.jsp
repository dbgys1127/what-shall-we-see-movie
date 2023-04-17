<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐?</title>
</head>

<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}
    .active div{color: red;}
</style>
<body>
<!-- 정렬 기준 -->
<ul class="sort">
    <li class="${pageData.sort eq 'movieOpenDate' ? 'active':''}" ><a href="/movie?page=1&sort=movieOpenDate">상영일자순</a></li>
    <li class="${pageData.sort eq 'avgSawCount' ? 'active':''}"><a href="/movie?page=1&sort=avgSawCount">평균 시청순</a></li>
    <li>장르별</li>
    <li>
    <form action = "/movie/search/genre" method="get">
        <li>        
            <select class="form-select form-select-sm" aria-label=".form-select-sm example" name="movieGenre">
                <option value="">=== 선택 ===</option>
                <option value="코미디">코미디</option>
                <option value="액션">액션</option>
                <option value="범죄">범죄</option>
                <option value="드라마">드라마</option>
                <option value="SF">SF</option>
                <option value="공포">공포</option>
                <option value="로맨스">로맨스</option>
            </select>
        </li>
        <button type="submit">적용</button>
    </form>
    </li>
</ul>
<c:forEach var="movie" items="${pageData.data}" varStatus="loop">
    <c:if test="${loop.index % 5 == 0}">
        <div class="row">
    </c:if>
        <div class="col-sm">
        <div class="card" style="width: 100%;">
            <a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}" class="card-img-top" style="width: 100%;"></a>
        <div class="card-header">
            <a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item">개봉일 : ${movie.movieOpenDate}</li>
            <li class="list-group-item">장르 : ${movie.movieGenre}</li>
            <li class="list-group-item">평균 시청횟수 :${movie.avgSawCount}</li>
        </ul>
        </div>
        </div>
    <c:if test="${(loop.index + 1) % 5 == 0 || loop.last}">
        </div>
    </c:if>
</c:forEach>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/movie?page=${pageData.startPage-1}&sort=${pageData.sort}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/movie?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/movie?page=${pageData.endPage+1}&sort=${pageData.sort}">Next</a></li>
                </c:if>
        </ul>
    </div>
</body>
</html>