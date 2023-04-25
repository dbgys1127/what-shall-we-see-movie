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
      $("#movie").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
</script>
<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}
</style>
<body>
<div style="margin: 50px;">
    <form action = "/admin/movie/search" method="get">
        <h2 style="text-align: center;"> 영화 검색결과 </h2>
        <br>
        <table class="table" >
        <!-- 표 헤더 -->
            <thead class="table-dark">
                <th>영화제목</th>
                <th>등록일</th>
                <th>평균 시청수</th>
                <th>영화 삭제</th>
            </thead>
            <c:forEach var="movie" items="${pageData.data}">
                <tr>
                <!-- 표안에 보여질 관리자 정보 -->
                    <td><a href="/admin/movie/patch?movieTitle=${movie.movieTitle}" style="color: black;">${movie.movieTitle}</a></td>
                    <td>
                        <fmt:parseDate value="${movie.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                        <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td>${movie.avgSawCount}</td>
                    <td><button type="button" class="btn btn-dark" onclick="location.href='/admin/movie/delete?movieTitle=${movie.movieTitle}';">삭제</button></td>
                </tr>
            </c:forEach>
        </table>
        <input class="form-control me-2" type="text" name="movieTitle" placeholder="검색할 영화를 입력하세요." aria-label="Search" style="width:50%; height: 50%; display: inline-block;">
        <button type="submit" class="btn btn-dark" style="margin-left: auto; display: inline-block;">검색</button>
        <button type="button" onclick="location.href='/admin/movie/add-movie-form';" class="btn btn-dark" style="display: inline-block;">영화 추가</button>
        <br><br>
        <!-- 페이징 단추 -->
        <div style="display: block;">
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <c:if test="${pageData.prev}">
                        <li class="page-item">
                            <a class="page-link" href="/admin/movie/search?page=${pageData.startPage-1}&sort=${pageData.sort}&movieTitle=${pageData.target}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                        <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                            <a class="page-link" href="/admin/movie/search?page=${num}&sort=${pageData.sort}&movieTitle=${pageData.target}">${num}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${pageData.next}">
                        <li class="page-item">
                            <a class="page-link" href="/admin/movie/search?page=${pageData.endPage+1}&sort=${pageData.sort}&movieTitle=${pageData.target}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </form>
</div>
</body>
</html>