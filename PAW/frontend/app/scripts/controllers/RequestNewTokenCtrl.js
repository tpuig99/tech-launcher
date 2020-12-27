'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {
  frontend.controller('RequestNewTokenCtrl', function($scope, $routeParams, sessionService, $filter, $location) {
    $scope.registerConfirmed = false;
    $scope.requestNewToken = () => {
      sessionService.requestNewToken().then((response) => {
        if( response.status !== 200 ){

        }
      }).catch((error) => {
        if(error.status === 410 ){

        }
      })
    }
  });
});
