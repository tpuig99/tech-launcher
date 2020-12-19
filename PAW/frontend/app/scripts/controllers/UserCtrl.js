'use strict';
define(['frontend','services/userService'], function(frontend) {

  frontend.controller('userCtrl', function($scope, $location, $window, $routeParams, userService, Restangular) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;
    $scope.username = 'pepe';

    userService.getUser($routeParams.id).then(function (user) {
      $scope.profile = user;
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
