'use strict';
define(['frontend','services/postService','services/sessionService'], function(frontend) {

  frontend.controller('PostsCtrl', function($scope, sessionService, $window, $routeParams, postService, $localStorage) {
    $scope.isPresent = false;
    if ($localStorage.currentUser !== undefined) {
      sessionService.getCurrentUser($localStorage.currentUser.location).then(function (response) {
        $scope.username = response.data.username;
        $scope.isMod = response.data.verify;
        $scope.isAdmin = response.data.admin;
        $scope.isEnable = response.data.enabled;
        $scope.isPresent = true;
      });
    }
    $scope.getPosts = function() {
      postService.getPosts().then(function (posts) {
        $scope.posts = posts.data;
        $scope.pagingLinks = posts.headers('link');
      });
    };
    $scope.setData = function(response) {
        $scope.posts = response.data;
        $scope.pagingLinks = response.headers('link');
    };
    $scope.getPosts();
    $scope.deletePost = function(url) {
      postService.deletePost(url).then($scope.getPosts());
    };


  });

});
