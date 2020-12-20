'use strict';
define(['frontend'], function(frontend) {

  frontend.service('userService', function(Restangular) {
    this.getUser = function(id) {
      return Restangular.one('users',id).get();
    };
    this.getPicture = function(id) {
      return Restangular.one('users/' + id + '/image').get();
    };
    this.getData = function(url) {
      return Restangular.one(url).get();
    };
  });
});
