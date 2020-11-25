<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="forum.question.title"/></title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/posts.css"/>"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user.authenticated}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>


<!-- Question Section -->
<div class="content-no-sidebar-left">
    <div class="ml-4 mr-1 description-text"><h1><c:out value="${post.title}"/></h1></div>

    <c:if test="${isAdmin || isOwner}">
        <div class="row d-flex my-1 justify-content-end">
            <div class="row d-flex">
                <div class="mb-4 mx-2">
                    <a href="<c:url value="/posts/edit_post/${post.postId}"/>" >
                        <button class="btn btn-info btn-block text-nowrap" type="button">
                            <i class="fa fa-edit fa-sm mr-1"></i>
                            <spring:message code="button.edit_post"/>
                        </button>
                    </a>
                </div>
                <div class="mx-2">
                    <button class="btn btn-danger btn-block text-nowrap" type="button" onclick="openDeletePostModal(${post.postId})" data-toggle="modal" data-target="#deletePostModal">
                        <i class="fas fa-trash-alt fa-sm mr-1"></i>
                        <spring:message code="button.delete_post"/>
                    </button>
                </div>
            </div>
        </div>
    </c:if>
    <div class="post-cards">
        <div class="row post-data">
            <!-- Up Vote - Down Vote section -->
            <div class=" ml-4 col-1 net-votes">
                <c:choose>
                    <c:when test="${user.name == 'anonymousUser'}">
                        <button class="btn pt-0 pb-0"><i class="fa fa-2x fa-arrow-up" data-toggle="modal" data-target="#loginModal"></i></button>
                        <div>
                            <h4>${post.votesUp - post.votesDown}</h4>
                        </div>
                        <button class="btn pt-0 pb-0"><i class="fa fa-2x fa-arrow-down" data-toggle="modal" data-target="#loginModal"></i></button>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${!isEnable}">
                                <button class="btn pt-0 pb-0"><i class="fa fa-2x fa-arrow-up" data-toggle="modal" data-target="#confirmMailModal"></i></button>
                                <div>
                                    <h4>${post.votesUp - post.votesDown}</h4>
                                </div>
                                <button class="btn pt-0 pb-0"><i class="fa fa-2x fa-arrow-down" data-toggle="modal" data-target="#confirmMailModal"></i></button>
                            </c:when>
                            <c:otherwise>
                                <c:url value="/posts/${post.postId}/upVote/" var="postPathUpVote"/>
                                <form:form modelAttribute="upVoteForm" id="upVoteForm${post.postId}" action="${postPathUpVote}" method="post" class="mb-0 mt-0">
                                    <form:label path="upVotePostId">
                                        <form:input id="upVotePostId${post.postId}" class="input-wrap" path="upVotePostId" type="hidden" value="${post.postId}"/>
                                    </form:label>
                                    <div class="net-votes">
                                        <button class="btn pt-0 pb-0" type="submit">
                                            <c:choose>
                                                <c:when test="${post.getUserAuthVote(user.name) > 0}">
                                                    <i class="fa fa-2x fa-arrow-up votedUp"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa fa-2x fa-arrow-up"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                        <div>
                                            <h4>${post.votesUp - post.votesDown}</h4>
                                        </div>
                                    </div>
                                </form:form>
                                <c:url value="/posts/${post.postId}/downVote/" var="postPathDownVote"/>
                                <form:form modelAttribute="downVoteForm" id="downVoteForm${post.postId}" action="${postPathDownVote}" method="post" class="mt-0">
                                    <form:label path="downVotePostId">
                                        <form:input path="downVotePostId" id="downVotePostId${post.postId}" class="input-wrap" type="hidden" value="${post.postId}"/>
                                    </form:label>
                                    <div class="net-votes">
                                        <button class="btn pt-0 pb-0" type="submit">
                                            <c:choose>
                                                <c:when test="${post.getUserAuthVote(user.name) < 0}">
                                                    <i class="fa fa-2x fa-arrow-down votedDown"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa fa-2x fa-arrow-down"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                    </div>
                                </form:form>

                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-9">
                <div class="row post-description description-text">
                    <c:out value="${post.description}" />
                </div>
                <div class="row extra-info">
                    <!-- Tags section -->
                    <div class="col-9 tags">
                        <c:forEach items="${post.postTags}" var="tag">
                            <button  class="badge badge-color ml-1" onclick="goToExplore('${tag.tagName}','${tag.type.name()}')">
                                    <span>
                                        <c:choose>
                                            <c:when test="${tag.type.name() == 'tech_name'}">
                                                <c:out value="${tag.tagName}"/>
                                            </c:when>
                                            <c:when test="${tag.type.name() == 'tech_type'}">
                                                <spring:message code="type.${tag.tagName}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="category.${tag.tagName}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                            </button>
                        </c:forEach>
                    </div>
                    <div class="col">
                        <div class="row d-flex secondary-color text-right post-date">
                            <c:out value="${post.timestamp.toLocaleString()}" />
                        </div>
                        <div class="row d-flex secondary-color text-right">
                            <a href="<c:url value="/users/${post.user.username}"/>">${post.user.username}</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div>

    <!-- Answers Section -->
    <div class="answers">
        <div class="title"><h3><spring:message code="post.answers" /></h3></div>
        <c:choose>
            <c:when test="${not empty answers}">
                <c:forEach var="answer" items="${answers}">
                    <div class="post-cards">
                        <div class="card mb-3 post-card-answer">
                            <div class="card-body">
                                <!-- Delete comment -->
                                <c:if test="${isAdmin || isOwner || answer.user.username == user.name}">
                                    <div class="row">
                                            <span class="col d-flex justify-content-end align-items-end">
                                                <button class="btn btn-link" onclick="openDeleteCommentModal(${answer.postCommentId})"  data-toggle="modal" data-target="#deleteCommentModal"><i class="fa fa-trash"></i></button>
                                            </span>
                                    </div>
                                </c:if>
                                <div class="row">
                                    <!-- Up Vote - Down Vote section -->
                                    <div class="col-1 net-votes">
                                        <c:choose>
                                            <c:when test="${user.name == 'anonymousUser'}">
                                                <button class="btn pt-0 pb-0"><i class="fa fa-1x fa-arrow-up" data-toggle="modal" data-target="#loginModal"></i></button>
                                                <div>
                                                    <h4>${answer.votesUp- answer.votesDown}</h4>
                                                </div>
                                                <button class="btn pt-0 pb-0"><i class="fa fa-1x fa-arrow-down" data-toggle="modal" data-target="#loginModal"></i></button>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${!isEnable}">
                                                        <button class="btn pt-0 pb-0"><i class="fa fa-1x fa-arrow-up" data-toggle="modal" data-target="#confirmMailModal"></i></button>
                                                        <div>
                                                            <h4>${answer.votesUp - answer.votesDown}</h4>
                                                        </div>
                                                        <button class="btn pt-0 pb-0"><i class="fa fa-1x fa-arrow-down" data-toggle="modal" data-target="#confirmMailModal"></i></button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:url value="/posts/${post.postId}/upVoteComment/" var="postPathUpVoteComment"/>
                                                        <form:form modelAttribute="upVoteCommentForm" id="upVoteCommentForm${post.postId}CommentId${answer.postCommentId}" action="${postPathUpVoteComment}" method="post" class="mb-0 mt-0 pt-0">
                                                            <form:label path="upVoteCommentPostId" class="hidden-no-space">
                                                                <form:input id="upVoteCommentPostId${post.postId}CommentId${answer.postCommentId}" class="hidden-no-space" path="upVoteCommentPostId" value="${post.postId}"/>
                                                            </form:label>
                                                            <form:label path="postCommentUpVoteId" class="hidden-no-space">
                                                                <form:input id="postCommentId${answer.postCommentId}UpVote" class="hidden-no-space" path="postCommentUpVoteId" value="${answer.postCommentId}"/>
                                                            </form:label>

                                                            <div class="net-votes pt-0 mt-0 mb-0 pb-0">
                                                                <button class="btn pt-0 pb-0" type="submit">
                                                                    <c:choose>
                                                                        <c:when test="${answer.getUserAuthVote(user.name) > 0}">
                                                                            <i class="fa fa-1x fa-arrow-up votedUp"></i>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <i class="fa fa-1x fa-arrow-up"></i>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </button>
                                                                <div>
                                                                    <div>${answer.votesUp - answer.votesDown}</div>
                                                                </div>
                                                            </div>
                                                        </form:form>

                                                        <c:url value="/posts/${post.postId}/downVoteComment/" var="postPathDownVoteComment"/>
                                                        <form:form modelAttribute="downVoteCommentForm" id="downVoteCommentForm${post.postId}/${answer.postCommentId}" action="${postPathDownVoteComment}" method="post" class="mb-0 mt-0">
                                                            <form:label path="downVoteCommentPostId">
                                                                <form:input id="downVoteCommentPostId${post.postId}CommentId${answer.postCommentId}" class="input-wrap hidden-no-space" path="downVoteCommentPostId" value="${post.postId}"/>
                                                            </form:label>

                                                            <div class="net-votes">
                                                                <button class="btn pt-0 pb-0" type="submit">
                                                                    <c:choose>
                                                                        <c:when test="${answer.getUserAuthVote(user.name) < 0}">
                                                                            <i class="fa fa-1x fa-arrow-down votedDown"></i>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <i class="fa fa-1x fa-arrow-down"></i>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </button>
                                                            </div>
                                                            <form:label path="postCommentDownVoteId">
                                                                <form:input id="postCommentId${answer.postCommentId}DownVote" class="input-wrap hidden-no-space" path="postCommentDownVoteId" value="${answer.postCommentId}"/>
                                                            </form:label>
                                                        </form:form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col-10">
                                        <div class="row post-description align-items-center description-text">
                                            <c:out value="${answer.description}"/>
                                        </div>
                                        <div class="row extra-info">
                                            <div class="col-9 tags"></div>
                                            <div class="col">
                                                <div class="row d-flex secondary-color text-right post-date">
                                                    <c:out value="${answer.timestamp.toLocaleString()}"/>
                                                </div>
                                                <div class="row d-flex secondary-color text-right">
                                                    <a href="<c:url value="/users/${answer.user.username}"/>">${answer.user.username}</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="row justify-content-center mt-2">
                    <spring:message code="post.no_answers_yet"/>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <!-- Answers pagination -->
    <jsp:include page="../components/pagination.jsp">
        <jsp:param name="total" value="${post.answersAmount}"/>
        <jsp:param name="page" value="${page}"/>
        <jsp:param name="page_size" value="${page_size}"/>
        <jsp:param name="origin" value='post_comment'/>
        <jsp:param name="post_id" value='${post.postId}'/>
        <jsp:param name="comments_page" value="${page}"/>
    </jsp:include>

    <div class="answers">
        <div class="title">
            <h4><spring:message code="post.answer_yourself"/></h4>
        </div>

        <!-- Leave a comment -->
        <div class="container margin-left">
            <div class="row comment-yourself">
                <div>
                    <c:url value="/posts/comment" var="postPathComment"/>
                    <form:form modelAttribute="postCommentForm" action="${postPathComment}" method="post">
                        <form:errors path="comment" element="p" cssClass="formError"/>
                        <form:label path="commentPostId"><form:input  class="input-wrap" path="commentPostId" type="hidden" value="${post.postId}"/></form:label>

                        <form:label path="comment"/>
                        <form:textarea path="comment" id="commentInput" class="form-control" aria-label="With textarea" rows="5" cols="100"/>

                        <c:choose>
                            <c:when test="${user.name != 'anonymousUser'}">
                                <c:choose>
                                    <c:when test="${!isEnable}">
                                        <button type="button" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#confirmMailModal"><spring:message code="button.submit"/></button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end"><spring:message code="button.submit"/></button>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <button type="button" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#loginModal"><spring:message code="button.submit"/></button>
                            </c:otherwise>
                        </c:choose>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modals -->
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
                    <c:url value="/posts/${post.postId}/comment/delete" var="postPathDeleteCommment"/>
                    <form:form modelAttribute="deletePostCommentForm" action="${postPathDeleteCommment}" method="post">
                        <form:label path="commentDeletePostId"><form:input  class="input-wrap" path="commentDeletePostId" type="hidden" value="${post.postId}"/></form:label>
                        <form:label path="commentDeleteId"><form:input  class="input-wrap" path="commentDeleteId" type="hidden" id="commentIdDeleteInput"/></form:label>
                        <button type="button" class="btn btn-secondary mr-4" data-dismiss="modal" aria-label="Close"><spring:message code="button.cancel"/></button>
                        <button type="submit" class="btn btn-danger ml-4"><spring:message code="button.delete"/></button>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

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
                    <button type="button" class="btn btn-info" onclick="window.location.href = '<c:url value="/login"/>'"><spring:message code="button.login"/></button>
                </div>
                <div class="row  justify-content-center align-items-center margin-top">
                    <div><spring:message code="login.sign_up_question"/> <a href="<c:url value="/register"/>"><spring:message code="button.sign_up"/></a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

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
                <div class="row  justify-content-center align-items-center margin-top">
                    <div><a href="<c:url value="/register/resend_token"/>"><spring:message code="button.resend"/></a></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="deletePostModal" tabindex="-1" role="dialog" aria-labelledby="deletePostModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deletePostModalLabel"><spring:message code="post.delete"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row  justify-content-center align-items-center margin-top">
                    <div><spring:message code="post.delete.message"/></div>
                </div>
                <div class="row justify-content-center align-items-center margin-top">
                    <c:url value="/posts/delete_post" var="postPathDeletePost"/>
                    <form:form modelAttribute="deletePostForm" action="${postPathDeletePost}" method="post">
                        <form:label path="postIdx"><form:input  class="input-wrap" path="postIdx" type="hidden" id="postIdDeleteInput"/></form:label>
                        <button type="button" class="btn btn-secondary mr-4" data-dismiss="modal" aria-label="Close"><spring:message code="button.cancel"/></button>
                        <button type="submit" class="btn btn-danger ml-4"><spring:message code="button.delete"/></button>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
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

    function openDeleteCommentModal(commentId){
        $('#commentIdDeleteInput').val(commentId);

        $('#deleteCommentModal').modal('show');
    }

    function openDeletePostModal(postId){
        $('#postIdDeleteInput').val(postId);

        $('#deletePostModal').modal('show');
    }


    function goToExplore( tagName, type ){
        let url = "<c:url value="/search?"/>"

        if(type === 'tech_name'){
            url = url + 'toSearch=' + tagName;
        }else if(type === 'tech_type'){
            url = url + 'types=' + tagName;
        }else{
            url = url + 'categories=' + tagName;
        }

        window.location.href = url + '&isPost=true'
    }
</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>