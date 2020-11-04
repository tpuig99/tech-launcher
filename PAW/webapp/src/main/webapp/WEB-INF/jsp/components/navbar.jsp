<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!--NavBar -->

<head>
    <c:url value="/resources/js/search.js" var="searchJs"/>
    <!-- <script src="/resources/js/search.js"></script>-->
</head>
<body>

<nav class="navbar sticky-top navbar-expand-lg">
    <a class="navbar-brand" href="<c:url value="/"/>">
        <img src="<c:url value="/resources/assets/logo.png"/>" width="30" height="30" class="d-inline-block align-top" alt="">
        <spring:message code="navbar.title"/>
    </a>

    <div class="navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/search"/>"><spring:message code="explore.title"/></a>
            </li>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/techs"/>"><spring:message code="home.techs.title"/></a>
            </li>
        </ul>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/posts"/>"><spring:message code="forum.title"/></a>
            </li>
        </ul>
        <div>
            <c:if test="${!param.search_page}">
                <form class="form-inline my-2 my-lg-0" method="post" onsubmit="searchFrameworks()" id="search">
                    <input id="searchInput" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="search.title"/>" aria-label="Search">
                    <button class="btn my-2 my-sm-0" type="button" onclick="searchFrameworks()"><i class="fas fa-search"></i></button>
                </form>
            </c:if>
        </div>
        <div class="nav-item dropdown" id="profile-settings">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <c:if test="${param.username != 'anonymousUser'}">${param.username}</c:if>
                <i class="fas fa-user"></i>
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">

                <c:if test="${param.isMod}">
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/mod"><spring:message code="button.moderate"/></a>
                </c:if>
                <c:choose>
                    <c:when test="${param.username != 'anonymousUser'}">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/users/${param.username}"><spring:message code="button.profile"/></a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><spring:message code="button.logout"/></a>
                    </c:when>
                    <c:otherwise>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/register"><spring:message code="button.sign_up"/></a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/login"><spring:message code="button.login"/></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

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

        if( !isEmpty(input) ) {
            window.location.href = '<c:url value="/search?toSearch="/>' + input;
            return;
        }
        window.location.reload();
    }
    if( document.getElementById("search") != null ) {
        form = document.getElementById("search").addEventListener('submit', e => {
            e.preventDefault();
            searchFrameworks();
        });
    }
</script>
</body>