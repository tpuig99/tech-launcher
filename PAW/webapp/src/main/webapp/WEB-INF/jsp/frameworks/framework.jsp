<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Framework</title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="<c:url value="/styles/framework.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/styles/base_page.css"/>"/>
    </head>

    <body>
        <div>

            <jsp:include page="../components/navbar.jsp"/>
            <jsp:include page="../components/sidebar.jsp"/>

            <div class="content">

                <div class="container d-flex">
                    <div class="row">
                        <div class="col-4">
                            <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                        </div>
                        <div class="col">
                            <div class="framework-title">
                                <span><h2>${framework.frameworkname}</h2></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star checked"></span>
                                <span class="fa fa-star"></span>
                            </div>
                            <div class="description">
                                ${framework.description}
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Bibliography -->
                <div>
                   <h4 class="title ">Bibliografía</h4>
                    <ul class="list-group margin-left list-group-flush description">
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Libro de Angular</a></li>
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Esto es un librito</a></li>
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Acá Tami hubiese puesto perro</a></li>
                    </ul>
                </div>
                <!-- Tutorials -->
                <div>
                   <h4 class="title "> Videos</h4>
                    <ul class="list-group margin-left list-group-flush description">
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Aprender Angular</a></li>
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Aca va un tutorial</a></li>
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Este es un video</a></li>
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Dummy data</a></li>
                    </ul>
                </div>

                <div>
                    <h4 class="title">Quizás también te interese</h4>
                </div>

                <!-- Competition Cards -->
                <div class="container d-flex">

                    <div class="card mini-card margin-left">
                        <div class="card-body">
                            <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                        </div>
                        <div class="card-footer">Angular</div>
                    </div>
                </div>

                <!-- Comments -->
                <div>
                    <h4 class="title">Dejá tu comentario </h4>
                </div>
                <div class="description margin-left">
                    <textarea class="form-control" aria-label="With textarea"></textarea>
                </div>

                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>
    </body>
</html>
