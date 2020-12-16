'use strict';
define(['frontend'], function(frontend) {

    frontend.controller('PostCtrl', function($scope) {
      $scope.isAdmin = true;
      $scope.isOwner = true;
      $scope.isEnable = false;
    });

});
