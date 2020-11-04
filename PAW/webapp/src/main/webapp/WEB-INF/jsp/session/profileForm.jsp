<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>

<c:url value="/users/${param.username}" var="path" />

<form:form id="editProfileForm" modelAttribute="profileForm" action="${path}" method="post" enctype="multipart/form-data">
    <form:errors cssClass="formError" element="p"/>
    <form:input type="hidden"  path="userId"/>
    <div class="form-group">
        <form:label path="picture"><spring:message code="profile.change_picture"/></form:label>
        <div class="text-center">
            <form:input path="picture" class="form-control" type="file" accept="image/*" />
        </div>
        <form:errors path="picture" element="p" cssClass="formError"/>
    </div>

    <div class="form-group">
        <form:label path="description"><spring:message code="user.description"/></form:label>
        <form:textarea path="description"  class="form-control" type="textarea" />
        <form:errors path="description" element="p" cssError="formError"/>
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