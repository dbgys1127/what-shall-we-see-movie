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
      $("#inquiry").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
    });
    $(document).on("click", ".save-answer", function(){
        var params = {
            inquiryId: $("#inquiryId").val()
            , answerDescription : $("#answerDescription").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/admin/inquiry/answer",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click", ".delete-answer", function(){
        var answerIdVal = $(this).siblings("#answerId").val();
        var params = {
            answerId : answerIdVal
        }; 
        $.ajax({
            type : "POST",            
            url : "/admin/inquiry/answer/delete",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click", ".check", function(){
        var isCheck=$(this).hasClass("check-처리");
        if(isCheck){
            var params = {
                  inquiryId: $("#inquiryId").val()
                , inquiryStatus : "off"
            } 
            $.ajax({
                type : "POST",            
                url : "/admin/inquiry/answer/status",   
                data : params,           
                success : function(res){ 
                    $(".check").removeClass("check-처리");
                    $(".check").addClass("check-대기");
                    location.reload(true);
                }
            });
        }else{
            var params = {
                  inquiryId: $("#inquiryId").val()
                , inquiryStatus : "on"
            } 
            $.ajax({
                type : "POST",            
                url : "/admin/inquiry/answer/status",   
                data : params,           
                success : function(res){ 
                    $(".check").removeClass("check-대기");
                    $(".check").addClass("check-처리");
                    location.reload(true);
                }
            });
        }
    });
    $(document).on("click",".patch-answer-init", function(){
        var answerNum = $(this).attr("data-answer_num");
        console.log(answerNum);
        $("#answernum-"+answerNum).removeAttr("disabled");
        $("#answernum-"+answerNum).removeClass("patch-answer-off");
        $("#answernum-"+answerNum).addClass("patch-answer-on");
        $(this).html("수정완료");
        $(this).removeClass("patch-answer-init");
        $(this).addClass("patch-answer-fin");
    });
    $(document).on("click", ".patch-answer-fin", function(){
        var answerNum = $(this).attr("data-answer_num");
        var answerDescription = $("#answernum-"+answerNum).val();
        var params = {
            answerId : answerNum,
            answerDescription : answerDescription
        } 
        $.ajax({
            type : "POST",            
            url : "/admin/inquiry/answer/patch",   
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
        background: white;
        color: black; 
        border: 1px solid black;
    }
    .check-처리{
        background-image: url("/image/checked.png");
        background-size: cover;
        background-color: white;
        border: none;
    }
    .check-대기{
        background-image: url("/image/empty-check.png");
        background-size: cover;
        background-color: white;
        border: none;
    }
    .patch-answer-off{
        background: none;
        border: none;
        color: white;
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }
    .patch-answer-on{
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
    <h2 style="text-align: center;">문의 상세화면</h2>
<table class="table" style="margin-top: 50px;" >
    <!-- 표 헤더 -->
        <thead class="table-dark">
            <th>작성자</th>
            <th>작성일</th>
            <th>처리 상태</th>
            <th>처리 체크</th>
        </thead>
            <tr>
                <!-- 표안에 보여질 관리자 정보 -->
                <td>${inquiry.createdBy}</td>
                <td>
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </td>
                <td>${inquiry.inquiryStatus}</td>
                <td>
                    <p class="content-frame check check-${inquiry.inquiryStatus}" style="width: 22px; height: 22px;">
                        <input type="hidden" id="inquiryId" name="inquiryId" value="${inquiry.inquiryId}"/>
                        <input type="hidden" id="inquiryStatus" name="inquiryStatus" value="${inquiry.inquiryStatus}"/>
                    </p>
                </td>
            </tr>
        </table>
        <div style="text-align: left;">
            <label>문의 제목</label>
            <div class="content-frame" style="width: 100%; display: block; color: black;">
                ${inquiry.inquiryTitle}
            </div>
            <label>문의 내용</label>
            <div class="content-frame" style="width: 100%; display: block; color: black;">
                ${inquiry.inquiryDescription}
            </div>
        </div>
        <hr>
        <div>
            <input type="hidden" id="inquiryId" name="inquiryId" value="${inquiry.inquiryId}"/>
            <textarea class="form-control" id="answerDescription" name="answerDescription" placeholder="답변을 등록하세요"></textarea>
            <div style="text-align: right; margin-bottom: 10px;">
                <button type="button" class="btn btn-dark save-answer" style="border: none; margin-top: 10px;">답변 등록</button>
            </div>
        </div>
    <div style="text-align: left;">
        <label >답변 내역</label>
    </div>
    <c:forEach var="answer" items="${inquiry.answers}">    
        <div>
            <div class="content-frame" style="width: 100%; display: block; text-align: left; color: white; background: black; border: none; padding: 0px;">
                <input type="text" class="content-frame patch-answer-off" id="answernum-${answer.answerId}" value="${answer.answerDescription}" disabled/>
            </div>
        </div>
        <div style="text-align: right;">
            <div class="content-frame" style="margin: 5px; display:inline-block;">
                ${answer.createdBy}
            </div>
            <div class="content-frame" style="margin: 5px; display:inline-block;">
                <fmt:parseDate value="${answer.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
            </div>
            <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                <button type="button" class="btn btn-dark patch-answer-init" data-answer_num="${answer.answerId}">수정</button>
            </div>
            <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                <input type="hidden" id="answerId" value="${answer.answerId}"/>
                <button type="button" class="btn btn-dark delete-answer">삭제</button>
            </div>
        </div>  
    </c:forEach>  
</div>
</body>
</html>