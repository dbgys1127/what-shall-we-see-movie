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
<h2> 영화 </h2>

<form action = "/movie/search" method="get">
    <input type = "text" name="movieTitle" placeholder = "검색할 영화를 입력하세요."/>
    <button type="submit">검색</button>
</form>
<!-- 정렬 기준 -->
<ul class="sort">
    <li class="${pageData.sort eq 'avgSawCount' ? 'active':''}"><a href="/movie?page=1&sort=avgSawCount">평균 시청순</a></li>
    <li class="${pageData.sort eq 'movieOpenDate' ? 'active':''}" ><a href="/movie?page=1&sort=movieOpenDate">상영일자순</a></li>
    <li>장르별</li>
    <form action = "/movie/search/genre" method="get">
        <li>        
            <select name="movieGenre">
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
</ul>
    <table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>포스터</th>
        <th>영화제목</th>
        <th>평균 시청수</th>
        <c:forEach var="movie" items="${pageData.data}">
            <tr>
                <td> <a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}"/></a></td>
                <td> <a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
                <td>${movie.avgSawCount}</td>
            </tr>
        </c:forEach>
    </table>
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