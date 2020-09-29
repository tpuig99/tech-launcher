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
</head>
<body>

<jsp:include page="../components/navbar-search.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>

<div class="content-search">
    <div class="sidebar-search">

        <div class="subtitle"><h4><spring:message code="explore.categories"/></h4></div>
            <c:forEach items="${categories}" var="category" begin="0" end="5">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="check${category}">
                    <label class="form-check-label" for="check${category}">
                            ${category}
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
                                ${category}
                        </label>
                    </div>
                </c:forEach>
                <div>
                    <button id="showLessCategories" class="btn btn-link" onclick="showLess('Categories')"><spring:message code="explore.show_less"/></button>
                </div>
            </div>


        <div class="subtitle"> <h4><spring:message code="explore.types"/></h4></div>
        <c:forEach items="${types}" var="type" begin="0" end="5">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="check${type}">
                <label class="form-check-label" for="check${type}">
                        ${type}
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
                            ${type}
                    </label>
                </div>
            </c:forEach>
            <div>
                <button id="showLessTypes" class="btn btn-link" onclick="showLess('Types')"><spring:message code="explore.show_less"/></button>
            </div>
        </div>

        <div class="subtitle"><h4><spring:message code="explore.rating"/></h4></div>

        <span><spring:message code="explore.from"/></span>
        <span>
            <select id="stars-dropdown">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select>
         </span>
        <span><spring:message code="explore.to_5_stars"/></span>

    </div>

    <div class="search-bar">
        <form class="form-inline my-2 my-lg-0" method="post" onsubmit="searchFrameworks(0)" id="search">
            <input id="searchInput" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="search.title"/>" aria-label="Search" size="80">
            <button class="btn my-2 my-sm-0 primary-button" type="button" onclick="searchFrameworks(0)"><spring:message code="search.title"/></button>

        </form>
    </div>
    <div class="page-description"></div>
    <div class="page-title">
        <c:choose>
            <c:when test="${empty techNameQuery and empty starsQuery and empty categoriesQuery and empty typesQuery and empty orderQuery}">
            <h2><spring:message code="explore.title"/></h2>
            </c:when>
            <c:otherwise>
                <h2><spring:message code="explore.search_results"/></h2>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="row">
        <div class="col-10">
            <div class="margin-top">
                <c:if test="${not empty techNameQuery}">
                    <span id="name" class="my-badge-inline badge-pill secondary-badge "><c:out value="${techNameQuery}"/></span>
                </c:if>
                <c:if test="${not empty categoriesQuery}">
                    <c:forEach items="${categoriesQuery}" var="categoryQuery">
                        <span id="name${categoryQuery}" class="my-badge-inline badge-pill secondary-badge "><c:out value="${categoryQuery}"/></span>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty typesQuery}">
                    <c:forEach items="${typesQuery}" var="typeQuery">
                        <span id="name${typeQuery}" class="my-badge-inline badge-pill secondary-badge "><c:out value="${typeQuery}"/></span>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty starsQuery}">
                    <span id="stars" class="my-badge-inline badge-pill secondary-badge ">
                        <spring:message code="explore.stars_query"
                                        arguments="${starsQuery}"
                                        htmlEscape="true"/>
                    </span>
                </c:if>

            </div>
        </div>
        <div class="col">
            <div class="btn-group d-flex justify-content-end margin-top">
                <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <spring:message code="explore.sort_by"/>
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
                    <button class="dropdown-item" type="button" onclick="sortFrameworks(-1)"><spring:message code="explore.rating_high_to_low"/></button>
                    <button class="dropdown-item" type="button" onclick="sortFrameworks(1)"><spring:message code="explore.rating_low_to_high"/></button>
                </div>
            </div>
        </div>
    </div>

    <div class="page-description"></div>

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
                        <a href="<c:url value="/${framework.frameCategory}/${framework.id}"/>">
                            <div class="card-body">
                                <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="${framework.name} logo"></div>
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
        </c:otherwise>
    </c:choose>
</div>

<script>
    function isEmpty( input ){
        for (let i = 0; i < input.length; i++) {
            if(input.charAt(i) !== " " ){
                return false;
            }
        }

        return true;
    }

    function searchFrameworks(order) {
        let name = document.getElementById("searchInput").value;
        console.log(name);
        let categories = getCheckedCategories();
        let types = getCheckedTypes();
        let stars = getRating();
        console.log("<c:url value="/search"/>?" + 'toSearch=' + name + '&categories=' + categories + '&types=' + types + '&stars=' + stars + '&order=' + order);

        if(!(isEmpty(name) && isEmpty(categories) && isEmpty(types) && isEmpty(stars))) {
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + name + '&categories=' + categories + '&types=' + types + '&stars=' + stars + '&order=' + order;
        }

    }

    function sortFrameworks(order){
        let name ="";
        let categories="";
        let types="";
        let stars="";

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

        <c:if test="${not empty starsQuery}">
            stars =  ${starsQuery};
        </c:if>

        <c:if test="${fn:length(matchingFrameworks) > 1}">
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + name + '&categories=' + categories + '&types=' + types + '&stars=' + stars + '&order=' + order;
        </c:if>
    }

    form = document.getElementById("search").addEventListener('submit', e => {
        e.preventDefault();
        searchFrameworks(0);
    })

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



    function getCheckedTypes(){

        let queryTypes = "";

        <c:forEach items="${types}" var="type" >

        if(document.getElementById('check'+'${type}').checked) {
            <%--document.getElementById('badge'+'${type}').style.display = "inline";--%>
            queryTypes = queryTypes.concat('${type},');
        }else{
            <%--document.getElementById('badge'+'${type}').style.display = "none";--%>
        }
        </c:forEach>

        queryTypes = queryTypes.substring(0,queryTypes.length-1);
        return queryTypes;
    }

    function getCheckedCategories(){
        let queryCategories = "";

        <c:forEach items="${categories}" var="category" >

        if(document.getElementById('check'+'${category}').checked) {
            <%--document.getElementById('badge'+'${category}').style.display = "inline";--%>
            queryCategories = queryCategories.concat('${category},');

        }else{
            <%--document.getElementById('badge'+'${category}').style.display = "none";--%>
        }
        </c:forEach>

        queryCategories = queryCategories.substring(0,queryCategories.length-1);
        return queryCategories;
    }

    function getRating(){

        let left = document.getElementById("stars-dropdown").value;
        console.log(left)
 /*
        if(left ==="" && right !==""){
            $("#badgeRating").text(right+' stars');
            // document.getElementById('badgeRating').style.display = "inline";

        }else if(left !=="" && right ===""){
            $("#badgeRating").text(left+' stars');
            // document.getElementById('badgeRating').style.display = "inline";

        }else if(left !=="" && right !==""){
            $("#badgeRating").text(left+' to '+right+' stars');
            // document.getElementById('badgeRating').style.display = "inline";
        }else {
            // document.getElementById('badgeRating').style.display = "none";
        }
*/
        return left;
    }

    function parseListToString(list){
        let string = ""
        <c:forEach items="list" var="element">
            string = string.concat('${element},');
        </c:forEach>
        string = string.substring(0,string.length-1);
        return string;
    }

    function setDropDownValue(value){
        $(document).getElementById("stars-dropdown").value(value);
    }


</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
