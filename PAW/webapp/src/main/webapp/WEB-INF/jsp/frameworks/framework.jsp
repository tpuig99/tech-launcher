<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <title>
            <spring:message code="tech.wref"
                            arguments="${framework.name}"
                            htmlEscape="true"/>
        </title>

        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/framework.css"/>"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>
    <body>
        <div>

            <jsp:include page="../components/navbar.jsp">
                <jsp:param name="connected" value="${user.authenticated}"/>
                <jsp:param name="username" value="${user.name}"/>
                <jsp:param name="isMod" value="${user_isMod}"/>
            </jsp:include>
            <jsp:include page="../components/sidebar.jsp"/>

            <div class="content">

                <div class="container d-flex">
                    <div class="row">
                        <div class="col-2">
                            <div class="max-logo"><img src="${framework.logo}" alt="${framework.name} logo"></div>
                        </div>
                        <div class="col-10">
                            <div class="row">
                                <div class="col">
                                <span class="framework-title"><h2>${framework.name}</h2></span>
                                    <span class="badge badge-pill secondary-badge" data-toggle="tooltip" title="Category"> ${framework.category}</span>
                                    <span class="badge badge-pill secondary-badge" data-toggle="tooltip" title="Type"> ${framework.type}</span>
                                </div>
                                <div class="col d-flex align-items-center justify-content-center">
                                <span class="fa fa-star color-star"></span>
                                <span> ${framework.starsFormated} | ${framework.votesCant}</span>
                                    <span class="fa fa-user"></span>
                                </div>
                            </div>
                            <div class="description">
                                ${framework.description}
                            </div>
                        </div>
                        <div class="margin-top margin-left justify">${framework.introduction}</div>
                    </div>
                </div>


                <!-- Content -->
                <div class="container">
                    <div><h4 class="title"><spring:message code="tech.content"/></h4></div>
                    <div class="d-flex justify-content-end">
                        <c:choose>
                            <c:when test="${user.name != 'anonymousUser'}">
                                <c:choose>
                                    <c:when test="${!isEnable}">
                                        <button class="btn primary-button" type="button" data-toggle="modal" data-target="#confirmMailModal">
                                            <spring:message code="tech.content.button"/>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn primary-button" type="button" data-toggle="modal" data-target="#addContentModal">
                                            <spring:message code="tech.content.button"/>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                    <button class="btn primary-button" type="button" data-toggle="modal" data-target="#loginModal">
                                        <spring:message code="tech.content.button"/>
                                    </button>
                            </c:otherwise>
                        </c:choose>
                   </div>
                </div>
                <c:if test="${empty books && empty courses && empty tutorials}">
                    <div class="d-flex align-items-center justify-content-center"><spring:message code="tech.content.not_available"/></div>
                </c:if>

                <!-- Bibliography -->
                <c:if test="${not empty books}">
                <div>
                   <span><h4 class="subtitle margin-left"><spring:message code="tech.content.bibliography"/></h4></span>
                    <ul class="margin-bottom list-group margin-left list-group-flush description">
                        <c:forEach var="book" items="${books}">
                        <li class="list-group-item">
                            <div class="row">
                                <div class="col-10">
                                    <a target="_blank" href="${book.link}">${book.title}</a>
                                </div>
<%--                                <c:if test="${isAdmin || verifyForFramework}">--%>
                                    <div class="col d-flex justify-content-end align-items-end">
                                        <button class="btn btn-link" onclick="openDeleteContentModal(${book.contentId})" data-toggle="modal" data-target="#deleteContentModal"><i class="fa fa-trash"></i></button>
                                    </div>
