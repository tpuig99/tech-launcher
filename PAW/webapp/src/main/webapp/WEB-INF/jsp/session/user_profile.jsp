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
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/framework.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/user_profile.css"/>"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user.authenticated}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>

<div class="content-profile row mx-2">
    <div class="col border-right">
        <div class="sticky-top-row">
            <div class="page-title mb-4">
                <h2>User Profile</h2>
            </div>
            <div>
                <div class="row mx-2 justify-content-center">
                    <div>
                        <div class="row justify-content-end">
                            <c:if test="${profile.username == user.name}">
                                <button class="btn primary-button" type="button" data-toggle="modal" data-target="#editProfileModal">
                                    <i class="far fa-edit fa-sm"></i>
                                </button>
                            </c:if>
                        </div>

                        <img src="https://picsum.photos/536/354" alt="" class="rounded-circle img-slot">
                        <div class="row justify-content-center">
                        <h2><c:out value="${profile.username}"/></h2>
                            <c:choose>
                                <c:when test="${profile.admin}">
                                    <i class="ml-2 mt-2 fas fa-rocket fa-2x rocket-color-admin" data-toggle="tooltip" title="This user is an Admin!"></i>
                                </c:when>
                                <c:when test="${profile.verify}">
                                    <i class="ml-2 mt-2 fas fa-rocket fa-2x rocket-color" data-toggle="tooltip" title="This user is a Moderator!"></i>
                                </c:when>
                            </c:choose>
                        </div>
                        <p><strong>Email: </strong><c:out value="${profile.mail}"/></p>
                        <c:if test="${not empty profile.description}">
                            <p><strong>Description: </strong><c:out value="${profile.description}"/></p>
                        </c:if>
                        <c:if test="${profile.verify}">
                            <p><strong>Moderator: </strong>
                            <c:forEach items="${verifiedList}" var="verifiedTech">
                                <c:if test="${!verifiedTech.pending}">
                                    <a class="tags" href="<c:url value="/Framework/${verifiedTech.frameworkId}"/>">${verifiedTech.frameworkName}</a>
                                </c:if>
                            </c:forEach>
                            </p>
                        </c:if>

                    </div>
                </div>
                <div class="row mx-2 justify-content-center">
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
    <div class="col-8">
        <!-- Comments -->
        <c:if test="${not empty comments}">
            <div class="page-title mb-4 ml-2 text-left">
                <h2>Comments</h2>
            </div>
            <div class="container d-flex justify-content-center">
                <c:forEach var="comment" items="${comments}">
                    <div class="card emphasis emph-comment row mb-2">
                        <div class="card-body row mt-1">
                            <div class="col-3 secondary-font">
                                <a href="<c:url value="/${comment.category}/${comment.frameworkId}"/>">
                                    <c:out value="${comment.frameworkName}" default=""/>
                                </a>
                            </div>
                            <div class="col-6 text-left"> <c:out value="${comment.description}" default=""/> </div>
                            <div class="col third-font text-right"> <c:out value="${comment.timestamp.toLocaleString()}" default=""/> </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Contents -->
        <c:if test="${not empty contents}">
            <div class="page-title mb-4 ml-2 text-left">
                <h2>Contents</h2>
            </div>
            <div class="container d-flex justify-content-center">
                <c:forEach var="content" items="${contents}">
                    <div class="card emphasis emph-content row mb-2">
                        <div class="card-body row mt-1">
                            <div class="col-3 secondary-font">
                                <a href="<c:url value="/Framework/${content.frameworkId}"/>">
                                    <c:out value="${content.frameworkName}" default=""/>
                                </a>
                            </div>
                            <div class="col-6 text-left"> <c:out value="${content.type.name()}: ${content.title}" default=""/> </div>
                            <div class="col third-font text-right"> <c:out value="${content.timestamp.toLocaleString()}" default=""/> </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Votes -->
        <c:if test="${not empty votes}">
            <div class="page-title mb-4 ml-2 text-left">
                <h2>Votes</h2>
            </div>
            <div class="container row equal justify-content-center">
                <c:forEach var="vote" items="${votes}">
                    <div class="card col-4 d-flex emphasis emph-votes mb-2 mx-2">
                        <div class="card-body row mt-1">
                            <div class="col secondary-font">
                                <a href="<c:url value="/${vote.category}/${vote.frameworkId}"/>">
                                    <c:out value="${vote.frameworkName}" default=""/>
                                </a>
                            </div>
                            <div class="col"> <c:out value="${vote.stars} / 5" default=""/> </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>

    <div class="modal fade" id="editProfileModal" tabindex="-1" role="dialog" aria-labelledby="editProfileModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header container">

                    <h5 class="modal-title" id="editProfileLabel">Edit your profile</h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    </button>
                    <span aria-hidden="true">&times;</span>
                </div>
                <div class="modal-body container">
                    <jsp:include page="profileForm.jsp">
                        <jsp:param name="username" value="${profile.username}" />
                        <jsp:param name="description" value="${previousDescription}" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </div>

    <div id="snackbar">Your profile has been updated !</div>
</div>



<script>
    <c:if test="${not empty profileFormError}">
    $(window).on('load', function() {
        if(${profileFormError}) {
            $('#editProfileModal').modal('show');
        }else{
            showSnackbar();
        }
    });
    </c:if>

    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
    });

    function editProfile(){
        window.location.href = '<c:url value="/users/${profile.id}/edit" />';
    }

    function showSnackbar() {
        let x = document.getElementById("snackbar");
        x.className = "show";
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
    }

</script>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
