<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>Tech Launcher - Forum</title>

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


    <div class="content-no-sidebar">

        <div class="title"><h1> Forum </h1></div>
        <div class="d-flex flex-row-reverse">
            <button class="btn btn-info mt-2 mb-4"> ADD POST </button>
        </div>
        <c:forEach items="${posts}" var="post">
            <div class="card mb-3">
                <div class="card-body">
                    <div class="row">
                        <div class="col-1">
                            <button class="btn btn-link pt-0 pb-0"><i class="fa fa-2x fa-angle-up"></i></button>
                            <div>
                                <h4>${post.votesUp - post.votesDown}</h4>
                            </div>
                            <button class="btn btn-link pt-0 pb-0"><i class="fa fa-2x fa-angle-down"></i></button>
                        </div>
                        <div class="col">

                            <div class="row d-flex justify-content-flex-end secondary-color">
                                    ${post.timestamp.toLocaleString()}      By ${post.user.username}
                            </div>
                            <div class="row post-title">
                                <a href="<c:url value='/posts/${post.postId}'/>">
                                    ${post.title}
                                </a>
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

                </div>
            </div>

        </c:forEach>
    </div>



    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
