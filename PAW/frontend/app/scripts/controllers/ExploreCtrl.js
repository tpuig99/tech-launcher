'use strict';
define(['frontend', 'services/exploreService', 'services/techsService'], function(frontend) {

    frontend.controller('ExploreCtrl', function($scope, exploreService,techsService) {
      $scope.$parent.searchPage = true;
     if($scope.$parent.navbarSearch !== undefined){
       exploreService.search($scope.$parent.navbarSearch, $scope.categories, $scope.types, $scope.starsLeft, $scope.starsRight, $scope.nameFlag === undefined ? false : $scope.nameFlag.selected, $scope.commentAmount, $scope.lastComment, $scope.lastUpdate, $scope.groupBy, $scope.orderValue).then(function (response) {
         $scope.matchingTechs = response.data;
         $scope.navbarNameToSearch = $scope.$parent.navbarSearch;
       });
     } else {
       exploreService.getTechs().then(function (response) {
         $scope.matchingTechs = response.data;
       });
     }
      exploreService.getPosts().then(function (response) {
        $scope.posts = response.data;
      });

      techsService.getCategories().then(function (response) {
        $scope.categories = response.data;
      });
      techsService.getTypes().then(function (response) {
        $scope.types = response.data;
      });

      $scope.search = function () {
        exploreService.search($scope.nameToSearch, $scope.categories, $scope.types, $scope.starsLeft, $scope.starsRight, $scope.nameFlag === undefined ? false : $scope.nameFlag.selected, $scope.commentAmount, $scope.lastComment, $scope.lastUpdate, $scope.groupBy, $scope.orderValue).then(function (response) {
          $scope.matchingTechs = response.data;
          $scope.navbarNameToSearch = undefined;
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

    $scope.$on("$destroy",function() {
       $scope.$parent.searchPage = false;
       $scope.$parent.navbarSearch = undefined;
    });

    });
});
