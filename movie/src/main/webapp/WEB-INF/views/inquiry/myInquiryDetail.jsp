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
      $("#h-inquiry").addClass("now-click");
    });
    $(document).on("click", ".delete-inquiry", function(){
        var params = {
            inquiryId : $("#inquiryId").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/inquiry/delete",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
    $(document).on("click",".patch-inquiry-init", function(){
        var inquiryNum = $(this).attr("data-inquiry_num");
        console.log(inquiryNum);
        $("#inquirytitlenum-"+inquiryNum).removeAttr("disabled");
        $("#inquirytitlenum-"+inquiryNum).removeClass("patch-inquiry-off");
        $("#inquirytitlenum-"+inquiryNum).addClass("patch-inquiry-on");
        $("#inquirydescriptionnum-"+inquiryNum).removeAttr("disabled");
        $("#inquirydescriptionnum-"+inquiryNum).removeClass("patch-inquiry-off");
        $("#inquirydescriptionnum-"+inquiryNum).addClass("patch-inquiry-on");
        $(this).html("수정완료");
        $(this).removeClass("patch-inquiry-init");
        $(this).addClass("patch-inquiry-fin");
    });
    $(document).on("click", ".patch-inquiry-fin", function(){
        var inquiryNum = $(this).attr("data-inquiry_num");
        var inquiryTitle = $("#inquirytitlenum-"+inquiryNum).val();
        var inquiryDescription = $("#inquirydescriptionnum-"+inquiryNum).val();
        var params = {
            inquiryId : inquiryNum,
            inquiryTitle : inquiryTitle,
            inquiryDescription : inquiryDescription
        } 
        $.ajax({
            type : "POST",            
            url : "/inquiry/patch",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
    });
</script>
<style>
    .pagination li{display:inline-block;}
    .sort li{display: inline-block;}
    .active a{color:red;}
    .content-frame{
        width: fit-content;
        margin: 0 auto;
        border-radius: 5px;
        padding: 6px 12px;
        background: #EB4F5A;
        color: white;
    }
    .patch-inquiry-off{
        background: none;
        border: none;
        color: white;
        width: 100%;
        height: 100%;
        border-radius: 5px;
    }
    .patch-inquiry-on{
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
            <div>
                <label>문의 제목</label>
                <div class="content-frame" style="width: 100%; display: block; color: white; border: none; padding: 0px;">
                    <input type="text" class="content-frame patch-inquiry-off" id="inquirytitlenum-${inquiry.inquiryId}" value="${inquiry.inquiryTitle}" disabled/>
                </div>
                <label>문의 내용</label>
                <div class="content-frame" style="width: 100%; display: block; color: white; border: none; padding: 0px;">
                    <input type="text" class="content-frame patch-inquiry-off" id="inquirydescriptionnum-${inquiry.inquiryId}" value="${inquiry.inquiryDescription}" disabled/>
                </div>
            </div>
            <div style="text-align: right;">
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <c:out value="${inquiry.inquiryStatus}"></c:out>
                </div>
                <div style="margin: 5px; display:inline-block;">
                    <button type="button" class="btn btn-dark patch-inquiry-init" data-inquiry_num="${inquiry.inquiryId}">수정</button>
                </div>
                <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                    <input type="hidden" id="inquiryId" value="${inquiry.inquiryId}"/>
                    <button type="button" class="btn btn-dark delete-inquiry">삭제</button>
                </div>
            </div>
            <hr>
            <h2 style="text-align: center;">문의 답변</h2>  
            <c:forEach var="answer" items="${inquiry.answers}">  
            <div>
                <div class="content-frame" style="width: 100%; display: block; color: white;">
                    ${answer.answerDescription}
                </div>
            </div>
            <div style="text-align: right;">
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <c:out value="${answer.createdBy}"></c:out>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <fmt:parseDate value="${answer.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </div>
            </div>
            </c:forEach>
    </div>
</body>
</html>