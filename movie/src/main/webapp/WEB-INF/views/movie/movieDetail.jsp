<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<h2> 영화 상세화면 </h2>
<ul>
    <img src="${movie.moviePoster}" />
    <li>영화제목: ${movie.movieTitle}</li>
    <li>상영시간: ${movie.movieRunningTime}</li>
    <li>개봉일: ${movie.movieOpenDate}</li>
    <li>장르: ${movie.movieGenre}</li>
    <li>영화 설명: ${movie.movieDescription}</li>
    <form action = "/movie/saw-movie?movieTitle=${movie.movieTitle}" method="post">
        <li><input type="text" name="movieSawCount" placeholder="${movie.avgSawCount}"></li>
        <button type="submit">시청 횟수 등록</button>
    </form>
    <li>나의 시청횟수: ${movie.memberSawCount}</li>
    <form action = "/movie/want-movie?movieTitle=${movie.movieTitle}" method="post">
        <input type="checkbox" name="wantMovie" <c:if test="${movie.isWant eq 'on'}">checked</c:if> />
    <button type="submit">찜 등록</button>
    </form>
    <form action="/movie/comment?movieTitle=${movie.movieTitle}" method="post">
        <input type="text" name="commentDetail" placeholder="댓글을 등록하세요">
        <button type="submit">댓글 등록</button>
    </form>
    
    <table style="border: 1px solid black;" >
        <c:forEach var="comment" items="${movie.comments}">    
            <tr><td>${comment.commentDetail}</td></tr>
                <tr>
                    <td><a href="/admin/member/warning-page?email=${comment.createdBy}">${comment.createdBy}</a></td>
                    <td>${comment.createdAt}</td>
                    <td>${comment.claimCount}</td>
                    <td>
                        <form action="/movie/comment/claim?commentId=${comment.commentId}" method="post">
                            <button type="submit">신고</button>
                        </form>
                    </td>
                </tr>
        </c:forEach>   
    </table>
    
    <button type="button"><a href="/">메인</a></button>
</ul>
</body>
</html>