<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <title>${framework.name}</title>

        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/framework.css"/>"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>
    <body>
        <div>

            <jsp:include page="../components/navbar.jsp">
                <jsp:param name="connected" value="${user.authenticated}"/>
                <jsp:param name="username" value="${user.name}"/>
                <jsp:param name="isMod" value="${user_isMod}"/>
            </jsp:include>
            <jsp:include page="../components/sidebar.jsp"/>

            <div class="content">

                <div class="container d-flex">
                    <div class="row">
                        <div class="col-2">
                            <div class="max-logo"><img src="${framework.logo}" alt="${framework.name} logo"></div>
                        </div>
                        <div class="col-10">

                            <div class="row">
                                <div class="col">
                                <span class="framework-title"><h2>${framework.name}</h2></span>
                                </div>
                                <div class="col d-flex align-items-center justify-content-center">
                                <span class="fa fa-star color-star"></span>
                                <span> ${framework.starsFormated} | ${framework.votesCant}</span>
                                    <span class="fa fa-user"></span>
                                </div>
                            </div>
                            <div class="description">
                                ${framework.description}
                            </div>
                        </div>
                        <div class="margin-top margin-left justify">${framework.introduction}</div>
                    </div>
                </div>


                <!-- Content -->
                <div class="container">
                    <div><h4 class="title">Content</h4></div>
                    <div class="d-flex justify-content-end">
                       <button class="btn primary-button" type="button" data-toggle="modal" data-target="#addContentModal"> <!--onclick="uploadContent()"-->
                           <i class="fa fa-plus"></i>
                        </button>
                   </div>
                </div>
                <c:if test="${empty books && empty courses && empty tutorials}">
                    <div class="d-flex align-items-center justify-content-center">There is no content available for this tech yet</div>
                </c:if>

                <!-- Bibliography -->
                <c:if test="${not empty books}">
                <div>
                   <span><h4 class="title">Bibliography</h4></span>
                   <span>
                       <c:choose>
                           <c:when test="${user.name != 'anonymousUser'}">
                               <button class="btn fab-button" type="button" data-toggle="modal" data-target="#addContentModal"> <!--onclick="uploadContent()"-->
                                   <i class="fa fa-plus"></i>
                               </button>
                           </c:when>
                           <c:otherwise>
                               <button class="btn fab-button" type="button" data-toggle="modal" data-target="#loginModal"> <!--onclick="uploadContent()"-->
                                    <i class="fa fa-plus"></i>
                               </button>
                           </c:otherwise>
                       </c:choose>
                   </span>
                    <ul class="list-group margin-left list-group-flush description">
                        <c:forEach var="book" items="${books}">
                        <li class="list-group-item"><a target="_blank" href="${book.link}">${book.title}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Courses -->
                <c:if test="${not empty courses}">
                <div>
                    <h4 class="subtitle margin-left ">Courses</h4>

                    <ul class=" margin-bottom list-group margin-left list-group-flush description">

                        <c:forEach var="course" items="${courses}">
                            <li class="list-group-item"><a target="_blank" href="${course.link}">${course.title}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                </c:if>

                <!-- Tutorials -->
                <c:if test="${not empty tutorials}">
                <div>
                   <h4 class="subtitle margin-left"> Tutorials</h4>
                    <ul class="  margin-bottom list-group margin-left list-group-flush description">
                        <c:forEach var="tutorial" items="${tutorials}">
                            <li class="list-group-item"><a target="_blank" href="${tutorial.link}">${tutorial.title}</a></li>
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
                    <div class="row margin-left margin-bottom">

                        <div class="col-1">
                            <div>
                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <button class=" btn upVote btn-link" onclick="voteUpComment(${comment.commentId})">
                                            <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class=" btn upVote btn-link" data-toggle="modal" data-target="#loginModal">
                                            <i class="fa fa-arrow-up arrow"> ${comment.votesUp}</i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <button class="btn downVote  btn-link" onclick="voteDownComment(${comment.commentId})">
                                            <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class=" btn downVote btn-link" data-toggle="modal" data-target="#loginModal">
                                            <i class="fa fa-arrow-down arrow"> ${comment.votesDown}</i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col">
                            <div class="row">
                                <div class="col secondary-font">
                                    <c:out value="${comment.userName}" default=""/>
                                </div>
                                <div class="col third-font d-flex justify-content-flex-end">
                                    <c:out value="${comment.timestamp}" default=""/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <c:out value="${comment.description}" default=""/>
                                </div>
                            </div>

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

                                <c:choose>
                                    <c:when test="${user.name != 'anonymousUser'}">
                                        <button type="button" onclick="publishComment()" class="btn primary-button margin-top d-flex justify-content-flex-end">SUBMIT</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn primary-button margin-top d-flex justify-content-flex-end" data-toggle="modal" data-target="#loginModal">SUBMIT</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col">
                            <h5>Rating</h5>




                            <div class="stars">
                                <form id="rating-form">
                                    <input class="star star-5" id="star-5" type="radio" name="star"/>
                                    <label class="star star-5" for="star-5"></label>
                                    <input class="star star-4" id="star-4" type="radio" name="star"/>
                                    <label class="star star-4" for="star-4"></label>
                                    <input class="star star-3" id="star-3" type="radio" name="star"/>
                                    <label class="star star-3" for="star-3"></label>
                                    <input class="star star-2" id="star-2" type="radio" name="star"/>
                                    <label class="star star-2" for="star-2"></label>
                                    <input class="star star-1" id="star-1" type="radio" name="star"/>
                                    <label class="star star-1" for="star-1"></label>
                                    <c:choose>
                                        <c:when test="${user.name != 'anonymousUser'}">
                                            <button class="btn primary-button" type="submit">RATE</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn primary-button"  data-toggle="modal" data-target="#loginModal">RATE</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
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
                    <div class="card mini-card mx-3 mb-4">
                        <a href="<c:url value="/${competitors[i].frameCategory}/${competitors[i].id}"/>">
                            <div class="card-body d-flex align-items-center justify-content-center">
                                <div class="mini-logo d-flex align-items-center justify-content-center"><img src="${competitors[i].logo}" alt="${framework.name} logo"></div>
                            </div>
                            <div class="card-footer text-dark">${competitors[i].name}</div>
                        </a>
                    </div>

                    </c:forEach>
                </div>

                </c:if>
                
                <!-- Modal -->

                <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="loginModalLabel">You have be logged in to do this</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="modal-log-in">
                                    <button type="button" class="btn btn-danger" onclick="window.location.href = '/register'">Sign Up</button>
                                </div>
                                <div class="modal-log-in">
                                    <button type="button" class="btn btn-primary" onclick="window.location.href = '/login'">Log in</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Content Modal -->
                <div class="modal fade" id="addContentModal" tabindex="-1" role="dialog" aria-labelledby="addContentModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header container">

                                <h5 class="modal-title" id="addContentLabel">Add Content</h5>

                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                </button>
                                    <span aria-hidden="true">&times;</span>
                            </div>
                            <div class="modal-body container">
                                <jsp:include page="contentForm.jsp">
                                    <jsp:param name="frameworkId" value="${framework.id}" />
                                </jsp:include>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="snackbar">Your content is now pending approval !</div>

                <!-- Scripts -->
                <script>

                    <%--$(window).on('load', function() {--%>
                    <%--    console.log(${contentFormError});--%>
                    <%--    if(${contentFormError}) {--%>
                    <%--        $('#addContentModal').modal('show');--%>
                    <%--    }else{--%>
                    <%--        showSnackbar();--%>
                    <%--    }--%>
                    <%--});--%>

                    function publishComment() {
                        let content = document.getElementById("commentInput").value;
                        let id= ${framework.id};

                        let path = '<c:url value="/create" />?id='+id+'&content='+content;
                        console.log(path);
                        window.location.href = path;
                        console.log(location.href);
                    }

                    function voteUpComment(commentId) {
                        let frameworkId = ${framework.id};
                        window.location.href = '<c:url value="/voteup" />?id=' + frameworkId + '&comment_id=' + commentId;
                    }

                    function voteDownComment(commentId) {
                        let frameworkId = ${framework.id};
                        window.location.href = '<c:url value="/votedown" />?id=' + frameworkId + '&comment_id=' + commentId;
                    }

                    function publishRating(){
                        let rate_value = 0;
                        if (document.getElementById('star-1').checked) {
                            rate_value = 1;
                        }else if(document.getElementById('star-2').checked){
                            rate_value = 2;
                        }else if(document.getElementById('star-3').checked){
                            rate_value = 3;
                        }
                        else if(document.getElementById('star-4').checked){
                            rate_value = 4;
                        }
                        else if(document.getElementById('star-5').checked){
                            rate_value =  5;
                        }
                        console.log(rate_value);
                        let id= ${framework.id};

                        let path = '<c:url value="/rate" />?id='+id+'&rating='+rate_value;
                        console.log(path);
                        window.location.href = path;
                        console.log(location.href);
                    }

                    $(document).ready(function() {
                        $('#rating-form').on('submit', function(e){
                            if(${user.name != 'anonymousUser'}) {
                                publishRating();
                            }
                            e.preventDefault();
                        });
                    });



                    function selectFiles() {
                        let el = document.getElementById("fileElem");
                        if (el) {
                            el.click();
                        }
                    }

                    function uploadContent(){
                        let id= ${framework.id};
                        window.location.href = '<c:url value="/content" />?id=' + id;
                    }

                    function showSnackbar() {
                        console.log("En snackbar");
                        let x = document.getElementById("snackbar");
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 4000);
                    }

                </script>


                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
            </div>
            </div>
        </div>


    </body>
</html>
