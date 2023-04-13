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
<h2>관리자 영화 메인페이지</h2>

<form action = "/admin/movie/search" method="get">
<!-- 정렬 기준 -->
<ul class="sort">
    <li class="${pageData.sort eq 'movieId' ? 'active':''}"><a href="/admin/movie?page=1&sort=movieId">등록일</a></li>
    <li class="${pageData.sort eq 'warningCard' ? 'active':''}" ><a href="/admin/movie?page=1&sort=xxx">평균 시청순</a></li>
</ul>
    <table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>영화제목</th>
        <th>등록일</th>
        <th>평균 시청수</th>
        <c:forEach var="movie" items="${pageData.data}">
            <tr>
                <td> <a href="/admin/movie/patch?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
                <td>
                    <fmt:parseDate value="${movie.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${movie.avgSawCount}</td>
                <td><button type="button" onclick="location.href='/admin/movie/delete?movieTitle=${movie.movieTitle}';">삭제</button></td>
            </tr>
        </c:forEach>
    </table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/admin/movie?page=${pageData.startPage-1}&sort=${pageData.sort}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/admin/movie?page=${num}&sort=${pageData.sort}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/admin/movie?page=${pageData.endPage+1}&sort=${pageData.sort}">Next</a></li>
                </c:if>
        </ul>
    </div>

    <input type = "text" name="movieTitle" placeholder = "검색할 영화를 입력하세요."/>
    <button type="submit">검색</button>
</form>
<button type="button" onclick="location.href='/admin/movie/add-movie-form';">영화 추가</button>
</body>
</html>