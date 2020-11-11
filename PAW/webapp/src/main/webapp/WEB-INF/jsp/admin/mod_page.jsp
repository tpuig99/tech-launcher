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
    <div class="page-title-big row"><spring:message code="moderate.moderation_panel"/></div>

    <!-- Mods managing -->
    <%! public String tab = "";%>
    <div class="container">
        <ul class="nav nav-tabs" id="mod-tab">
            <li class="nav-item">
                <a class="nav-link" href="#promote" data-toggle="tab" role="tab" aria-controls="promote" aria-selected="true"><spring:message code="moderate.promote"/></a>
            </li>
            <c:if test="${isAdmin}">
                <li class="nav-item">
                    <a class="nav-link" href="#demote" data-toggle="tab" role="tab" aria-controls="demote" aria-selected="false" ><spring:message code="moderate.demote"/></a>
                </li>
            </c:if>
            <li class="nav-item">
                <a class="nav-link" href="#reports" data-toggle="tab" role="tab" aria-controls="reports" aria-selected="false"><spring:message code="moderate.see_reports"/></a>
            </li>
        </ul>

        <div class="tab-content">
            <!-- PROMOTE -->
            <div class="tab-pane active" id="promote">
                <div class="add-margin"><h5><spring:message code="moderate.promote_description"/></h5></div>
                <div class="row">
                    <div class="col-6">
                        <!-- pending to verify by comments -->
                        <div class="page-title"><spring:message code="moderator.pending"/> (${verifyAmount})</div>
                        <div class="page-description"></div>
                        <div class="d-flex flex-column justify-content-center">
                            <c:choose>
                                <c:when test="${not empty pendingToVerify || verifyPage != 1}">
                                    <c:forEach var = "pendingUser" items="${pendingToVerify}">
                                        <div class="card emphasis emph-comment row mb-2 mx-4 verified">
                                            <div class="card-body row mt-1">
                                                <div class="col-3 secondary-font"> <a href="<c:url value="/users/${pendingUser.userName}"/>"><c:out value="${pendingUser.userName}" default=""/></a>
                                                    <c:out value="/" default=""/>
                                                    <a href="<c:url value="/techs/${pendingUser.frameworkName}/${pendingUser.frameworkId}"/>"><c:out value="${pendingUser.frameworkName}" default=""/></a>
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
                            <!-- Pendings pagination -->
                            <jsp:include page="../components/pagination.jsp">
                                <jsp:param name="total" value="${verifyAmount}"/>
                                <jsp:param name="page" value="${verifyPage}"/>
                                <jsp:param name="page_size" value="${pageSize}"/>
                                <jsp:param name="origin" value="mod_verify"/>
                                <jsp:param name="modsPage" value="${modsPage}"/>
                                <jsp:param name="rComPage" value="${rComPage}"/>
                                <jsp:param name="applicantsPage" value="${applicantsPage}"/>
                                <jsp:param name="verifyPage" value="${verifyPage}"/>
                                <jsp:param name="rConPage" value="${rConPage}"/>
                            </jsp:include>
                        </c:if>
                    </div>
                    <div class="col">
                        <!-- pending applicants to verify -->
                        <div class="page-title"><spring:message code="moderator.pendingApplicants"/>  (${applicantsAmount})</div>
                        <div class="page-description"></div>
                        <div class="d-flex flex-wrap justify-content-center">
                            <c:choose>
                                <c:when test="${not empty pendingApplicants || applicantsPage != 1}">
                                    <c:forEach var = "applicant" items="${pendingApplicants}">
                                        <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                            <div class="card-body mt-1">
                                                <div class="secondary-font"> <a href="<c:url value="/users/${applicant.userName}"/>"><c:out value="${applicant.userName}" default=""/></a>
                                                    <c:out value="/" default=""/>
                                                    <a href="<c:url value="/techs/${applicant.frameworkName}/${applicant.frameworkId}"/>"><c:out value="${applicant.frameworkName}" default=""/></a>
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
                            <!-- Applicants pagination -->
                            <jsp:include page="../components/pagination.jsp">
                                <jsp:param name="total" value="${applicantsAmount}"/>
                                <jsp:param name="page" value="${applicantsPage}"/>
                                <jsp:param name="page_size" value="${pageSize}"/>
                                <jsp:param name="origin" value="mod_applicants"/>
                                <jsp:param name="modsPage" value="${modsPage}"/>
                                <jsp:param name="rComPage" value="${rComPage}"/>
                                <jsp:param name="applicantsPage" value="${applicantsPage}"/>
                                <jsp:param name="verifyPage" value="${verifyPage}"/>
                                <jsp:param name="rConPage" value="${rConPage}"/>
                            </jsp:include>
                        </c:if>
                    </div>
                </div>

            </div>
            <!-- DEMOTE -->
            <div class="tab-pane" id="demote">
                <div class="add-margin"><h5><spring:message code="moderate.demote_description"/></h5></div>
                <div class="page-title"><spring:message code="moderator.title"/>  (${modsAmount})</div>
                <div>
                    <c:if test="${isAdmin}">
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
                                                        <a href="<c:url value="/techs/${moderator.frameworkName}/${moderator.frameworkId}"/>"><c:out value="${moderator.frameworkName}" default=""/></a>
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
                            <!-- Mods pagination -->
                            <jsp:include page="../components/pagination.jsp">
                                <jsp:param name="total" value="${modsAmount}"/>
                                <jsp:param name="page" value="${modsPage}"/>
                                <jsp:param name="page_size" value="${pageSize}"/>
                                <jsp:param name="origin" value="mod_mod"/>
                                <jsp:param name="modsPage" value="${modsPage}"/>
                                <jsp:param name="rComPage" value="${rComPage}"/>
                                <jsp:param name="applicantsPage" value="${applicantsPage}"/>
                                <jsp:param name="verifyPage" value="${verifyPage}"/>
                                <jsp:param name="rConPage" value="${rConPage}"/>
                            </jsp:include>
                        </c:if>
                    </c:if>
                </div>
            </div>
            <!-- SEE REPORTS -->
            <div class="tab-pane" id="reports">
                <div class="add-margin"><h5><spring:message code="moderate.see_reports_description"/></h5></div>
                <!-- Reported Comments -->
                <c:if test="${isAdmin}"><div class="row">
                <div class="col-6"></c:if>
                    <c:if test="${isAdmin}">
                        <div class="page-title"><spring:message code="moderate.comment.title"/>  (${reportedCommentsAmount})</div>
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
                                                        <p class="my-1"><spring:message code="moderate.report.tech"/>:&nbsp;<a href="<c:url value="/techs/${reportedComment.categoryAsString}/${reportedComment.frameworkId}"/>"><c:out value="${reportedComment.frameworkName}" default=""/></a></p>
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
                            <!-- Report Comment pagination -->
                            <jsp:include page="../components/pagination.jsp">
                                <jsp:param name="total" value="${reportedCommentsAmount}"/>
                                <jsp:param name="page" value="${rComPage}"/>
                                <jsp:param name="page_size" value="${pageSize}"/>
                                <jsp:param name="origin" value="mod_report_comment"/>
                                <jsp:param name="modsPage" value="${modsPage}"/>
                                <jsp:param name="rComPage" value="${rComPage}"/>
                                <jsp:param name="applicantsPage" value="${applicantsPage}"/>
                                <jsp:param name="verifyPage" value="${verifyPage}"/>
                                <jsp:param name="rConPage" value="${rConPage}"/>
                            </jsp:include>
                        </c:if>
                    </c:if>
                    <c:if test="${isAdmin}"></div></c:if>

                <c:if test="${isAdmin}"><div class="col"></c:if>
                <!-- reported content -->
                <div class="page-title"><spring:message code="moderate.content.title"/>  (${reportedContentAmount})</div>
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
                                                <p class="my-1"><spring:message code="moderate.report.tech"/>:&nbsp;<a href="<c:url value="/techs/${reportedContent.categoryAsString}/${reportedContent.frameworkId}"/>"><c:out value="${reportedContent.frameworkName}" default=""/></a></p>
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
                    <!-- Report Content pagination -->
                    <jsp:include page="../components/pagination.jsp">
                        <jsp:param name="total" value="${reportedContentAmount}"/>
                        <jsp:param name="page" value="${rConPage}"/>
                        <jsp:param name="page_size" value="${pageSize}"/>
                        <jsp:param name="origin" value="mod_report_content"/>
                        <jsp:param name="modsPage" value="${modsPage}"/>
                        <jsp:param name="rComPage" value="${rComPage}"/>
                        <jsp:param name="applicantsPage" value="${applicantsPage}"/>
                        <jsp:param name="verifyPage" value="${verifyPage}"/>
                        <jsp:param name="rConPage" value="${rConPage}"/>
                    </jsp:include>
                </c:if>
                <c:if test="${isAdmin}"></div></c:if>
                <c:if test="${isAdmin}"></div></c:if>
            </div>
        </div>
    </div>
</div>


<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
<script>
    $(document).ready(function() {
        tab = "${selectTab}";
        let value = '#mod-tab a[href="#'+tab+'"]';
        $(value).tab('show');
    });
    $(function () {
        $('.nav-link').click(function () {
            tab = $(this).attr('href').replace("#","");
        })
    });

</script>
