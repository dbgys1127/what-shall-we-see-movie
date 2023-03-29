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

<table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>포스터</th>
        <th>영화제목</th>
        <c:forEach var="movie" items="${pageData.data}">
            <tr>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}"/></a></td>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
            </tr>
        </c:forEach>
       
</table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/mypage/want-movie?page=${pageData.startPage-1}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/mypage/want-movie?page=${num}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/mypage/want-movie?page=${pageData.endPage+1}">Next</a></li>
                </c:if>
        </ul>
    </div>
</body>
</html>