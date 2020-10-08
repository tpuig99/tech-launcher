<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>
        <spring:message code="update_tech.wref"/>
    </title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
        <spring:message code="update_tech.title"/>
    </div>
    <div class="page-description"></div>

    <c:url value="/update_tech" var="postPath"/>
    <form:form modelAttribute="frameworkForm" action="${postPath}" method="post" id="addTechForm" enctype="multipart/form-data">
        <form:input type="hidden"  path="frameworkId"/>

        <div class="form-group">
            <spring:message code="add_tech.name.placeholder" var="techname_placeholder" />
            <form:label path="frameworkName"><spring:message code="add_tech.name"/></form:label>
            <form:input class="form-control" type="text" path="frameworkName" placeholder="${techname_placeholder}"/>
            <form:errors path="frameworkName" cssClass="formError" element="p"/>
            <form:errors cssClass="formError" element="p"/>

        </div>

        <div class="form-group">
                <form:label path="category"><spring:message code="add_tech.category"/></form:label>
                <form:select path="category" class="form-control" onfocus='this.size=5;' onblur='this.size=1;' onchange='this.size=1; this.blur();'>
                    <form:option value="Back-End Development"><spring:message code="category.back_end"/></form:option>
                    <form:option value="Big Data"><spring:message code="category.big_data"/></form:option>
                    <form:option value="Business"><spring:message code="category.business"/></form:option>
                    <form:option value="Artificial Intelligence"><spring:message code="category.artificial_intelligence"/></form:option>
                    <form:option value="Networking"><spring:message code="category.networking"/></form:option>
                    <form:option value="Security"><spring:message code="category.security"/></form:option>
                    <form:option value="Front-End Development"><spring:message code="category.front_end"/></form:option>
                    <form:option value="Platforms"><spring:message code="category.platforms"/></form:option>
                    <form:option value="Gaming"><spring:message code="category.gaming"/></form:option>
                    <form:option value="Editors"><spring:message code="category.editors"/></form:option>
                    <form:option value="Development Environment"><spring:message code="category.development"/></form:option>
                    <form:option value="Databases"><spring:message code="category.databases"/></form:option>
                    <form:option value="Media"><spring:message code="category.media"/></form:option>
                    <form:option value="Functional Programming"><spring:message code="category.functional"/></form:option>
                    <form:option value="Imperative Programming"><spring:message code="category.imperative"/></form:option>
                    <form:option value="Object Oriented Programming"><spring:message code="category.object_oriented"/></form:option>
                </form:select>
            <form:errors path="category" cssClass="formError" element="p"/>
        </div>

        <div class="form-group">
            <form:label path="type"><spring:message code="add_tech.type"/></form:label>
            <form:select path="type" class="form-control" onfocus='this.size=5;' onblur='this.size=1;' onchange='this.size=1; this.blur();'>
                <form:option value="Online Platform"><spring:message code="type.online_platform"/></form:option>
                <form:option value="Framework"><spring:message code="type.framework"/></form:option>
                <form:option value="Service"><spring:message code="type.service"/></form:option>
                <form:option value="Database System"><spring:message code="type.database_system"/></form:option>
                <form:option value="Programming Language"><spring:message code="type.programming_language"/></form:option>
                <form:option value="Operating System"><spring:message code="type.operating_system"/></form:option>
                <form:option value="Runtime Platform"><spring:message code="type.runtime_platform"/></form:option>
                <form:option value="Libraries"><spring:message code="type.libraries"/></form:option>
                <form:option value="Engine"><spring:message code="type.engine"/></form:option>
                <form:option value="Shell"><spring:message code="type.shell"/></form:option>
                <form:option value="Terminal"><spring:message code="type.terminal"/></form:option>
                <form:option value="Application"><spring:message code="type.application"/></form:option>
                <form:option value="Text Editor"><spring:message code="type.text_editor"/></form:option>
                <form:option value="CSS Modifier"><spring:message code="type.css_modifier"/></form:option>
                <form:option value="API"><spring:message code="type.api"/></form:option>
                <form:option value="Toolkit"><spring:message code="type.toolkit"/></form:option>
                <form:option value="IDE"><spring:message code="type.ide"/></form:option>
            </form:select>
            <form:errors path="type" cssClass="formError" element="p"/>
        </div>

        <div class="form-group">
            <form:label path="description"><spring:message code="add_tech.description"/></form:label>
            <form:textarea  path="description"  class="form-control" type="textarea"/>
            <form:errors path="description" element="p" cssClass="formError"/>
        </div>

        <div class="form-group">
            <form:label path="introduction"><spring:message code="add_tech.introduction"/></form:label>
            <form:textarea path="introduction"  class="form-control" type="text"/>
            <form:errors path="introduction" element="p" cssClass="formError"/>
        </div>

        <div class="form-group">
            <form:label path="picture"><spring:message code="add_tech.picture"/></form:label>
            <div class="text-center">
                <form:input path="picture" class="form-control" type="file" accept="image/*" />
            </div>
            <form:errors path="picture" element="p" cssClass="formError"/>
        </div>

        <div class="d-flex justify-content-center mt-4">
            <input class="btn btn-primary" id="addTechButton" type="submit" value="<spring:message code="button.submit"/>"/>
            <div class="btn btn-primary disabled" id="addTechLoading" hidden>
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                <spring:message code="button.loading"/>
            </div>
        </div>
    </form:form>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>


<script>
    $(document).ready(function() {
        $('#addTechForm').on('submit', function(){
            $('#addTechButton').prop("hidden",true);
            $('#addTechLoading').prop("hidden",false);
        });
    });
</script>

</body>
</html>
