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

    this.deleteData = function(url) {
      return Restangular.one(url).remove();
    };

    this.addContent = function (id, title, type, link) {
      var content = {
        'title': title,
        'type': type,
        'link': link
      };
      return Restangular.all('techs/' + id + '/content').post(content);
    };

    this.addComment = function (id, input) {
      var comment = {
        'description': input
      };
      return Restangular.all('techs/' + id + '/comment').post(comment);
    };

    this.report = function (url, description) {
      var report = {
        'description': description
      };
      return Restangular.all(url).post(report);
    };

  });
});
