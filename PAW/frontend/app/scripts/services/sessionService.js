'use strict';
define(['frontend'], function(frontend) {

	frontend.service('sessionService', function(Restangular, $localStorage, $sessionStorage) {


    /* Register */
    this.createUser = function (email, username, password) {
      var user = {
        'username': username,
        'mail': email,
        'password': password
      };
      return Restangular.all('register').post(user);
    };

    /* Login */
    this.login = function (username, password) {

      var user = {
        'username': username,
        'password': password
      };
      return Restangular.all('login').post(user);
    };

    /* User Status */
    this.getCurrentUser = function (location) {
      return Restangular.one(location).get();
    };


    /* Recover Password */
    this.setPasswordToken = function (email) {
      var mail = {
        'mail': email
      };

      return Restangular.all('register/forgot_password').post(mail);
    };

    /* Change Password */
    this.changePassword = function(token, password) {
      console.log(token);
      console.log(password);
      var newPassword = {
        'token': token,
        'password': password
      };
      return Restangular.all('users/password').post(newPassword);
    };

    this.getStorageUser = function() {
      var user;
      if ($localStorage.remember.me) {
        user = $localStorage.currentUser;
      } else {
        user = $sessionStorage.currentUser;
      }
      return user;
    };


	});
});
