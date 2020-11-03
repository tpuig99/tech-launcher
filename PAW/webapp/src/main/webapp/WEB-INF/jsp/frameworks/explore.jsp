<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>
        <spring:message code="explore.wref"/>
    </title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/search.css"/>"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.css" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
    <jsp:param name="search_page" value="${search_page}"/>
</jsp:include>

<div class="content-search">
    <div class="sidebar-search">
        <div class="d-flex flex-row-reverse">
        <button class="btn my-2 my-sm-0 primary-button" type="button" onclick="searchFrameworks()"><spring:message code="explore.filter"/></button>
        </div>

        <!-- Filter By Categories -->
        <div class="subtitle"><h4><spring:message code="explore.categories"/></h4></div>
            <c:forEach items="${categories}" var="category" begin="0" end="5">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="check${category}">
                    <label class="form-check-label" for="check${category}">
                        <spring:message code="category.${category}"/>
                    </label>
                </div>
            </c:forEach>
            <div>
                <button id="showMoreCategories" class="btn btn-link" onclick="showMore('Categories')"><spring:message code="explore.show_more"/></button>
            </div>
            <div class="hide" id="hiddenCategories">
                <c:forEach items="${categories}" var="category" begin="6" end="${fn:length(categories)}">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="check${category}">
                        <label class="form-check-label" for="check${category}">
                            <spring:message code="category.${category}"/>
                        </label>
                    </div>
                </c:forEach>
                <div>
                    <button id="showLessCategories" class="btn btn-link" onclick="showLess('Categories')"><spring:message code="explore.show_less"/></button>
                </div>
            </div>

        <!-- Filter By Types -->
        <div class="subtitle sidebar-title"><h4><spring:message code="explore.types"/></h4></div>
        <c:forEach items="${types}" var="type" begin="0" end="5">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="check${type}">
                <label class="form-check-label" for="check${type}">
                    <spring:message code="type.${type}"/>
                </label>
            </div>
        </c:forEach>
        <div>
            <button id="showMoreTypes" class="btn btn-link" onclick="showMore('Types')"><spring:message code="explore.show_more"/></button>
        </div>
        <div class="hide" id="hiddenTypes">
            <c:forEach items="${types}" var="type" begin="6" end="${fn:length(types)}">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="check${type}">
                    <label class="form-check-label" for="check${type}">
                        <spring:message code="type.${type}"/>
                    </label>
                </div>
            </c:forEach>
            <div>
                <button id="showLessTypes" class="btn btn-link" onclick="showLess('Types')"><spring:message code="explore.show_less"/></button>
            </div>
        </div>

        <!--Filter By Comments Date-->

        <div class="subtitle sidebar-title"><h4><spring:message code="explore.update_date"/></h4></div>

        <span>
            <select id="update-date-dropdown">
                <option value="0" <c:if test="${dateUpdate == 0}"> selected </c:if>><spring:message code="explore.any_date"/></option>
                <option value="1" <c:if test="${dateUpdate == 1}"> selected </c:if>><spring:message code="explore.last_days"/></option>
                <option value="2"<c:if test="${dateUpdate == 2}"> selected </c:if>><spring:message code="explore.last_week" /></option>
                <option value="3"<c:if test="${dateUpdate == 3}"> selected </c:if>><spring:message code="explore.last_month" /></option>
                <option value="4"<c:if test="${dateUpdate == 4}"> selected </c:if>><spring:message code="explore.last_months" /></option>
                <option value="5"<c:if test="${dateUpdate == 5}"> selected </c:if>><spring:message code="explore.last_year" /></option>
            </select>
         </span>

        <!--Filter By Comments Date-->

        <div class="subtitle sidebar-title"><h4><spring:message code="explore.comment_date"/></h4></div>
        <span>
            <select id="comments-date-dropdown">
                <option value="0" <c:if test="${dateComment == 0}"> selected </c:if>><spring:message code="explore.any_date"/></option>
                <option value="1" <c:if test="${dateComment == 1}"> selected </c:if>><spring:message code="explore.last_days"/></option>
                <option value="2"<c:if test="${dateComment == 2}"> selected </c:if>><spring:message code="explore.last_week"/></option>
                <option value="3"<c:if test="${dateComment == 3}"> selected </c:if>><spring:message code="explore.last_month"/></option>
                <option value="4"<c:if test="${dateComment == 4}"> selected </c:if>><spring:message code="explore.last_months"/></option>
                <option value="5"<c:if test="${dateComment == 5}"> selected </c:if>><spring:message code="explore.last_year"/></option>
            </select>
         </span>

        <!--Filter By Comments Amount-->

        <div class="subtitle sidebar-title"><h4><spring:message code="explore.comments"/></h4></div>

        <span>
            <select id="comments-dropdown">
                <option value="0" <c:if test="${commentAmount == 0}"> selected </c:if>><spring:message code="explore.comments_all"/></option>
                <option value="1" <c:if test="${commentAmount == 1}"> selected </c:if>><spring:message code="explore.comments_from" arguments="1"/></option>
                <option value="5"<c:if test="${commentAmount == 5}"> selected </c:if>><spring:message code="explore.comments_from" arguments="5"/></option>
                <option value="10"<c:if test="${commentAmount == 10}"> selected </c:if>><spring:message code="explore.comments_from" arguments="10"/></option>
                <option value="20"<c:if test="${commentAmount == 20}"> selected </c:if>><spring:message code="explore.comments_from" arguments="20"/></option>
                <option value="50"<c:if test="${commentAmount == 50}"> selected </c:if>><spring:message code="explore.comments_from" arguments="50"/></option>
            </select>
         </span>

        <!--Filter By Rating-->
        <div class="subtitle sidebar-title"><h4><spring:message code="explore.rating"/></h4></div>

        <span><spring:message code="explore.from"/></span>
        <span>
            <select id="stars-dropdown-1">
                <option value="0" <c:if test="${starsQuery1 == 0}"> selected </c:if>>0</option>
                <option value="1"<c:if test="${starsQuery1 == 1}"> selected </c:if>>1</option>
                <option value="2"<c:if test="${starsQuery1 == 2}"> selected </c:if>>2</option>
                <option value="3"<c:if test="${starsQuery1 == 3}"> selected </c:if>>3</option>
                <option value="4"<c:if test="${starsQuery1 == 4}"> selected </c:if>>4</option>
                <option value="5"<c:if test="${starsQuery1 == 5}"> selected </c:if>>5</option>
            </select>
         </span>
        <span><spring:message code="explore.to_5_stars"/></span>
        <span>
            <select id="stars-dropdown-2">
                <option value="0" <c:if test="${starsQuery2 == 0}"> selected </c:if>>0</option>
                <option value="1"<c:if test="${starsQuery2 == 1}"> selected </c:if>>1</option>
                <option value="2"<c:if test="${starsQuery2 == 2}"> selected </c:if>>2</option>
                <option value="3"<c:if test="${starsQuery2 == 3}"> selected </c:if>>3</option>
                <option value="4"<c:if test="${starsQuery2 == 4}"> selected </c:if>>4</option>
                <option value="5"<c:if test="${starsQuery2 == 5 || starsQuery2==null}"> selected </c:if>>5</option>
            </select>
        </span>
    </div>

    <!-- Search Bar -->
    <div class="search-bar sidebar-title">
        <form class="form-inline my-2 my-lg-0" method="post" onsubmit="searchFrameworks()" id="search">
            <input id="searchInput" class="form-control mr-sm-2" type="text" value="${techNameQuery}" placeholder="<spring:message code="search.title"/>" aria-label="Search" size="80">
            <button class="btn my-2 my-sm-0 primary-button" type="button" onclick="searchFrameworks()"><spring:message code="search.title"/></button>
        </form>
        <div class="form-check">
            <input class="form-check-input" type="checkbox" value="" id="searchOnlyByName">
            <label class="form-check-label" for="searchOnlyByName">
                <spring:message code="explore.search_only_by_name"/>
            </label>
        </div>
        <div class="d-flex flex-row justify-content-end">
        <div class="mx-2">
            <label class="subtitle" for="sortSelect"><spring:message code="explore.sort_by"/></label>
            <select class="form-control" id="sortSelect" oninput="sortFrameworks()">
                <option value="0" <c:if test="${sortValue == 0}"> selected </c:if>><spring:message code="explore.sort_by.none"/></option>
                <option value="1"<c:if test="${sortValue == 1}"> selected </c:if>><spring:message code="explore.sort_by.rating"/></option>
                <option value="2"<c:if test="${sortValue == 2}"> selected </c:if>><spring:message code="explore.sort_by.comments_amount"/></option>
                <option value="3"<c:if test="${sortValue == 3}"> selected </c:if>><spring:message code="explore.sort_by.tech_updated"/></option>
                <option value="4"<c:if test="${sortValue == 4}"> selected </c:if>><spring:message code="explore.sort_by.recently_commented"/></option>
            </select>
        </div>
        <div class="mx-2">
            <label class="subtitle" for="orderSelect"><spring:message code="explore.order_by"/></label>
            <select class="form-control" id="orderSelect" oninput="sortFrameworks()">
                <option value="1" <c:if test="${orderValue == 1}"> selected </c:if>><spring:message code="explore.order_by.descendant"/></option>
                <option value="-1"<c:if test="${orderValue == -1}"> selected </c:if>><spring:message code="explore.order_by.ascendant"/></option>
            </select>
        </div>
        </div>
    </div>

    <!--Search Results For / Explore -->
    <div class="page-description"></div>
    <div class="page-title">

        <c:choose>
            <c:when test="${empty techNameQuery and empty starsQuery1 and empty starsQuery2 and empty categoriesQuery and empty typesQuery and empty orderQuery and empty commentAmount and empty dateUpdate and empty dateComment and empty selectOrder}">
            <h2><spring:message code="explore.title"/></h2>
            </c:when>
            <c:otherwise>
                <h2><spring:message code="explore.search_results"/> (${searchResultsNumber})</h2>
            </c:otherwise>
        </c:choose>
    </div>

    <!--Search Results Badges -->
    <div class="row">
        <div class="col-8">
            <div class="margin-top">
                <c:if test="${not empty techNameQuery}">
                    <span id="name" class="badge badge-pill secondary-badge"><c:out value="${techNameQuery}"/></span>
                </c:if>
                <c:if test="${not empty categoriesQuery}">
                    <c:forEach items="${categoriesQuery}" var="categoryQuery">
                        <span id="name${categoryQuery}" class="badge badge-pill secondary-badge"><spring:message code="category.${categoryQuery}"/></span>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty typesQuery}">
                    <c:forEach items="${typesQuery}" var="typeQuery">
                        <span id="name${typeQuery}" class="badge badge-pill secondary-badge"><spring:message code="type.${typeQuery}"/></span>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty starsQuery1}">
                    <span id="stars" class="badge badge-pill secondary-badge">
                        <spring:message code="explore.stars_query"
                                        arguments="${starsQuery1},${starsQuery2}"
                                        htmlEscape="true"/>
                    </span>
                </c:if>
                <c:if test="${not empty commentAmount && commentAmount!=0}">
                <span id="comments" class="badge badge-pill secondary-badge">
                    <spring:message code="explore.comments_query"
                                    arguments="${commentAmount}"
                                    htmlEscape="true"/>
                </span>
                </c:if>
                <c:if test="${not empty dateUpdate && dateUpdate!=0}">
                <span id="comments" class="badge badge-pill secondary-badge">
                    <spring:message code="explore.update_date_query"
                                    arguments="${dateUpdateTranslation}"
                                    htmlEscape="true"/>
                </span>
                </c:if>
                <c:if test="${not empty dateComment && dateComment!=0}">
                <span id="comments" class="badge badge-pill secondary-badge">
                    <spring:message code="explore.comments_date_query"
                                    arguments="${dateCommentTranslation}"
                                    htmlEscape="true"/>
                </span>
                </c:if>
            </div>
        </div>

    </div>

    <div class="page-description"></div>
    <!-- Display Matching Techs -->
    <c:choose>
        <c:when test="${matchingFrameworks.size() == 0 }">
            <div>
                <spring:message code="explore.not_found"/>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row equal">
                <c:forEach var="framework" items="${matchingFrameworks}" >
                    <div class="card mx-4 mb-4">
                        <a href="<c:url value="/techs/${framework.category}/${framework.id}"/>">
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty framework.base64image}">
                                        <div class="max-logo d-flex align-items-center justify-content-center"><img src="data:${framework.contentType};base64,${framework.base64image}" alt="<spring:message code="tech.picture"/>"/></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="<spring:message code="tech.picture"/>"></div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="card-footer text-dark">
                                <span>${framework.name} | </span>
                                <span class="fa fa-star fa-sm"></span>
                                <span>${framework.starsFormated}</span>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
            <!-- Paginación -->
            <jsp:include page="../components/pagination.jsp">
                <jsp:param name="total" value="${searchResultsNumber}"/>
                <jsp:param name="page" value="${page}"/>
                <jsp:param name="page_size" value="${page_size}"/>
                <jsp:param name="origin" value="search"/>
                <jsp:param name="toSearch" value="${techNameQuery}"/>
                <jsp:param name="categories" value="${categoriesQuery}"/>
                <jsp:param name="types" value="${typesQuery}"/>
                <jsp:param name="star1" value="${starsQuery1}"/>
                <jsp:param name="star2" value="${starsQuery2}"/>
                <jsp:param name="nameFlag" value="${nameFlagQuery}"/>
                <jsp:param name="order" value="${orderValue}"/>
                <jsp:param name="commentAmount" value="${commentAmount}"/>
                <jsp:param name="commentDate" value="${dateComment}"/>
                <jsp:param name="updateDate" value="${dateUpdate}"/>


            </jsp:include>

        </c:otherwise>
    </c:choose>
