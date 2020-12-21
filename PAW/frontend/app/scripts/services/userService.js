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
    this.update = function(picture,description,id) {
      var update = {
        'description': description,
        'picture': picture
      };
      var fd = new FormData();
      fd.append('picture',picture);
      fd.append('description',description);
      return Restangular.one('users/'+id).withHttpConfig({transformRequest: angular.identity})
        .customPUT(fd, '', undefined, {'Content-Type': undefined});
    };
  });
});
