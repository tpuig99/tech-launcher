<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="forum.wref"/></title>

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

    <div class="content-no-sidebar-left">
        <div class="title"><h1><spring:message code="forum.title"/></h1></div>
        <div class="post-cards">
            <div class="d-flex flex-row-reverse">
                <c:choose>
                    <c:when test="${user.name == 'anonymousUser'}">
                        <button class="btn btn-info mt-2 mb-4" data-toggle="modal" data-target="#loginModal"">
                            <spring:message code="forum.add_post"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-info mt-2 mb-4" onclick="window.location.href = '<c:url value="/posts/add_post"/>'">
                            <spring:message code="forum.add_post"/>
                        </button>
                    </c:otherwise>
                </c:choose>
                            </div>
            <div class="row showing-results">
                <div class="col-9"></div>
                <div class="col showing-results data">
                    ${pageSize*(postsPage-1)+1}-<c:choose><c:when test="${posts.size() < pageSize}">${pageSize*(postsPage-1)+ posts.size()}</c:when><c:otherwise>${pageSize*(postsPage)}</c:otherwise></c:choose>
                     <spring:message code="forum.showing_results"/> ${postsAmount}
                </div>
            </div>
            <c:choose>
                <c:when test="${not empty posts}">
                    <c:forEach items="${posts}" var="post">
                        <div class="card mb-3 post-card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-1 net-votes">
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
                                                        <c:url value="/posts/upVote/" var="postPathUpVote"/>
                                                        <form:form modelAttribute="upVoteForm" id="upVoteForm${post.postId}" action="${postPathUpVote}" method="post" class="mb-0">
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
                                                        <c:url value="/posts/downVote/" var="postPathDownVote"/>
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

                                        <div class="row post-title">
                                            <a href="<c:url value='/posts/${post.postId}'/>">
                                                    <c:out value="${post.title}"/>
                                            </a>
                                        </div>
                                        <div class="row posts-description">
                                            <c:out value="${post.description}"/>
                                        </div>
                                        <div class="row extra-info">
                                            <div class="col-9 tags">
                                                <c:forEach items="${post.postTags}" var="tag">
                                                    <button  class="badge badge-color ml-1"<%-- onclick="goToTag('${tag.tagName}')"--%>>
                                                        <span>
                                                            <c:out value="${tag.tagName}"/>
                                                        </span>
                                                    </button>
                                                </c:forEach>
                                            </div>
                                            <div class="col">
                                                <div class="row d-flex secondary-color text-right post-date">
                                                        ${post.timestamp.toLocaleString()}
                                                </div>
                                                <div class="row d-flex secondary-color text-right">
                                                    <a href="<c:url value="/users/${post.user.username}"/>">${post.user.username}</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row d-flex my-1 justify-content-end">
                                            <div class="col-3">
                                                <c:if test="${isAdmin || post.user.username == user.name}">
                                                    <div class="row d-flex">
                                                            <%--                                                        <div class="mb-4">--%>
                                                            <%--                                                            <a href="<c:url value="/edit_post?id=${post.postId}"/>" >--%>
                                                            <%--                                                                <button class="btn btn-info btn-block text-nowrap" type="button">--%>
                                                            <%--                                                                    <i class="fa fa-edit fa-sm mr-1"></i>--%>
                                                            <%--                                                                    <spring:message code="button.edit_post"/>--%>
                                                            <%--                                                                </button>--%>
                                                            <%--                                                            </a>--%>
                                                            <%--                                                        </div>--%>
                                                        <div>
                                                            <button class="btn btn-danger text-nowrap" type="button" onclick="openDeletePostModal(${post.postId})" data-toggle="modal" data-target="#deletePostModal">
                                                                <i class="fas fa-trash-alt fa-sm mr-1"></i>
                                                                <spring:message code="button.delete_post"/>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>

                    </c:forEach>
                </c:when>
                <c:otherwise><spring:message code="profile.empty.comments"/></c:otherwise>
            </c:choose>
        </div>
        <jsp:include page="../components/pagination.jsp">
            <jsp:param name="total" value="${postsAmount}"/>
            <jsp:param name="page" value="${postsPage}"/>
            <jsp:param name="page_size" value="${pageSize}"/>
            <jsp:param name="origin" value="posts"/>
            <jsp:param name="posts_page" value="${postsPage}"/>
        </jsp:include>
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
        function goToTag( tagName ){
            window.location.href = "<c:url value="/"/>" + "posts/tag/" + tagName;
        }

        function openDeletePostModal(postId){
            $('#postIdDeleteInput').val(postId);

            $('#deletePostModal').modal('show');
        }
    </script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
