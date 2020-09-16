<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>
        Tech Launcher/Registration
    </title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/register.css"/>"/>
</head>
    <body>

        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="connected" value="${user.authenticated}"/>
            <jsp:param name="username" value="${user.name}"/>
        </jsp:include>

        <div class="content-no-sidebar">
            <div class="page-title">
                Register
            </div>
            <c:if test="${not empty errorMessage}">
                <c:out value="${errorMessage}"/>
            </c:if>
            <div class="page-description"></div>

            <c:url value="/create" var="postPath"/>
            <form:form modelAttribute="registerForm" action="${postPath}" method="post">
                <div class="form-group">
                    <form:label path="username">Username: </form:label>
                    <form:input class="form-control" type="text" path="username" placeholder="myusername"/>
                    <form:errors path="username" cssClass="formError" element="p"/>
                </div>
                <div class="form-group">
                    <form:label path="password">Password: </form:label>
                    <form:input class="form-control" type="password" path="password" placeholder="mypassword"/>
                    <form:errors path="password" cssClass="formError" element="p"/>
                </div>
                <div class="form-group">
                    <form:label path="repeatPassword">Repeat password: </form:label>
                    <form:input class="form-control" type="password" path="repeatPassword"/>
                    <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                    <form:errors cssClass="formError" element="p"/>
                </div>
                <div class="form-group">
                    <form:label path="email">Email: </form:label>
                    <form:input class="form-control" type="email" path="email" placeholder="mygreatemail@gmail.com"/>
                    <form:errors path="email" cssClass="formError" element="p"/>
                </div>
                <div>
                    <input class="btn btn-primary" type="submit" value="Register"/>
                </div>
            </form:form>
        </div>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
