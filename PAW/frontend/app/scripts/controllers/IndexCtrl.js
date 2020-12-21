'use strict';

define(['frontend','services/sessionService'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope,$location, $window, $localStorage, $sessionStorage, Restangular, $routeParams,$route,sessionService) {
    if ($localStorage.remember === undefined) {
      $localStorage.remember = {me: false};
    }
	  if ($sessionStorage.remember === undefined) {
      $sessionStorage.remember = {me: false};
    }

	  var user = sessionService.getStorageUser();

    $scope.searchPage = false;
    $scope.checkUser = function() {
      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          var code = 'Bearer ' + user.token;
          Restangular.setDefaultHeaders({'Authorization': code});
          $scope.username = response.data.username;
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.userLocation = user.location;
        });
      }
    };
    $scope.checkUser();

    $scope.navbarSearch = undefined;
    $scope.logout = function () {
      $localStorage.currentUser = undefined;
      $sessionStorage.currentUser = undefined;
      Restangular.setDefaultHeaders({'Authorization': undefined});
      $scope.username = undefined;
      $scope.isMod = undefined;
      $scope.isAdmin = undefined;
      $scope.userLocation = undefined;
    };
    $scope.toSearch = function(toSearch) {
      $scope.navbarSearch = (toSearch === undefined || toSearch === 0) ? '' : toSearch;
      $window.location.href = '#/explore';
    };
	});
});
