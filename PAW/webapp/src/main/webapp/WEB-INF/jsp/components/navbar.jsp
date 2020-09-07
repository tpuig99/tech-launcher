<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--NavBar -->

<head>
    <c:url value="/resources/js/search.js" var="searchJs"/>

</head>
<body>

<nav class="navbar fixed-top navbar-expand-lg">
    <a class="navbar-brand" href="/">TECH LAUNCHER</a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/">Home</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0" method="post">
            <input id="searchInput" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
            <button class="btn my-2 my-sm-0" type="button" onclick="searchFrameworks()"><i class="fas fa-search"></i></button>
        </form>
    </div>
</nav>
<!--<script type=text/javascript" src="../../../resources/js/search.js"></script>-->

<script>
    function searchFrameworks() {
        let input = document.getElementById("searchInput").value;
        console.log(input);
        let path = "/search/"+input;
        console.log(path);
        window.location.href = path;
        console.log(location.href);
    }
</script>
</body>