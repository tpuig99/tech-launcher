<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>
        User Profile
    </title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/user_profile.css"/>"/>
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user.authenticated}"/>
    <jsp:param name="username" value="${user.name}"/>
</jsp:include>

<div class="content-no-sidebar">
    <div class="page-title">
        <h2>User Profile</h2>
    </div>
    <div class="page-description"></div>

    <div class="row justify-content-center">
            <div class="well profile">
                <div class="row justify-content-center">
                    <div>
                        <img src="https://picsum.photos/536/354" alt="" class="rounded-circle img-slot">
                        <h2><c:out value="${profile.username}"/></h2>
                        <p><strong>Email: </strong><c:out value="${profile.mail}"/></p>
                        <p><strong>Description: </strong> Hi. I'm <c:out value="${profile.username}"/>. I like cupcakes. </p>
                        <p><strong>Guide of: </strong>
                            <a class="tags" href="<c:url value="/Front_End_Development/22"/>">HTML5</a>
                            <a class="tags" href="<c:url value="/Programming_Languages/269"/>">C</a>
                        </p>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(contents)}"/></strong></h2>
                        <p><small>Uploaded Contents</small></p>
                    </div>
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(comments)}"/></strong></h2>
                        <p><small>Comments</small></p>
                    </div>
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(votes)}"/> </strong></h2>
                        <p><small>Votes Given</small></p>
                    </div>
                </div>
            </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
