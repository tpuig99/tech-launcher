'use strict';
define(['frontend','services/techsService','services/sessionService'], function(frontend) {

  frontend.controller('TechsCtrl', function($scope, $localStorage, sessionService,techsService) {
    var user = sessionService.getStorageUser();
    $scope.isAdmin = false;
    $scope.isMod = false;
    $scope.isPresent = false;
    if (user !== undefined) {
      sessionService.getCurrentUser(user.location).then(function (response) {
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
