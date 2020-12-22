'use strict';
define(['frontend'], function(frontend) {

  frontend.service('techsService', function(Restangular) {
    this.getHomeInfo = function() {
      return Restangular.one('techs').get();
    };

    this.getByCategory = function (category) {
      return Restangular.one('techs/category/' + category).get();
    };

    this.getCategories = function() {
      return Restangular.one('techs/category').get();
    };
    this.getTypes = function() {
      return Restangular.one('techs/types').get();
    };

    this.getTech = function(id) {
      return Restangular.one('techs',id).get();
    };

    this.getPicture = function(id) {
      return Restangular.one('techs/' + id + '/image').get();
    };

    this.getData = function(url) {
      return Restangular.one(url).get();
    };


  });
});
