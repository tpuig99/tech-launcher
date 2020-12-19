'use strict';
define(['frontend'], function(frontend) {

	frontend.service('sessionService', function(Restangular) {

    this.createUser = function (email, username, password) {
      var user = {
        'username': username,
        'mail': email,
        'password': password
      };
      console.log(username); console.log(password); console.log(email);
      return Restangular.all('register').post(user);
    };

    this.login = function (username, password) {

      var user = {
        'username': username,
        'password': password
      };
      console.log(username); console.log(password);
      return Restangular.all('login').post(user);
    };

    this.getCurrentUser = function (location) {
      return Restangular.one(location).get();
    };
	});
});
