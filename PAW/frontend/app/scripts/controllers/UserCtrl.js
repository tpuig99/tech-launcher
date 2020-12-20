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
          $scope.commentPaging = comments.headers('link');
        });
      }
      if (user.contentAmount !== 0) {
        userService.getData($scope.profile.content).then(function (content) {
          $scope.profile.content = content.data;
          $scope.contentPaging = content.headers('link');
        });
      }
      if (user.postsAmount !== 0) {
        userService.getData($scope.profile.posts).then(function (posts) {
          $scope.profile.posts = posts.data;
          $scope.postsPaging = posts.headers('link');
        });
      }
      if (user.techsAmount !== 0) {
        userService.getData($scope.profile.techs).then(function (techs) {
          $scope.profile.techs = techs.data;
          $scope.techsPaging = techs.headers('link');
        });
      }
      if (user.votesAmount !== 0) {
        userService.getData($scope.profile.votes).then(function (votes) {
          $scope.profile.votes = votes.data;
          $scope.votesPaging = votes.headers('link');

        });
      }
    });
    $scope.setData = function(response,id) {
      switch (id) {
        case 'comments':
          console.log('comment');
          $scope.profile.comments = response.data;
          $scope.commentPaging = response.headers('link');
          break;
        case 'content':
          console.log('content');
          $scope.profile.content = response.data;
          $scope.contentPaging = response.headers('link');
          break;
        case 'techs':
          console.log('tech');
          $scope.profile.techs = response.data;
          $scope.techsPaging = response.headers('link');
          break;
        case 'posts':
          console.log('post');
          $scope.profile.posts = response.data;
          $scope.postsPaging = response.headers('link');
          break;
        case 'votes':
          console.log('vote');
          $scope.profile.votes = response.data;
          $scope.votesPaging = response.headers('link');
          break;
        default:
          console.log(id);
          break;
      }
    };
  });
});
