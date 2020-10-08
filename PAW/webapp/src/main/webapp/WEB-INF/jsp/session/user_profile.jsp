<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>
        <spring:message code="profile.wref"/>
    </title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
                <h2><spring:message code="profile.title"/></h2>
            </div>
            <div>
                <div class="row mx-2 justify-content-center">
                    <div>
                        <div class="row justify-content-end">
                            <c:if test="${profile.username == user.name}">
                                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#editProfileModal">
                                    <i class="far fa-edit fa-sm"></i>
                                </button>
                            </c:if>
                        </div>

                        <c:choose>
                            <c:when test="${not empty profile.base64image}">
                                <img src="data:${profile.contentType};base64,${profile.base64image}" alt="<spring:message code="image.profile"/>" class="rounded-circle img-slot" />
                            </c:when>
                            <c:otherwise>
                                <img src="https://picsum.photos/536/354" alt="<spring:message code="image.profile.random"/>" class="rounded-circle img-slot">
                            </c:otherwise>
                        </c:choose>

                        <div class="row justify-content-center">
                        <h2><c:out value="${profile.username}"/></h2>
                            <c:choose>
                                <c:when test="${profile.admin}">
                                    <i class="ml-2 mt-2 fas fa-rocket fa-2x rocket-color-admin" data-toggle="tooltip" title="<spring:message code="tooltip.admin"/>"></i>
                                </c:when>
                                <c:when test="${profile.verify}">
                                    <i class="ml-2 mt-2 fas fa-rocket fa-2x rocket-color" data-toggle="tooltip" title="<spring:message code="tooltip.moderator"/>"></i>
                                </c:when>
                            </c:choose>
                        </div>
                        <p><strong><spring:message code="profile.email"/></strong> <c:out value="${profile.mail}"/></p>
                        <c:if test="${not empty profile.description}">
                            <p><strong><spring:message code="profile.description"/></strong> <c:out value="${profile.description}"/></p>
                        </c:if>
                        <c:if test="${user.name == profile.username && (user_isMod || !isAllowMod) && !isAdmin}">
                            <div class="row allow-mod">
                                <strong>Allow moderator: </strong>
                                <label class="switch align-items-end">
                                    <input type="checkbox" id="enableMod" onclick="setModEnable()">
                                    <span class="slider round"></span>
                                </label>
                            </div>
                        </c:if>
                        <c:if test="${profile.verify}">
                            <p><strong><spring:message code="profile.moderator"/></strong>
                            <c:forEach items="${verifiedList}" var="verifiedTech">
                                <c:if test="${!verifiedTech.pending}">
                                    <a class="tags" href="<c:url value="/${verifiedTech.category}/${verifiedTech.frameworkId}"/>">${verifiedTech.frameworkName}</a>
                                </c:if>
                            </c:forEach>
                            </p>
                        </c:if>

                    </div>
                </div>
                <div class="row mx-2 justify-content-center">
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(contents)}"/></strong></h2>
                        <p><small><spring:message code="profile.uploaded_contents"/></small></p>
                    </div>
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(comments)}"/></strong></h2>
                        <p><small><spring:message code="profile.comments"/></small></p>
                    </div>
                    <div class="col-4 emphasis">
                        <h2><strong><c:out value="${fn:length(votes)}"/> </strong></h2>
                        <p><small><spring:message code="profile.votes_given"/></small></p>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <div class="col-8">
        <!-- Comments -->
        <div class="page-title mb-4 ml-2 text-left">
            <h2><spring:message code="profile.comments"/></h2>
        </div>
        <c:choose>
            <c:when test="${not empty comments || comments_page != 1}">
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
                    <ul class="pagination">
                        <c:choose>
                            <c:when test="${comments_page == 1}">
                                <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                                <li class="page-item ">
                            </c:otherwise>
                        </c:choose>
                            <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page-1}&contents_page=${contents_page}&votes_page=${votes_page}&frameworks_page=${frameworks_page}"/>" aria-label="Previous">
                                <span aria-hidden="true">&lsaquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <c:choose>
                            <c:when test="${comments.size() < page_size}">
                                <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                                <li class="page-item">
                            </c:otherwise>
                        </c:choose>
                                <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page+1}&contents_page=${contents_page}&votes_page=${votes_page}&frameworks_page=${frameworks_page}"/>" aria-label="Next">
                                    <span aria-hidden="true">&rsaquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <spring:message code="profile.empty.comments"/>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Contents -->
        <div class="page-title mb-4 ml-2 text-left">
            <h2><spring:message code="profile.contents"/></h2>
        </div>
        <c:choose>
            <c:when test="${not empty contents || contents_page != 1}">
                <div class="container d-flex justify-content-center">
                    <c:forEach var="content" items="${contents}">
                        <div class="card emphasis emph-content row mb-2">
                            <div class="card-body row mt-1">
                                <div class="col-3 secondary-font">
                                    <a href="<c:url value="/${content.category}/${content.frameworkId}"/>">
                                        <c:out value="${content.frameworkName}" default=""/>
                                    </a>
                                </div>

                                <div class="col-6 text-left">
                                    <c:choose>
                                        <c:when test="${content.type.name() == 'course'}"><spring:message code="profile.content.course" /></c:when>
                                        <c:when test="${content.type.name() == 'book'}"><spring:message code="profile.content.bibliography" /></c:when>
                                        <c:when test="${content.type.name() == 'tutorial'}"><spring:message code="profile.content.tutorial" /></c:when>
                                    </c:choose>
                                    <c:out value=" ${content.title}" default=""/>
                                </div>

                                <div class="col third-font text-right"> <c:out value="${content.timestamp.toLocaleString()}" default=""/> </div>
                            </div>
                        </div>
                    </c:forEach>
                    <ul class="pagination">
                        <c:choose>
                        <c:when test="${contents_page == 1}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item ">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page-1}&votes_page=${votes_page}&frameworks_page=${frameworks_page}"/>" aria-label="Previous">
                                <span aria-hidden="true">&lsaquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <c:choose>
                        <c:when test="${contents.size() < page_size}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page + 1}&votes_page=${votes_page}&frameworks_page=${frameworks_page}"/>" aria-label="Next">
                                <span aria-hidden="true">&rsaquo;</span>
                                <span class="sr-only">Next</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <spring:message code="profile.empty.contents"/>
            </c:otherwise>
        </c:choose>

        <!-- Votes -->
        <div class="page-title mb-4 ml-2 text-left">
            <h2><spring:message code="profile.votes"/></h2>
        </div>
        <c:choose>
            <c:when test="${not empty votes || votes_page != 1}">
                <div class="container row equal justify-content-center">
                    <c:forEach var="vote" items="${votes}">
                        <div class="card col-4 d-flex emphasis emph-votes mb-2 mx-2">
                            <div class="card-body row mt-1">
                                <div class="col secondary-font">
                                    <a href="<c:url value="/${vote.category}/${vote.frameworkId}"/>">
                                        <c:out value="${vote.frameworkName}" default=""/>
                                    </a>
                                </div>
                                <div class="col">
                                    <spring:message code="profile.votes_over_5"
                                                    arguments="${vote.stars}"
                                                    htmlEscape="true"
                                    />
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <ul class="pagination justify-content-center">
                    <c:choose>
                    <c:when test="${votes_page == 1}">
                    <li class="page-item disabled">
                        </c:when>
                        <c:otherwise>
                    <li class="page-item ">
                        </c:otherwise>
                        </c:choose>
                        <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page}&votes_page=${votes_page-1}&frameworks_page=${frameworks_page}"/>" aria-label="Previous">
                            <span aria-hidden="true">&lsaquo;</span>
                            <span class="sr-only">Previous</span>
                        </a>
                    </li>
                    <c:choose>
                    <c:when test="${votes.size() < page_size*2}">
                    <li class="page-item disabled">
                        </c:when>
                        <c:otherwise>
                    <li class="page-item">
                        </c:otherwise>
                        </c:choose>
                        <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page}&votes_page=${votes_page+1}&frameworks_page=${frameworks_page}"/>" aria-label="Next">
                            <span aria-hidden="true">&rsaquo;</span>
                            <span class="sr-only">Next</span>
                        </a>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <spring:message code="profile.empty.votes"/>
            </c:otherwise>
        </c:choose>

    <!-- Frameworks -->
    <c:if test="${not empty frameworks || frameworks_page != 1}">
        <div class="page-title mb-4 ml-2 text-left">
            <h2><spring:message code="profile.frameworks"/></h2>
         </div>
            <div class="container row equal justify-content-center">
                <c:forEach var="framework" items="${frameworks}">
                    <div class="card mx-4 mb-4">
                        <a href="<c:url value="/${framework.frameCategory}/${framework.id}"/>">
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty framework.base64image}">
                                        <div class="max-logo d-flex align-items-center justify-content-center"><img src="data:${framework.contentType};base64,${framework.base64image}" alt="<spring:message code="tech.picture"/>"/></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="<spring:message code="tech.picture"/>"></div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="card-footer text-dark">
                                <span>${framework.name} | </span>
                                <span class="fa fa-star fa-sm"></span>
                                <span>${framework.starsFormated}</span>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
            <ul class="pagination justify-content-center">
                <c:choose>
                <c:when test="${frameworks_page == 1}">
                <li class="page-item disabled">
                    </c:when>
                    <c:otherwise>
                <li class="page-item ">
                    </c:otherwise>
                    </c:choose>
                    <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page}&votes_page=${votes_page}&frameworks_page=${frameworks_page-1}"/>" aria-label="Previous">
                        <span aria-hidden="true">&lsaquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
                <c:choose>
                <c:when test="${frameworks.size() < frameworks_page_size}">
                <li class="page-item disabled">
                    </c:when>
                    <c:otherwise>
                <li class="page-item">
                    </c:otherwise>
                    </c:choose>
                    <a class="page-link" href="<c:out value="/users/${username}/pages?comments_page=${comments_page}&contents_page=${contents_page}&votes_page=${votes_page}&frameworks_page=${frameworks_page+1}"/>" aria-label="Next">
                        <span aria-hidden="true">&rsaquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </li>
            </ul>
        </c:if>
    </div>

    <div class="modal fade" id="editProfileModal" tabindex="-1" role="dialog" aria-labelledby="editProfileModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header container">

                    <h5 class="modal-title" id="editProfileLabel"><spring:message code="profile.edit.title"/></h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    </button>
                    <span aria-hidden="true">&times;</span>
                </div>
                <div class="modal-body container">
                    <form id="updatePictureForm" class="border-bottom" action="/users/${username}/upload"  method="post" enctype="multipart/form-data">
                        <div class="mb-2"><spring:message code="profile.change_picture"/></div>
                        <div class="d-flex justify-content-center mb-4">
                            <input id="uploadPictureInput" name="picture" type="file" accept="image/*" />
                        </div>
                        <div class="d-flex justify-content-center mb-4">
                            <input class="btn btn-primary" disabled id="updatePictureButton" type="submit" value="<spring:message code="button.change_picture"/>"/>
                            <div class="btn btn-primary disabled" id="updatePictureLoading" hidden>
                                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                <spring:message code="button.loading"/>
                            </div>
                        </div>
                    </form>
                    <jsp:include page="profileForm.jsp">
                        <jsp:param name="username" value="${profile.username}" />
                        <jsp:param name="description" value="${previousDescription}" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </div>

    <div id="snackbar"><spring:message code="profile.updated"/></div>

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

    function setModEnable(){
        window.location.href = '<c:url value="/user/${username}/"/>' + 'enableMod/' +  $('#enableMod').is(":checked")
    }

    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
        $("#enableMod").prop("checked", ${isAllowMod});
    });

    function editProfile(){
        window.location.href = '<c:url value="/users/${profile.id}/edit" />';
    }

    function showSnackbar() {
        let x = document.getElementById("snackbar");
        x.className = "show";
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
    }

    $(document).ready(function () {
        $('#uploadPictureInput').change(function () {
            $("#updatePictureButton").prop("disabled",false)
        });
    });

    $(document).ready(function() {
        $('#updatePictureForm').on('submit', function(e){
            $("#updatePictureButton").prop("hidden",true);
            $("#updatePictureLoading").prop("hidden",false);
        });
    });

</script>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
