'use strict';
define(['frontend', 'services/exploreService', 'services/techsService'], function(frontend) {

    frontend.controller('ExploreCtrl', function($scope, exploreService,techsService) {

      exploreService.getTechs().then(function(response) {
        $scope.matchingTechs = response;
      });

      exploreService.getPosts().then(function(response) {
        $scope.posts = response;
      });

      techsService.getCategories().then(function (response) {
        $scope.categories = response;
      });


      $scope.search = function() {
        exploreService.search($scope.toSearch, $scope.starsLeft, $scope.starsRight,$scope.nameFlag === undefined ? false : $scope.nameFlag.selected, $scope.commentAmount, $scope.lastComment, $scope.lastUpdate, $scope.groupBy, $scope.orderValue).then(function(response) {
          $scope.matchingTechs = response;
        });
      };


    });
});
