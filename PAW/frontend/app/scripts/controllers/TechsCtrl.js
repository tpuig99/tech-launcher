'use strict';
define(['frontend','services/TechsService'], function(frontend) {

  frontend.controller('TechsCtrl', function($scope, $location, $window, $routeParams, techsService, Restangular) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;

    $scope.categories = [{category: 'category1',location: 'location1'},{category: 'category2',location: 'location2'},{category: 'category3',location: 'location3'}];

    techsService.getHomeInfo().then(function (techs){
      $scope.home = techs;
    });
  });
});
