<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="UTF-8">
    <title> 무봐? 시큐리티 테스트 홈 화면 </title>
</head>
<script>
    $(document).ready(function(){
      $("#h-join").addClass("now-click");
    });
    $(function(){
        $("#alert-success").hide();
        $("#alert-danger").hide();
        $("#alert-danger-email").hide();
        $("#alert-danger-password").hide();
        $("#alert-danger-fail-email").hide();
        $("#alert-success-email").hide();
        $(".type-password").keyup(function(){
            var pwd1=$("#check-password1").val();
            var pwd2=$("#check-password2").val();
            if( pwd2 != ""){
                if(pwd1 == pwd2){
                    $("#alert-success").show();
                    $("#alert-danger").hide();
                    if($("#alert-danger-email").css("display") == "none" && $("#alert-danger-password").css("display") == "none"){
                        $("#submit").removeAttr("disabled");
                    }
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
                    var email = $("#email").val();
                    var params = {
                            email: email
                    } 
                    $.ajax({
                        url:'/email/check',
                        type:'POST',
                        data: params,
                        success:function(cnt){ 
                            if(cnt == 0){
                                $("#alert-danger-fail-email").hide();
                                $("#alert-success-email").show();
                                $("#submit").attr("disabled", "disabled");
                            } else {
                                $("#alert-success-email").hide();
                                $("#alert-danger-fail-email").show();
                                $("#submit").attr("disabled", "disabled");
                            }
                        },
                        error:function(){
                            alert("에러입니다");
                        }
                    });
                    if($("#alert-danger-email").css("display") == "none" && $("#alert-danger-password").css("display") == "none"){
                        $("#submit").removeAttr("disabled");
                    }
                    $("#alert-danger-email").hide();
                }else{
                    $("#alert-danger-email").show();
                    $("#submit").attr("disabled", "disabled");
                }
            }else{
                $("#alert-danger-email").hide();
                $("#alert-success-email").hide();
                $("#alert-danger-fail-email").hide();
            }
        });
        $("input[name=password]").keyup(function(){
            var pattern_password = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@!%*#?&])[A-Za-z\d@!%*#?&]{8,}$/;
            var regExp = new RegExp(pattern_password);
            var password = $("input[name=password]").val();
            if(password !=""){
                if(regExp.test(password)){
                    if($("#alert-danger-email").css("display") == "none" && $("#alert-danger-password").css("display") == "none"){
                        $("#submit").removeAttr("disabled");
                    }
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
<body>
<div style="margin: 50px;">
<h2 style="text-align: center;"> 회원가입 </h2>
<div style="margin-top: 50px;">
<form action="/join" method="post" style="text-align: center;">        
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="text" class="form-control" id="email" name="email">
        <label for="floatingInput">이메일을 입력하세요</label>
        <div class="alert alert-success" id="alert-success-email">사용가능한 이메일입니다.</div>
        <div class="alert alert-danger" id="alert-danger-fail-email">이미 사용하고 있는 이메일입니다.</div>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="password" class="form-control type-password" id="check-password1" name="password">
        <label for="floatingPassword">비밀번호를 입력하세요</label>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <input type="password" class="form-control type-password" id="check-password2" name="password-repeat">
        <label for="floatingPassword">비밀번호를 재입력 해주세요</label>
    </div>
    <div class="form-floating mb-3 offset-md-3 col-md-6">
        <div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div>
        <div class="alert alert-danger" id="alert-danger">비밀번호가 일치하지 않습니다.</div>
        <div class="alert alert-danger" id="alert-danger-email">이메일 주소를 다시 기입하세요.</div>
        <div class="alert alert-danger" id="alert-danger-password">최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.</div>
    </div>
    <button type="submit" id="submit" class="btn btn-dark" disabled>회원가입</button>
</form>
</div>
</div>
</body>
</html>