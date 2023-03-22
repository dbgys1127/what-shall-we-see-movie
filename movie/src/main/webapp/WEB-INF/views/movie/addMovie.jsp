<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 영화 등록 화면 </h2>
<form action="/admin/movie" method="post" enctype="multipart/form-data">
    포스터 등록 : <input type="file" name="moviePoster" accept=".jpg,.png,.jpeg">
    <br>
    영화제목: <input type="text" name="movieTitle" />
    <br>
    상영시간: <input type="text" name="movieRunningTime" />
    <br>
    개봉일: <input type="date" name="movieOpenDate" />
    <br>
    영화장르:
        <select name="movieGenre" >
          <option value="none">=== 선택 ===</option>
          <option value="코미디">코미디</option>
          <option value="액션">액션</option>
          <option value="범죄">범죄</option>
          <option value="드라마">드라마</option>
          <option value="SF">SF</option>
          <option value="공포">공포</option>
          <option value="로맨스">로맨스</option>
        </select>
        <br>
    영화소개: <input type="text" name="movieDescription"/>

<button type="submit">영화 등록</button>
</form>
    <button type="button"><a href="/">메인</a></button>

</body>
</html>