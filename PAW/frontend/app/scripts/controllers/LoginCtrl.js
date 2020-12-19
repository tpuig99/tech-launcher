'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {

    frontend.controller('LoginCtrl', function($scope, $http, $location, $localStorage, sessionService,Restangular) {

      $scope.failedLogin = false;
      $scope.login = function() {
        sessionService.login($scope.usernameInput, $scope.passwordInput).then(function(response) {
          console.log(response.status);
          if(response.status === 200){
            if (response.data.token) {
              // store username and token in local storage to keep user logged in between page refreshes
              $localStorage.currentUser = {location: response.data.location, token: response.data.token};
              // add jwt token to auth header for all requests made by the $http service
              // $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
              $scope.$parent.checkUser();
              $location.path('/');
            }
          } else {
            $scope.failedLogin = true;
            console.log('failed login');
          };


        });
      };

    });

});
