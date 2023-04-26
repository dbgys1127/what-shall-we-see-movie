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
    $("#alert-danger").hide();
    $("#alert-danger-movieTitle").hide();
    $("#alert-danger-movieRunningTime").hide();
    $("#alert-danger-movieOpenDate").hide();
    $("#alert-danger-movieDescription").hide();
    $("#alert-success-movieTitle").hide();
    $("#alert-danger-fail-movieTitle").hide();
    $("input, textarea, select").change(function(){
        var input1 = $("#input1").val();
        var input2 = $("#input2").val();
        var input3 = $("#input3").val();
        var input4 = $("#input4").prop("value");
        var input5 = $("#input5").val();
        var selectVal = $("#selectVal").val(); 
        if(input1==""||input2==""||input3==""||input4==""||input5==""||selectVal=="none"){
            $("#submit").attr("disabled", "disabled");
            $("#alert-danger").show();
        }else{
            $("#alert-danger").hide();
            if($("#alert-danger-movieTitle").css("display") == "none" && $("#alert-danger-movieRunningTime").css("display") == "none" && $("#alert-danger-movieOpenDate").css("display") == "none" && $("#alert-danger-movieDescription").css("display") == "none"){
                $("#submit").removeAttr("disabled");
            }
        }
    });
    $("#input2").keyup(function(){
        var pattern_movieOpenDate = /^.{1,20}$/;
        var regExp = new RegExp(pattern_movieOpenDate);
        var movieTitle = $("#input2").val();
        if(movieTitle !=""){
            if(regExp.test(movieTitle)){
                var movieTitle = $("#input2").val();
                var params = {
                    movieTitle: movieTitle
                } 
                $.ajax({
                    url:'/admin/movie/check',
                    type:'POST',
                    data: params,
                    success:function(cnt){ 
                        if(cnt == 0){
                            $("#alert-danger-fail-movieTitle").hide();
                            $("#alert-success-movieTitle").show();
                            $("#submit").removeAttr("disabled");
                        } else {
                            $("#alert-success-movieTitle").hide();
                            $("#alert-danger-fail-movieTitle").show();
                            $("#submit").attr("disabled", "disabled");
                        }
                    },
                    error:function(){
                        alert("에러입니다");
                    }
                });
                $("#alert-danger-movieTitle").hide();
            }else{
                $("#alert-danger-movieTitle").show();
                $("#submit").attr("disabled", "disabled");
            }
        }else{
            $("#alert-danger-movieTitle").hide();
            $("#alert-success-movieTitle").hide();
            $("#alert-danger-fail-movieTitle").hide();
        }
        if($("#alert-danger-movieTitle").css("display") == "none" && $("#alert-danger-movieRunningTime").css("display") == "none" && $("#alert-danger-movieOpenDate").css("display") == "none" && $("#alert-danger-movieDescription").css("display") == "none"){
            $("#submit").removeAttr("disabled");
        }
    });
    $("#input3").keyup(function(){
        var pattern_movieRunningTime = /^[1-9]\d*$/;
        var regExp = new RegExp(pattern_movieRunningTime);
        var movieRunningTime = $("#input3").val();
        if(movieRunningTime !=""){
            if(regExp.test(movieRunningTime)){
                $("#alert-danger-movieRunningTime").hide();
                $("#submit").attr("disabled", "disabled");
            }else{
                $("#alert-danger-movieRunningTime").show();
                $("#submit").attr("disabled", "disabled");
            }
        }else{
            $("#alert-danger-movieRunningTime").hide();
        }
        if($("#alert-danger-movieTitle").css("display") == "none" && $("#alert-danger-movieRunningTime").css("display") == "none" && $("#alert-danger-movieOpenDate").css("display") == "none" && $("#alert-danger-movieDescription").css("display") == "none"){
            $("#submit").removeAttr("disabled");
        }
    });
    $("#input4").change(function(){
        var today = new Date().toISOString().slice(0, 10);
        var pattern_movieOpenDate = /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/;
        var regExp = new RegExp(pattern_movieOpenDate);
        var movieOpenDate = $("#input4").prop("value");
        if(movieOpenDate !=""){
            if(regExp.test(movieOpenDate) && movieOpenDate<=today){
                $("#alert-danger-movieOpenDate").hide();
                $("#submit").attr("disabled", "disabled");
            }else{
                $("#alert-danger-movieOpenDate").show();
                $("#submit").attr("disabled", "disabled");
            }
        }else{
            $("#alert-danger-movieOpenDate").hide();
        }
        if($("#alert-danger-movieTitle").css("display") == "none" && $("#alert-danger-movieRunningTime").css("display") == "none" && $("#alert-danger-movieOpenDate").css("display") == "none" && $("#alert-danger-movieDescription").css("display") == "none"){
            $("#submit").removeAttr("disabled");
        }
    });
    $("#input5").keyup(function(){
        var pattern_movieDescription = /^.{1,300}$/;
        var regExp = new RegExp(pattern_movieDescription);
        var movieDescription = $("#input5").val();
        if(movieDescription !=""){
            if(regExp.test(movieDescription)){
                $("#alert-danger-movieDescription").hide();
                $("#submit").attr("disabled", "disabled");
            }else{
                $("#alert-danger-movieDescription").show();
                $("#submit").attr("disabled", "disabled");
            }
        }else{
            $("#alert-danger-movieDescription").hide();
        }
        if($("#alert-danger-movieTitle").css("display") == "none" && $("#alert-danger-movieRunningTime").css("display") == "none" && $("#alert-danger-movieOpenDate").css("display") == "none" && $("#alert-danger-movieDescription").css("display") == "none"){
            $("#submit").removeAttr("disabled");
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
                    <input class="form-control" type="file" id="input1" name="moviePoster" accept=".jpg,.png,.jpeg">
                </div>
            </div>
            </div>
        </div>
        <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <input type="text" name="movieTitle" id="input2" placeholder="제목을 입력하세요.">
                    <div class="alert alert-success" id="alert-success-movieTitle" style="width:50%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">신규 영화입니다.</div>
                    <div class="alert alert-danger" id="alert-danger-fail-movieTitle" style="width:50%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">이미 등록된 영화입니다.</div>
                </li>
                <li class="list-group-item"><input type="text" name="movieRunningTime" id="input3" placeholder="상영시간을 입력하세요."></li>
                <li class="list-group-item"><input type="date" name="movieOpenDate" id="input4"></li>
                <li class="list-group-item">       
                    <select name="movieGenre" id="selectVal" >
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
        <textarea class="form-control" name="movieDescription" id="input5" placeholder="상세 설명을 입력하세요."></textarea>
    </div>
    <br>
    <div class="alert alert-danger" id="alert-danger" style="width:100%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">모든 값을 입력해주세요.</div>
    <div class="alert alert-danger alert-danger-movieTitle" id="alert-danger-movieTitle" style="width:100%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">영화 제목은 20자 이내로 작성하시오.</div>
    <div class="alert alert-danger alert-danger-movieRunningTime" id="alert-danger-movieRunningTime" style="width:100%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">1이상의 자연수만 입력하세요.</div>
    <div class="alert alert-danger alert-danger-movieOpenDate" id="alert-danger-movieOpenDate" style="width:100%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">오늘 이전을 선택해주세요.</div>
    <div class="alert alert-danger alert-danger-movieDescription" id="alert-danger-movieDescription" style="width:100%; height: 40%; display: inline-block; font-size: 15px; padding: 5px; margin-bottom: 10px;">300자 이내로 작성해주세요.</div>
    <br>
    <button type="submit" id="submit" class="btn btn-dark" disabled>영화 등록</button>
</form>
</div>
</body>
</html>