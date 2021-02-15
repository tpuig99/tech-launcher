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
      return Restangular.oneUrl('data',url).get();
    };

    this.deleteData = function(url) {
      return Restangular.oneUrl('data',url).remove();
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

    this.addReply = function (location, input) {
      var reply = {
        'description': input
      };
      return Restangular.allUrl('comment',location).post(reply);
    };

    this.report = function (url, description) {
      var report = {
        'description': description
      };
      return Restangular.allUrl('report',url).post(report);
    };

    this.vote = function (url, type) {
      switch(type) {
        case 'up':
          return Restangular.oneUrl('comments',url+'/upvote').post();
        case 'down':
          return Restangular.oneUrl('comments',url+'/downvote').post();
        default:
          break;
      }
    }

    this.rate = function (id, stars) {
      var vote = {
        'count': stars
      }
      return Restangular.all('techs/'+id+'/stars').post(vote);
    }

    this.addTech = function(add) {
      return Restangular.all('techs').post(add);
    };

    this.addTech = function(add) {
      var fd = new FormData();
      fd.append('name', add.name);
      fd.append('category', add.category);
      fd.append('type', add.type);
      fd.append('description', add.description);
      fd.append('introduction', add.introduction);
      fd.append('picture', add.picture);

      return Restangular.one('techs/').withHttpConfig({transformRequest: angular.identity})
        .customPOST(fd, '', undefined, {'Content-Type': undefined});
    };

    this.editTech = function(id, edit) {
      var fd = new FormData();
      fd.append('name', edit.name);
      fd.append('category', edit.category);
      fd.append('type', edit.type);
      fd.append('description', edit.description);
      fd.append('introduction', edit.introduction);
      if (edit.picture !== undefined) {
        fd.append('picture', edit.picture);
      }

      return Restangular.one('techs/'+id).withHttpConfig({transformRequest: angular.identity})
        .customPUT(fd, '', undefined, {'Content-Type': undefined});
    };

    this.applyForMod = function (location) {
      return Restangular.allUrl('mod',location).post();
    };

    this.stopBeingAMod = function (location) {
      return Restangular.oneUrl('mod',location).remove();
    };

  });
});
