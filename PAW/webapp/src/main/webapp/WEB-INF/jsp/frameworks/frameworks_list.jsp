<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>
        Tech Launcher - Explore
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

        <div class="subtitle"><h4>Categories </h4></div>
            <c:forEach items="${categories}" var="category" begin="1" end="5">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="check${category}">
                    <label class="form-check-label" for="check${category}">
                            ${category}
                    </label>
                </div>
            </c:forEach>
            <div>
                <button id="showMoreCategories" class="btn btn-link" onclick="showMore('Categories')"> Show More </button>
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
                    <button id="showLessCategories" class="btn btn-link" onclick="showLess('Categories')"> Show Less </button>
                </div>
            </div>


        <div class="subtitle"> <h4>Types </h4></div>
        <c:forEach items="${types}" var="type" begin="1" end="5">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="check${type}">
                <label class="form-check-label" for="check${type}">
                        ${type}
                </label>
            </div>
        </c:forEach>
        <div>
            <button id="showMoreTypes" class="btn btn-link" onclick="showMore('Types')"> Show More </button>
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
                <button id="showLessTypes" class="btn btn-link" onclick="showLess('Types')"> Show Less </button>
            </div>
        </div>

        <div class="subtitle"><h4>Rating</h4></div>
        <span>
        <input type="text" class="my-form-inline" placeholder="From" aria-label="From" aria-describedby="basic-addon1">
        </span>
        <span>
        <input type="text" class="my-form-inline" placeholder="To" aria-label="To" aria-describedby="basic-addon1">
        </span>



    </div>

    <div class="search-bar">
        <form class="form-inline my-2 my-lg-0" method="post" onsubmit="searchFrameworks()" id="search">
            <input id="searchInput" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" size="80">
            <button class="btn my-2 my-sm-0 primary-button" type="button" onclick="searchFrameworks()">SEARCH</button>
        </form>
    </div>
    <div class="page-description"></div>
    <div class="page-title">
        <h2>Search Results for: ${search_result}</h2>
    </div>
    <div class="page-description"></div>

    <c:choose>
        <c:when test="${matchingFrameworks.size() == 0 }">
            <div>
                We are sorry, there were no results matching "${search_result}", but you can try again some other day, as we are continuously adding content!
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
                            <div class="card-footer text-dark">${framework.name}</div>
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

    function searchFrameworks() {
        let input = document.getElementById("searchInput").value
        let category = document.getElementById("categories").value
        let type = document.getElementById("types").value

        if( !(isEmpty(input) && isEmpty(category) && isEmpty(type)) ) {
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + input + '&category=' + category + '&type=' + type;
            return;
        }
        window.location.reload();
    }

    form = document.getElementById("search").addEventListener('submit', e => {
        e.preventDefault();
        searchFrameworks();
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

</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
