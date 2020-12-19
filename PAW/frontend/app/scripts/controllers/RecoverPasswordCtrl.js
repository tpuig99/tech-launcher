'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


    frontend.controller('RecoverPasswordCtrl', function($scope, sessionService, $location) {

      $scope.emailSent = false;

      $scope.sendEmail = function(mail){
        sessionService.setPasswordToken(mail).then(function (response) {
          $scope.emailSent = true;
        });
      }

    });

});
