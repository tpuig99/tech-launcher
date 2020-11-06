<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Tech Launcher - Add Post</title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/posts.css"/>"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="connected" value="${user.authenticated}"/>
        <jsp:param name="username" value="${user.name}"/>
        <jsp:param name="isMod" value="${user_isMod}"/>
    </jsp:include>


    <div class="content-no-sidebar">
        <div class="page-title">
            <spring:message code="add_tech.title"/>
        </div>
        <div class="page-description"></div>

        <c:url value="/posts/addPost/add" var="postPath"/>
        <form:form modelAttribute="addPostForm" action="${postPath}" method="post" id="addTechForm" enctype="multipart/form-data">
            <form:errors cssClass="formError" element="p"/>
            <div class="form-group">
                <spring:message code="add_tech.name.placeholder" var="techname_placeholder" />
                <form:label path="title"><spring:message code="add_tech.name"/></form:label>
                <form:input class="form-control" type="text" path="title" placeholder="${techname_placeholder}"/>
                <form:errors path="title" cssClass="formError" element="p"/>
            </div>

            <div class="form-group">
                <form:label path="description"><spring:message code="add_tech.description"/></form:label>
                <form:textarea  path="description"  class="form-control" type="textarea"/>
                <form:errors path="description" element="p" cssClass="formError"/>
            </div>


            <div class="d-flex justify-content-center mt-4">
                <a href="<c:url value="/"/>" class="btn btn-danger mr-4"><spring:message code="button.cancel"/></a>
                <input class="btn btn-primary" id="addTechButton" type="submit" value="<spring:message code="button.submit"/>"/>
                <div class="btn btn-primary disabled" id="addTechLoading" hidden>
                    <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    <spring:message code="button.loading"/>
                </div>
            </div>
        </form:form>
    </div>

    <script>
        $(document).ready(function() {
            $('#addTechForm').on('submit', function(){
                $('#addTechButton').prop("hidden",true);
                $('#addTechLoading').prop("hidden",false);
            });
        });
    </script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
