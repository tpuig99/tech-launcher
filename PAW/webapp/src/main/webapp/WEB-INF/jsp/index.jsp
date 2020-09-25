<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

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

    <jsp:include page="components/navbar-home.jsp">
        <jsp:param name="connected" value="${user}"/>
        <jsp:param name="username" value="${user.name}"/>
        <jsp:param name="isMod" value="${user_isMod}"/>
    </jsp:include>

    <div class="introduction">
        <img class="mb-2" src="<c:url value="/resources/assets/logo.png"/>" alt="Page logo">
        <h1> Welcome to Tech Launcher </h1>
        <h4> Starting a project is hard, we know it. That's why we created Tech Launcher. Here, you will be able to gather information from a large amount of technologies useful in the world of development.</h4>
    </div>

    <div class="d-flex justify-content-center align-items-center">
        <div class="card text-center">
            <div class="card-header title-background-frameworks"><h3 class="card-title">Techs</h3></div>
            <div class="card-body">
                <p class="card-text">Here you can se a wide number of technologies that you may be interested in </p>
            </div>
            <div class="card-footer">
                <a href="<c:url value="/frameworks" />" class="btn btn-homepage">Visit Techs</a>
            </div>
        </div>
    </div>

    <%--<div class="card-deck mx-2 mb-4">

            <div class="card text-center">
                <div class="card-header title-background-forum"><h3 class="card-title">Forum</h3></div>
                <div class="card-body">
                    <p class="card-text">Do you have an specific question? In the Forum section you can ask questions to other users, or reply theirs</p>
                </div>
                <div class="card-footer">
                    <a href="<c:url value="/" />" class="btn btn-homepage align-bottom">Visit Forum</a>
                </div>
            </div>

            <div class="card text-center">
                <div class="card-header title-background-frameworks"><h3 class="card-title">Techs</h3></div>
                <div class="card-body">
                    <p class="card-text">Here you can se a wide number of technologies that you may be interested in </p>
                </div>
                <div class="card-footer">
                    <a href="<c:url value="/frameworks" />" class="btn btn-homepage">Visit Techs</a>
                </div>
            </div>

            <div class="card text-center">
                <div class="card-header title-background-projects"><h3 class="card-title">Projects</h3></div>
                <div class="card-body">
                    <p class="card-text">Have a look at the projects other users are engaged on, give your opinions or even publish your own </p>
                </div>
                <div class="card-footer">
                    <a href="<c:url value="/" />" class="btn btn-homepage align-bottom">Visit Projects</a>
                </div>
            </div>


    </div>--%>


    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
