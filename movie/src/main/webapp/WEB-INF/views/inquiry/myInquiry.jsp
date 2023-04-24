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
      $("#h-inquiry").addClass("now-click");
    });
    $(document).on("click", ".save-inquiry", function(){
        var params = {
            inquiryTitle: $("#inquiryTitle").val()
            , inquiryDescription : $("#inquiryDescription").val()
        } 
        $.ajax({
            type : "POST",            
            url : "/inquiry",   
            data : params,           
            success : function(res){ 
                location.reload(true);
            }
        });
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
</style>
<body>
<div style="margin: 50px;">
    <h2 style="text-align: center;">문의</h2>
    <div style="text-align: left;">
        <label>문의 작성</label>
        <input type="text" class="form-control" id="inquiryTitle" name="inquiryTitle" placeholder="문의 제목을 등록하세요" />
        <textarea class="form-control" id="inquiryDescription" name="inquiryDescription" placeholder="문의 내용을 등록하세요"></textarea>
        <div style="text-align: right; margin: 10px;">
            <button type="button" class="btn btn-dark save-inquiry">문의 추가</button>
        </div>
    </div>
    <hr>
    <label>문의 목록</label>
    <c:forEach var="inquiry" items="${pageData.data}">    
        <div>
            <div class="content-frame" style="width: 100%; display: block;">
                <a href="/inquiry/detail?inquiryId=${inquiry.inquiryId}" style="color: white;">${inquiry.inquiryTitle}</a>
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
                <form action="/inquiry/patch?inquiryId=${inquiry.inquiryId}" method="post">
                    <button type="submit" class="btn btn-dark">수정</button>
                </form>
            </div>
            <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                <input type="hidden" id="inquiryId" value="${inquiry.inquiryId}"/>
                <button type="button" class="btn btn-dark delete-inquiry">삭제</button>
            </div>
        </div>
    </c:forEach>  
</div>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/inquiry?page=${pageData.startPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/inquiry?page=${num}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/inquiry?page=${pageData.endPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>