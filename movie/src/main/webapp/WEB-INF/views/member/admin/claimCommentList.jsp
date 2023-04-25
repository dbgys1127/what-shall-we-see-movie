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
      $("#comment").addClass("now-click");
    });
    $(document).ready(function(){
      $("#h-admin").addClass("now-click");
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
        background: white;
        color: black; 
        border: 1px solid black;
    }
</style>
<body>
    <div style="margin: 50px;">
        <h2 style="text-align: center;" style="margin-bottom: 50px;">신고 댓글목록</h2>  
        <div style="margin-top: 50px;">
        <c:forEach var="comment" items="${pageData.data}">    
            <div>
                <div class="content-frame" style="width: 100%; display: block; text-align: left; color: white; background: black;">
                    ${comment.commentDetail}
                </div>
            </div>
            <div style="text-align: right;">
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <a href="/admin/member/warning-page?email=${comment.createdBy}" style="color: black;">${comment.createdBy}</a>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <a href="/movie/detail?movieTitle=${comment.movieTitle}" style="color: black;">${comment.movieTitle}</a>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <fmt:parseDate value="${comment.createdAt}" var="createdAt" pattern="yyyyMMdd"/>                       
                    <fmt:formatDate value="${createdAt}" pattern="yyyy-MM-dd"/>
                </div>
                <div class="content-frame" style="margin: 5px; display:inline-block;">
                    <c:out value="신고내역:${comment.claimCount}회"></c:out>
                </div>
                <div style="margin: 5px 5px 5px; margin-right: 0px; display:inline-block;">
                    <input type="hidden" id="commentId" value="${comment.commentId}"/>
                    <button type="button" class="btn btn-dark delete-comment">삭제</button>
                </div>
            </div>
        </c:forEach>    
    </div>
    </div>
<!-- 페이징 단추 -->
<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
            <c:if test="${pageData.prev}">
                <li class="page-item">
                    <a class="page-link" href="/admin/comment?page=${pageData.startPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>

            <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                    <a class="page-link" href="/admin/comment?page=${num}">${num}</a>
                </li>
            </c:forEach>

            <c:if test="${pageData.next}">
                <li class="page-item">
                    <a class="page-link" href="/admin/comment?page=${pageData.endPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
    </ul>
</nav>
</body>
</html>