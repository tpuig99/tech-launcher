<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--NavBar -->

<head>
    <c:url value="/resources/js/search.js" var="searchJs"/>
    <!-- <script src="/resources/js/search.js"></script>-->
</head>
<body>

<nav class="navbar fixed-top navbar-expand-lg">
    <a class="navbar-brand" href="<c:url value="/"/>">
        <img src="<c:url value="/resources/assets/logo.png"/>" width="30" height="30" class="d-inline-block align-top" alt="">
        TECH LAUNCHER
    </a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/"/>">Home</a>
            </li>
        </ul>
        <div class="nav-item dropdown" id="profile-settings">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-user"></i>
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">

                <c:if test="${param.isMod}">
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/mod">Moderate</a>
                </c:if>
                <c:choose>
                    <c:when test="${param.username != 'anonymousUser'}">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/users/${param.username}">Profile</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Log out</a>
                    </c:when>
                    <c:otherwise>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/register">Sign Up</a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}/login">Log in</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
<!--<script type=text/javascript" src="../../../resources/js/search.js"></script>-->
</body>