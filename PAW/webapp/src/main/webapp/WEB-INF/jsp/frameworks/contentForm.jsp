<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url value="/content" var="postPath"/>
<form:form modelAttribute="contentForm" action="${postPath}" method="post">
    <div>
        <form:label path="title">Title <form:input  path="title" type="text"/></form:label>
        <form:label path="frameworkId"><form:input  path="frameworkId" type="hidden" value="${param.frameworkId}"/></form:label>
    </div>
    <div class="form-group">
        <form:label path="type">Select Type
        <div class="input-group col-xs-8">
            <form:select path="type" class="form-control" name="newRating" id="newRating">
                <form:option value="book">Bibliography</form:option>
                <form:option value="course">Course</form:option>
                <form:option value="tutorial">Tutorial</form:option>
            </form:select>
        </div>
        </form:label>
    </div>
    <div>
        <form:label path="link">Link <form:input  path="link" type="text"/></form:label>
    </div>
    <!--<input type="file" id="fileElem" multiple accept="application/pdf" style="display:none" onchange="handleFiles(this.files)">
    <a href="javascript:selectFiles()">Select some files</a>-->
    <div>
        <input class="btn primary-button" type="submit" value="SUBMIT"/>
    </div>
   <!-- <button type="submit" class="btn primary-button d-flex align-items-center justify-content-center">SUBMIT</button>-->
</form:form>


