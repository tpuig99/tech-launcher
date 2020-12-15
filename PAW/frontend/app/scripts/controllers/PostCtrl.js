'use strict';
define(['frontend',
        'services/postService'
  ], function(frontend) {

    frontend.controller('PostCtrl', ['$scope', '$route', '$routeParams', 'postService', 'Restangular', function($scope, $route, $routeParams, postService, Restangular) {

      $scope.isAdmin = true;
      $scope.isOwner = true;
      $scope.isEnabled = true;

      $scope.post = Restangular.one('posts',$routeParams.id).get();  // GET /posts/:id

      /* $scope.upVote = function(post){

        postService.postUpVote(post);

      } */
    }]);

});
