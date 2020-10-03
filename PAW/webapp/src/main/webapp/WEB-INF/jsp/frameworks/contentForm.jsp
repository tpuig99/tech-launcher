<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>

<c:url value="/content" var="postPath" />
<form:form modelAttribute="contentForm" id="contentForm" action="${postPath}" method="post">
    <div class="form-group">
        <div><form:label path="title"><spring:message code="tech.content.form.title"/></form:label></div>
        <div><form:input  path="title"  class="form-control" type="text"/></div>
        <form:errors path="title" element="p" cssClass="formError"/>
    </div>
    <form:label path="frameworkId"><form:input  class="input-wrap" path="frameworkId" type="hidden" value="${param.frameworkId}"/></form:label>

    <div class="form-group">
        <div>
        <form:label path="type"><spring:message code="tech.content.form.select_type"/></form:label>
        </div>
        <div>
            <form:select path="type" class="form-control" name="newRating" id="newRating">
                <form:option value="book"><spring:message code="tech.content.bibliography"/></form:option>
                <form:option value="course"><spring:message code="tech.content.course"/></form:option>
                <form:option value="tutorial"><spring:message code="tech.content.tutorial"/></form:option>
            </form:select>
        </div>
    </div>
    <div class="form-group">
        <div>
            <form:label path="link"><spring:message code="tech.link"/></form:label>
        </div>
        <div>
            <form:input  path="link" class="form-control" type="text" />
        </div>
        <form:errors path="link" element="p" cssClass="formError"/>


    </div>
    <!--<input type="file" id="fileElem" multiple accept="application/pdf" style="display:none" onchange="handleFiles(this.files)">
    <a href="javascript:selectFiles()">Select some files</a>-->
    <div class="d-flex justify-content-center">
        <input class="btn btn-primary" id="contentFormButton" type="submit" value="<spring:message code="button.submit"/>"/>
        <div class="btn btn-primary disabled" id="contentLoading" hidden>
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <spring:message code="button.loading"/>
        </div>
    </div>
</form:form>

<script>
    $(document).ready(function() {
        $('#contentForm').on('submit', function(e){
            $("#contentFormButton").prop("hidden",true);
            $("#contentLoading").prop("hidden",false);
        });
    });
</script>

</body>

