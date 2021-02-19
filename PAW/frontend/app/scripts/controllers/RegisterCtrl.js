'use strict';
define(['frontend','services/sessionService'], function(frontend) {


    frontend.controller('RegisterCtrl', function($location, $scope, sessionService) {
      $('.modal-backdrop').hide();
      $scope.userAlreadyExists = false;
      $scope.registered = false;

      $scope.register = function(email, username, password) {
        sessionService.createUser(email, username, password).then(function(response) {
          $scope.userAlreadyExists = false;
          $scope.registered = true;
        }, function (response) {
          $scope.registered = false;
          $scope.userAlreadyExists = true;
        }).catch((error) => {
          if(error.status === 404) {
            $location.path('/404');
          }
          else {
            $location.path('/500');
          }
        });
      };

      // Form Validations
      $scope.usernameValidator = {
        minLen: 3,
        maxLen: 100,
        pattern: /[a-zA-Z0-9]+/
      };

      $scope.passwordValidator = {
        minLen: 6,
        maxLen: 100,
      };

    });

});
