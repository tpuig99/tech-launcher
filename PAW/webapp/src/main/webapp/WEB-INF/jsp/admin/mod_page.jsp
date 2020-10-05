<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <title>
            <spring:message code="moderator.wref"/>
        </title>
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/mod.css"/>"/>
    </head>
    <body>

        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="connected" value="${user.authenticated}"/>
            <jsp:param name="username" value="${user.name}"/>
            <jsp:param name="isMod" value="${user_isMod}"/>
        </jsp:include>


        <div class="content-no-sidebar">
            <c:if test="${isAdmin}">
                <div class="page-title"><spring:message code="moderate.comment.title"/></div>
                <div class="page-description"></div>
                <div class="row justify-content-center">
                    <c:choose>
                        <c:when test="${not empty reportedComments}">
                            <div class="d-flex flex-column">
                                <c:forEach items="${reportedComments}" var="reportedComment">
                                        <div class="card emphasis row emph-comment mb-2 verified">
                                            <div class="card-body row mt-1">
                                                <div class="col-3 secondary-font"> <a href="/users/${reportedComment.userNameOwner}"><c:out value="${reportedComment.userNameOwner}" default=""/></a>
                                                    <c:out value="/" default=""/>
                                                    <a href="<c:out value="/${reportedComment.categoryAsString}/${reportedComment.frameworkId}"/>"><c:out value="${reportedComment.frameworkName}" default=""/></a>
                                                </div>
                                                <div class="col-6 text-left"> <c:out value="${reportedComment.commentDescription}" default=""/> </div>
                                                <div class="col third-font text-right"> <c:out value="${reportedComment.timestamp}" default=""/> </div>
                                            </div>
                                            <div class="card-footer">
                                                <button type="button" class="btn btn-secondary" onclick="ignoreComment(${reportedComment.commentId})"><spring:message code="button.ignore"/></button>
                                                <button type="button" class="btn btn-danger" onclick="deleteComment(${reportedComment.commentId})"><spring:message code="button.delete"/></button>
                                            </div>
                                        </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div><spring:message code="moderate.comment.empty"/></div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="page-title"><spring:message code="moderator.title"/></div>
                <div class="page-description"></div>
                <div class="d-flex flex-wrap justify-content-center">
                    <c:choose>
                        <c:when test="${not empty mods}">
                            <c:forEach var = "moderator" items="${mods}">
                                <c:if test="${!moderator.admin}">
                                    <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                        <div class="card-body mt-1">
                                            <div class="secondary-font">
                                                <a href="/users/${moderator.userName}"><c:out value="${moderator.userName}" default=""/></a>
                                                <c:out value="/" default=""/>
                                                <a href="<c:out value="${moderator.frameworkName}/${moderator.frameworkId}"/>"><c:out value="${moderator.frameworkName}" default=""/></a>
                                            </div>
                                        </div>
                                        <div class="card-footer">
                                            <button type="button" class="btn btn-danger" onclick="revokePromotion(${moderator.verificationId})"><spring:message code="button.demote"/></button>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div><spring:message code="moderator.no_mods"/></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <div class="page-title"><spring:message code="moderate.content.title"/></div>
            <div class="page-description"></div>
            <div class="row justify-content-center">
                <c:choose>
                    <c:when test="${not empty reportedContents}">
                        <div class="d-flex flex-column">
                            <c:forEach items="${reportedContents}" var="reportedContent">
                                <div class="card emphasis row emph-comment mb-2 verified">
                                    <div class="card-body row mt-1">
                                        <div class="col-3 secondary-font"> <a href="/users/${reportedContent.userNameOwner}"><c:out value="${reportedContent.userNameOwner}" default=""/></a>
                                            <c:out value="/" default=""/>
                                            <a href="<c:out value="/${reportedContent.categoryAsString}/${reportedContent.frameworkId}"/>"><c:out value="${reportedContent.frameworkName}" default=""/></a>
                                        </div>
                                        <div class="col-6 text-left"> <c:out value="${reportedContent.title}" default=""/> </div>
                                        <div class="col third-font text-right"> <c:out value="${reportedContent.reportDescription}" default=""/> </div>
                                    </div>
                                    <div class="card-footer">
                                        <button type="button" class="btn btn-secondary" onclick="ignoreContent(${reportedContent.contentId})"><spring:message code="button.ignore"/></button>
                                        <button type="button" class="btn btn-danger" onclick="deleteContent(${reportedContent.contentId})"><spring:message code="button.delete"/></button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div><spring:message code="moderate.content.empty"/></div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="page-title"><spring:message code="moderator.pending"/></div>
            <div class="page-description"></div>
            <div class="d-flex flex-column justify-content-center">
            <c:choose>
                <c:when test="${not empty pendingToVerify}">
                    <c:forEach var = "pendingUser" items="${pendingToVerify}">
                        <div class="card emphasis emph-comment row mb-2 verified">
                            <div class="card-body row mt-1">
                                <div class="col-3 secondary-font"> <a href="/users/${pendingUser.userName}"><c:out value="${pendingUser.userName}" default=""/></a>
                                    <c:out value="/" default=""/>
                                    <a href="<c:out value="${pendingUser.frameworkName}/${pendingUser.frameworkId}"/>"><c:out value="${pendingUser.frameworkName}" default=""/></a>
                                </div>
                                <div class="col-6 text-left"> <c:out value="${pendingUser.comment.description}" default=""/> </div>
                                <div class="col third-font text-right"> <c:out value="${pendingUser.comment.timestamp.toLocaleString()}" default=""/> </div>
                            </div>
                            <div class="card-footer">
                                <button type="button" class="btn btn-secondary" onclick="rejectUser(${pendingUser.verificationId})"><spring:message code="button.deny"/></button>
                                <button type="button" class="btn primary-button" onclick="promoteUser(${pendingUser.verificationId})"><spring:message code="button.promote"/></button>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div><spring:message code="moderator.emptyPending"/></div>
                </c:otherwise>
            </c:choose>
            </div>
            <div class="page-title"><spring:message code="moderator.pendingApplicants"/></div>
            <div class="page-description"></div>
            <div class="d-flex flex-wrap justify-content-center">
                <c:choose>
                    <c:when test="${not empty pendingApplicants}">
                        <c:forEach var = "applicant" items="${pendingApplicants}">
                            <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                <div class="card-body mt-1">
                                    <div class="secondary-font"> <a href="<c:url value="/users/${applicant.userName}"/>"><c:out value="${applicant.userName}" default=""/></a>
                                        <c:out value="/" default=""/>
                                        <a href="${applicant.frameworkName}/${applicant.frameworkId}"><c:out value="${applicant.frameworkName}" default=""/></a>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <button type="button" class="btn primary-button" onclick="promoteUser(${applicant.verificationId})"><spring:message code="button.promote"/></button>
                                    <button type="button" class="btn btn-secondary" onclick="rejectUser(${applicant.verificationId})"><spring:message code="button.deny"/></button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div><spring:message code="moderator.emptyApplicants"/></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>



        <script>
            function ignoreComment(commentId){
                window.location.href = '<c:url value="/mod/comment/ignore" />?commentId=' + commentId;
            }

            function deleteComment(commentId){
                window.location.href = '<c:url value="/mod/comment/delete" />?commentId=' + commentId;
            }

            function ignoreContent(contentId){
                window.location.href = '<c:url value="/mod/content/ignore" />?contentId=' + contentId;
            }

            function deleteContent(contentId){
                window.location.href = '<c:url value="/mod/content/delete" />?contentId=' + contentId;
            }

            function promoteUser(verificationId){
                window.location.href = '<c:url value="/accept" />?id=' + verificationId;
            }

            function rejectUser(verificationId){
                window.location.href = '<c:url value="/reject" />?id=' + verificationId;
            }

            function revokePromotion(verificationId){
                window.location.href = '<c:url value="/demote" />?id=' + verificationId;
            }
        </script>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
