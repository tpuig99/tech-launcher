'use strict';
define(['frontend', 'services/exploreService', 'services/techsService'], function(frontend) {

    frontend.controller('ExploreCtrl', function($scope, exploreService,techsService) {

      exploreService.getTechs().then(function(response){
        $scope.matchingTechs = response;
      });

      exploreService.getPosts().then(function(response){
        $scope.posts = response;
      });

      techsService.getCategories().then(function (response) {
        $scope.categories = response;
      });



    });

});
