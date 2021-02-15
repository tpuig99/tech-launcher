'use strict';
define(['frontend','services/techsService','services/sessionService'], function(frontend) {

  frontend.controller('TechsCtrl', function($scope, $localStorage, sessionService,techsService) {

    $scope.isAdmin = false;
    $scope.isMod = false;
    $scope.isPresent = false;
    $scope.$parent.$watch('username',function () {
      var user = sessionService.getStorageUser();
      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.isPresent = true;
        }).catch((error) => {
          $location.path('/404');
        });
      }
    });
    techsService.getCategories().then(function (cats) {
      $scope.categories = cats.data;
    }).catch((error) => {
      $location.path('/404');
    });

    techsService.getTypes().then(function (cats) {
      $scope.types = cats.data;
    }).catch((error) => {
      $location.path('/404');
    });

    $scope.getInfo = function() {
      techsService.getHomeInfo().then(function (techs) {
        $scope.home = techs.data;
      }).catch((error) => {
        $location.path('/404');
      });
    };
    $scope.getInfo();
  });
});
