'use strict';
define(['frontend','services/sessionService'], function(frontend) {


    frontend.controller('RegisterCtrl', function($scope, sessionService) {

      $scope.userAlreadyExists = false;

      $scope.register = function() {
        sessionService.createUser($scope.emailInput, $scope.usernameInput, $scope.passwordInput).then(function(response) {
          if (response.status === 409) {
            $scope.userAlreadyExists = true;
            console.log($scope.userAlreadyExists);
          }
        });
      };

    });

});
