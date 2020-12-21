'use strict';
define(['frontend','services/userService','services/sessionService'], function(frontend) {

  frontend.controller('userCtrl', function($scope, $routeParams, userService,sessionService,$localStorage) {

    var user = sessionService.getStorageUser();
    if ($scope.$parent.username !== undefined) {
      $scope.username = $scope.$parent.username;
      sessionService.getCurrentUser(user.location).then(function (response) {
        $scope.allowMod = response.data.allowedModeration;
        $scope.modValue = $scope.allowMod;
        $scope.username = response.data.username;
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
          $scope.profile.comments = response.data;
          $scope.commentPaging = response.headers('link');
          break;
        case 'content':
          $scope.profile.content = response.data;
          $scope.contentPaging = response.headers('link');
          break;
        case 'techs':
          $scope.profile.techs = response.data;
          $scope.techsPaging = response.headers('link');
          break;
        case 'posts':
          $scope.profile.posts = response.data;
          $scope.postsPaging = response.headers('link');
          break;
        case 'votes':
          $scope.profile.votes = response.data;
          $scope.votesPaging = response.headers('link');
          break;
        default:
          break;
      }
    };
    $scope.updateMod = function(set) {
      userService.setMod($scope.profile.modLocation,set).then(function (response) {
        if (response.status === 200) {
          userService.getUser($routeParams.id).then(function (user) {
            $scope.profile = user.data;
            $scope.allowMod = user.data.allowedModeration;
            $scope.modValue = $scope.allowMod;
            if (set === false) {
              $('#stopBeingAModModal').modal('hide');
            }
          });

        }
      });
    };
    $scope.changeMod = function() {
      if ($scope.allowMod === true) {
        $('#stopBeingAModModal').modal('show');
      } else {
        $scope.updateMod(true);
      }
    };

    $('#stopBeingAModModal').on('hide.bs.modal',function () {
      $scope.modValue = $scope.allowMod;
      $('#modCheckbox').prop('checked',$scope.allowMod);
    });

  });
});
