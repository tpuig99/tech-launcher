'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


    frontend.controller('ChangePasswordCtrl', function($scope, $routeParams, sessionService) {
      $scope.changePassword =  function(){
        sessionService.changePassword($routeParams.token, $scope.passwordInput);
      }

    });

});
