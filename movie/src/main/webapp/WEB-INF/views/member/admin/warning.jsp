<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#user").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
    $(document).on("click", ".warning", function(){
        var params = {
            email: $("#email").val()
            , warning : "on"
        } 
        $.ajax({
            type : "POST",            
            url : "/admin/member/warning",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click", ".block", function(){
        var isCheck=$(this).hasClass("block-차단");
        if(isCheck){
            var params = {
                  email: $("#email").val()
                , block : "off"
            } 
            $.ajax({
                type : "POST",            
                url : "/admin/member/block",   
                data : params,           
                success : function(res){ 
                    $(".block").removeClass("block-차단");
                    $(".block").addClass("block-활성");
                    location.reload(true);
                }
            });
        }else{
            var params = {
                email: $("#email").val()
                , block : "on"
            } 
            $.ajax({
                type : "POST",            
                url : "/admin/member/block",   
                data : params,           
                success : function(res){ 
                    $(".block").removeClass("block-활성");
                    $(".block").addClass("block-차단");
                    location.reload(true);
                }
            });
        }
    });
</script>
<style>
    .content-frame{
        width: fit-content;
        margin: 0 auto;
        border-radius: 5px;
        padding: 6px 12px;
        background: white;
        color: black; 
        border: 1px solid black;
    }
    .block{
        background-image: url("/image/redcard.png");
        background-size: cover;
        background-color: white;
        border: none;
    }
    .warning{
        background-image: url("/image/yellowcard.png");
        background-size: cover;
        background-color: white;
        border: none;
    }

</style>
<body>
<div style="margin: 50px;">
    <h2 style="text-align: center;"> 회원 경고 </h2>
    <!-- 정렬 기준 -->
    <br>
        <table class="table" >
        <!-- 표 헤더 -->
        <thead class="table-dark">
            <th>이메일</th>
            <th>경고수</th>
            <th>회원상태</th>
            <th>경고추가</th>
            <th>회원차단</th>
        </thead>
            <tr>
            <!-- 표안에 보여질 관리자 정보 -->
                <td>${email}</td>
                <td>${warningCard}</td>       
                <td>${memberStatus}</td>         
                <td>
                    <p class="content-frame warning" style="width: 22px; height: 22px;">
                        <input type="hidden" id="email" name="email" value="${email}"/>
                        <input type="hidden" id="warning" name="warning" value="${warningCard}"/>
                    </p>
                </td>
                <td>
                    <p class="content-frame block block-${memberStatus}" style="width: 22px; height: 22px;">
                        <input type="hidden" id="email" name="email" value="${email}"/>
                        <input type="hidden" id="block" name="block" value="${memberStatus}"/>
                    </p>
                </td>
            </tr>
        </table>
</div>
</body>
</html>