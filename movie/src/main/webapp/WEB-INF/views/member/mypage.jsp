<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).on("click", ".patch-password", function(){
        var params = {
            password : $("#password").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/mypage/myPassword",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
</script>
<style>
    .content-frame{
        width: fit-content;
        margin: 0 auto;
        border-radius: 5px;
        padding: 6px 12px;
        background: #EB4F5A;
        color: white;
    }
</style>
<body>
    <div style="margin: 50px;">
        <div>
            <div style="width: 300px; height: 300px; display: inline-block;">
                <img src="${member.memberImage}" style="width: 100%; height: 100%;" />
            </div>
            <div style="display: inline-block; margin-left: 30px; width: calc( 100% - 350px ); float: right;">     
                <ul style="margin: 0; font-size: 20px; ">
                    <li style="font-size: 30px;">
                        ${member.email}
                    </li>
                    <li>가입일 :                     
                        <fmt:parseDate value="${member.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                        <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                    </li>
                    <hr>
                    <li>
                        대표 이미지 수정
                        <form action="/mypage/myImage" method="post" enctype="multipart/form-data">        
                            <input class="form-control" type="file" id="formFileMultiple" name="myImage" accept=".jpg,.png,.jpeg" style="width:80%; height: 50%; display: inline-block;" >
                            <button type="submit" class="btn btn-dark" style="display: inline-block;">수정</button>
                        </form>
                    </li>
                    <hr>
                    <li>
                        비밀번호 수정     
                        <br>
                        <input type="password" class="form-control" id="password" name="password" style="width:80%; height: 50%; display: inline-block;" placeholder="새로운 비밀번호를 입력하세요." >
                        <button type="submit" class="btn btn-dark patch-password" style="display: inline-block;" >수정</button>
                    </li>
                </ul>
            </div>
        </div>
        <br>
        <div>
            <div>
                <table style="width: 100%; text-align: center; ">
                    <colgroup>
                        <col width="30%" /> 
                        <col width="30%" /> 
                        <col width="30%" /> 
                    </colgroup>
                    <th>
                        <div class="content-frame" style="width: 50%; text-align: left;">
                            시청한 영화 수 : ${member.sawMoviesTotalCount}
                        </div>
                    </th>
                    <th>
                        <div class="content-frame" style="width: 50%; text-align: left;">
                            찜한 영화 수 : ${member.wantMoviesTotalCount}</th>
                        </div>
                    <th>
                        <div class="content-frame" style="width: 50%; text-align: left;">
                            댓글 수 : ${member.commentCount}
                        </div>
                    </th>
                    <tr>
                        <td>
                            <c:forEach var="sawMovie" items="${member.sawMovies}">
                                <div style="width: 35; height: 50px; display: inline-block;">
                                    <img src="${sawMovie.moviePoster}" style="width: 100%; height: 100%;" />
                                </div>
                                <div style="display: inline-block;">
                                    <ul style="margin: 0; font-size: 10px; ">
                                        <li>
                                            <a href="/movie/detail?movieTitle=${sawMovie.movieTitle}">${sawMovie.movieTitle}</a>
                                        </li>
                                        <li>시청횟수: ${sawMovie.sawCount}</li>
                                    </ul>
                                </div>
                                <hr>
                            </c:forEach>
                            <form action="/mypage/saw-movie" method="get">
                                <button type="submit" class="btn btn-dark">더보기</button>
                            </form>
                        </td>
                        <td>
                            <c:forEach var="wantMovie" items="${member.wantMovies}">
                                <div style="width: 35; height: 50px; display: inline-block;">
                                    <img src="${wantMovie.moviePoster}" style="width: 100%; height: 100%;" />
                                </div>
                                <div style="display: inline-block;">
                                    <ul style="margin: 0; font-size: 10px; ">
                                        <li>
                                            <a href="/movie/detail?movieTitle=${wantMovie.movieTitle}">${wantMovie.movieTitle}</a>
                                        </li>
                                        <li>평균 시청횟수: ${wantMovie.avgSawCount}</li>
                                    </ul>
                                </div>
                                <hr>
                            </c:forEach>
                            <form action="/mypage/want-movie" method="get">
                                <button type="submit" class="btn btn-dark">더보기</button>
                            </form>       
                        </td>
                        <td style="font-size: 10px;">
                            <c:forEach var="comment" items="${member.comments}">    
                                <div>
                                    <div class="content-frame" style="width: 100%; display: block; text-align: left;">
                                            ${comment.commentDetail}
                                    </div>
                                </div>
                                <div style="text-align: right;">
                                    <div class="content-frame" style="margin: 5px; display:inline-block;">
                                        <a href="/movie/detail?movieTitle=${comment.movieTitle}" style="color: white;">${comment.movieTitle}</a>
                                    </div>
                                    <div class="content-frame" style="margin: 5px; display:inline-block;">
                                        <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                                        <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                                    </div>
                                    <div class="content-frame" style="margin: 5px; display:inline-block;">
                                        <c:out value="신고내역:${comment.claimCount}회"></c:out>
                                    </div>    
                                </div>
                            </c:forEach>  
                            <form action="/mypage/comment" method="get">
                                <button type="submit" class="btn btn-dark">더보기</button>
                            </form>
                        </td>
                    </tr>
                </table>
            </div>
        </div>  
    </div>
</body>
</html>