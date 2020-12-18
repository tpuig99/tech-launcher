'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {

    frontend.controller('LoginCtrl', function($scope, sessionService) {

      $scope.login = function() {
        sessionService.login($scope.usernameInput, $scope.passwordInput);
      };

    });

});
