'use strict';
define(['frontend','services/userService'], function(frontend) {

  frontend.controller('userCtrl', function($scope, $location, $window, $routeParams, userService, Restangular) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;
    $scope.username = 'pepe';

    userService.getUser($routeParams.id).then(function (user) {
      $scope.profile = user;
      console.log($scope.profile)
    });

    userService.getData($scope.profile.comments).then(function (comments) {
      $scope.profile.comments = comments;
      console.log($scope.profile.comments);
    });

  });
});
