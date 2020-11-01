<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <!--Paginación -->
<c:if test="${(param.total/param.page_size) > 1}">
    <ul class="pagination justify-content-center mt-2">
            <c:if test="${param.page != 1}">
              <li class="page-item ">
                    <a class="page-link" onclick="moveToPage(${param.page-1},'${param.origin}')" aria-label="Previous">
                        <span aria-hidden="true">&lsaquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
            </c:if>
        <c:choose>
            <c:when test="${(param.total/param.page_size) <= 2}">
                <li class="page-item <c:if test="${param.page == 1}">active</c:if>"><div class="page-link" onclick="moveToPage(1,'${param.origin}')" >1</div></li>
                <li class="page-item <c:if test="${param.page == 2}">active</c:if>"><div class="page-link" onclick="moveToPage(2,'${param.origin}')" >2</div></li>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${param.page < 3}">
                        <li class="page-item <c:if test="${param.page == 1}">active</c:if>"><div class="page-link" onclick="moveToPage(1,'${param.origin}')" >1</div></li>
                        <li class="page-item <c:if test="${param.page == 2}">active</c:if>"><div class="page-link" onclick="moveToPage(2,'${param.origin}')" >2</div></li>
                        <li class="page-item <c:if test="${param.page == 3}">active</c:if>"><div class="page-link" onclick="moveToPage(3,'${param.origin}')" >3</div></li>
                        <c:if test="${(param.total/param.page_size) > 3}">
                            <li class="page-item <c:if test="${param.page == 4}">active</c:if>"><div class="page-link" onclick="moveToPage(4,'${param.origin}')" >4</div></li>
                        </c:if>
                        <c:if test="${(param.total/param.page_size) > 4}">
                            <li class="page-item <c:if test="${param.page == 5}">active</c:if>"><div class="page-link" onclick="moveToPage(5,'${param.origin}')" >5</div></li>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><div class="page-link" onclick="moveToPage(${param.page-2},'${param.origin}')">${param.page-2}</div></li>
                        <li class="page-item"><div class="page-link" onclick="moveToPage(${param.page-1},'${param.origin}')">${param.page-1}</div></li>
                        <li class="page-item active"><div class="page-link">${param.page}</div></li>
                        <c:if test="${(param.total/param.page_size)-param.page > 0 }">
                            <li class="page-item"><div class="page-link" onclick="moveToPage(${param.page+1},'${param.origin}')">${param.page+1}</div></li>
                            <c:if test="${(param.total/param.page_size)-param.page >=1 }">
                                <li class="page-item"><div class="page-link" onclick="moveToPage(${param.page+2},'${param.origin}')">${param.page+2}</div></li>
                            </c:if>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:if test="${param.page_size*param.page < param.total}">
            <li class="page-item">
                <a class="page-link" onclick="moveToPage(${param.page+1},'${param.origin}')" aria-label="Next">
                    <span aria-hidden="true">&rsaquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
        </c:if>
    </ul>
</c:if>

