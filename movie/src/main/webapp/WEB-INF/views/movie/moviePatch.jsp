<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 영화수정 화면 </h2>
<form action="/admin/movie/patch?preMovieTitle=${movie.movieTitle}" method="post" enctype="multipart/form-data">
<ul>
    <img src="${movie.moviePoster}" />
    <li><input type="file" name="moviePoster" accept=".jpg,.png,.jpeg"></li>
    <li>영화제목: <input type="text" name="movieTitle" value="${movie.movieTitle}"></li>
    <li>상영시간: <input type="text" name="movieRunningTime" value="${movie.movieRunningTime}"> </li>
    <li>개봉일: <input type="text" name="movieOpenDate" value="${movie.movieOpenDate}"></li>
    <li>영화 장르:         
        <select name="movieGenre" >
            <option value="${movie.movieGenre}">현재 장르 : ${movie.movieGenre}</option>
            <option value="코미디">코미디</option>
            <option value="액션">액션</option>
            <option value="범죄">범죄</option>
            <option value="드라마">드라마</option>
            <option value="SF">SF</option>
            <option value="공포">공포</option>
            <option value="로맨스">로맨스</option>
        </select>
    </li>
    <li>영화 설명: <input type="text" name="movieDescription" value="${movie.movieDescription}"></li>
</ul>
<button type="submit">영화 수정</button>
</form>
<button type="button"><a href="/">메인</a></button>
</body>
</html>