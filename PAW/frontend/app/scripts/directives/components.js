'use strict';
define(['angular'], function(angular) {
  var mod = angular.module('components',['restangular']);
  mod.controller('PagingCtrl', function($scope,Restangular) {
    $scope.data = $scope.paging;
    $scope.refreshData = function () {
        $scope.prev = undefined;
        $scope.next = undefined;
        let parts = $scope.data.split(',').reduce((acc, link) => {
        let match = link.match(/<(.*)>; rel="(\w*)"/)
        let url = match[1]
        let rel = match[2]
        acc[rel] = url
        return acc;
      }, {});

      if (parts.next !== undefined) {
        $scope.next = parts.next;
      }
      if (parts.prev !== undefined) {
        $scope.prev = parts.prev;
      }
    };
    $scope.refreshData();

    $scope.getPrev = function () {
      Restangular.oneUrl('routeName',$scope.prev).get().then( function (response) {
        if($scope.id === undefined) {
          $scope.$parent.setData(response);
        } else {
          $scope.$parent.setData(response,$scope.id);
        }
        $scope.data = response.headers('link');
        $scope.refreshData();
      });
    };
    $scope.getNext = function () {
      Restangular.oneUrl('routeName',$scope.next).get().then( function (response) {
        if($scope.id === undefined) {
          $scope.$parent.setData(response);
        } else {
          $scope.$parent.setData(response,$scope.id);
        }
        $scope.data = response.headers('link');
        $scope.refreshData();
      });
    };
  });
	mod.directive('myNavbar', function() {
    return {
      restrict: 'E',
      controller: 'IndexCtrl',
      templateUrl: '../../views/components/navbar.html'
    };
  });
  mod.directive('pagination', function() {
    return {
      restrict: 'E',
      scope: {
        paging: '@',
        id: '@'
      },
      controller: 'PagingCtrl',
      templateUrl: '../../views/components/pagination.html'
    };
  });

});
