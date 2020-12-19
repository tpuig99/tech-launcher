'use strict';
define(['angular'], function(angular) {
  var mod =angular.module('components',[]);
	mod.directive('unicorn', function() {
		return {
			restrict: 'E',
			template: '<a>This is Unicorn</a>'
		};
	});
	mod.directive('myNavbar', function() {
    return {
      restrict: 'E',
      controller: 'IndexCtrl',
      templateUrl: '../../navbar.html'
    };
  });
});
