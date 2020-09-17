<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>

<c:url value="/content" var="postPath"/>
<form:form modelAttribute="contentForm" action="${postPath}" method="post">
    <div class="form-group">
        <div><form:label path="title">Title</form:label></div>
        <div><form:input  path="title"  class="form-control" type="text"/></div>
        <form:errors path="title" element="p" cssError="formError"/>
    </div>
    <form:label path="frameworkId"><form:input  class="input-wrap" path="frameworkId" type="hidden" value="${param.frameworkId}"/></form:label>

    <div class="form-group">
        <div>
        <form:label path="type">Select Type</form:label>
        </div>
        <div>
            <form:select path="type" class="form-control" name="newRating" id="newRating">
                <form:option value="book">Bibliography</form:option>
                <form:option value="course">Course</form:option>
                <form:option value="tutorial">Tutorial</form:option>
            </form:select>
        </div>
    </div>
    <div class="form-group">
        <div>
            <form:label path="link">Link </form:label>
        </div>
        <div>
            <form:input  path="link" class="form-control" type="text" />
        </div>
        <form:errors path="link" element="p" cssError="formError"/>


    </div>
    <!--<input type="file" id="fileElem" multiple accept="application/pdf" style="display:none" onchange="handleFiles(this.files)">
    <a href="javascript:selectFiles()">Select some files</a>-->
    <div class="d-flex justify-content-center">
        <input class="btn primary-button" type="submit" value="SUBMIT" data-toggle="snackbar" data-content="Your content is now on a pending list"/>
    </div>
   <!-- <button type="submit" class="btn primary-button d-flex align-items-center justify-content-center">SUBMIT</button>-->
</form:form>
</body>

