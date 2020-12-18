'use strict';
define(['frontend', 'services/sessionService'], function(frontend) {

    frontend.controller('LoginCtrl', function($scope, $http, $location, $localStorage, sessionService) {

      $scope.login = function() {
        sessionService.login($scope.usernameInput, $scope.passwordInput).then(function(response) {
          console.log(response);
          console.log(response.token);
          if (response.token) {
            // store username and token in local storage to keep user logged in between page refreshes
            $localStorage.currentUser = {location: response.location, token: response.token};
            console.log($localStorage.currentUser);
            // add jwt token to auth header for all requests made by the $http service
            $http.defaults.headers.common.Authorization = 'Bearer ' + response.token;

            $location.path("/");
            console.log("logged in");
          } else {

            console.log("failed login");
          };
        });
      };

    });

});
