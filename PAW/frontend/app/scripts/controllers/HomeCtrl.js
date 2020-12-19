'use strict';
define(['frontend'], function(frontend) {

	frontend.controller('HomeCtrl', function($scope, $localStorage) {
		$scope.homePageText = 'This is your homepage';
	});
});
