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
      }).catch((error) => {
        $location.path('/404');
      });
    }

    $scope.category = $routeParams.category

    techsService.getCategories().then(function (cats) {
      $scope.categories = cats.data;
    }).catch((error) => {
      $location.path('/404');
    });


    techsService.getByCategory($routeParams.category).then( function (techs) {
      $scope.techs = techs.data;
      $scope.pagingLinks = techs.headers('link');
    }).catch((error) => {
      $location.path('/404');
    });

    $scope.setData = function(response) {
      $scope.techs = response.data;
      $scope.pagingLinks = response.headers('link');
    };
  });
});
