'use strict';
define(['frontend','services/techsService','services/sessionService'], function(frontend) {

  frontend.controller('TechsCtrl', function($scope, $localStorage, sessionService,techsService) {
    $scope.isAdmin = false;
    $scope.isMod = false;
    $scope.isPresent = false;
    if ($localStorage.currentUser !== undefined) {
      sessionService.getCurrentUser($localStorage.currentUser.location).then(function (response) {
        $scope.isMod = response.data.verify;
        $scope.isAdmin = response.data.admin;
        $scope.isPresent = true;
      });
    }
    techsService.getCategories().then(function (cats) {
      $scope.categories = cats.data;
    });
    techsService.getHomeInfo().then(function (techs) {
      $scope.home = techs.data;
    });

  });
});
