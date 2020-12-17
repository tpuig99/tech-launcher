'use strict';
define(['frontend', 'services/exploreService'], function(frontend) {

    frontend.controller('ExploreCtrl', function($scope, exploreService) {

      exploreService.getTechs().then(function(response){
        $scope.matchingTechs = response;
      });

      exploreService.getPosts().then(function(response){

        $scope.posts = response;
      });



    });

});
