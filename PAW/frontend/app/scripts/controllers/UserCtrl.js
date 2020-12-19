'use strict';
define(['frontend','services/userService','services/sessionService'], function(frontend) {

  frontend.controller('userCtrl', function($scope, $routeParams, userService,sessionService,$localStorage) {

    if ($scope.$parent.username !== undefined) {
      $scope.username = $scope.$parent.username;
      sessionService.getCurrentUser($localStorage.currentUser.location).then(function (response) {
        $scope.allowMod = response.data.allowedModeration;
      });
    }

    userService.getUser($routeParams.id).then(function (user) {
      $scope.profile = user.data;
      if (user.commentAmount !== 0) {
        userService.getData($scope.profile.comments).then(function (comments) {
          $scope.profile.comments = comments.data;
        });
      }
      if (user.contentAmount !== 0) {
        userService.getData($scope.profile.content).then(function (content) {
          $scope.profile.content = content.data;
        });
      }
      if (user.postsAmount !== 0) {
        userService.getData($scope.profile.posts).then(function (posts) {
          $scope.profile.posts = posts.data;
        });
      }
      if (user.techsAmount !== 0) {
        userService.getData($scope.profile.techs).then(function (techs) {
          $scope.profile.techs = techs.data;
        });
      }
      if (user.votesAmount !== 0) {
        userService.getData($scope.profile.votes).then(function (votes) {
          $scope.profile.votes = votes.data;
        });
      }
    });


  });
});
