'use strict';
define(['frontend','services/postService'], function(frontend) {

  frontend.controller('PostsCtrl', function($scope, $location, $window, $routeParams, postService, Restangular) {
    $scope.isAdmin = true;
    $scope.isEnable = true;
    $scope.isPresent = true;
    $scope.username = 'pepe';
    $scope.pageSize = 7;
    postService.getPosts().then(function (posts) {
      $scope.posts = posts;
    });
  });

});
