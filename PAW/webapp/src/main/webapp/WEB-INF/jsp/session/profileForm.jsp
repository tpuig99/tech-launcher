<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>

<c:url value="/users/${param.username}" var="path" />
<form:form modelAttribute="profileForm" action="${path}" method="post">

    <div class="form-group">
        <div><form:label path="description">Description</form:label></div>
        <div><form:input  path="description"  class="form-control" type="text" value="${param.description}"/></div>
        <form:errors path="description" element="p" cssError="formError"/>
    </div>

    <!--<input type="file" id="fileElem" multiple accept="application/pdf" style="display:none" onchange="handleFiles(this.files)">
    <a href="javascript:selectFiles()">Select some files</a>-->
    <div class="d-flex justify-content-center">
        <input class="btn primary-button" type="submit" value="SUBMIT"/>
    </div>
    <!-- <button type="submit" class="btn primary-button d-flex align-items-center justify-content-center">SUBMIT</button>-->
</form:form>


</body>