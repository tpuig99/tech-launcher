<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
    <head>
        <title id="title">Validation Error</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>
    <body>
        <h1 th:text="${param.message[0]}">error</h1>
        <br>
        <a th:href="@{/user/registration}">signup</a>
        <div th:if="${param.expired[0]}">
            <h1>${message}</h1>
            <button onclick="resendToken(${token},${message})" th:text="#{label.form.resendRegistrationToken}">Resend</button>

            <script type="text/javascript">

                let serverContext = '[[@{/}]]';

                    function resendToken(token,message){
                        $.get(serverContext + "user/resendRegistrationToken?token=" + token,
                            function(data){
                                window.location.href =
                                    serverContext +"login.html?message=" + data.message;
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
            <script src="jquery.min.js"></script>
        </div>
    </body>
</html>