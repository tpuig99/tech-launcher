<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>
        <spring:message code="category.${category}" var="categoryFormatted"/>
        <spring:message code="tech.wref"
                        arguments="${categoryFormatted}"
                        htmlEscape="true"/>
    </title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<jsp:include page="../components/navbar.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>

<div class="sidenav overflow-auto">
    <c:forEach var="category" items="${categories_sidebar}">
        <a href="<c:url value="/${category}"/>"><spring:message code="category.${category}"/></a>
    </c:forEach>
</div>

<div class="content">
    <div class="page-title">
        <h2>
            <spring:message code="category.${category}"/>
        </h2>
    </div>
    <div class="page-description">

    </div>
    <div class="row equal">
        <c:forEach items="${frameworksList}" var="framework">
            <div class="card mx-4 mb-4">
                <a href="<c:url value="/${framework.category}/${framework.id}"/>">
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
                    <div class="card-footer text-dark">${framework.name}</div>
                </a>
            </div>
        </c:forEach>
    </div>
    <!-- List pagination -->
    <jsp:include page="../components/pagination.jsp">
        <jsp:param name="total" value="${framework_amount}"/>
        <jsp:param name="page" value="${frameworks_page}"/>
        <jsp:param name="page_size" value="${page_size}"/>
        <jsp:param name="origin" value="category_list"/>
        <jsp:param name="category" value="${category}"/>
    </jsp:include>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
