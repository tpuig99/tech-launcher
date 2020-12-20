'use strict';
define(['frontend','services/sessionService'], function(frontend) {


    frontend.controller('RegisterCtrl', function($scope, sessionService) {

      $scope.userAlreadyExists = false;
      $scope.registered = false;

      $scope.register = function(email, username, password) {
        sessionService.createUser(email, username, password).then(function(response) {
          $scope.userAlreadyExists = false;
          $scope.registered =  true;
        }, function (response){
          $scope.registered =  false;
          $scope.userAlreadyExists = true;
        });
      };

    });

});
