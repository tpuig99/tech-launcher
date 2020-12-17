'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('HomeCtrl', ["$scope", function($scope) {
		$scope.homePageText = 'This is your homepage';
	}]);
});
