<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> Mypage </h2>
<img src="${member.memberImage}" />
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
        <c:forEach var="sawMovie" items="${member.sawMovies}">
            <tr>
                <td><a href="/movie/detail?movieTitle=${sawMovie.movieTitle}"><img src="${sawMovie.moviePoster}"/></a></td>
                <td><a href="/movie/detail?movieTitle=${sawMovie.movieTitle}">${sawMovie.movieTitle}</a></td>
                <td>${sawMovie.sawCount}</td>
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
        <c:forEach var="wantMovie" items="${member.wantMovies}">
            <tr>
                <td><a href="/movie/detail?movieTitle=${wantMovie.movieTitle}"><img src="${wantMovie.moviePoster}"/></a></td>
                <td><a href="/movie/detail?movieTitle=${wantMovie.movieTitle}">${wantMovie.movieTitle}</a></td>
            </tr>
        </c:forEach>
        <h3><a href="/mypage/want-movie">더보기</a></h3>
</table>
</div>
<div>
<h3>댓글 : ${member.commentCount}</h3>
<table style="border: 1px solid black;" >
        <c:forEach var="comment" items="${member.comments}">
            <tr>
                <td>${comment.commentDetail}</td>
            </tr>
            <tr>
                <td>${comment.movieTitle}</td>
                <td>
                    <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>신고횟수:${comment.claimCount}회</td>
            </tr>
        </c:forEach>
        <h3><a href="/mypage/comment">더보기</a></h3>
</table>
</div>
</body>
</html>