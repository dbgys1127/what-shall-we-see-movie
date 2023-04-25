<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#movie").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
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
</div>
</body>
</html>