<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>
        Tech Launcher
    </title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
</jsp:include>

<jsp:include page="../components/sidebar.jsp"/>

<div class="content">
    <div class="page-title">
        <h2>Techs</h2>
    </div>
    <div class="page-description">

    </div>

    <div>
        <h4 class="title">Best Rated</h4>

        <div class="row equal">
            <c:forEach items="${hottestList}" var="framework">
                <div class="card mx-4 mb-4">
                    <a href="<c:url value="/${framework.frameCategory}/${framework.id}"/>">
                        <div class="card-body">
                            <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="${framework.logo} logo"></div>
                        </div>
                        <div class="card-footer text-dark">${framework.name}</div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <div>
        <c:if test="${user.name != 'anonymousUser'}">
                <c:if test="${not empty interestsList}">
                    <h4 class="title">Based on your tastes</h4>
                    <div class="row equal">
                        <c:forEach items="${interestsList}" var="framework">
                            <div class="card mx-4 mb-4">
                                <a href="<c:url value="/${framework.frameCategory}/${framework.id}"/>">
                                    <div class="card-body">
                                        <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="${framework.logo} logo"></div>
                                    </div>
                                    <div class="card-footer text-dark">${framework.name}</div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
        </c:if>

    </div>

</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
