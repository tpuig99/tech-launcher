'use strict';
define(['frontend', 'services/postService'], function(frontend) {

    frontend.controller('PostCtrl', function($scope, $location, $window, $routeParams, postService, Restangular) {
      $scope.isAdmin = true;
      $scope.isOwner = true;
      $scope.isEnable = false;


      postService.getPost($routeParams.id).then(function(response){
        $scope.post = response;
      })

      $scope.redirect = function(url){
        $location.path(url);
      }

      $scope.deletePost = function(){
        postService.deletePost($routeParams.id);
      }


    });

});
