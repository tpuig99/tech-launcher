'use strict';
define(['frontend', 'services/postService', 'services/sessionService'], function(frontend) {

    frontend.controller('PostCtrl', function($scope, $location, $window, $routeParams, postService, $sessionStorage,Restangular, sessionService, $localStorage) {

      $scope.isPresent = false;
      var user = sessionService.getStorageUser();

      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          $scope.username = response.data.username;
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.isEnabled = response.data.enabled;
          $scope.isPresent = true;
        });
      }


      $scope.getPost = function() {
          postService.getPost($routeParams.id).then(function(response) {
          $scope.post = response.data;
        });
      };


      $scope.getAnswers = function() {
       postService.getAnswers($routeParams.id).then(function(response) {
          $scope.answers = response.data;
          $scope.pagingLinks = response.headers('link');
       });

      };

      $scope.getPost();
      $scope.getAnswers();


      $scope.redirect = function(url) {
        $location.path(url);
      };

      $scope.setDel = function (url) {
        $scope.toDel = url;
      };
      $scope.cleanDel = function () {
        $scope.toDel = undefined;
      };

      $scope.deletePost = function() {
        postService.deletePost($scope.toDel).then(function() {
          $('#deletePostModal').modal('hide');
          $location.path('/#/posts');
        });


      };

      $scope.deleteAnswer = function() {
        postService.deletePost($scope.toDel).then(function() {
          $scope.getPost();
          $scope.getAnswers();
          $scope.cleanDel();
          $('#deleteCommentModal').modal('hide');
        });
      };

      $scope.upVote = function() {
        postService.upVote($routeParams.id).then($scope.getPost());
      };

      $scope.downVote = function() {
        postService.downVote($routeParams.id).then($scope.getPost());
      };

      $scope.upVoteAnswer = function(location) {
        postService.upVoteAnswer(location).then($scope.getAnswers());
      };

      $scope.downVoteAnswer = function(location) {
        postService.downVoteAnswer(location).then($scope.getAnswers());
      };

      $scope.commentPost = function(answer) {
        postService.commentPost($routeParams.id, answer).then(function () {
          $scope.getPost();
          $scope.getAnswers();
        });
      };

      $scope.setData = function(response) {
        $scope.answers = response.data;
        $scope.pagingLinks = response.headers('link');
      };

      $('#deletePostModal').on('hide.bs.modal',function () {
        $scope.cleanDel();
      });

      $('#deleteCommentModal').on('hide.bs.modal',function () {
        $scope.cleanDel();
      });


    });

});
