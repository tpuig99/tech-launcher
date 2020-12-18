'use strict';
define(['frontend','services/techsService'], function(frontend) {

  frontend.controller('TechsCtrl', function($scope, $location, $window, $routeParams, techsService, Restangular) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;

    techsService.getCategories().then(function (cats) {
      $scope.categories = cats;
    });
    techsService.getHomeInfo().then(function (techs) {
      $scope.home = techs;
    });

    /*
    techsService.getByCategory(category).then( function (techs) {
      $scope.techs = techs;
    });
     */
    $scope.techs = {
      "amount": 17,
      "techs": [
        {
          "location": "http://localhost:8080/techs/13",
          "loggedStars": 0,
          "name": "ES6 Tools"
        },
        {
          "location": "http://localhost:8080/techs/15",
          "loggedStars": 0,
          "name": "CSS"
        },
        {
          "location": "http://localhost:8080/techs/17",
          "loggedStars": 0,
          "name": "ReactAAAAA"
        },
        {
          "location": "http://localhost:8080/techs/19",
          "loggedStars": 0,
          "name": "Polymer"
        },
        {
          "location": "http://localhost:8080/techs/20",
          "loggedStars": 0,
          "name": "AngularJS"
        },
        {
          "location": "http://localhost:8080/techs/21",
          "loggedStars": 0,
          "name": "Backbone.js"
        },
        {
          "location": "http://localhost:8080/techs/22",
          "loggedStars": 0,
          "name": "HTML5"
        }
      ]
    };

  });
});
