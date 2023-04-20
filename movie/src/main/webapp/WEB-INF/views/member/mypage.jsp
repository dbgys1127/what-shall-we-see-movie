<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<div class="container text-center" style="border: 1px solid;">
    <div class="row">
        <div class="col-sm-4">
            <img src="${member.memberImage}" />
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <form action="/mypage/myImage" method="get">
                <button type="submit" class="btn btn-dark">대표 이미지 수정</button>
            </form>
        </div>
        <div class="col-sm-2">
            <form action="/mypage/myPassword" method="get">
                <button type="submit" class="btn btn-dark">비밀번호 수정</button>
            </form>
        </div>
    </div>
</div>
<div style="display: flex; justify-content: space-between;">
<div class="container" style="border: 1px solid; width: 31%; height: 500px; display: inline-block; position: relative;">
        <h3>시청한 영화수 : ${member.sawMoviesTotalCount}</h3>
        <c:forEach var="sawMovie" items="${member.sawMovies}">
        <div class="row" style="justify-content: center;">
            <div class="col-2">
                <a href="/movie/detail?movieTitle=${sawMovie.movieTitle}">
                    <img src="${sawMovie.moviePoster}" style="width: 50px;"/>
                </a>
            </div>
            <div class="col-2" style="width: 50%;">        
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                        <a href="/movie/detail?movieTitle=${sawMovie.movieTitle}">
                            ${sawMovie.movieTitle}
                        </a>
                    </li>
                    <li class="list-group-item">
                        시청횟수: ${sawMovie.sawCount}
                    </li>
                </ul>
            </div>
        </div>
        </c:forEach>
        <form action="/mypage/saw-movie" method="get" style="position: absolute; bottom: 10px; right: 10px;">
            <button type="submit" class="btn btn-dark">더보기</button>
        </form>
</div>
<div class="container" style="border: 1px solid;width: 31%; height: 500px; display: inline-block; position: relative;">
        <h3>찜한 영화수 : ${member.wantMoviesTotalCount}</h3>
        <c:forEach var="wantMovie" items="${member.wantMovies}">
            <div class="row" style="justify-content: center;">
            <div class="col-sm-2">
                <a href="/movie/detail?movieTitle=${wantMovie.movieTitle}">
                    <img src="${wantMovie.moviePoster}" style="width: 50px;"/>
                </a>
            </div>
            <div class="col-sm-2" style="width: 50%;">        
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                        <a href="/movie/detail?movieTitle=${wantMovie.movieTitle}">
                            ${wantMovie.movieTitle}
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        </c:forEach>
        <form action="/mypage/want-movie" method="get" style="position: absolute; bottom: 10px; right: 10px;">
            <button type="submit" class="btn btn-dark">더보기</button>
        </form>
</div>    
<div class="container" style="border: 1px solid; width: 31%; height: 500px; display: inline-block; position: relative;">
        <h3>댓글 수 : ${member.commentCount}</h3>
        <c:forEach var="comment" items="${member.comments}">
        <div class="row" style="justify-content: center;">
            <div class="row" style="margin-top: 15px;">
                <div class="col-sm-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                    ${comment.commentDetail}
                </div>
            </div>
            <div class="row">
                <div class="col-sm-1 flex-grow-1 me-2">
                    <a href="/movie/detail?movieTitle=${comment.movieTitle}">${comment.movieTitle}</a>
                </div>
                <div class="col-sm-1 flex-grow-1 me-2">
                    <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </div>
                <div class="col-sm-1 flex-grow-1 me-2">
                    <c:out value="신고내역:${comment.claimCount}회"></c:out>
                </div>
            </div>
        <div>
        </c:forEach>
        <form action="/mypage/comment" method="get" style="position: absolute; bottom: 10px; right: 10px;">
            <button type="submit" class="btn btn-dark">더보기</button>
        </form>
</div>  
</div>
</body>
</html>