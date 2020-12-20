'use strict';
define(['frontend','services/pagingService'], function(frontend) {

  frontend.controller('PagingCtrl', function($scope, pagingService) {
    console.log($scope.paging);
    // this.getNext = pagingService.getPage($scope.paging.next);
    // this.getPrev = pagingService.getPage($scope.paging.prev);
    // let parts = $scope.paging.split(',').reduce((acc, link) => {
    //   let match = link.match(/<(.*)>; rel="(\w*)"/)
    //   let url = match[1]
    //   let rel = match[2]
    //   acc[rel] = url
    //   return acc;
    // }, {});
    // console.log(parts);

  });

});
