<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
    $(document).ready(function(){
      $("#h-movie").addClass("now-click");
    });
    $(document).on("click", ".more-btn", function(){
        $(this).toggleClass("on");
        var isMore = $(this).hasClass("on");
        if(isMore){
            $(".more").css("display", "block");
            $(".more-btn .content-frame").html("-");
        }else{
            $(".more").css("display", "-webkit-box");
            $(".more-btn .content-frame").html("+");
        }
    });
    $(document).on("click", ".heart", function(){
        var isHeart=$(this).hasClass("heart-on");
        if(isHeart){
            var params = {
                  movieTitle: $("#movieTitle").val()
                , wantMovie : "off"
            } 
            $.ajax({
                type : "POST",            
                url : "/movie/want-movie",   
                data : params,           
                success : function(res){ 
                    $(".heart").removeClass("heart-on");
                    $(".heart").addClass("heart-off");
                    location.reload(true);
                }
            });
        }else{
            var params = {
                  movieTitle: $("#movieTitle").val()
                , wantMovie : "on"
            } 
            $.ajax({
                type : "POST",            
                url : "/movie/want-movie", 
                data : params,            
                success : function(res){ 
                    $(".heart").removeClass("heart-off");
                    $(".heart").addClass("heart-on");
                    location.reload(true);
                }
            });
        }
    });
    $(document).on("click", ".sawcount", function(){
        var isPlus=$(this).hasClass("plus-sawcount");
        var sawcount = parseInt($("#movieSawCount").val());

        if(sawcount==0 && !isPlus){
            return false;
        }

        if(isPlus){
            var params = {
                  movieTitle: $("#movieTitle").val()
                , movieSawCount : parseInt($("#movieSawCount").val())+1
            } 
            $.ajax({
                type : "POST",            
                url : "/movie/saw-movie",   
                data : params,           
                success : function(res){ 
                    location.reload(true);
                }
            });
        }else{
            var params = {
                  movieTitle: $("#movieTitle").val()
                , movieSawCount : parseInt($("#movieSawCount").val())-1
            } 
            // ajax 통신
            $.ajax({
                type : "POST",            
                url : "/movie/saw-movie",   
                data : params,           
                success : function(res){ 
                    location.reload(true);
                }
            });
        }
    });
    $(document).on("click", ".save-comment", function(){
        var params = {
            movieTitle: $("#movieTitle").val()
            , commentDetail : $("#commentDetail").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/movie/comment",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click", ".delete-comment", function(){
        var commentIdVal = $(this).siblings("#commentId").val();
        var params = {
            commentId : commentIdVal
        } 
        $.ajax({
            type : "POST",            
            url : "/movie/comment/delete",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click", ".add-claim", function(){
        var params = {
            commentId : $("#commentId").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/movie/comment/claim",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click",".patch-comment-init", function(){
        var commentNum = $(this).attr("data-comment_num");
        console.log(commentNum);
        $("#commentnum-"+commentNum).removeAttr("disabled");
        $("#commentnum-"+commentNum).removeClass("patch-comment-off");
        $("#commentnum-"+commentNum).addClass("patch-comment-on");
        $(this).html("수정완료");
        $(this).removeClass("patch-comment-init");
        $(this).addClass("patch-comment-fin");
    });
    $(document).on("click", ".patch-comment-fin", function(){
        var commentNum = $(this).attr("data-comment_num");
        var commentDetail = $("#commentnum-"+commentNum).val();
        console.log(commentNum);
        console.log(commentDetail);
        var params = {
            commentId : commentNum,
            commentDetail : commentDetail
        } 
        $.ajax({
            type : "POST",            
            url : "/movie/comment/patch",   
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
    .heart-on{
        background-image: url("/image/heart.png");
        background-size: cover;
        background-color: white;
    }
    .heart-off{
        background-image: url("/image/empty_heart.png");
        background-size: cover;
        background-color: white;
    }
    .patch-comment-off{
        background: none;
        border: none;
        color: white;
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }
    .patch-comment-on{
        background: white;
        border-radius: 5px;
        color: black;
        width: 100%;
        height: 100%;
        border: 1px solid black;
    }
</style>
<body>
<div style="margin: 50px;">
    <div>
        <div style="width: 210px; height: 300px; display: inline-block;">
            <img src="${movie.moviePoster}" style="width: 100%; height: 100%;" />
        </div>
        <div style="display: inline-block; margin-left: 30px; width: calc( 100% - 240px ); float: right;">     
            <ul style="margin: 0; font-size: 20px; ">
                <li style="font-size: 30px;">
                    <input type="hidden" id="movieTitle" value="${movie.movieTitle}"/>
                    ${movie.movieTitle}
                </li>
                <li>개봉일: ${movie.movieOpenDate}</li>
                <li>장르: ${movie.movieGenre}</li>
                <li>상영시간: ${movie.movieRunningTime}분</li>
                <li class="more" style="overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 4; -webkit-box-orient: vertical;">
                    간략설명: <br>${movie.movieDescription}
                </li>
                <li class="more-btn">
                    <button type="submit" class="content-frame" style="float: right; border: none;">+</button>
                </li>
            </ul>
        </div>
    </div>
    <br>
    <div>
        <div>
            <table style="width: 100%; text-align: center;">
                <colgroup>
                    <col width="33%" /> 
                    <col width="33%" /> 
                    <col width="33%" /> 
                </colgroup>
                <th>무봐 평균 시청횟수</th>
                <th>
                    나의 시청횟수
                </th>
                <th>찜</th>
                <tr>
                    <td><p class="content-frame">${movie.avgSawCount}회</p></td>
                    <td>
                        <p class="content-frame">
                            <button type="button" class="content-frame sawcount plus-sawcount" style="border: none;">+</button>
                            <input type="hidden" id="movieSawCount" name="movieSawCount" value="${movie.memberSawCount}"/>
                            ${movie.memberSawCount}
                            <button type="button" class="content-frame sawcount minus-sawcount" style="border: none;">-</button>
                        </p>
                    </td>
                    <td>
                        <p class="content-frame heart heart-${movie.isWant}" style="width: 33px; height: 33px;">
                            <input type="hidden" name="wantMovie" value="${movie.isWant}"/>
                        </p>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div>
        <label>댓글 작성</label>
        <textarea class="form-control" id="commentDetail" name="commentDetail" placeholder="댓글을 등록하세요"></textarea>
        <div style="text-align: right; margin-bottom: 10px;">
            <button type="button" class="content-frame save-comment" style="border: none; margin-top: 10px;">댓글 등록</button>
        </div>
    </div>
    <c:forEach var="comment" items="${movie.comments}">    
        <div>
            <div class="content-frame" style="width: 100%; display: block; padding: 0px;">
                <input type="text" class="content-frame patch-comment-off" id="commentnum-${comment.commentId}" value="${comment.commentDetail}" disabled/>
            </div>
        </div>
        <div style="text-align: right;">
            <div class="content-frame" style="margin: 5px; display:inline-block;">
                <a href="/admin/member/warning-page?email=${comment.createdBy}" style="color: white;">${comment.createdBy}</a>
            </div>
            <div class="content-frame" style="margin: 5px; display:inline-block;">
                <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div class="content-frame" style="margin: 5px; display:inline-block;">
                <c:out value="신고횟수 : ${comment.claimCount}"></c:out> 회
            </div>
            <div style="margin: 5px; display:inline-block;">
                <input type="hidden" id="commentId" value="${comment.commentId}"/>
                <button type="submit" class="btn btn-dark add-claim" >댓글신고</button>
            </div>
            <c:if test = "${movie.currentMember eq comment.createdBy}">
                <div style="margin: 5px; display:inline-block;">
                    <button type="button" class="btn btn-dark patch-comment-init" data-comment_num="${comment.commentId}">수정</button>
                </div>
                <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                    <input type="hidden" id="commentId" value="${comment.commentId}"/>
                    <button type="button" class="btn btn-dark delete-comment">삭제</button>
                </div>
            </c:if>
        </div>
    </c:forEach>    
</div>
</body>
</html>