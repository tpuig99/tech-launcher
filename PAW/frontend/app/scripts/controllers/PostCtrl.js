'use strict';
define(['frontend', 'services/postService'], function(frontend) {

    frontend.controller('PostCtrl', function($scope, $location, $window, $routeParams, postService, Restangular) {
      $scope.isAdmin = true;
      $scope.isOwner = true;
      $scope.isEnable = false;



      postService.getPost($routeParams.id).then(function(response){
        $scope.post = response;
      });

      postService.getAnswers($routeParams.id).then(function(response){
        $scope.answers = response;
      });

      $scope.redirect = function(url){
        $location.path(url);
      };

      $scope.deletePost = function(){
        postService.deletePost($routeParams.id);
      };

      $scope.upVote = function(post){
        postService.upVote(post);
      };

      $scope.downVote = function(post){
        postService.downVote(post);
      };


    });

});