<script>

   function moveToPage(goingPage,origin){
       if(origin === 'search'){
           let url = "<c:url value="/search?toSearch="/>" ;
           <c:if test="${not empty param.toSearch}">
               url = url + ${param.toSearch};
           </c:if>

            url = url + '&categories=' + '${param.categories}';
            url = url + '&types=' + '${param.types}';
            url = url.replaceAll('[','').replaceAll(']','');
           <c:if test="${not empty param.star1}">
                url = url + '&starsLeft=' + ${param.star1};
           </c:if>
           <c:if test="${not empty param.star2}">
                url = url + '&starsRight=' + ${param.star2};
           </c:if>
           <c:if test="${not empty param.nameFlag}">
                url = url + '&nameFlag=' + ${param.nameFlag};
           </c:if>
           <c:if test="${not empty param.commentAmount}">
                url = url +'&commentAmount=' + ${param.commentAmount};
           </c:if>
           <c:if test="${not empty param.commentDate}">
                url = url +'&lastComment=' + ${param.commentDate};
           </c:if>
           <c:if test="${not empty param.updateDate}">
                url = url +'&lastUpdate=' + ${param.updateDate};
           </c:if>
           <c:if test="${not empty param.order}">
                url = url +'&order=' + ${param.order};
           </c:if>
           url = url +'&page='+goingPage;
           window.location.href = url;
       }
       else if(origin == 'profile_comment'){
        window.location.href="<c:url value="/users/${param.username}?comments_page="/>".concat(goingPage).concat("&contents_page=${param.contents_page}&votes_page=${param.votes_page}&frameworks_page=${param.techs_page} ");
       }
       else if(origin == 'profile_content'){
        window.location.href="<c:url value="/users/${param.username}?comments_page=${param.comments_page}&contents_page="/>".concat(goingPage).concat("&votes_page=${param.votes_page}&frameworks_page=${param.techs_page} ");
       }
       else if(origin == 'profile_votes'){
        window.location.href="<c:url value="/users/${param.username}?comments_page=${param.comments_page}&contents_page=${param.contents_page}&votes_page="/>".concat(goingPage).concat("&frameworks_page=${param.techs_page} ");
       }
       else if(origin == 'profile_techs'){
        window.location.href="<c:url value="/users/${param.username}?comments_page=${param.comments_page}&contents_page=${param.contents_page}&votes_page=${param.votes_page}&frameworks_page="/>".concat(goingPage);
       }
       else if(origin == 'tech_book'){
           window.location.href="<c:url value="/${param.category}/${param.techs_id}?books_page="/>".concat(goingPage).concat("&courses_page=${param.courses_page}&tutorials_page=${param.tutorials_page}&comments_page=${param.comments_page}");
       }
       else if(origin == 'tech_courses'){
           window.location.href="<c:url value="/${param.category}/${param.techs_id}?books_page=${param.books_page}&courses_page="/>".concat(goingPage).concat("&tutorials_page=${param.tutorials_page}&comments_page=${param.comments_page}");
       }
       else if(origin == 'tech_tutorial'){
           window.location.href="<c:url value="/${param.category}/${param.techs_id}?books_page=${param.books_page}&courses_page=${param.courses_page}&tutorials_page="/>".concat(goingPage).concat("&comments_page=${param.comments_page}");
       }
       else if(origin == 'tech_comments'){
           window.location.href="<c:url value="/${param.category}/${param.techs_id}?books_page=${param.books_page}&courses_page=${param.courses_page}&tutorials_page=${param.tutorials_page}&comments_page="/>".concat(goingPage);
       }
       else if(origin == 'category_list'){
           window.location.href="<c:url value="/${param.category}?frameworks_page="/>".concat(goingPage);
       }
       else if(origin == 'mod_mod'){
           window.location.href="<c:url value="/mod?modsPage="/>".concat(goingPage).concat("&rComPage=${param.rComPage}&applicantsPage=${param.applicantsPage}&verifyPage=${param.verifyPage}&rConPage=${param.rConPage}");
       }
       else if(origin == 'mod_report_comment'){
           window.location.href="<c:url value="/mod?modsPage=${param.modsPage}&rComPage="/>".concat(goingPage).concat("&applicantsPage=${param.applicantsPage}&verifyPage=${param.verifyPage}&rConPage=${param.rConPage}");
       }
       else if(origin == 'mod_applicants'){
           window.location.href="<c:url value="/mod?modsPage=${param.modsPage}&rComPage=${param.rComPage}&applicantsPage="/>".concat(goingPage).concat("&verifyPage=${param.verifyPage}&rConPage=${param.rConPage}");
       }
       else if(origin == 'mod_verify'){
           window.location.href="<c:url value="/mod?modsPage=${param.modsPage}&rComPage=${param.rComPage}&applicantsPage=${param.applicantsPage}&verifyPage="/>".concat(goingPage).concat("&rConPage=${param.rConPage}");
       }
       else if(origin == 'mod_report_content'){
           window.location.href="<c:url value="mod?modsPage=${param.modsPage}&rComPage=${param.rComPage}&applicantsPage=${param.applicantsPage}&verifyPage=${param.verifyPage}&rConPage="/>".concat(goingPage);
       }

       }

</script>