</div>

<script>


    $(document).ready(function() {

        let tags = [];
        <c:forEach items="${categories}" var="category">
            tags.push('${categories_translated.get(category)}');
        </c:forEach>
        <c:forEach items="${types}" var="type">
            tags.push('${types_translated.get(type)}');
        </c:forEach>
        <c:forEach items="${frameworkNames}" var="names">
            tags.push('${names}');
        </c:forEach>

        $('#searchInput').autocomplete({
            source : tags,

        })

        <c:forEach items="${categoriesQuery}" var="category">
            document.getElementById("check${category}").checked = true;
        </c:forEach>
        <c:forEach items="${typesQuery}" var="type">
            document.getElementById("check${type}").checked = true;
        </c:forEach>
        document.getElementById("searchOnlyByName").checked = ${nameFlagQuery};
    });

    function isEmpty( input ){
        for (let i = 0; i < input.length; i++) {
            if(input.charAt(i) !== " " ){
                return false;
            }
        }

        return true;
    }

    function searchFrameworks() {
        let name = document.getElementById("searchInput").value;
        let categories = getCheckedCategories();
        let types = getCheckedTypes();
        let star1 = getRatingLeft();
        let star2 = getRatingRight();
        let nameFlag = getNameFlag();
        let commentAmount = getCommentAmount();
        let commentDate = getCommentDate();
        let updateDate = getUpdateDate();
        let order = getOrder();

        if(!(isEmpty(name) && isEmpty(categories) && isEmpty(types) && isEmpty(star1) && isEmpty(star2) && isEmpty(commentAmount) && isEmpty(commentDate) && isEmpty(updateDate))) {
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + name + '&categories=' + categories + '&types=' + types + '&starsLeft=' + star1 + '&starsRight=' + star2 + '&nameFlag=' + nameFlag +'&commentAmount=' + commentAmount +'&lastComment=' + commentDate +'&lastUpdate=' + updateDate +'&order=' + order + '&page=1';
        }

    }

    function sortFrameworks(){
        let name ="";
        let categories="";
        let types="";
        let star1="";
        let star2="";
        let commentAmount="";
        let dateComment="";
        let dateUpdate="";
        let nameFlag= getNameFlag();
        let order = getOrder();

        <c:if test="${not empty techNameQuery}">
            name = "${techNameQuery}";
        </c:if>

        <c:if test="${not empty categoriesQuery}">
            <c:forEach items="${categoriesQuery}" var="element">
                categories = categories.concat('${element},');
            </c:forEach>
            categories = categories.substring(0,categories.length-1);
        <%--categories = parseListToString(${categoriesQuery});--%>
        </c:if>

        <c:if test="${not empty typesQuery}">
            <c:forEach items="${typesQuery}" var="element">
                types = types.concat('${element},');
            </c:forEach>
            types = types.substring(0,types.length-1);
        </c:if>

        <c:if test="${not empty starsQuery1}">
            star1 =  ${starsQuery1};
        </c:if>

        <c:if test="${not empty starsQuery2}">
            star2 =  ${starsQuery2};
        </c:if>

        <c:if test="${not empty commentAmount}">
            commentAmount =  ${commentAmount};
        </c:if>

        <c:if test="${not empty dateComment}">
        dateComment =  ${dateComment};
        </c:if>

        <c:if test="${not empty dateUpdate}">
        dateUpdate =  ${dateUpdate};
        </c:if>

        <c:if test="${fn:length(matchingFrameworks) > 1}">
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + name + '&categories=' + categories + '&types=' + types + '&starsLeft=' + star1 + '&starsRight=' + star2 + '&nameFlag=' + nameFlag + '&commentAmount=' +commentAmount +'&lastComment=' + dateComment +'&lastUpdate=' + dateUpdate+ '&order=' + order + '&page=${page}';
        </c:if>
    }
    if( document.getElementById("search") != null ) {
        form = document.getElementById("search").addEventListener('submit', e => {
            e.preventDefault();
            searchFrameworks(0);
        });
    }

    function showMore(element){
        document.getElementById("hidden"+element).style.display = "block";
        document.getElementById("showMore"+element).style.display = "none";
        document.getElementById("showLess"+element).style.display = "block";
    }

    function showLess(element){
        document.getElementById("hidden"+element).style.display = "none";
        document.getElementById("showMore"+element).style.display = "block";
        document.getElementById("showLess"+element).style.display = "none";
    }
    <%--function moveToPage(goingPage){--%>
    <%--    console.log(${page} ${searchResultsNumber} ${page_size});--%>
    <%--    let url = "<c:url value="/search?toSearch="/>" ;--%>
    <%--    <c:if test="${not empty techNameQuery}">--%>
    <%--    url = url + ${techNameQuery};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty categoriesQuery}">--%>
    <%--    url = url + '&categories=' + ${categoriesQuery};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty typesQuery}">--%>
    <%--    url = url + '&types=' + ${typesQuery};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty starsQuery1}">--%>
    <%--    url = url + '&starsLeft=' + ${starsQuery1};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty starsQuery2}">--%>
    <%--    url = url + '&starsRight=' + ${starsQuery2};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty nameFlagQuery}">--%>
    <%--    url = url + '&nameFlag=' + ${nameFlagQuery};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty commentAmount}">--%>
    <%--    url = url +'&commentAmount=' + ${commentAmount};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty dateComment}">--%>
    <%--    url = url +'&lastComment=' + ${dateComment};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty dateUpdate}">--%>
    <%--    url = url +'&lastUpdate=' + ${dateUpdate};--%>
    <%--    </c:if>--%>
    <%--    <c:if test="${not empty orderValue}">--%>
    <%--    url = url +'&order=' + ${orderValue};--%>
    <%--    </c:if>--%>
    <%--    url = url +'&page='+goingPage;--%>
    <%--    window.location.href = url;--%>

    <%--}--%>


    function getCheckedTypes(){

        let queryTypes = "";

        <c:forEach items="${types}" var="type" >

        if(document.getElementById('check'+'${type}').checked) {
            queryTypes = queryTypes.concat('${type},');
        }
        </c:forEach>

        queryTypes = queryTypes.substring(0,queryTypes.length-1);
        return queryTypes;
    }

    function getCheckedCategories(){
        let queryCategories = "";

        <c:forEach items="${categories}" var="category" >

        if(document.getElementById('check'+'${category}').checked) {
            queryCategories = queryCategories.concat('${category},');
        }
        </c:forEach>

        queryCategories = queryCategories.substring(0,queryCategories.length-1);
        return queryCategories;
    }

    function getRatingLeft(){

        let left = document.getElementById("stars-dropdown-1").value;
        return left;
    }
    function getRatingRight(){

        let right = document.getElementById("stars-dropdown-2").value;

        return right;
    }
    function getCommentAmount(){

        let amount = document.getElementById("comments-dropdown").value;
        console.log(amount)
        return amount;
    }
    function getCommentDate(){
        let date = document.getElementById("comments-date-dropdown").value;
        console.log(date)
        return date;
    }
    function getUpdateDate(){
        let date = document.getElementById("update-date-dropdown").value;
        console.log(date)
        return date;
    }

    function getNameFlag(){
        return document.getElementById("searchOnlyByName").checked;
    }

    function getOrder() {
        return document.getElementById("sortSelect").value * document.getElementById("orderSelect").value;
    }

    function parseListToString(list){
        let string = ""
        <c:forEach items="list" var="element">
            string = string.concat('${element},');
        </c:forEach>
        string = string.substring(0,string.length-1);
        return string;
    }

    function setDropDownValueLeft(value){
        $(document).getElementById("stars-dropdown-1").value(value);
    }
    function setDropDownValueRight(value){
        $(document).getElementById("stars-dropdown-2").value(value);
    }

    function getDateArgs(value){
        let string=""
        <c:if test="value==1">
            string=<spring:message code="explore.last_days"></spring:message>
        </c:if>
        <c:if test="value == 2">
            string=<spring:message code="explore.last_week"></spring:message>
        </c:if>
        <c:if test="value==3">
            string=<spring:message code="explore.last_month"></spring:message>
        </c:if>
        <c:if test="value == 4">
            string=<spring:message code="explore.last_months"></spring:message>
        </c:if>
        <c:if test="value == 5">
                string=<spring:message code="explore.last_year"></spring:message>
        </c:if>
        return string;
    }


</script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha384-xBuQ/xzmlsLoJpyjoggmTEz8OWUFM0/RC5BsqQBDX2v5cMvDHcMakNTNrHIW2I5f" crossorigin="anonymous"></script>
<%--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script
        src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
