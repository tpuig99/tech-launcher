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
      }).catch((error) => {
        $location.path('/404');
      });
    }

    $scope.getTech = function() {
      techsService.getTech($routeParams.id).then(function (tech) {
        $scope.tech = tech.data;
        $scope.tech.picture = undefined;
      }).catch((error) => {
        $location.path('/404');
      });
    };

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

    $scope.getTech();

    $scope.setPic = function(file) {
      $scope.tech.picture = file;
    };

    $scope.editTech = function () {
      $scope.techNameError = false;
      techsService.editTech($routeParams.id, $scope.tech).then(function (response) {
        if (response.status === 200) {
          $location.path($scope.tech.location);
        }
      }).catch(function () {
        $scope.techNameError = true;
      });
    };

    // Form Validations
    $scope.techNameValidator = {
      minLen: 1,
      maxLen: 50,
      pattern: /[a-zA-Z0-9 -+#*]+/
    };

    $scope.techIntroValidator = {
      minLen: 1,
      maxLen: 5000,
    };

    $scope.techDescriptionValidator = {
      minLen: 1,
      maxLen: 500,
    };
  });
});
