'use strict';
define(['frontend'], function(frontend) {

    frontend.controller('PostCtrl', function($scope, Restangular) {

      $scope.isAdmin = true;
      $scope.isOwner = true;

      $scope.post = Restangular.one('posts',1).get();  // GET /posts/:id


    });

});
