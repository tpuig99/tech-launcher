'use strict';
define(['frontend'], function(frontend) {

  frontend.controller('NavCtrl', ["$scope", function($scope) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.searchPage = false;
    $scope.username = 'pepe';
  }]);

});
