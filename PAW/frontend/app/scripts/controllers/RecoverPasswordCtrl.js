'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


    frontend.controller('RecoverPasswordCtrl', function($scope, sessionService, $location) {

      $scope.sendEmail = function(){
        sessionService.setPasswordToken($scope.emailInput).then(function (response) {
          var tokenUrl = response.data.token;
          $location.path(tokenUrl); //  Go to /register/forgot_password?token= ..
        });
      }

    });

});
