<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Tech Launcher/${framework.name}</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/framework.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

    </head>
    <body>
        <div>

            <jsp:include page="../components/navbar.jsp"/>
            <jsp:include page="../components/sidebar.jsp"/>

            <div class="content">

                <div class="container d-flex">
                    <div class="row">
                        <div class="col-2">
                            <div><img src="${framework.logo}" alt=""></div>
                        </div>
                        <div class="col-10">

                            <div class="row">
                                <div class="col">
                                <span class="framework-title"><h2>${framework.name}</h2></span>
                                </div>
                                <div class="col d-flex align-items-center justify-content-center">
                                <span class="fa fa-star color-star"></span>
                                <span> ${framework.starsFormated}| 10M</span>
                                </div>
                            </div>
                            <div class="description">
                                ${framework.description}
                            </div>
                        </div>
                        <div class="margin-top margin-left justify">${framework.introduction}</div>
                    </div>
                </div>



                <!-- Bibliography -->
                <c:if test="${not empty books}">
                <div>
                   <h4 class="title">Bibliography</h4>
                    <ul class="list-group margin-left list-group-flush description">
                        <c:forEach var="book" items="${books}">
                        <li class="list-group-item"><a href="${book.link}">${book.title}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Courses -->
                <c:if test="${not empty courses}">
                <div>
                    <h4 class="title ">Courses</h4>

                    <ul class="list-group margin-left list-group-flush description">

                        <c:forEach var="course" items="${courses}">
                            <li class="list-group-item"><a href="${course.link}">${course.title}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Tutorials -->
                <c:if test="${not empty tutorials}">
                <div>
                   <h4 class="title "> Tutorials</h4>
                    <ul class="list-group margin-left list-group-flush description">
                        <c:forEach var="tutorial" items="${tutorials}">
                            <li class="list-group-item"><a href="${tutorial.link}">${tutorial.title}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>


                <!-- Comments -->
                <c:if test="${not empty comments}">
                <div>
                    <h4 class="title">Comments </h4>
                </div>

                <div class="container d-flex">
                    <c:forEach var="comment" items="${comments}" varStatus="loop">
                    <div class="card comment-card margin-left">
                        <div class="card-body">
                            <div class="row">
                                <span><h6 class="card-subtitle mb-2 text-muted">${comment.userId}</h6></span>
                                <span>
                                    <button class="btn" data-toggle="modal" data-target="#upVoteModal">
                                        <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                    </button>
                                </span>
                                <span class="padding-left d-flex align-items-center justify-content-end ">
                                    <button class="btn" onclick="voteDownComment(${comment.commentId})">
                                        <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                    </button>
                                </span>
                            </div>
                            <p class="card-text">${comment.description}</p>
                        </div>
                    </div>
                    </c:forEach>
                </div>
                </c:if>

                <!-- User Interaction -->
                <div>
                    <h4 class="title">Give your opinion</h4>
                </div>

                <div class="margin-left">
                    <div class="row">
                        <div class="col-8">
                            <h5>Leave your comment</h5>
                            <div>
                                <textarea id="commentInput" class="form-control" aria-label="With textarea"></textarea>
                                <button type="button" data-toggle="modal" data-target="#exampleModal" class="btn primary-button margin-top d-flex justify-content-flex-end">SUBMIT</button>
                            </div>
                        </div>
                        <div class="col">
                            <h5>Rating</h5>
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex align-items-center justify-content-center">
                                        <button type="button" data-toggle="modal" data-target="#ratingModal1" class="btn primary-button"><span class="fa fa-star fa-lg checked"></span></button>
                                        <button type="button" data-toggle="modal" data-target="#ratingModal2" class="btn primary-button"><span class="fa fa-star fa-lg checked"></span></button>
                                        <button type="button" data-toggle="modal" data-target="#ratingModal3" class="btn primary-button"><span class="fa fa-star fa-lg checked"></span></button>
                                        <button type="button" data-toggle="modal" data-target="#ratingModal4" class="btn primary-button"><span class="fa fa-star fa-lg checked"></span></button>
                                        <button type="button" data-toggle="modal" data-target="#ratingModal5" class="btn primary-button"><span class="fa fa-star fa-lg checked"></span></button>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>

                </div>

                <!-- Competition Cards -->
                <c:if test="${not empty competitors}">
                <div>
                    <h4 class="title">You may also like</h4>
                </div>


                <div class="container d-flex">
                    <c:forEach var="i" begin="0" end="4">
                    <div class="card mini-card margin-left mx-5 mb-4">
                        <a href="/frameworks/${competitors.get(i).id}">
                            <div class="card-body">
                                <div><img class="mini-img" src="${competitors.get(i).logo}" alt=""></div>
                            </div>
                            <div class="card-footer text-dark" style="height: 5em">${competitors.get(i).name} | <span class="fa fa-star fa-sm color-star"></span> ${competitors.get(i).stars}</div>
                        </a>
                    </div>

                    </c:forEach>
                </div>

                </c:if>
                
                <!-- Modal -->
                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="inputName">Name</label>
                                        <input type="text" class="form-control" id="inputName" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEmail">Email</label>
                                        <input type="email" class="form-control" id="inputEmail" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishComment()">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="upVoteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="upVoteModalLabel">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="inputName">Name</label>
                                        <input type="text" class="form-control" id="upVoteName" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEmail">Email</label>
                                        <input type="email" class="form-control" id="upVoteEmail" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishComment()">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="downVoteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="downVoteModalLabel">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="inputName">Name</label>
                                        <input type="text" class="form-control" id="downVoteName" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="inputEmail">Email</label>
                                        <input type="email" class="form-control" id="downVoteEmail" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishComment()">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="modal fade" id="ratingModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="ratingModalLabel1">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="ratingInputName1">Name</label>
                                        <input type="text" class="form-control" id="ratingInputName1" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="ratingInputEmail1">Email</label>
                                        <input type="email" class="form-control" id="ratingInputEmail1" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishRating(1)">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="ratingModal2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="ratingModalLabel2">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="ratingInputName2">Name</label>
                                        <input type="text" class="form-control" id="ratingInputName2" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="ratingInputEmail2">Email</label>
                                        <input type="email" class="form-control" id="ratingInputEmail2" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishRating(2)">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="ratingModal3" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="ratingModalLabel3">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="ratingInputName3">Name</label>
                                        <input type="text" class="form-control" id="ratingInputName3" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="ratingInputEmail3">Email</label>
                                        <input type="email" class="form-control" id="ratingInputEmail3" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishRating(3)">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="ratingModal4" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="ratingModalLabel4">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="ratingInputName4">Name</label>
                                        <input type="text" class="form-control" id="ratingInputName4" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="ratingInputEmail4">Email</label>
                                        <input type="email" class="form-control" id="ratingInputEmail4" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishRating(4)">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="ratingModal5" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="ratingModalLabel5">Please fill out your information</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-group">
                                        <label for="ratingInputName5">Name</label>
                                        <input type="text" class="form-control" id="ratingInputName5" aria-describedby="emailHelp">
                                    </div>
                                    <div class="form-group">
                                        <label for="ratingInputEmail5">Email</label>
                                        <input type="email" class="form-control" id="ratingInputEmail5" aria-describedby="emailHelp">
                                    </div>
                                    <button type="button" class="btn primary-button d-flex align-items-center justify-content-center" onclick="publishRating(5)">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>


                <script>
                    function publishComment() {
                        let username = document.getElementById("inputName").value;
                        let email = document.getElementById("inputEmail").value;
                        let content = document.getElementById("commentInput").value;
                        let id= ${framework.id};

                        let path = "/create?id="+id+"&content="+content+"&username="+username+"&email="+email;
                        console.log(path);
                        window.location.href = path;
                        console.log(location.href);
                    }

                    function voteUpComment(commentId) {
                        let frameworkId = ${framework.id};
                        let path = "/voteup?id="+frameworkId+"&comment_id="+commentId;
                        window.location.href = path;
                    }

                    function voteDownComment(commentId) {
                        let frameworkId = ${framework.id};
                        let path = "/votedown?id="+frameworkId+"&comment_id="+commentId;
                        window.location.href = path;
                    }

                    function publishRating(n){
                        let username = document.getElementById("ratingInputName" + n).value;
                        let email = document.getElementById("ratingInputEmail" + n).value;
                        let ratingValue = n;
                        let id= ${framework.id};

                        let path = "/rate?id="+id+"&rating="+ratingValue+"&username="+username+"&email="+email;
                        console.log(path);
                        window.location.href = path;
                        console.log(location.href);
                    }

                    // $(document).ready(function () {
                    //     $(".open-AddBookDialog").click(function () {
                    //         $('#bookId').val($(this).data('id'));
                    //         $('#addBookDialog').modal('show');
                    //     });
                    // });
                </script>

                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>
    </body>
</html>
