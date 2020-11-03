<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>Tech Launcher - Post</title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/posts.css"/>"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="connected" value="${user.authenticated}"/>
        <jsp:param name="username" value="${user.name}"/>
        <jsp:param name="isMod" value="${user_isMod}"/>
    </jsp:include>


    <div class="content-no-sidebar-left">
            <div class="row d-flex justify-content-flex-start">
                <div class="col-1 mt-4">
                    <button class="btn btn-link pt-0 pb-0"><i class="fa fa-2x fa-angle-up"></i></button>
                    <div class="ml-3">
                        <h4>${post.votesUp - post.votesDown}</h4>
                    </div>
                    <button class="btn btn-link pt-0 pb-0"><i class="fa fa-2x fa-angle-down"></i></button>
                </div>
                <div class="col">

                    <div class="row d-flex flex-row-reverse secondary-color">
                            ${post.timestamp.toLocaleString()} By ${post.user.username}
                    </div>
                    <div class="row post-title">
                        <h2>${post.title}</h2>
                    </div>
                    <div class="row">

                        <c:forEach items="${post.postTags}" var="tag">
                        <span class="badge badge-color ml-1">
                                ${tag.tagName}
                        </span>
                        </c:forEach>

                    </div>
                </div>
            </div>
            <div class="row mt-4">
                ${post.description}
            </div>

    </div>



    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
