<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>
        Tech Launcher
    </title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/homepage.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
</head>
<body>

<jsp:include page="components/navbar.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
</jsp:include>

<div class="introduction">
    <img class="mb-2" src="../../resources/assets/logo.png" alt="Page logo">
    <h1> <spring:message code="home.title"/> </h1>
    <h4> <spring:message code="home.subtitle"/></h4>
</div>

<div class="card-deck mx-2 mb-4">
    <div class="card text-center">
        <div class="card-header title-background-frameworks"><h3 class="card-title"><spring:message code="home.techs.title"/></h3></div>
        <div class="card-body">
            <p class="card-text"><spring:message code="home.techs.description"/></p>
        </div>
        <div class="card-footer">
            <a href="<c:url value="/techs" />" class="btn btn-homepage"><spring:message code="home.techs.button"/></a>
        </div>
    </div>


    <div class="card text-center">
        <div class="card-header title-background-forum"><h3 class="card-title"><spring:message code="forum.title"/></h3></div>
        <div class="card-body">
            <p class="card-text"><spring:message code="home.forum.description"/></p>
        </div>
        <div class="card-footer">
            <a href="<c:url value="/posts" />" class="btn btn-homepage align-bottom"><spring:message code="home.forum.button"/></a>
        </div>
    </div>

</div>


<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>

