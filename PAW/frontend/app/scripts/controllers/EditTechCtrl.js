'use strict';
define(['frontend', 'services/techsService', 'services/sessionService'], function(frontend) {

  frontend.controller('EditTechCtrl', function($scope, $location, $window, $routeParams, techsService, $sessionStorage,Restangular, sessionService, $localStorage) {

    $scope.isPresent = false;
    $scope.isVerify = false;
    $scope.isOwner = false;

    var user = sessionService.getStorageUser();

    if (user !== undefined) {
      sessionService.getCurrentUser(user.location).then(function (response) {
        $scope.username = response.data.username;
        $scope.isMod = response.data.verify;
        $scope.isAdmin = response.data.admin;
        $scope.isEnable = response.data.enabled;
        $scope.isPresent = true;
        $scope.userVerifications = response.data.verifications;
      });
    }

    $scope.getTech = function() {
      techsService.getTech($routeParams.id).then(function (tech) {
        $scope.tech = tech.data;
        $scope.tech.picture = undefined;
      });
    };

    techsService.getCategories().then(function (cats) {
      $scope.categories = cats.data;
    });

    techsService.getTypes().then(function (cats) {
      $scope.types = cats.data;
    });

    $scope.getTech();

    $scope.setPic = function(file) {
      $scope.tech.picture = file;
    };

    $scope.editTech = function () {
      techsService.editTech($routeParams.id, $scope.tech).then(function (response) {
        if (response.status === 200) {
          $location.path($scope.tech.location);
        }
      });
    };
  });

});
