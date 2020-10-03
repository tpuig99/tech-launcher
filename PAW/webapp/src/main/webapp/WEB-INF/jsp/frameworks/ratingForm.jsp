<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/form.css"/>"/>
</head>
<body>
<c:url value="/rate" var="postPath" />
<form:form modelAttribute="ratingForm" action="${postPath}" method="post">

    <form:label path="frameworkId"><form:input id="ratingFormFrameworkId" class="input-wrap" path="frameworkId" type="hidden" value="${param.frameworkId}"/></form:label>

    <form:radiobutton path="rating" value="5" class="star star-5" id="star-5" name="star"/>
    <form:label path="rating" class="star star-5" for="star-5"/>

    <form:radiobutton path="rating" value="4" class="star star-4" id="star-4" name="star"/>
    <form:label path="rating" class="star star-4" for="star-4"/>

    <form:radiobutton path="rating" value="3" class="star star-3" id="star-3" name="star"/>
    <form:label path="rating" class="star star-3" for="star-3"/>

    <form:radiobutton path="rating" value="2" class="star star-2" id="star-2" name="star"/>
    <form:label path="rating" class="star star-2" for="star-2"/>

    <form:radiobutton path="rating" value="1" class="star star-1" id="star-1" name="star"/>
    <form:label path="rating" class="star star-1" for="star-1"/>
    <c:choose>
        <c:when test="${param.username != 'anonymousUser'}">
            <input class="btn primary-button" type="submit" value="RATE"/>
        </c:when>
        <c:otherwise>
            <button class="btn primary-button"  data-toggle="modal" data-target="#loginModal">RATE</button>
        </c:otherwise>
    </c:choose>
</form:form>
</body>

