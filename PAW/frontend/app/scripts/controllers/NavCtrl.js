'use strict';
define(['frontend'], function(frontend) {

  frontend.controller('NavCtrl', function($scope) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.search_page = false;
    $scope.username = 'pepe';
  });

});
