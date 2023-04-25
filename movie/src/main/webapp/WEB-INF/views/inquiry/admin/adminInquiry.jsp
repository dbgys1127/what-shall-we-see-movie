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
      $("#inquiry").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
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
        var inquiryIdVal = $(this).siblings("#inquiryId").val();
        var params = {
            inquiryId : inquiryIdVal
        }; 
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
    .btn-group li{
        display: inline-block;
        text-align: right;
    }
    .active a{color:red;}
    .content-frame{
        width: fit-content;
        margin: 0 auto;
        border-radius: 5px;
        padding: 6px 12px;
        background: white;
        color: black; 
        border: 1px solid black;
    }
</style>
<body>
    <div style="margin: 50px;">
        <h2 style="text-align: center;">문의 목록</h2>
            <!-- 정렬 기준 -->
    <div class="d-flex justify-content-end" style="margin: 10px;">
        <div class="btn-group btn-group-sm btn-group-right">
            <li><a href="/admin/inquiry?page=1&sort=createdAt" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'createdAt' ? 'active':''}">문의 일자순</a></li>
            <li><a href="/admin/inquiry?page=1&sort=inquiryStatus" class="btn btn-sm btn-outline-dark ${pageData.sort eq 'inquiryStatus' ? 'active':''}">처리 상태</a></li>
        </div>
    </div>
        <c:forEach var="inquiry" items="${pageData.data}">    
            <div style="text-align: left;">
                <div class="content-frame" style="width: 100%; display: block; background: black;">
                    <a href="/admin/inquiry/detail?inquiryId=${inquiry.inquiryId}" style="color: white;">${inquiry.inquiryTitle}</a>
                </div>
            </div>
            <div style="text-align: right;">
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    ${inquiry.createdBy}
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <fmt:parseDate value="${inquiry.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <c:out value="${inquiry.inquiryStatus}"></c:out>
                </div>
            </div>
        </c:forEach>  
    </div>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/admin/inquiry?page=${pageData.startPage-1}&sort=${pageData.sort}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/admin/inquiry?page=${num}&sort=${pageData.sort}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/admin/inquiry?page=${pageData.endPage+1}&sort=${pageData.sort}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>