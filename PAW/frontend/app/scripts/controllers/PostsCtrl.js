'use strict';
define(['frontend','services/postService','services/sessionService'], function(frontend) {

  frontend.controller('PostsCtrl', function($scope, sessionService, $window, $routeParams, $sessionStorage,postService, $localStorage) {
    $scope.isPresent = false;
    var user = sessionService.getStorageUser();

    if (user !== undefined) {
      sessionService.getCurrentUser(user.location).then(function (response) {
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
    $scope.setDel = function (url) {
      $scope.toDel = url;
    };
    $scope.cleanDel = function () {
      $scope.toDel = undefined;
    };
    $scope.deletePost = function () {
      postService.deletePost($scope.toDel).then(function() {
        $scope.getPosts();
        $scope.cleanDel();
      });
    };
  });

});
