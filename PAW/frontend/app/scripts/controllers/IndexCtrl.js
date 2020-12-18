'use strict';

define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope,$location, $window, $routeParams,$route) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.searchPage = false;
    $scope.username = 'pepe';
    $scope.toSearch = function(toSearch) {
      $window.location.href = '#/explore?toSearch=' + toSearch;
    };
	});
});
