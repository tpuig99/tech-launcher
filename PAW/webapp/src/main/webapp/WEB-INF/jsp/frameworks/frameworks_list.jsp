<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>
        Search Result - Tech Launcher
    </title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/search.css"/>"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>

<jsp:include page="../components/navbar-search.jsp">
    <jsp:param name="connected" value="${user}"/>
    <jsp:param name="username" value="${user.name}"/>
    <jsp:param name="isMod" value="${user_isMod}"/>
</jsp:include>

<div class="content-search">
    <div class="sidebar-search">
        <section class="option">
            <div>Category:</div>
            <select class="browser-default custom-select" aria-label="categories" id="categories">
                <option value="" disabled selected hidden>Select Category</option>
                <option value="Back-End Development">Back-End Development</option>
                <option value="Big Data">Big Data</option>
                <option value="Business">Business</option>
                <option value="Artificial Intelligence">Artificial Intelligence</option>
                <option value="Networking">Networking</option>
                <option value="Security">Security</option>
                <option value="Front-End Development">Front-End Development</option>
                <option value="Platforms">Platforms</option>
                <option value="Gaming">Gaming</option>
                <option value="Editors">Editors</option>
                <option value="Development Environment">Development Environment</option>
                <option value="Databases">Databases</option>
                <option value="Media">Media</option>
            </select>
        </section>
        <section class="option">
            <div>Type:</div>
            <select class="browser-default custom-select" aria-label="types" id="types">
                <option value="" disabled selected hidden>Select Type</option>
                <option value="Online Platform">Online Platform</option>
                <option value="Framework">Framework</option>
                <option value="Service">Service</option>
                <option value="Database System">Database System</option>
                <option value="Programming Languages">Programming Languages</option>
                <option value="Operating System">Operating System</option>
                <option value="Runtime Plataform">Runtime Plataform</option>
                <option value="Libraries">Libraries</option>
                <option value="Engine">Engine</option>
                <option value="Shell">Shell</option>
                <option value="Terminal">Terminal</option>
                <option value="Application">Application</option>
                <option value="Text Editor">Text Editor</option>
                <option value="CSS Modifier">CSS Modifier</option>
                <option value="API">API</option>
                <option value="Toolkit">Toolkit</option>
                <option value="IDE">IDE</option>
            </select>
        </section>
    </div>

    <div class="search-bar">
        <form class="form-inline my-2 my-lg-0" method="post" onsubmit="searchFrameworks()" id="search">
            <input id="searchInput" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" size="80">
            <button class="btn my-2 my-sm-0 primary-button" type="button" onclick="searchFrameworks()">SEARCH</button>
        </form>
    </div>
    <div class="page-description"></div>
    <div class="page-title">
        <h2>Search Results for: ${search_result}</h2>
    </div>
    <div class="page-description"></div>

    <c:choose>
        <c:when test="${matchingFrameworks.size() == 0 }">
            <div>
                We are sorry, there were no results matching "${search_result}", but you can try again some other day, as we are continuously adding content!
            </div>
        </c:when>
        <c:otherwise>
            <div class="row equal">
                <c:forEach var="framework" items="${matchingFrameworks}" >
                    <div class="card mx-4 mb-4">
                        <a href="<c:url value="/${framework.frameCategory}/${framework.id}"/>">
                            <div class="card-body">
                                <div class="max-logo d-flex align-items-center justify-content-center"><img src="${framework.logo}" alt="${framework.name} logo"></div>
                            </div>
                            <div class="card-footer text-dark">${framework.name}</div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function isEmpty( input ){
        for (let i = 0; i < input.length; i++) {
            if(input.charAt(i) !== " " ){
                return false;
            }
        }

        return true;
    }

    function searchFrameworks() {
        let input = document.getElementById("searchInput").value
        let category = document.getElementById("categories").value
        let type = document.getElementById("types").value

        if( !(isEmpty(input) && isEmpty(category) && isEmpty(type)) ) {
            window.location.href = "<c:url value="/search"/>?" + 'toSearch=' + input + '&category=' + category + '&type=' + type;
            return;
        }
        window.location.reload();
    }

    form = document.getElementById("search").addEventListener('submit', e => {
        e.preventDefault();
        searchFrameworks();
    })

</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
