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
                        <c:if test="${user.name == profile.username && !isAdmin}">
                            <div class="custom-control custom-checkbox mb-2">
                                <input type="checkbox" class="custom-control-input" onclick="openStopBeingAModModal()" id="modCheckbox">
                                <label class="custom-control-label" for="modCheckbox"><strong><spring:message code="button.moderate"/></strong></label>
                            </div>
                        </c:if>
                        <c:if test="${profile.verify}">
                            <p>
                                    <strong><spring:message code="profile.moderator"/></strong>
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
                    <div class="col-3 emphasis">
                        <h2><strong><c:out value="${contentCount}"/></strong></h2>
                        <p><small><spring:message code="profile.uploaded_contents"/></small></p>
                    </div>
                    <div class="col-3 emphasis">
                        <h2><strong><c:out value="${commentsCount}"/></strong></h2>
                        <p><small><spring:message code="profile.comments"/></small></p>
                    </div>
                    <div class="col-3 emphasis">
                        <h2><strong><c:out value="${votesCount}"/> </strong></h2>
                        <p><small><spring:message code="profile.votes_given"/></small></p>
                    </div>
                    <div class="col-3 emphasis">
                        <h2><strong><c:out value="${frameworksCount}"/> </strong></h2>
                        <p><small><spring:message code="profile.frameworks"/></small></p>
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
            <c:when test="${not empty comments}">
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
                    <!-- Comment pagination -->
                    <jsp:include page="../components/pagination.jsp">
                        <jsp:param name="total" value="${commentsCount}"/>
                        <jsp:param name="page" value="${comments_page}"/>
                        <jsp:param name="page_size" value="${page_size}"/>
                        <jsp:param name="origin" value="profile_comment"/>
                        <jsp:param name="username" value="${username}"/>
                        <jsp:param name="techs_page" value="${frameworks_page}"/>
                        <jsp:param name="contents_page" value="${contents_page}"/>
                        <jsp:param name="votes_page" value="${votes_page}"/>
                    </jsp:include>
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
            <c:when test="${not empty contents}">
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
                                    <spring:message code="profile.content.${content.type.name()}"/>
                                    <c:out value=" ${content.title}" default=""/>
                                </div>

                                <div class="col third-font text-right"> <c:out value="${content.timestamp.toLocaleString()}" default=""/> </div>
                            </div>
                        </div>
                    </c:forEach>
                    <!-- Content pagination -->
                    <jsp:include page="../components/pagination.jsp">
                        <jsp:param name="total" value="${contentCount}"/>
                        <jsp:param name="page" value="${contents_page}"/>
                        <jsp:param name="page_size" value="${page_size}"/>
                        <jsp:param name="origin" value="profile_content"/>
                        <jsp:param name="username" value="${username}"/>
                        <jsp:param name="techs_page" value="${frameworks_page}"/>
                        <jsp:param name="comments_page" value="${comments_page}"/>
                        <jsp:param name="votes_page" value="${votes_page}"/>
                    </jsp:include>
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
            <c:when test="${not empty votes}">
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
                <!-- Votes pagination -->
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="total" value="${votesCount}"/>
                    <jsp:param name="page" value="${votes_page}"/>
                    <jsp:param name="page_size" value="${votes_page_size}"/>
                    <jsp:param name="origin" value="profile_votes"/>
                    <jsp:param name="username" value="${username}"/>
                    <jsp:param name="techs_page" value="${frameworks_page}"/>
                    <jsp:param name="comments_page" value="${comments_page}"/>
                    <jsp:param name="contents_page" value="${contents_page}"/>
                </jsp:include>
            </c:when>
            <c:otherwise>
                <spring:message code="profile.empty.votes"/>
            </c:otherwise>
        </c:choose>

    <!-- Frameworks -->
    <c:if test="${not empty frameworks}">
        <div class="page-title mb-4 ml-2 text-left">
            <h2><spring:message code="profile.frameworks"/></h2>
         </div>
            <div class="container row equal justify-content-center">
                <c:forEach var="framework" items="${frameworks}">
                    <div class="card mx-4 mb-4">
                        <a href="<c:url value="/${framework.category.name()}/${framework.id}"/>">
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
        <!-- Tech pagination -->
        <jsp:include page="../components/pagination.jsp">
            <jsp:param name="total" value="${frameworksCount}"/>
            <jsp:param name="page" value="${frameworks_page}"/>
            <jsp:param name="page_size" value="${frameworks_page_size}"/>
            <jsp:param name="origin" value="profile_techs"/>
            <jsp:param name="username" value="${username}"/>
            <jsp:param name="comments_page" value="${comments_page}"/>
            <jsp:param name="contents_page" value="${contents_page}"/>
            <jsp:param name="votes_page" value="${votes_page}"/>
            <jsp:param name="techs_page" value="${frameworks_page}"/>
        </jsp:include>
        </c:if>
    </div>

    <!--Edit Profile Modal -->

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
                    <!-- Change profile picture-->
                    <c:url value="/users/${username}/upload" var="postPathUploadPhoto"/>
                    <form id="updatePictureForm" class="border-bottom" action="${postPathUploadPhoto}"  method="post" enctype="multipart/form-data">
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
                    <!-- Change description-->
                    <jsp:include page="profileForm.jsp">
                        <jsp:param name="username" value="${profile.username}" />
                        <jsp:param name="description" value="${previousDescription}" />
                    </jsp:include>

                    <!-- Change password -->
                    <div>
                        <span><a href="${pageContext.request.contextPath}/recover/change_password"><spring:message code="profile.change_password"/></a></span>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- Stop being a mod Modal -->

    <div class="modal fade" id="stopBeingAModModal" tabindex="-1" role="dialog" aria-labelledby="stopBeingAModModalLabel"  aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="stopBeingAModModalLabel"><spring:message code="profile.allow_mod"/></h5>
                    <button type="button" class="close" data-dismiss="modal" onclick="checkMod()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row padding-left justify-content-center align-items-center">
                        <div><spring:message code="profile.are_you_sure_stop_mod"/></div>
                        <div><spring:message code="profile.description_stop_mod"/></div>
                    </div>
                    <div class="row justify-content-center align-items-center margin-top">
                        <button type="button" class="btn btn-secondary mr-4" data-dismiss="modal" onclick="checkMod()" aria-label="Close"><spring:message code="button.cancel"/></button>
                        <button class="btn btn-danger ml-4" onclick="stopBeingAMod()"><spring:message code="button.stop_being_a_mod"/></button>
                    </div>
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

    $(document).on('focusout','#stopBeingAModModal', function () {
        checkMod();
    });

    function stopBeingAMod(){
        window.location.href = '<c:url value="/user/${username}/"/>' + 'enableMod/false'
    }

    function enableBeingAMod(){
        window.location.href = '<c:url value="/user/${username}/"/>' + 'enableMod/true'
    }

    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
        $("#modCheckbox").prop("checked", ${isAllowMod});
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

    function openStopBeingAModModal(){
        $('#modCheckbox').on('change', function() {
            // From the other examples
            if (this.checked) {
                enableBeingAMod();
            } else {
                $('#stopBeingAModModal').modal('show');
            }
        });
    }
    function checkMod(){
        $("#modCheckbox").prop("checked", ${isAllowMod});
    }
</script>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
