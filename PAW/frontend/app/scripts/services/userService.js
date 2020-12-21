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
    this.setMod = function(url,set) {
      return Restangular.one(url + set).put();
    };
    this.update = function(file,description,id) {
      var fd = new FormData();
      if (file !== undefined) {
        fd.append('picture', file);
      }
      if (description !== undefined) {
        fd.append('description', description);
      }
      return Restangular.one('users/' + id).withHttpConfig({transformRequest: angular.identity})
        .customPUT(fd, '', undefined, {'Content-Type': undefined});
    };
  });
});
