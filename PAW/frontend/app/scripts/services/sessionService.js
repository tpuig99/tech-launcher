'use strict';
define(['frontend'], function(frontend) {

	frontend.service('sessionService', function(Restangular) {


    /* Register */
    this.createUser = function (email, username, password) {
      var user = {
        'username': username,
        'mail': email,
        'password': password
      };
      console.log(username); console.log(password); console.log(email);
      return Restangular.all('register').post(user);
    };

    /* Login */
    this.login = function (username, password) {

      var user = {
        'username': username,
        'password': password
      };
      console.log(username); console.log(password);
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

      return Restangular.all('register/forgot_password').post();
    }

    /* Change Password */
    this.changePassword = function(token, password){
      var newPassword = {
        'token': token,
        'password': password,
      }
      return Restangular.all('users/password').post();
    }


	});
});
