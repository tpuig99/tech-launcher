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
                        <div class="col">
                            <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                        </div>
                        <div class="col-8">

                            <div class="row">
                                <div class="col">
                                <span class="framework-title"><h2>${framework.frameworkname}</h2></span>
                                </div>
                                <div class="col d-flex align-items-center justify-content-center">
                                <span class="fa fa-star"></span>
                                <span>4.5 | 10M</span>
                                </div>
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
                        <li class="list-group-item"><a href="https://tutorialesenpdf.com/angular/">Dummy data</a></li>
                    </ul>
                </div>


                <!-- Comments -->
                <div>
                    <h4 class="title">Comentarios </h4>
                </div>

                <div class="container d-flex">
                    <div class="card comment-card margin-left">
                        <div class="card-body">
                            <div class="row">
                                <span><h6 class="card-subtitle mb-2 text-muted">Nombre del user</h6></span>
                                <span>
                                    <button class="btn" data-toggle="modal" data-target="#exampleModal">
                                        <i class="fa fa-arrow-up arrow"></i>
                                    </button>
                                </span>
                                <span class="padding-left d-flex align-items-center justify-content-end ">
                                    <button class="btn" data-toggle="modal" data-target="#exampleModal">
                                        <i class="fa fa-arrow-down arrow"></i>
                                    </button>
                                </span>
                            </div>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                        </div>
                    </div>
                    <div class="card comment-card margin-left">
                        <div class="card-body">
                            <div class="row">
                                <span><h6 class="card-subtitle mb-2 text-muted">Nombre del user</h6></span>
                                <span class="fa fa-arrow-up margin-left arrow"></span><span class="padding-left fa fa-arrow-down arrow"></span>
                            </div>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                        </div>
                    </div>
                    <div class="card comment-card margin-left">
                        <div class="card-body">
                            <div class="row">
                                <span><h6 class="card-subtitle mb-2 text-muted ">Nombre del user</h6></span>
                                <span class="fa fa-arrow-up margin-left arrow"></span><span class="padding-left fa fa-arrow-down arrow"></span>
                            </div>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                        </div>
                    </div>
                </div>

                <!-- User Interaction -->
                <div>
                    <h4 class="title">Da tu opinión a los demás</h4>
                </div>

                <div class="margin-left">
                    <div class="row">
                        <div class="col-8">
                            <h5>Deja tu comentario </h5>
                            <div>
                                <textarea class="form-control" aria-label="With textarea"></textarea>
                                <button type="button" data-toggle="modal" data-target="#exampleModal" class="btn btn-primary margin-top d-flex justify-content-flex-end">PUBLICAR</button>
                            </div>
                        </div>
                        <div class="col">
                            <h5>Valora este framework</h5>
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex align-items-center justify-content-center">
                                        <span class="fa fa-star fa-lg checked"></span>
                                        <span class="fa fa-star fa-lg checked"></span>
                                        <span class="fa fa-star fa-lg checked"></span>
                                        <span class="fa fa-star fa-lg checked"></span>
                                        <span class="fa fa-star fa-lg checked"></span>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>

                </div>

                <!-- Competition Cards -->

                <div>
                    <h4 class="title">Quizás también te interese</h4>
                </div>

                <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner margin-bottom">
                        <div class="carousel-item active">
                            <div class="container d-flex">
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">Angular</div>
                                </div>
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">CSS</div>
                                </div>
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">html</div>
                                </div>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <div class="container d-flex">
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">Angular</div>
                                </div>
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">CSS</div>
                                </div>
                                <div class="card mini-card margin-left">
                                    <div class="card-body">
                                        <div><img class="mini-img" src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                                    </div>
                                    <div class="card-footer">html</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                        <span class="carousel-control-prev-icon carousel-color" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>

                <!-- Modal -->
                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Dejanos tu email</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="exampleInputEmail1">Email address</label>
                                        <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
                                    </div>
                                    <button type="submit" class="btn btn-primary d-flex align-items-center justify-content-center">Submit</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>


                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>
    </body>
</html>
