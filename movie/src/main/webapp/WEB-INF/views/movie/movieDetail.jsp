<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<body>
<div class="container text-center" style="border: 1px solid;">
    <div class="row">
            <div class="col-md-6 col-sm-6"><img src="${movie.moviePoster}" width="100%" /></div>
            <div class="col-md-6 col-sm-6">        
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">영화제목: ${movie.movieTitle}</li>
                    <li class="list-group-item">상영시간: ${movie.movieRunningTime}</li>
                    <li class="list-group-item">개봉일: ${movie.movieOpenDate}</li>
                    <li class="list-group-item">장르: ${movie.movieGenre}</li>
                    <li class="list-group-item">영화 설명: ${movie.movieDescription}</li>
                </ul>
            </div>
    </div>
    <br>
    <div class="row">
        <form action="/movie/saw-movie?movieTitle=${movie.movieTitle}" method="post" class="d-flex">
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2">나의 시청 횟수</div>
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2"><input type="text" name="movieSawCount" placeholder="${movie.avgSawCount}" class="form-control form-control-sm" width="100px"></div>
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2"><button type="submit" class="btn btn-dark">등록</button></div>
        </form>
        <form action = "/movie/want-movie?movieTitle=${movie.movieTitle}" method="post" class="d-flex">
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2">영화 찜 등록</div>
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2"><input type="checkbox" name="wantMovie" <c:if test="${movie.isWant eq 'on'}">checked</c:if> /></div>
            <div class="col-md-2 col-sm-2 flex-grow-1 me-2"><button type="submit" class="btn btn-dark">등록</button></div>
        </form>
    </div>
    <div class="row">
        <form action="/movie/comment?movieTitle=${movie.movieTitle}" method="post" style="margin-bottom: 0;">
            <div class="col-md-12 col-sm-12">
                <div class="mb-3">
                    <div class="d-flex justify-content-start">
                        <label class="form-label">댓글 작성</label>
                    </div>
                    <textarea class="form-control" name="commentDetail" placeholder="댓글을 등록하세요"></textarea>
                </div>
            </div>
    </div>
    <div class="row">
        <div class="offset-md-10 flex-grow-1 me-2"></div>
        <div class="col-md-2 col-sm-2 flex-grow-1 me-2"><button type="submit" class="btn btn-dark" style="float: right;">댓글 등록</button></div>
        </form>
    </div>
    <c:forEach var="comment" items="${movie.comments}">    
        <div class="row" style="margin-top: 15px;">
            <div class="col-md-12 flex-grow-1 me-2" style="text-align: left; border: 1px solid; border-radius:4px;">
                ${comment.commentDetail}
            </div>
        </div>
        <div class="row">
            <div class="col-md-1 flex-grow-1 me-2">
                <a href="/admin/member/warning-page?email=${comment.createdBy}">${comment.createdBy}</a>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <c:out value="${comment.claimCount}"></c:out> 회
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <form action="/movie/comment/claim?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                    <button type="submit" class="btn btn-dark">신고</button>
                </form>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <form action="/movie/comment/patch?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                    <button type="submit" class="btn btn-dark">수정</button>
                </form>
            </div>
            <div class="col-md-1 flex-grow-1 me-2">
                <form action="/movie/comment/delete?movieTitle=${movie.movieTitle}&commentId=${comment.commentId}" method="post">
                    <button type="submit" class="btn btn-dark">삭제</button>
                </form>
            </div>
        </div>
    </c:forEach>    
</div>
</body>
</html>