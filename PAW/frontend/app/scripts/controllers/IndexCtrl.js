'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('IndexCtrl', function($scope) {
		$scope.welcomeText = 'Welcome to your frontend page';
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.searchPage = false;
    $scope.username = 'pepe';
	});
});
