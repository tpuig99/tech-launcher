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


        <div class="content-no-sidebar row">


            <!-- Mods managing -->
            <div class="container">
               <ul class="nav nav-tabs">
                    <li class="nav-item active">
                        <a class="nav-link" href="#1" data-toggle="tab">Overview</a>
                    </li>
                    <li><a class="nav-link" href="#2" data-toggle="tab">Without clearfix</a>
                    </li>
                    <li><a class="nav-link" href="#3" data-toggle="tab">Solution</a>
                    </li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane active" id="1">
                        <h3>Standard tab panel created on bootstrap using nav-tabs</h3>
                    </div>
                    <div class="tab-pane" id="2">
                        <h3>Notice the gap between the content and tab after applying a background color</h3>
                    </div>
                    <div class="tab-pane" id="3">
                        <h3>add clearfix to tab-content (see the css)</h3>
                    </div>
                </div>
            </div>


            <div class="left col-6 border-right">
                <c:if test="${isAdmin}">
                    <div class="page-title"><spring:message code="moderator.title"/></div>
                    <div class="page-description"></div>
                    <div class="d-flex flex-wrap justify-content-center">
                        <c:choose>
                            <c:when test="${not empty mods || modsPage != 1}">
                                <c:forEach var = "moderator" items="${mods}">
                                    <c:if test="${!moderator.admin}">
                                        <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                            <div class="card-body mt-1">
                                                <div class="secondary-font">
                                                    <a href="<c:url value="/users/${moderator.userName}"/>"><c:out value="${moderator.userName}" default=""/></a>
                                                    <c:out value="/" default=""/>
                                                    <a href="<c:url value="/${moderator.frameworkName}/${moderator.frameworkId}"/>"><c:out value="${moderator.frameworkName}" default=""/></a>
                                                </div>
                                            </div>
                                            <div class="card-footer row mx-2 justify-content-center">
                                                <c:url value="/demote" var="postPathDemote"/>
                                                <form:form modelAttribute="revokePromotionForm" action="${postPathDemote}" method="post">
                                                    <form:label path="revokePromotionVerificationId">
                                                        <form:input path="revokePromotionVerificationId" id="revokePromotionUserVerificationIdId${moderator.verificationId}" class="input-wrap" type="hidden" value="${moderator.verificationId}"/>
                                                    </form:label>
                                                    <button type="submit" class="btn btn-danger" ><spring:message code="button.demote"/></button>
                                                </form:form>
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
                    <c:if test="${not empty mods || modsPage != 1}">
                        <ul class="pagination justify-content-center">
                            <c:choose>
                            <c:when test="${modsPage == 1}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item ">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage-1}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Previous">
                                    <span aria-hidden="true">&lsaquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${mods.size() < pageSize}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage+1}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Next">
                                    <span aria-hidden="true">&rsaquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </ul>
                    </c:if>
                </c:if>

                <!-- pending to verify by comments -->
                <div class="page-title"><spring:message code="moderator.pending"/></div>
                <div class="page-description"></div>
                <div class="d-flex flex-column justify-content-center">
                <c:choose>
                    <c:when test="${not empty pendingToVerify || verifyPage != 1}">
                        <c:forEach var = "pendingUser" items="${pendingToVerify}">
                            <div class="card emphasis emph-comment row mb-2 mx-4 verified">
                                <div class="card-body row mt-1">
                                    <div class="col-3 secondary-font"> <a href="<c:url value="/users/${pendingUser.userName}"/>"><c:out value="${pendingUser.userName}" default=""/></a>
                                        <c:out value="/" default=""/>
                                        <a href="<c:url value="/${pendingUser.frameworkName}/${pendingUser.frameworkId}"/>"><c:out value="${pendingUser.frameworkName}" default=""/></a>
                                    </div>
                                    <div class="col-6 text-left"> <c:out value="${pendingUser.comment.description}" default=""/> </div>
                                    <div class="col third-font text-right"> <c:out value="${pendingUser.comment.timestamp.toLocaleString()}" default=""/> </div>
                                </div>
                                <div class="card-footer row mx-2">
                                    <c:url value="/acceptPending" var="postPathAcceptPending"/>
                                    <form:form modelAttribute="promotePendingUserForm" action="${postPathAcceptPending}" method="post">
                                        <form:label path="promotePendingUserVerificationId">
                                            <form:input path="promotePendingUserVerificationId" id="promotePendingUserVerificationIdId${pendingUser.verificationId}" class="input-wrap" type="hidden" value="${pendingUser.verificationId}"/>
                                        </form:label>
                                        <button type="submit" class="btn primary-button"><spring:message code="button.promote"/></button>
                                    </form:form>
                                    <c:url value="/rejectPending" var="postPathRejectPending"/>
                                    <form:form modelAttribute="rejectPendingUserForm" action="${postPathRejectPending}" method="post">
                                        <form:label path="rejectPendingUserVerificationId">
                                            <form:input path="rejectPendingUserVerificationId" id="rejectPendingUserVerificationIdId${pendingUser.verificationId}" class="input-wrap" type="hidden" value="${pendingUser.verificationId}"/>
                                        </form:label>
                                        <button type="submit" class="btn btn-secondary"><spring:message code="button.deny"/></button>
                                    </form:form>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div><spring:message code="moderator.emptyPending"/></div>
                    </c:otherwise>
                </c:choose>
                </div>
                <c:if test="${not empty pendingToVerify || verifyPage != 1}">
                    <ul class="pagination justify-content-center">
                        <c:choose>
                        <c:when test="${verifyPage == 1}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item ">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage-1}&rConPage=${rConPage}"/>" aria-label="Previous">
                                <span aria-hidden="true">&lsaquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <c:choose>
                        <c:when test="${pendingToVerify.size() < pageSize}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage+1}&rConPage=${rConPage}"/>" aria-label="Next">
                                <span aria-hidden="true">&rsaquo;</span>
                                <span class="sr-only">Next</span>
                            </a>
                        </li>
                    </ul>
                </c:if>

                <!-- pending applicants to verify -->
                <div class="page-title"><spring:message code="moderator.pendingApplicants"/></div>
                <div class="page-description"></div>
                <div class="d-flex flex-wrap justify-content-center">
                    <c:choose>
                        <c:when test="${not empty pendingApplicants || applicantsPage != 1}">
                            <c:forEach var = "applicant" items="${pendingApplicants}">
                                <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                    <div class="card-body mt-1">
                                        <div class="secondary-font"> <a href="<c:url value="/users/${applicant.userName}"/>"><c:out value="${applicant.userName}" default=""/></a>
                                            <c:out value="/" default=""/>
                                            <a href="<c:url value="/${applicant.frameworkName}/${applicant.frameworkId}"/>"><c:out value="${applicant.frameworkName}" default=""/></a>
                                        </div>
                                    </div>
                                    <div class="card-footer row">
                                        <c:url value="/accept" var="postPathAccept"/>
                                        <form:form modelAttribute="promoteUserForm" action="${postPathAccept}" method="post">
                                            <form:label path="promoteUserVerificationId">
                                                <form:input path="promoteUserVerificationId" id="promoteUserVerificationIdId${applicant.verificationId}" class="input-wrap" type="hidden" value="${applicant.verificationId}"/>
                                            </form:label>
                                            <button type="submit" class="btn primary-button"><spring:message code="button.promote"/></button>
                                        </form:form>
                                        <c:url value="/reject" var="postPathReject"/>
                                        <form:form modelAttribute="rejectUserForm" action="${postPathReject}" method="post">
                                            <form:label path="rejectUserVerificationId">
                                                <form:input path="rejectUserVerificationId" id="rejecUserVerificationIdId${applicant.verificationId}" class="input-wrap" type="hidden" value="${applicant.verificationId}"/>
                                            </form:label>
                                            <button type="submit" class="btn btn-secondary"><spring:message code="button.deny"/></button>
                                        </form:form>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div><spring:message code="moderator.emptyApplicants"/></div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${not empty pendingApplicants || applicantsPage != 1}">
                    <ul class="pagination justify-content-center">
                        <c:choose>
                        <c:when test="${applicantsPage == 1}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item ">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage-1}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Previous">
                                <span aria-hidden="true">&lsaquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <c:choose>
                        <c:when test="${pendingApplicants.size() < pageSize}">
                        <li class="page-item disabled">
                            </c:when>
                            <c:otherwise>
                        <li class="page-item">
                            </c:otherwise>
                            </c:choose>
                            <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage+1}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Next">
                                <span aria-hidden="true">&rsaquo;</span>
                                <span class="sr-only">Next</span>
                            </a>
                        </li>
                    </ul>
                </c:if>
            </div>
            <!-- Reported Comments -->
            <div class="right col">
                <c:if test="${isAdmin}">
                    <div class="page-title"><spring:message code="moderate.comment.title"/></div>
                    <div class="page-description"></div>
                    <div class="row justify-content-center">
                        <c:choose>
                            <c:when test="${not empty reportedComments || rComPage != 1}">
                                <div class="d-flex flex-column">
                                    <c:forEach items="${reportedComments}" var="reportedComment">
                                        <div class="card emphasis emph-comment mb-2 verified">
                                            <div class="card-body my-auto">
                                                <div class="border-bottom">
                                                <p class="my-1"><spring:message code="moderate.report.comment.owner"/>:&nbsp;<a href="<c:url value="/users/${reportedComment.userNameOwner}"/>"><c:out value="${reportedComment.userNameOwner}" default=""/></a></p>
                                                <p class="my-1"><spring:message code="moderate.report.tech"/>:&nbsp;<a href="<c:url value="/${reportedComment.categoryAsString}/${reportedComment.frameworkId}"/>"><c:out value="${reportedComment.frameworkName}" default=""/></a></p>
                                                <p class="my-1"><spring:message code="moderate.comment.description"/>:&nbsp;<c:out value="${reportedComment.commentDescription}" default=""/></p>
                                                </div>
                                                <div class="mt-2">
                                                <p class="my-1"><spring:message code="moderate.report.description"/>:&nbsp;<c:out value=" ${reportedComment.reportDescription}" default=""/></p>
                                                <p class="my-1"><spring:message code="moderate.report.quantity"/>:&nbsp;${reportedComment.reportsUserName.size()}</p>
                                                </div>
                                            </div>
                                            <div class="card-footer row mx-2 justify-content-center">
                                                <c:url value="/mod/comment/delete" var="postPathDeleteComment"/>
                                                <form:form modelAttribute="deleteCommentForm" action="${postPathDeleteComment}" method="post">
                                                    <form:label path="deleteCommentId">
                                                        <form:input path="deleteCommentId" id="deleteCommentFormId${reportedComment.commentId}" class="input-wrap" type="hidden" value="${reportedComment.commentId}"/>
                                                    </form:label>
                                                    <button type="submit" class="btn btn-danger"><spring:message code="button.delete"/></button>
                                                </form:form>
                                                <c:url value="/mod/comment/ignore" var="postPathIgnoreComment"/>
                                                <form:form modelAttribute="ignoreCommentForm" action="${postPathIgnoreComment}" method="post">
                                                    <form:label path="ignoreCommentId">
                                                        <form:input path="ignoreCommentId" id="ignoreCommentFormId${reportedComment.commentId}" class="input-wrap" type="hidden" value="${reportedComment.commentId}"/>
                                                    </form:label>
                                                    <button type="submit" class="btn btn-secondary"><spring:message code="button.ignore"/></button>
                                                </form:form>
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
                    <c:if test="${not empty reportedComments || rComPage != 1}">
                        <ul class="pagination justify-content-center">
                            <c:choose>
                            <c:when test="${rComPage == 1}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item ">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage-1}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Previous">
                                    <span aria-hidden="true">&lsaquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${reportedComments.size() < pageSize}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage+1}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage}"/>" aria-label="Next">
                                    <span aria-hidden="true">&rsaquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </ul>
                    </c:if>
                </c:if>
                <!-- reported content -->
                <div class="page-title"><spring:message code="moderate.content.title"/></div>
                <div class="page-description"></div>
                <div class="row justify-content-center">
                    <c:choose>
                        <c:when test="${not empty reportedContents}">
                            <div class="d-flex flex-column">
                                <c:forEach items="${reportedContents}" var="reportedContent">
                                    <div class="card d-flex flex-wrap emphasis emph-comment mb-2 verified">
                                        <div class="card-body my-auto">
                                            <div class="border-bottom">
                                            <p class="my-1"><spring:message code="moderate.report.content.owner"/>:&nbsp;<a href="<c:url value="/users/${reportedContent.userNameOwner}"/>"><c:out value="${reportedContent.userNameOwner}" default=""/></a></p>
                                            <p class="my-1"><spring:message code="moderate.report.tech"/>:&nbsp;<a href="<c:url value="/${reportedContent.categoryAsString}/${reportedContent.frameworkId}"/>"><c:out value="${reportedContent.frameworkName}" default=""/></a></p>
                                            <p class="my-1"><spring:message code="moderate.content.description"/>:&nbsp;<a href="<c:url value="${reportedContent.link}"/>"><c:out value="${reportedContent.title}" default=""/></a></p>
                                            </div>
                                            <div class="mt-2">
                                            <p class="my-1"><spring:message code="moderate.report.description"/>:&nbsp;<c:out value=" ${reportedContent.reportDescription}" default=""/></p>
                                            <p class="my-1"><spring:message code="moderate.report.quantity"/>:&nbsp;${reportedContent.reportsUserName.size()}</p>
                                            </div>
                                        </div>
                                        <div class="card-footer row mx-2 justify-content-center">
                                            <c:url value="/mod/content/delete" var="postPathDeleteContent"/>
                                            <form:form modelAttribute="deleteContentForm" action="${postPathDeleteContent}" method="post">
                                                <form:label path="deleteContentId">
                                                    <form:input path="deleteContentId" id="deleteContentFormId${reportedContent.contentId}" class="input-wrap" type="hidden" value="${reportedContent.contentId}"/>
                                                </form:label>
                                                <button type="submit" class="btn btn-danger"><spring:message code="button.delete"/></button>
                                            </form:form>
                                            <c:url value="/mod/content/ignore" var="postPathIgnoreContent"/>
                                            <form:form modelAttribute="ignoreContentForm" action="${postPathIgnoreContent}" method="post">
                                                <form:label path="ignoreContentId">
                                                    <form:input id="ignoreContentFormId${reportedContent.contentId}" class="input-wrap" path="ignoreContentId" type="hidden" value="${reportedContent.contentId}"/>
                                                </form:label>
                                                <button type="submit" class="btn btn-secondary"><spring:message code="button.ignore"/></button>
                                            </form:form>

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
                    <c:if test="${not empty reportedContents || rConPage != 1}">
                        <ul class="pagination justify-content-center">
                            <c:choose>
                            <c:when test="${rConPage == 1}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item ">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage-1}"/>" aria-label="Previous">
                                    <span aria-hidden="true">&lsaquo;</span>
                                    <span class="sr-only">Previous</span>
                                </a>
                            </li>
                            <c:choose>
                            <c:when test="${reportedContents.size() < pageSize}">
                            <li class="page-item disabled">
                                </c:when>
                                <c:otherwise>
                            <li class="page-item">
                                </c:otherwise>
                                </c:choose>
                                <a class="page-link" href="<c:url value="/mod?modsPage=${modsPage}&rComPage=${rComPage}&applicantsPage=${applicantsPage}&verifyPage=${verifyPage}&rConPage=${rConPage+1}"/>" aria-label="Next">
                                    <span aria-hidden="true">&rsaquo;</span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </li>
                        </ul>
                    </c:if>
            </div>
        </div>

        
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
