'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {


  frontend.controller('RegisterConfirmCtrl', function($scope, $routeParams, sessionService, $filter, $location) {
    $scope.registerConfirmed = false;
    sessionService.confirmRegister($routeParams.token).then(function(response) {
      if (response.status === 200) {
        $scope.registerConfirmed = true;
        $scope.title = $filter('translate')('REGISTER_SUCCESS_EMAIL_VALIDATED');
        $scope.message = $filter('translate')('REGISTER_SUCCESS_ACCOUNT_VALIDATED');
      }
    });
    $scope.goHome = () => {
      $location.path('/')
    }
  });
});
