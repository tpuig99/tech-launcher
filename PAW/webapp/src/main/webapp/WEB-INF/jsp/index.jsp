<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
    <head>
        <title>
            Index Page
        </title>

        <link rel="stylesheet" type="text/css" href="<c:url value="/styles/base_page.css"/>"/>
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    </head>
    <body>

    <jsp:include page="components/navbar.jsp"/>
    <jsp:include page="components/sidebar.jsp"/>

    <div class="content">
        <div class="page-title">
            <h2>${framework.category}</h2>
        </div>
        <div class="page-description">

        </div>
        <div class="row equal">
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt="">
                    </div>
                    <div class="card-footer">F</div>
                </div>
            </div>
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                    </div>
                    <div class="card-footer">CSS</div>
                </div>
            </div>
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                    </div>
                    <div class="card-footer">HTML5</div>
                </div>
            </div>
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                    </div>
                    <div class="card-footer">jQuery</div>
                </div>
            </div>
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                    </div>
                    <div class="card-footer">React</div>
                </div>
            </div>
            <div class="col-md-3 d-flex pb-3">
                <div class="card">
                    <div class="card-body">
                        <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                    </div>
                    <div class="card-footer">D3</div>
                </div>
            </div>
        </div>
    </div>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>
