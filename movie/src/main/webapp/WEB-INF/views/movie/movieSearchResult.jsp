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

<form action = "/admin/movie/search" method="get">
<!-- 정렬 기준 -->
    <table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>영화제목</th>
        <th>등록일</th>
        <th>평균 시청수</th>
        <th>영화 삭제</th>
        <c:forEach var="movie" items="${pageData.data}">
            <tr>
            <!-- 표안에 보여질 관리자 정보 -->
                <td> <a href="/admin/patch-movie-form?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
                <td>${movie.createdAt}</td>
                <td>평균시청순</td>
                <td><button type="button" onclick="location.href='/admin/movie/delete?movieTitle=${movie.movieTitle}';">삭제</button></td>
            </tr>
        </c:forEach>
    </table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/movie/search?page=${pageData.startPage-1}&sort=${pageData.sort}&movieTitle=${pageData.target}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/movie/search?page=${num}&sort=${pageData.sort}&movieTitle=${pageData.target}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/movie/search?page=${pageData.endPage+1}&sort=${pageData.sort}&movieTitle=${pageData.target}">Next</a></li>
                </c:if>
        </ul>
    </div>

    <input type = "text" name="movieTitle" placeholder = "검색할 영화를 입력하세요."/>
    <button type="submit">검색</button>
</form>
<button type="button" onclick="location.href='/admin/movie/add-movie-form';">영화 추가</button>
</body>
</html>