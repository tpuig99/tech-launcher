'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {

    frontend.controller('LoginCtrl', function($scope, $window, $http, $location, $localStorage, $sessionStorage, sessionService,Restangular) {
      $('.modal-backdrop').hide();
      $scope.failedLogin = false;
      $scope.rememberMe = false;
      $scope.login = function() {
        sessionService.login($scope.usernameInput, $scope.passwordInput).then(function(response) {
            if (response.data.token) {
              // store username and token in local storage to keep user logged in between page refreshes
              $localStorage.remember = {me: $scope.rememberMe};
              if ($localStorage.remember.me) {
                $localStorage.currentUser = {location: response.data.location, token: response.data.token};
              } else {
                $sessionStorage.currentUser = {location: response.data.location, token: response.data.token};
              }
              // add jwt token to auth header for all requests made by the $http service
              // $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
              $scope.$parent.checkUser();

             // $location.path('/');
             $window.location.href = '/';
            }
        }, function(response) {
          $scope.failedLogin = true;
        });
      };




    });

});
