'use strict';
define(['frontend','services/sessionService'], function(frontend) {


    frontend.controller('RegisterCtrl', function($scope, sessionService) {

      $scope.register = function() {
        sessionService.createUser($scope.emailInput, $scope.usernameInput, $scope.passwordInput);
      };

    });

});
