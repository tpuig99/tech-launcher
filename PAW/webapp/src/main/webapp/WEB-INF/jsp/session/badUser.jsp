<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <title id="title">Validation Error</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/base_page.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles/register.css"/>"/>
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="connected" value="${user.authenticated}"/>
            <jsp:param name="username" value="${user.name}"/>
        </jsp:include>


        <div class="content">
            <div class="page-description">${message}</div>
            <button class="btn btn-primary" onclick="resendToken('${token}')">Resend</button>
        </div>


        <script type="text/javascript">

            let serverContext = '${pageContext.request.contextPath}';

            function resendToken(token){
                $.get(serverContext + "register/resendRegistrationToken?token=" + token,
                    function(){
                        window.location.href = serverContext +'/register/success/3';
                    })
                    .catch(function(message) {
                        /* fijarse que onda aca
                        if(data.responseJSON.error.indexOf("MailError") > -1) {
                            window.location.href = serverContext + "emailError.html";
                        }
                        else {
                            window.location.href =
                                serverContext + "login.html?message=" + data.responseJSON.message;
                        }
                         */
                    });
            }
        </script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </body>
</html>