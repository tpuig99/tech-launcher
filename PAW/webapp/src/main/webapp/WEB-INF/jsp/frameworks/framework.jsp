<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Framework</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="<c:url value="/styles/base_page.css"/>"/>
    </head>

    <body>
        <div class="wrapper">

            <jsp:include page="../components/navbar.jsp"/>
            <jsp:include page="../components/sidebar.jsp"/>

            <div class="content">

                <div class="page-title">
                    <h2>Nombre</h2>
                </div>
                <div class="page-description">
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.
                </div>

                <div>
                   <h4 class="title">Bibliografía</h4>
                    <ul class="list-group list-group-flush description">
                        <li class="list-group-item">Cras justo odio</li>
                        <li class="list-group-item">Dapibus ac facilisis in</li>
                        <li class="list-group-item">Morbi leo risus</li>
                        <li class="list-group-item">Porta ac consectetur ac</li>
                        <li class="list-group-item">Vestibulum at eros</li>
                    </ul>
                </div>
                <div>
                   <h4 class="title"> Videos</h4>
                    <ul class="list-group list-group-flush description">
                        <li class="list-group-item">Cras justo odio</li>
                        <li class="list-group-item">Dapibus ac facilisis in</li>
                        <li class="list-group-item">Morbi leo risus</li>
                        <li class="list-group-item">Porta ac consectetur ac</li>
                        <li class="list-group-item">Vestibulum at eros</li>
                    </ul>
                </div>

                <div>
                    <h4 class="title">Quizás también te interese</h4>
                </div>
                <div class="col-md-3 d-flex pb-3 description">
                    <div class="card">
                        <div class="card-body">
                            <div><img src="https://cdn.worldvectorlogo.com/logos/angular-icon.svg" alt=""></div>
                        </div>
                        <div class="card-footer">Angular</div>
                    </div>
                </div>

                <div>
                    <h4 class="title">Dejá tu comentario </h4>
                </div>
                <div class="description"><textarea class="form-control" aria-label="With textarea"></textarea></div>

                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>
    </body>
</html>