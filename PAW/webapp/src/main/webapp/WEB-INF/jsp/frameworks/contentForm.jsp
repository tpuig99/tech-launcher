<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>

<form:form modelAttribute="contentForm" action="/content" method="post">
    <div>
        <form:label path="title">Title <form:input  path="title" type="text"/></form:label>
    </div>
    <div class="form-group">
        <label for="contentType">Select Type</label>
        <div class="input-group col-xs-8">
            <select id="contentType" class="form-control" name="newRating" id="newRating">
                <option value="book">Bibliography</option>
                <option value="course">Course</option>
                <option value="tutorial">Tutorial</option>
            </select>
        </div>
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
</body>
</html>
