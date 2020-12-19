'use strict';

define(['frontend','services/sessionService'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope,$location, $window, $localStorage,$http, $routeParams,$route,sessionService) {
		$scope.welcomeText = 'Welcome to your frontend page';
		var user = $localStorage.currentUser;
    $scope.searchPage = false;
    $scope.checkUser = function() {
      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
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
      $http.defaults.headers.common.Authorization = undefined;
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
