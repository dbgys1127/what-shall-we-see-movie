<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <title>무봐?</title>
</head>
<script>
    $(document).ready(function(){
      $("#h-movie").addClass("now-click");
    });
</script>
<body>
    <!-- 페이징 단추 -->
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <c:if test="${not empty pageData.target}">
                <c:if test="${pageData.prev}">
                    <li class="page-item">
                        <a class="page-link" href="/movie/search/title?page=${pageData.startPage-1}&sort=${pageData.sort}&movieTitle=${pageData.target}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                        <a class="page-link" href="/movie/search/title?page=${num}&sort=${pageData.sort}&movieTitle=${pageData.target}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="page-item">
                        <a class="page-link" href="/movie/search/title?page=${pageData.endPage+1}&sort=${pageData.sort}&movieTitle=${pageData.target}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>
            </c:if>
            <c:if test="${pageData.genre ne ''}">
                <c:if test="${pageData.prev}">
                    <li class="page-item">
                        <a class="page-link" href="/movie/search/genre?page=${pageData.startPage-1}&sort=${pageData.sort}&movieGenre=${pageData.genre}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:forEach var="num" begin="${pageData.startPage}" end="${pageData.endPage}">
                    <li class = "page-item ${pageData.nowPage eq num ? 'active':''}" aria-current="page">
                        <a class="page-link" href="/movie/search/genre?page=${num}&sort=${pageData.sort}&movieGenre=${pageData.genre}">${num}</a>
                    </li>
                </c:forEach>

                <c:if test="${pageData.next}">
                    <li class="page-item">
                        <a class="page-link" href="/movie/search/genre?page=${pageData.endPage+1}&sort=${pageData.sort}&movieGenre=${pageData.genre}}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>
            </c:if>
        </ul>
    </nav>
</body>
</html>