<%--                                </c:if>--%>
                            </div>
                        </li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Courses -->
                <c:if test="${not empty courses}">
                <div>
                    <h4 class="subtitle margin-left "><spring:message code="tech.content.courses"/></h4>

                    <ul class=" margin-bottom list-group margin-left list-group-flush description">

                        <c:forEach var="course" items="${courses}">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-10">
                                        <a target="_blank" href="${course.link}">${course.title}</a>
                                    </div>
                                    <c:if test="${isAdmin || verifyForFramework}">
                                        <div class="col d-flex justify-content-end align-items-end">
                                            <button class="btn btn-link" onclick="openDeleteContentModal(${course.contentId})" data-toggle="modal" data-target="#deleteContentModal"><i class="fa fa-trash"></i></button>
                                        </div>
                                    </c:if>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Tutorials -->
                <c:if test="${not empty tutorials}">
                <div>
                   <h4 class="subtitle margin-left"><spring:message code="tech.content.tutorials"/></h4>
                    <ul class="  margin-bottom list-group margin-left list-group-flush description">
                        <c:forEach var="tutorial" items="${tutorials}">

                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-10">
                                        <a target="_blank" href="${tutorial.link}">${tutorial.title}</a>
                                    </div>
                                    <c:if test="${isAdmin || verifyForFramework}">
                                        <div class="col d-flex justify-content-end align-items-end">
                                            <button class="btn btn-link" onclick="openDeleteContentModal(${tutorial.contentId})" data-toggle="modal" data-target="#deleteContentModal"><i class="fa fa-trash"></i></button>
                                        </div>
                                    </c:if>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>


                <!-- Comments -->
                <c:if test="${not empty comments}">
                <div>
                    <h4 class="title"><spring:message code="tech.comments"/></h4>
                </div>

                <div class="container d-flex">
                    <c:forEach var="comment" items="${comments}" varStatus="loop">
                    <div class="row margin-left margin-bottom">

                        <div class="col-1">
                            <div>
                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <c:choose>
                                            <c:when test="${!isEnable}">
                                                <button class=" btn upVote btn-link" data-toggle="modal" data-target="#confirmMailModal">
                                                    <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <form:form modelAttribute="upVoteForm" id="upVoteForm${comment.commentId}" action="/upvote" method="post">
                                                    <form:label path="frameworkId"><form:input id="upVoteFormFrameworkId${comment.commentId}" class="input-wrap" path="frameworkId" type="hidden" value="${framework.id}"/></form:label>
                                                    <form:label path="commentId"><form:input id="upVoteFormCommentId${comment.commentId}" class="input-wrap" path="commentId" type="hidden" value="${comment.commentId}"/></form:label>

                                                    <button class="btn upVote btn-link" type="submit">
                                                        <c:choose>
                                                            <c:when test="${comment.hasUserAuthVote() && comment.userAuthVote > 0}">
                                                                <i class="fa fa-arrow-up arrow votedUp"> ${comment.votesUp}</i>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </button>
                                                </form:form>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <button class=" btn upVote btn-link" data-toggle="modal" data-target="#loginModal">
                                            <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <c:choose>
                                            <c:when test="${!isEnable}">
                                                <button class=" btn downVote btn-link" data-toggle="modal" data-target="#confirmMailModal">
                                                    <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <form:form modelAttribute="downVoteForm" id="downVoteForm${comment.commentId}" action="/downvote" method="post">
                                                    <form:label path="downVoteFrameworkId"><form:input id="downVoteFormFrameworkId${comment.commentId}" class="input-wrap" path="downVoteFrameworkId" type="hidden" value="${framework.id}"/></form:label>
                                                    <form:label path="downVoteCommentId"><form:input id="downVoteFormCommentId${comment.commentId}" class="input-wrap" path="downVoteCommentId" type="hidden" value="${comment.commentId}"/></form:label>

                                                    <button class=" btn upVote btn-link" type="submit">
                                                        <c:choose>
                                                            <c:when test="${comment.hasUserAuthVote() && comment.userAuthVote < 0}">
                                                                <i class="fa fa-arrow-down arrow votedDown"> ${comment.votesDown}</i>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </button>
                                                </form:form>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <button class=" btn downVote btn-link" data-toggle="modal" data-target="#loginModal">
                                            <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col">
                            <div class="row">
                                <div class="col secondary-font">
                                    <a href="<c:url value='/users/${comment.userName}'/>">
                                        <c:choose>
                                            <c:when test="${comment.admin}">
                                                <i class="ml-2 mt-2 fas fa-rocket fa-sm rocket-color-admin" data-toggle="tooltip" title="<spring:message code="tooltip.admin"/>"></i>
                                            </c:when>
                                            <c:when test="${comment.verify}">
                                                <i class="ml-2 mt-2 fas fa-rocket fa-sm rocket-color" data-toggle="tooltip" title="<spring:message code="tooltip.moderator"/>"></i>
                                            </c:when>
                                        </c:choose>
                                        <c:out value="${comment.userName}" default=""/>
                                    </a>
                                </div>
                                <div class="col third-font d-flex justify-content-flex-end">
                                    <c:out value="${comment.timestamp.toLocaleString()}" default=""/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <c:out value="${comment.description}" default=""/>
                                </div>
                            </div>
                            <div class="row padding-bottom">
                                <span>
                                    <c:choose>
                                        <c:when test="${user.name != 'anonymousUser'}">
                                            <c:choose>
                                                <c:when test="${!isEnable}">
                                                    <button type="button" class="btn btn-light" data-toggle="modal" data-target="#confirmMailModal">
                                                        <i class="arrow fas fa-comment-alt fa-xs"></i><span class="reply padding-left"><spring:message code="tech.comment.reply.button"/></span>
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-light" data-toggle="collapse" data-target="#${comment.commentId}" aria-expanded="false" aria-controls="multiCollapseExample2">
                                                       <i class="arrow fas fa-comment-alt fa-xs"></i><span class="reply padding-left"><spring:message code="tech.comment.reply.button"/></span>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="btn btn-light" data-toggle="modal" data-target="#loginModal">
                                               <i class="arrow fas fa-comment-alt fa-xs"></i><span class="reply padding-left"><spring:message code="tech.comment.reply.button"/></span>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="padding-left">
                                    <button type="button" class="btn btn-light" data-toggle="collapse" data-target="#${comment.commentId}See" aria-expanded="false" aria-controls="multiCollapseExample1">
                                        <i class="arrow fas fa-eye fa-xs"></i><span class="reply padding-left"><spring:message code="tech.comment.see_replies"/></span>
                                    </button>
                                </span>

                               <c:if test="${isAdmin || verifyForFramework}">
                                    <span class="col d-flex justify-content-end align-items-end">
                                        <button class="btn btn-link" onclick="openDeleteCommentModal(${comment.commentId})"  data-toggle="modal" data-target="#deleteCommentModal"><i class="fa fa-trash"></i></button>
                                    </span>
                               </c:if>
                            </div>

                            <div  class="collapse multi-collapse" id="${comment.commentId}See">
                                <c:if test="${empty replies.get(comment.commentId)}">
                                    <div><spring:message code="tech.comment.no_replies_yet"/></div>
                                </c:if>
                                <c:if test="${not empty replies.get(comment.commentId)}">
                                    <c:forEach var="reply" items="${replies.get(comment.commentId)}" varStatus="loop">
                                    <div class="row margin-left">
                                        <div class="row d-flex align-items-center ">
                                            <div class="vertical-divider margin-left">
                                                <div class="padding-left">
                                                    <span class="secondary-font medium-font ">
                                                        <c:out value="${reply.userName}" default=""/>
                                                    </span>
                                                    <span class="third-font">
                                                        <c:out value="${reply.timestamp.toLocaleString()}" default=""/>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row  medium-font">
                                            <div class="vertical-divider margin-left">
                                                <div class="padding-left">
                                                    <c:out value="${reply.description}" default=""/>
                                                </div>
                                            </div>
                                        </div>
                                    </div><div class="row padding-bottom"></div>
                                    </c:forEach>
                                </c:if>

                            </div>
                            <div class="row collapse multi-collapse" id="${comment.commentId}">
                                <form:form modelAttribute="replyForm" id="replyForm${comment.commentId}" action="/reply" method="post">
                                    <form:label path="replyFrameworkId"><form:input  class="input-wrap" path="replyFrameworkId" type="hidden" value="${framework.id}"/></form:label>
                                    <form:label path="replyCommentId"><form:input  class="input-wrap" path="replyCommentId" type="hidden" value="${comment.commentId}"/></form:label>

                                    <div>

                                    <form:label path="replyContent"/>
                                    <form:textarea path="replyContent" id="${comment.commentId}ReplyInput" class="form-control" aria-label="CommentReply"/>
                                </div>
                                <div>
                                    <button class="btn primary-button btn-sm padding-top" type="submit"><spring:message code="button.submit"/></button>
                                </div>
                                </form:form>

                            </div>
                        </div>

                    </div>


                    </c:forEach>
                </div>

                </c:if>

                <!-- User Interaction -->
                <div>
                    <h4 class="title"><spring:message code="tech.interactions.title"/></h4>
                </div>


                <div class="margin-left">
                    <div class="row">
                        <div class="col-8">
                            <h5><spring:message code="tech.interactions.leave_comment"/></h5>
                            <div>
                                <form:form modelAttribute="commentForm" action="/comment" method="post">
                                    <form:label path="commentFrameworkId"><form:input  class="input-wrap" path="commentFrameworkId" type="hidden" value="${framework.id}"/></form:label>

                                    <form:label path="content"/>
                                    <form:textarea path="content" id="commentInput" class="form-control" aria-label="With textarea"/>

                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <c:choose>
                                            <c:when test="${!isEnable}">
                                                <button type="button" id="commentButton" disabled class="btn primary-button margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#confirmMailModal"><spring:message code="button.submit"/></button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit" id="commentButton" disabled class="btn primary-button margin-top d-flex justify-content-flex-end"><spring:message code="button.submit"/></button>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" id="commentButton" disabled class="btn primary-button margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#loginModal"><spring:message code="button.submit"/></button>
                                    </c:otherwise>
                                </c:choose>
                                </form:form>
                            </div>
                        </div>
                        <div class="col">
                            <h5><spring:message code="tech.rating"/></h5>

                            <div class="stars">
                               <jsp:include page="ratingForm.jsp">
                                   <jsp:param name="frameworkId" value="${framework.id}" />
                                   <jsp:param name="isEnable" value="${isEnable}" />
                                   <jsp:param name="username" value="${user.name}" />
                               </jsp:include>
                            </div>
                        </div>
                    </div>
                    <c:if test="${!verifyForFramework && !isAdmin && user.name != 'anonymousUser' && isEnable}">
                    <div class="d-flex justify-content-center align-items-center">
                        <div class="card text-center">
                            <div class="card-header subtitle"><h5><spring:message code="tech.apply.title"/></h5></div>
                            <div class="card-body">
                                <p class="card-text"><spring:message code="tech.apply.message"/></p>
                            </div>
                            <div class="card-footer">
                                <button class="btn primary-button" onclick="applyForMod()"><spring:message code="tech.apply.button"/></button>
                            </div>
                        </div>
                    </div>
                    </c:if>
                </div>

                <!-- Competition Cards -->
                <c:if test="${not empty competitors}">
                <div>
                    <h4 class="title"><spring:message code="tech.competition"/></h4>
                </div>


                <div class="container d-flex">
                    <c:forEach items="${competitors}" var="competitor">
                    <div class="card mini-card mx-3 mb-4">
                        <a href="<c:url value="/${competitor.frameCategory}/${competitor.id}"/>">
                            <div class="card-body d-flex align-items-center justify-content-center">
                                <div class="mini-logo d-flex align-items-center justify-content-center"><img src="${competitor.logo}" alt="${framework.name} logo"></div>
                            </div>
                            <div class="card-footer text-dark">${competitor.name}</div>
                        </a>
                    </div>

                    </c:forEach>
                </div>

                </c:if>
                
                <!-- Modals -->

                <!--Login Modal -->

                <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="loginModalLabel"><spring:message code="user.not_logged"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row d-flex justify-content-center align-items-center">
                                <img src="<c:url value="/resources/assets/logo.png"/>" width="60" height="60" class="d-inline-block align-top" alt="Tech Launcher Logo">
                                </div>
                               <div class="row justify-content-center align-items-center margin-top">
                                    <button type="button" class="btn primary-button" onclick="window.location.href = '<c:url value="/login"/>'"><spring:message code="button.login"/></button>
                                </div>
                                <div class="row  justify-content-center align-items-center margin-top">
                                    <div><spring:message code="login.sign_up_question"/> <a href="<c:url value="/register"/>"><spring:message code="button.sign_up"/></a>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <!-- Confirm mail Modal -->
                <div class="modal fade" id="confirmMailModal" tabindex="-1" role="dialog" aria-labelledby="confirmMailModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="confirmMailModalLabel"><spring:message code="register.error.email_status"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row d-flex justify-content-center align-items-center">
                                    <img src="<c:url value="/resources/assets/logo.png"/>" width="60" height="60" class="d-inline-block align-top" alt="Tech Launcher Logo">
                                </div>
                                <div class="row justify-content-center align-items-center margin-top">
                                    <div><spring:message code="register.error.confirm_email"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Modal -->
                <div class="modal fade" id="addContentModal" tabindex="-1" role="dialog" aria-labelledby="addContentModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header container">

                                <h5 class="modal-title" id="addContentLabel"><spring:message code="tech.content.form"/></h5>

                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                </button>
                                    <span aria-hidden="true">&times;</span>
                            </div>
                            <div class="modal-body container">
                                <jsp:include page="contentForm.jsp">
                                    <jsp:param name="frameworkId" value="${framework.id}" />
                                </jsp:include>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="snackbar"><spring:message code="tech.content.uploaded_content"/></div>

                <div id="snackbarModApplication"><spring:message code="tech.content.pending_approval"/></div>

                <!--Delete Content Modal -->

                <div class="modal fade" id="deleteContentModal" tabindex="-1" role="dialog" aria-labelledby="deleteContentModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteContentModalLabel"><spring:message code="tech.content.delete"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row  justify-content-center align-items-center margin-top">
                                    <div><spring:message code="tech.content.delete.message"/></div>
                                </div>
                                <div class="row justify-content-center align-items-center margin-top">
                                    <span><button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close"><spring:message code="button.cancel"/></button></span>

                                    <form:form modelAttribute="deleteContentForm" action="/content/delete" method="post">
                                        <form:label path="deleteContentFrameworkId"><form:input  class="input-wrap" path="deleteContentFrameworkId" type="hidden" value="${framework.id}"/></form:label>
                                        <form:label path="deleteContentId"><form:input  class="input-wrap" path="deleteContentId" type="hidden" id="contentIdDeleteInput"/></form:label>
                                        <span class="margin-left"> <button type="submit" class="btn btn-danger"><spring:message code="button.delete"/></button></span>
                                    </form:form>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Delete Comment Modal -->

                <div class="modal fade" id="deleteCommentModal" tabindex="-1" role="dialog" aria-labelledby="deleteCommentModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteCommentModalLabel"><spring:message code="tech.comment.delete"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row  justify-content-center align-items-center margin-top">
                                    <div><spring:message code="tech.comment.delete.message"/></div>
                                </div>
                                <div class="row justify-content-center align-items-center margin-top">
                                    <span><button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close"><spring:message code="button.cancel"/></button></span>

                                    <form:form modelAttribute="deleteCommentForm" action="/comment/delete" method="post">
                                        <form:label path="commentDeleteFrameworkId"><form:input  class="input-wrap" path="commentDeleteFrameworkId" type="hidden" value="${framework.id}"/></form:label>
                                        <form:label path="commentDeleteId"><form:input  class="input-wrap" path="commentDeleteId" type="hidden" id="commentIdDeleteInput"/></form:label>
                                        <span class="margin-left"> <button type="submit" class="btn btn-danger"><spring:message code="button.delete"/></button></span>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Scripts -->
                <script>
                    $(window).on('load', function (){
                        <c:if test="${user.name != 'anonymousUser' && stars > 0}">
                            let starId = "star-" + ${stars};
                            $('#' + starId).prop("checked",true);
                        </c:if>
                    })

                    <c:if test="${not empty contentFormError}">
                        $(window).on('load', function() {
                            console.log(${contentFormError});
                            if(${contentFormError}) {
                                $('#addContentModal').modal('show');
                            }else{
                                showSnackbar();
                            }
                        });
                    </c:if>


                    $(document).ready(function(){
                        $('[data-toggle="tooltip"]').tooltip();
                    });



                    $(document).ready(function () {
                        $('#commentInput').on('keyup', function () {
                            if ($.trim($('#commentInput').val()).length < 1) {
                                $("#commentButton").prop("disabled",true);
                            } else {
                                $("#commentButton").prop("disabled",false);
                            }
                        });
                    });

                    $(document).ready(function () {
                        $('#commentInput').change(function () {
                            if ($.trim($('#commentInput').val()).length < 1) {
                                $("#commentButton").prop("disabled",true);
                            } else {
                                $("#commentButton").prop("disabled",false);
                            }
                        });
                    });

                    function applyForMod(){
                        let x = document.getElementById("snackbarModApplication");
                        window.location.href = '<c:url value="/apply"/>?id=' + ${framework.id};
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);


                    }

                    function selectFiles() {
                        let el = document.getElementById("fileElem");
                        if (el) {
                            el.click();
                        }
                    }

                    function uploadContent(){
                        let id= ${framework.id};
                        window.location.href = '<c:url value="/content" />?id=' + id;
                    }

                    function openDeleteContentModal(contentId){

                        $('#contentIdDeleteInput').val(contentId);
                        $('#deleteContentModal').modal('show');
                    }


                    function showSnackbar() {
                        console.log("En snackbar");
                        let x = document.getElementById("snackbar");
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
                    }

                    function openDeleteCommentModal(commentId){
                        $('#commentIdDeleteInput').val(commentId);

                        $('#deleteCommentModal').modal('show');
                    }

                </script>


                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>


    </body>
</html>
