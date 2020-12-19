'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


    frontend.controller('ChangePasswordCtrl', function($scope, $routeParams, sessionService) {
      $scope.passwordChanged = false;
      $scope.changePassword =  function(newPassword){
        sessionService.changePassword($routeParams.token, newPassword).then( function(response) {
          if(response.status === 200 ){
            $scope.passwordChanged = true;
          }

        });
      }

    });

});
