<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <title>
            Tech Launcher/Mod Options
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
            <div class="page-title">Pending to Verify by Comments</div>
            <div class="page-description"></div>
            <c:choose>
                <c:when test="${not empty pendingToVerify}">
                    <c:forEach var = "pendingUser" items="${pendingToVerify}">
                        <div class="card emphasis emph-comment row mb-2 verified">
                            <div class="card-body row mt-1">
                                <div class="col-3 secondary-font"> <a href="/users/${pendingUser.userName}"><c:out value="${pendingUser.userName}" default=""/></a>
                                    <c:out value="/" default=""/>
                                    <a href="${pendingUser.frameworkName}/${pendingUser.frameworkId}"><c:out value="${pendingUser.frameworkName}" default=""/></a>
                                </div>
                                <div class="col-6 text-left"> <c:out value="${pendingUser.comment.description}" default=""/> </div>
                                <div class="col third-font text-right"> <c:out value="${pendingUser.comment.timestamp.toLocaleString()}" default=""/> </div>
                            </div>
                            <div class="card-footer">
                                <button type="button" class="btn btn-secondary" onclick="rejectUser(${pendingUser.verificationId})">Ignore</button>
                                <button type="button" class="btn btn-primary" onclick="promoteUser(${pendingUser.verificationId})">Promote</button>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div>It seems you have no work here. You'll have to wait for more comments to reach hot..</div>
                </c:otherwise>
            </c:choose>
            <div class="page-title">Pending Applicants</div>
            <div class="page-description"></div>
            <div class="row">
                <c:choose>
                    <c:when test="${not empty pendingApplicants}">
                        <c:forEach var = "applicant" items="${pendingApplicants}">
                            <div class="card emphasis emph-comment col-4 mb-2 applicant mx-2">
                                <div class="card-body mt-1">
                                    <div class="secondary-font"> <a href="/users/${applicant.userName}"><c:out value="${applicant.userName}" default=""/></a>
                                        <c:out value="/" default=""/>
                                        <a href="${applicant.frameworkName}/${applicant.frameworkId}"><c:out value="${applicant.frameworkName}" default=""/></a>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <button type="button" class="btn btn-primary" onclick="promoteUser(${applicant.verificationId})">Promote</button>
                                    <button type="button" class="btn btn-secondary" onclick="rejectUser(${applicant.verificationId})">Ignore</button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div>It seems you have no work here. You'll have to wait for more users to apply for mod..</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <script>
            function promoteUser(verificationId){
                window.location.href = '<c:url value="/accept" />?id=' + verificationId;
            }

            function rejectUser(verificationId){
                window.location.href = '<c:url value="/reject" />?id=' + verificationId;
            }
        </script>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
