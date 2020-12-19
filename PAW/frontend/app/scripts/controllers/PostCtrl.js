'use strict';
define(['frontend', 'services/postService'], function(frontend) {

    frontend.controller('PostCtrl', function($scope, $location, $window, $routeParams, postService, Restangular) {
      $scope.isAdmin = true;
      $scope.isOwner = true;
      $scope.isEnabled = true;
      $scope.username = 'pepe';


      postService.getPost($routeParams.id).then(function(response) {
        $scope.post = response.data;
      });

      postService.getAnswers($routeParams.id).then(function(response) {
        $scope.answers = response.data;
      });

      $scope.redirect = function(url) {
        $location.path(url);
      };

      $scope.deletePost = function(post) {
        post.remove();
      };

      $scope.upVote = function(post) {
        postService.upVote(post);
      };

      $scope.downVote = function(post) {
        postService.downVote(post);
      };


    });

});
