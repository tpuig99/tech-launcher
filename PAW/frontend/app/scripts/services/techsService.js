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

    this.addReply = function (location, input) {
      var reply = {
        'description': input
      };
      return Restangular.all(location).post(reply);
    };

    this.report = function (url, description) {
      var report = {
        'description': description
      };
      return Restangular.all(url).post(report);
    };

    this.vote = function (url, type) {
      switch(type) {
        case 'up':
          return Restangular.all(url+'/upvote').post();
        case 'down':
          return Restangular.all(url+'/downvote').post();
        default:
          break;
      }
    }

    this.rate = function (id, stars) {
      let vote = {
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

  });
});
