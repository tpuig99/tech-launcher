'use strict';
define(['frontend'], function(frontend) {

  frontend.service('techsService', function(Restangular) {
    this.getHomeInfo = function() {
      return Restangular.one('techs').get();
    };

    this.getCategories = function() {
      return Restangular.one('techs/category').get();
    };
    this.getTypes = function() {
      return Restangular.one('techs/types').get();
    };
  });
});
