<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> Mypage </h2>
<img src="${memberImage}" />
<br>
<button type="button"><a href="/mypage/myImage">대표 이미지 수정</a></button>
<button type="button"><a href="/mypage/myPassword">비밀번호 수정</a></button>
<div>
    <h3>시청한 총 영화수 : ${member.sawMoviesTotalCount}</h3>
<table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>포스터</th>
        <th>영화제목</th>
        <th>나의 시청횟수</th>
        <c:forEach var="movie" items="${member.sawMovies}">
            <tr>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}"/></a></td>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
                <td>${movie.sawCount}</td>
            </tr>
        </c:forEach>
        <h3><a href="/mypage/saw-movie">더보기</a></h3>
</table>
</div>
<div>
    <h3>찜한 총 영화수 : ${member.wantMoviesTotalCount}</h3>
<table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>포스터</th>
        <th>영화제목</th>
        <c:forEach var="movie" items="${member.wantMovies}">
            <tr>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}"><img src="${movie.moviePoster}"/></a></td>
                <td><a href="/movie/detail?movieTitle=${movie.movieTitle}">${movie.movieTitle}</a></td>
            </tr>
        </c:forEach>
        <h3><a href="/mypage/want-movie">더보기</a></h3>
</table>
</div>
</body>
</html>