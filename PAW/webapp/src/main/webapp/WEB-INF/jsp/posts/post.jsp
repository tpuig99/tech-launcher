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
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/posts.css"/>"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="connected" value="${user.authenticated}"/>
        <jsp:param name="username" value="${user.name}"/>
        <jsp:param name="isMod" value="${user_isMod}"/>
    </jsp:include>

    <div class="sidenav overflow-auto">
        <c:forEach var="category" items="${categories_sidebar}">
            <a href="<c:url value="/posts/category/${category}"/>"><spring:message code="category.${category}"/></a>
        </c:forEach>
    </div>

    <!-- Question Section -->
    <div class="content">
        <div class="ml-4 mr-1"><h1>${post.title}</h1></div>
        <div class="post-cards">
            <div class="row">
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
                <div class="col">
                    <div class="row post-description mr-2">
                            ${post.description}
                    </div>
                    <div class="row extra-info">
                        <div class="col-9 tags">
                            <c:forEach items="${post.postTags}" var="tag">
                                <button  class="badge badge-color ml-1" onclick="goToCat('${tag.tagName}')">
                                    <span>
                                            ${tag.tagName}
                                    </span>
                                </button>
                            </c:forEach>
                        </div>
                        <div class="col">
                            <div class="row d-flex secondary-color text-right post-date">
                                    ${post.timestamp.toLocaleString()}
                            </div>
                            <div class="row d-flex secondary-color text-right">
                                <a href="<c:out value="/users/${post.user.username}"/>">${post.user.username}</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


        </div>

        <!-- Answers Section -->
        <div class="answers">
            <div class="title"><h3>Answers</h3></div>
            <c:choose>
                <c:when test="${not empty post.postComments}">
                    <c:forEach var="answer" items="${post.postComments}">
                        <div class="post-cards">
                            <div class="card mb-3 post-card-individual">
                                <div class="card-body">
                                    <div class="row">
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
                                                            <form:form modelAttribute="upVoteCommentForm" id="upVoteCommentForm${post.postId}CommentId${answer.postCommentId}" action="${postPathUpVoteComment}" method="post" class="mb-0 mt-0">
                                                                <form:label path="upVoteCommentPostId">
                                                                    <form:input id="upVoteCommentPostId${post.postId}CommentId${answer.postCommentId}" class="input-wrap" path="upVoteCommentPostId" type="hidden" value="${post.postId}"/>
                                                                </form:label>
                                                                <form:label path="postCommentUpVoteId">
                                                                    <form:input id="postCommentId${answer.postCommentId}UpVote" class="input-wrap" path="postCommentUpVoteId" type="hidden" value="${answer.postCommentId}"/>
                                                                </form:label>

                                                                <div class="net-votes">
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
                                                                        <h6>${answer.votesUp - answer.votesDown}</h6>
                                                                    </div>
                                                                </div>
                                                            </form:form>
                                                            <c:url value="/posts/${post.postId}/downVoteComment/" var="postPathDownVoteComment"/>
                                                            <div class="mt-0 pt-0">
                                                            <form:form modelAttribute="downVoteCommentForm" id="downVoteCommentForm${post.postId}/${answer.postCommentId}" action="${postPathDownVoteComment}" method="post" class="mb-0 mt-0">
                                                                <form:label path="downVoteCommentPostId">
                                                                    <form:input id="downVoteCommentPostId${post.postId}CommentId${answer.postCommentId}" class="input-wrap" path="downVoteCommentPostId" type="hidden" value="${post.postId}"/>
                                                                </form:label>
                                                                <form:label path="postCommentDownVoteId">
                                                                    <form:input id="postCommentId${answer.postCommentId}DownVote" class="input-wrap" path="postCommentDownVoteId" type="hidden" value="${answer.postCommentId}"/>
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
                                                            </form:form>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col">
                                            <div class="row post-description d-flex align-items-center">
                                                    ${answer.description}
                                            </div>
                                            <div class="row extra-info">
                                                <div class="col-9 tags"></div>
                                                <div class="col">
                                                    <div class="row d-flex secondary-color text-right post-date">
                                                            ${answer.timestamp.toLocaleString()}
                                                    </div>
                                                    <div class="row d-flex secondary-color text-right">
                                                        <a href="<c:out value="/users/${answer.user.username}"/>">${answer.user.username}</a>
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
                <c:otherwise><!-- TODO: spring message -->LACA</c:otherwise>
            </c:choose>
        </div>

        <div class="user-comment">
            <div class="container">
                <h4 class="title"><!-- TODO: spring message -->Answer yourself!</h4>
            </div>


            <div class="container margin-left">
                <div class="row">
                    <h5><!-- TODO: spring message --><spring:message code="tech.interactions.leave_comment"/></h5>
                    <div>
                        <c:url value="/posts/comment" var="postPathComment"/>
                        <form:form modelAttribute="postCommentForm" action="${postPathComment}" method="post">
                            <form:label path="commentPostId"><form:input  class="input-wrap" path="commentPostId" type="hidden" value="${post.postId}"/></form:label>

                            <form:label path="comment"/>
                            <form:textarea path="comment" id="commentInput" class="form-control" aria-label="With textarea" rows="5" cols="100"/>

                            <c:choose>
                                <c:when test="${user.name != 'anonymousUser'}">
                                    <c:choose>
                                        <c:when test="${!isEnable}">
                                            <button type="button" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#confirmMailModal"><!-- TODO: spring message --><spring:message code="button.submit"/></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end"><!-- TODO: spring message --><spring:message code="button.submit"/></button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" id="commentButton" disabled class="btn btn-info margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#loginModal"><!-- TODO: spring message --><spring:message code="button.submit"/></button>
                                </c:otherwise>
                            </c:choose>
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

        function goToCat( catName ){
            window.location.href = "<c:url value="/"/>" + "posts/categories/" + catName;
        }
    </script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>