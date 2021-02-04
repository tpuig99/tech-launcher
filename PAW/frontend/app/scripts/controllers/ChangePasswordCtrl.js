'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


    frontend.controller('ChangePasswordCtrl', function($scope, $routeParams, sessionService) {
      $('.modal-backdrop').hide();

      $scope.passwordChanged = false;

      $scope.isPresent = false;
      $scope.$parent.$watch('username',function () {
        var user = sessionService.getStorageUser();
        if (user !== undefined) {
          sessionService.getCurrentUser(user.location).then(function (response) {
            $scope.username = response.data.username;
            $scope.isPresent = true;
            $scope.userLocation = user.location;
          });
          $scope.getPosts();
        }
      });



      $scope.changePassword = function(newPassword) {
        sessionService.changePassword($routeParams.token, newPassword).then(function(response) {
          if (response.status === 200) {
            $scope.passwordChanged = true;
          }

        });
      };

    });

});
