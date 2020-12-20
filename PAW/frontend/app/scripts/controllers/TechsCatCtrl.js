'use strict';
define(['frontend','services/techsService','services/sessionService'], function(frontend) {

  frontend.controller('TechsCatCtrl', function($scope, $localStorage, $location, $window, $routeParams, sessionService,techsService) {
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


    techsService.getByCategory($routeParams.category).then( function (techs) {
      $scope.techs = techs.data;
    });

  });
});