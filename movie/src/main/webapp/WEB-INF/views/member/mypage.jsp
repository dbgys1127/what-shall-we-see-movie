<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#h-mypage").addClass("now-click");
    });
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
    $(function(){
        $("#alert-success").hide();
        $("#alert-danger").hide();
        $("#alert-danger-password").hide();
        $(".type-password").keyup(function(){
            var pwd1=$("#check-password1").val();
            var pwd2=$("#check-password2").val();
            if( pwd2 != ""){
                if(pwd1 == pwd2){
                    $("#alert-success").show();
                    $("#alert-danger").hide();
                    $("#submit").removeAttr("disabled");
                }else{
                    $("#alert-success").hide();
                    $("#alert-danger").show();
                    $("#submit").attr("disabled", "disabled");
                }    
            }else{
                $("#alert-success").hide();
                $("#alert-danger").hide();
            }
        });
        $("input[name=password]").keyup(function(){
            var pattern_password = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@!%*#?&])[A-Za-z\d@!%*#?&]{8,}$/;
            var regExp = new RegExp(pattern_password);
            var password = $("input[name=password]").val();
            if(password !=""){
                if(regExp.test(password)){
                    $("#submit").removeAttr("disabled");
                    $("#alert-danger-password").hide();
                }else{
                    $("#alert-danger-password").show();
                    $("#submit").attr("disabled", "disabled");
                }
            }else{
                $("#alert-danger-password").hide();
            }
        });
    });
</script>
<style>
    .content-frame{
        width: fit-content;
        border-radius: 5px;
        padding: 6px 12px;
        background: #EB4F5A;
        color: white;
    }
    .table-content tr td{
        position: relative;
    }
    .table-content tr td > div{
        width: 70%;
        height: fit-content;
        position: absolute;
        top: 15px;
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
                    <li>
                        비밀번호 수정    
                    </li>
                    <li>
                        <div class="alert alert-success" id="alert-success" style="width:80%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 2px;">비밀번호가 일치합니다.</div>
                        <div class="alert alert-danger" id="alert-danger" style="width:80%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 2px;">비밀번호가 일치하지 않습니다.</div>
                        <div class="alert alert-danger" id="alert-danger-password" style="width:80%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 2px;">최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.</div>
                    </li>
                    <li>
                        <input type="password" class="form-control type-password" id="check-password1" name="password" style="width:80%; height: 50%; display: inline-block;" placeholder="새로운 비밀번호를 입력하세요." >
                    </li>
                    <li>
                        <input type="password" class="form-control type-password" id="check-password2" name="password-repeat" style="width:80%; height: 50%; display: inline-block;" placeholder="비밀번호를 재입력 해주세요.">
                        <button type="submit" id="submit" class="btn btn-dark patch-password" style="display: inline-block;" disabled >수정</button>
                    </li>
                </ul>
            </div>
        </div>
        <br>
        <div>
            <div>
                <table style="width: 100%; text-align: center;" class="table-content">
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
                            <div>
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
                            </div>
                        </td>
                        <td>
                            <div>
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
                            </div>
     
                        </td>
                        <td style="font-size: 10px;">
                            <div>
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
                            <hr>
                            <form action="/mypage/comment" method="get">
                                <button type="submit" class="btn btn-dark">더보기</button>
                            </form>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>  
    </div>
</body>
</html>