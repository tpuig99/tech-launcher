'use strict';
define(['frontend', 'services/exploreService', 'services/techsService'], function(frontend) {

    frontend.controller('ExploreCtrl', function($scope, exploreService,techsService) {

      exploreService.getTechs().then(function (response) {
        $scope.matchingTechs = response;
      });

      exploreService.getPosts().then(function (response) {
        $scope.posts = response;
      });

      techsService.getCategories().then(function (response) {
        $scope.categories = response;
      });
      techsService.getTypes().then(function (response) {
        $scope.types = response;
      });

      $scope.search = function () {
        exploreService.search($scope.nameToSearch, $scope.categories, $scope.types, $scope.starsLeft, $scope.starsRight, $scope.nameFlag === undefined ? false : $scope.nameFlag.selected, $scope.commentAmount, $scope.lastComment, $scope.lastUpdate, $scope.groupBy, $scope.orderValue).then(function (response) {
          $scope.matchingTechs = response;
        });
      };

      $scope.showMore = function (element) {
        if (element === 'Types') {
          $scope.typesVisible = true;
          $scope.typesShowMoreBtnVisible = true;
          $scope.typesShowLessBtnVisible = true;
        } else {
          $scope.categoriesVisible = true;
          $scope.categoriesShowMoreBtnVisible = true;
          $scope.categoriesShowLessBtnVisible = true;
        }
      };

      $scope.showLess = function(element) {
        if ( element === 'Types'){
          $scope.typesVisible = false;
          $scope.typesShowMoreBtnVisible = true;
          $scope.typesShowLessBtnVisible = true;
        } else {
          $scope.categoriesVisible = false;
          $scope.categoriesShowMoreBtnVisible = true;
          $scope.categoriesShowLessBtnVisible = true;
        }
      };



    });
});
