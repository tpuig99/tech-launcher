<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Error</title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/framework.css"/>"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body class="error-background">
        <jsp:include page="components/navbar.jsp">
            <jsp:param name="connected" value="${user}"/>
            <jsp:param name="username" value="${user.name}"/>
            <jsp:param name="isMod" value="${user_isMod}"/>
        </jsp:include>

        <div>
            <div class="content-no-sidebar">
                <div class="mx-auto d-flex"></div>
                <div class="mx-auto">
                    <div class="d-flex flex-column justify-content-center mx-4 mb-4 mt-4">
                        <div class="mx-auto">
                            <img src="<c:url value="/resources/assets/error_image.png"/>" class="d-block align-top" alt="Error image">
                        </div>
                        <div class="mx-auto mt-4">
                            <h4>There was an error with the requested resource</h4></div>
                        <div class="mx-auto mt-4">
                            <a class="btn primary-button" href="<c:url value="/"/>" role="button">GO BACK TO HOME PAGE</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>
