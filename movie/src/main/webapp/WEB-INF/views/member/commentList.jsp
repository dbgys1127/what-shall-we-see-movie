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
<h2> 댓글 목록 </h2>
<br>

<table style="border: 1px solid black;" >
    <c:forEach var="comment" items="${pageData.data}">    
        <tr><td>${comment.commentDetail}</td></tr>
            <tr>
                <td><a href="/movie/detail?movieTitle=${comment.movieTitle}">${comment.movieTitle}</a></td>
                <td>${comment.createdAt}</td>
                <td>신고내역:${comment.claimCount}회</td>
                <td>
                    <form action="/movie/comment/patch?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                        <button type="submit">수정</button>
                    </form>
                </td>
                <td>
                    <form action="/movie/comment/delete?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                        <button type="submit">삭제</button>
                    </form>
                </td>
            </tr>
    </c:forEach>   
</table>
    <!-- 페이징 단추 -->
    <div class="pull-right">
        <ul class="pagination">
                <c:if test="${pageData.prev}">
                    <li class="paginate_button previous"><a href="/mypage/comment?page=${pageData.startPage-1}">Prev</a></li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "${pageData.nowPage eq num ? 'active':''}">
                        <a href="/mypage/comment?page=${num}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="paginate_button next"><a href="/mypage/comment?page=${pageData.endPage+1}">Next</a></li>
                </c:if>
        </ul>
    </div>
</body>
</html>