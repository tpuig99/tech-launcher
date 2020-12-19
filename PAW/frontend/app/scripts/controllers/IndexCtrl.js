'use strict';

define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope,$location, $window, $routeParams,$route) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.searchPage = false;
    $scope.navbarSearch = undefined;
    $scope.toSearch = function(toSearch) {
      $scope.navbarSearch = (toSearch === undefined || toSearch === 0) ? '' : toSearch;
      $window.location.href = '#/explore';
    };
	});
});
