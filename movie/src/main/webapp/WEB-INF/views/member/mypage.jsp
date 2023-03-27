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
${email}
<button type="button"><a href="/my-info/myImage">대표 이미지 수정</a></button>
<button type="button"><a href="/my-info/myPassword">비밀번호 수정</a></button>

<table style="border: 1px solid black;" >
    <!-- 표 헤더 -->
        <th>포스터</th>
        <th>영화제목</th>
        <th>평균 시청수</th>
        <c:forEach var="movie" items="${member.sawMovies}">
            <tr>
                <td><img src="${movie.moviePoster}"/></td>
                <td>${movie.movieTitle}</td>
                <td>${movie.avgSawCount}</td>
            </tr>
        </c:forEach>
</table>
</body>
</html>