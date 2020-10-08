<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>
        <spring:message code="change_password.wref"/>
    </title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/register.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
    <body>

        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="connected" value="${user.authenticated}"/>
            <jsp:param name="username" value="${user.name}"/>
            <jsp:param name="isMod" value="${user_isMod}"/>
        </jsp:include>

        <div class="content-no-sidebar">
            <div class="page-title">
                <spring:message code="change_password.title"/>
            </div>
            <c:if test="${not empty errorMessage}">
                <c:out value="${errorMessage}"/>
            </c:if>
            <div class="page-description"></div>

            <c:url value="/updpassword" var="postPath"/>
            <form:form modelAttribute="passwordForm" action="${postPath}" method="post">
                <form:label path="userId"><form:input  class="input-wrap" path="userId" type="hidden" value="${user_id}"/></form:label>
                <div class="form-group">
                    <spring:message code="login.password.placeholder" var="password_placeholder" />
                    <form:label path="password"><spring:message code="login.password"/></form:label>
                    <form:input class="form-control" type="password" path="password" placeholder="${password_placeholder}"/>
                    <form:errors path="password" cssClass="formError" element="p"/>
                </div>
                <div class="form-group">
                    <spring:message code="login.password.placeholder" var="password_placeholder" />
                    <form:label path="repeatPassword"><spring:message code="login.password.repeat"/></form:label>
                    <form:input class="form-control" type="password" path="repeatPassword" placeholder="${password_placeholder}"/>
                    <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                    <form:errors cssClass="formError" element="p"/>
                </div>
                <div class="d-flex justify-content-center">
                    <c:if test="${user.name != 'anonymousUser'}">
                        <a href="/users/${user.name}" class="btn btn-danger mr-4"><spring:message code="button.cancel"/></a>
                    </c:if>
                    <input class="btn primary-button" type="submit" value="<spring:message code="button.submit"/>"/>
                </div>
            </form:form>
        </div>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
