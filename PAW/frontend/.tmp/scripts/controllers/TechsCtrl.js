'use strict';
define(['frontend','services/techsService'], function(frontend) {

  frontend.controller('TechsCtrl', ["$scope", "$location", "$window", "$routeParams", "techsService", "Restangular", function($scope, $location, $window, $routeParams, techsService, Restangular) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;

    techsService.getCategories().then(function (cats) {
      $scope.categories = cats;
    });
    techsService.getHomeInfo().then(function (techs) {
      $scope.home = techs;
    });

  }]);
});
