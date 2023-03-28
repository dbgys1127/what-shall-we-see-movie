<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 영화 생성 수정 성공 화면 </h2>
<ul>
    <img src="${movie.moviePoster}" />
    <li>영화제목: ${movie.movieTitle}</li>
    <li>상영시간: ${movie.movieRunningTime}</li>
    <li>개봉일: ${movie.movieOpenDate}</li>
    <li>장르: ${movie.movieGenre}</li>
    <li>영화 설명: ${movie.movieDescription}</li>
    <form action = "/movie/saw-movie?movieTitle=${movie.movieTitle}" method="post">
        <li><input type="text" name="movieSawCount" placeholder="${movie.avgSawCount}"></li>
        <button type="submit">등록</button>
    </form>
    <li>나의 시청횟수: ${movie.memberSawCount}</li>
    <button type="button"><a href="/">메인</a></button>
</ul>
</body>
</html>