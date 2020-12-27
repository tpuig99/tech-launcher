'use strict';
define(['frontend','services/postService','services/sessionService'], function(frontend) {

  frontend.controller('PostsCtrl', function($scope, $location, sessionService, $window, $routeParams, $sessionStorage,postService, $localStorage) {
    $scope.isPresent = false;
    $scope.$parent.$watch('username',function () {
      var user = sessionService.getStorageUser();
      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          $scope.username = response.data.username;
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.isEnable = response.data.enabled;
          $scope.isPresent = true;
        });
        $scope.getPosts();
      }
    });

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
        $('#deletePostModal').modal('hide');
      });
    };
    $('#deletePostModal').on('hide.bs.modal',function () {
      $scope.cleanDel();
    });
    $scope.upVote = function(location) {
      postService.upVote(location).then($scope.getPosts());
    };

    $scope.downVote = function(location) {
      postService.downVote(location).then($scope.getPosts());
    };



    $scope.getTags = function () {
       postService.getTags().then((tags => {
         $scope.names = tags.data.names;
         $scope.categories = tags.data.categories;
         $scope.types = tags.data.types;
       }));
    }
    $scope.getTags();
    $scope.namesChosen = [];
    $scope.categoriesChosen = [];
    $scope.typesChosen = [];

    $scope.tagsEmpty = () => {
      return _.isEmpty($scope.namesChosen) && _.isEmpty($scope.categoriesChosen) && _.isEmpty($scope.typesChosen)
    }

    $scope.addName = function (name) {
      if( !$scope.namesChosen.includes(name) ) {
        $scope.namesChosen.push(name);
      } else {
        _.remove($scope.namesChosen, (value) => {
          return value === name
        })
      }
    }

    $scope.addCategory = function (category) {
      if( !$scope.categoriesChosen.includes(category) ) {
        $scope.categoriesChosen.push(category);
      } else {
        _.remove($scope.categoriesChosen, (value) => {
          return value === category;
        })
      }
    }

    $scope.addType = function (type) {
      if( !$scope.typesChosen.includes(type)) {
        $scope.typesChosen.push(type);
      } else {
        _.remove($scope.typesChosen,(value) => {
          return value === type;
        });
      }
    }

    $scope.addPost = (title, description) => {
      if( postTitleInput.$modelValue.length < 3 || postTitleInput.$modelValue.length > 200 || postDescriptionInput.$modelValue.length > 5000 || $scope.tagsEmpty()) {
        return;
      }
      let post = {
        'title' : title.$modelValue,
        'description' : description.$modelValue,
        'names' : $scope.namesChosen,
        'categories' : $scope.categoriesChosen,
        'types' : $scope.typesChosen
      };
      postService.addPost(post, 'posts');
      $location.path('/#/posts');
    }
  });
});
