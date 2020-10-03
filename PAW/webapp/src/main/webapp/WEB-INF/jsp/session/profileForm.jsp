<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>

<c:url value="/users/${param.username}" var="path" />

<form:form id="editProfileForm" modelAttribute="profileForm" action="${path}" method="post">

    <div class="form-group">
        <div><form:label path="description"><spring:message code="user.description"/></form:label></div>
        <div><form:input  path="description"  class="form-control" type="text" value="${param.description}"/></div>
        <form:errors path="description" element="p" cssError="formError"/>
        <div>
            <span><a href="${pageContext.request.contextPath}/chgpassword"><spring:message code="profile.change_password"/></a></span>
        </div>
    </div>

    <div class="d-flex justify-content-center">
        <input class="btn btn-primary" id="editProfileButton" type="submit" value="<spring:message code="button.submit"/>"/>
        <div class="btn btn-primary disabled" id="editProfileLoading" hidden>
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <spring:message code="button.loading"/>
        </div>
    </div>
</form:form>

<script>
    $(document).ready(function() {
        $('#editProfileForm').on('submit', function(e){
            $("#editProfileButton").prop("hidden",true);
            $("#editProfileLoading").prop("hidden",false);
        });
    });
</script>

</body>