<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    $(function(){
        $("#alert-success").hide();
        $("#alert-danger").hide();
        $("#alert-danger-email").hide();
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
        $("input[name=email]").keyup(function(){
            var pattern_email = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
            var regExp = new RegExp(pattern_email);
            var email = $("input[name=email]").val();
            if(email !=""){
                if(regExp.test(email)){
                    $("#submit").removeAttr("disabled");
                    $("#alert-danger-email").hide();
                }else{
                    $("#alert-danger-email").show();
                    $("#submit").attr("disabled", "disabled");
                }
            }else{
                $("#alert-danger-email").hide();
            }
        });
        $("input[name=password]").keyup(function(){
            var pattern_password = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@!%*#?&])[A-Za-z\d@!%*#?&]{8,}$/;
            var regExp = new RegExp(pattern_password);
            var password = $("input[name=password]").val();
            if(password !=""){
                if(regExp.test(password)){
                    console.log("eq",password);
                    $("#submit").removeAttr("disabled");
                    $("#alert-danger-password").hide();
                }else{
                    console.log("ne",password);
                    $("#alert-danger-password").show();
                    $("#submit").attr("disabled", "disabled");
                }
            }else{
                $("#alert-danger-password").hide();
            }
        });
    });
</script>
<body>
<div style="margin: 50px;">
<form action="/admin/movie" method="post" enctype="multipart/form-data">
    <h2 style="text-align: center;"> 영화 등록 화면 </h2>
    <br>
    <div class="row">
        <div class="col-md-6">
            <div class="card" style="width: 100%;">
                <img src="" class="card-img-top" style="width: 100%;">
            <div class="card-header">
                <div class="mb-3">
                        <input class="form-control" type="file" id="formFileMultiple" name="moviePoster" accept=".jpg,.png,.jpeg">
                </div>
            </div>
            </div>
        </div>
        <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><input type="text" name="movieTitle" placeholder="제목을 입력하세요."></li>
                <li class="list-group-item"><input type="text" name="movieRunningTime" placeholder="상영시간을 입력하세요."></li>
                <li class="list-group-item"><input type="date" name="movieOpenDate"></li>
                <li class="list-group-item">       
                    <select name="movieGenre" >
                        <option value="none">=== 선택 ===</option>
                        <option value="코미디">코미디</option>
                        <option value="액션">액션</option>
                        <option value="범죄">범죄</option>
                        <option value="드라마">드라마</option>
                        <option value="SF">SF</option>
                        <option value="공포">공포</option>
                        <option value="로맨스">로맨스</option>
                    </select>
                </li>
            </ul>
        </div>
    </div>
    <br>
    <div class="row">
        <textarea class="form-control" name="movieDescription" placeholder="상세 설명을 입력하세요."></textarea>
    </div>
    <br>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div>
        <div class="alert alert-danger" id="alert-danger">비밀번호가 일치하지 않습니다.</div>
        <div class="alert alert-danger" id="alert-danger-email">이메일 주소를 다시 기입하세요.</div>
        <div class="alert alert-danger" id="alert-danger-password">최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.</div>
    </div>
    <button type="submit" id="submit" class="btn btn-dark" disabled>영화 등록</button>
</form>
</div>
</body>
</html>