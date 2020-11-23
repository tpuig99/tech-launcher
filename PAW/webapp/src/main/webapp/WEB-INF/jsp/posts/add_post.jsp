<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Tech Launcher - Add Post</title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/posts.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.css" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>

    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="connected" value="${user.authenticated}"/>
        <jsp:param name="username" value="${user.name}"/>
        <jsp:param name="isMod" value="${user_isMod}"/>
    </jsp:include>


    <div class="content-no-sidebar-left">
        <div class="title">
            <h1><spring:message code="post.add_post"/></h1>
        </div>
        <div class="page-description"></div>

        <c:url value="/posts/addPost/add" var="postPath"/>
        <form:form modelAttribute="addPostForm" action="${postPath}" method="post" id="addPostForm" >
            <div class="form-group">
                <spring:message code="add_tech.name.placeholder" var="techname_placeholder" />
                <form:label path="title"><h4><spring:message code="add_tech.name"/></h4></form:label>
                <form:input class="form-control" type="text" path="title" placeholder="${techname_placeholder}"/>
                <form:errors path="title" cssClass="formError" element="p"/>
            </div>

            <div class="form-group">
                <form:label path="description"><h4><spring:message code="add_tech.description"/></h4></form:label>
                <form:textarea  path="description"  class="form-control" type="textarea"/>
                <form:errors path="description" element="p" cssClass="formError"/>
            </div>

            <!-- Tags Start -->
            <div><h4><spring:message code="post.tags"/></h4></div>
            <form:errors cssClass="formError" element="p"/>
            <div class="row">
                <div class="col-4">
                    <div class="search-bar sidebar-title">
                        <form:label path="names"><spring:message code="post.tech_names"/></form:label>
                        <form:select onclick="updateNameTags()" path="names" items="${frameworkNames}" multiple="true" id="selectName" class="form-control" />
                        <form:errors path="names" element="p" cssClass="formError"/>
                    </div>
                </div>
                <div class="col-4">
                    <div class="form-group">
                        <form:label path="categories"><spring:message code="add_tech.category"/></form:label>
                        <form:select onclick="updateCategoryTags()" path="categories" multiple="true" id="selectCategory" class="form-control" >
                            <c:forEach items="${categories}" var="cat">
                                <form:option value="${cat}"><spring:message code="category.${cat}"/></form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="categories" cssClass="formError" element="p"/>
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <form:label path="types"><spring:message code="add_tech.type"/></form:label>
                        <form:select onclick="updateTypeTags()" multiple="true" id="selectType" path="types" class="form-control">
                            <c:forEach items="${types}" var="typ">
                                <form:option value="${typ}"><spring:message code="type.${typ}"/></form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="types" cssClass="formError" element="p"/>
                    </div>
                </div>
            </div>


            <div>
                <c:forEach items="${categories}" var="category">
                    <div class="badge badge-color ml-1" hidden id="${category}">
                        <span><spring:message code="category.${category}"/></span>
                    </div>
                </c:forEach>
                <c:forEach items="${types}" var="type">
                    <div class="badge badge-color ml-1" hidden id="${type}">
                        <span><spring:message code="type.${type}"/></span>
                    </div>
                </c:forEach>
                <c:forEach items="${frameworkNames}" var="name">
                    <div class="badge badge-color ml-1" hidden id="${name}">
                        <span>${name}</span>
                    </div>
                </c:forEach>
            </div>


            <!-- Tags Finish -->

            <div class="d-flex justify-content-center mt-4">
                <a href="<c:url value="/posts"/>" class="btn btn-danger mr-4"><spring:message code="button.cancel"/></a>

                <input class="btn btn-info" id="addPostButton" type="button" onclick="this.form.submit()" value="<spring:message code="button.submit"/>"/>
                <div class="btn btn-info disabled" id="addPostLoading" hidden>
                    <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    <spring:message code="button.loading"/>
                </div>
            </div>
        </form:form>
    </div>

    <script>

        $(document).ready(function() {

            let values = $('#selectCategory').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }

            values = $('#selectType').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }

            values = $('#selectName').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }

            $('#editPostForm').on('submit', function(){
                $('#editPostButton').prop("hidden",true);
                $('#editPostLoading').prop("hidden",false);
            });

            $('option').mousedown(function(e) {
                e.preventDefault();
                $(this).prop('selected', !$(this).prop('selected'));
                return false;
            });

        });

        function updateNameTags() {
            let hiddenTagName;
            let hiddenAux;
            <c:forEach items="${frameworkNames}" var="name">
            hiddenTagName = '${name}';
            hiddenAux = '[id="'+hiddenTagName+'"]';
            $(hiddenAux).prop("hidden",true);
            </c:forEach>

            let values = $('#selectName').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }
        }

        function updateCategoryTags(){
            let hiddenTagName;
            let hiddenAux;
            <c:forEach items="${categories}" var="category">
            hiddenTagName = '${category}';
            hiddenAux = '[id="'+hiddenTagName+'"]';
            $(hiddenAux).prop("hidden",true);
            </c:forEach>

            let values = $('#selectCategory').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }
        }

        function updateTypeTags() {
            let hiddenTagName;
            let hiddenAux;
            <c:forEach items="${types}" var="type">
            hiddenTagName = '${type}';
            hiddenAux = '[id="'+hiddenTagName+'"]';
            $(hiddenAux).prop("hidden",true);
            </c:forEach>

            let values = $('#selectType').val();
            for (let i = 0; i < values.length ; i++) {
                let tagName = values[i];
                let aux = '[id="'+tagName+'"]';
                $(aux).prop("hidden",false);
            }
        }


    </script>

    <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha384-xBuQ/xzmlsLoJpyjoggmTEz8OWUFM0/RC5BsqQBDX2v5cMvDHcMakNTNrHIW2I5f" crossorigin="anonymous"></script>
    <%--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script
            src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous">
    </script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</body>
</html>
