<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Tech Launcher - Login</title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/register.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>
<jsp:include page="components/navbar.jsp">
    <jsp:param name="connected" value="${user.authenticated}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>

    <div class="content-no-sidebar">
        <div class="page-title">
            Login
        </div>
        <div class="page-description"></div>
        <c:if test="${param.error != null}">
            <div class="formError" id="error">
                The username or password is incorrect.
            </div>
        </c:if>
        <c:url value="/login" var="loginUrl" />
        <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
            <div class="form-group">
                <label for="username">Username: </label>
                <input class="form-control" id="username" name="j_username" type="text" placeholder="myusername"/>
            </div>
            <div class="form-group">
                <label for="password">Password: </label>
                <input class="form-control" id="password" name="j_password" type="password" placeholder="mypassword"/>
            </div>
            <div class="form-group">
                <label><input name="j_rememberme" type="checkbox"/> Remember me</label>
            </div>
            <div class="d-flex justify-content-center">
                <input class="btn primary-button" type="submit" value="LOG IN"/>
            </div>
        </form>
        <div>
            <span>Don't have an account? </span><span><a href="${pageContext.request.contextPath}/register">Sign Up</a></span>
        </div>
    </div>


    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
