'use strict';
define(['frontend', 'services/techsService', 'services/sessionService'], function(frontend) {

  frontend.controller('TechCtrl', function($scope, $location, $window, $routeParams, postService, $sessionStorage,Restangular, sessionService, $localStorage) {

    $scope.isPresent = false;

    var user = sessionService.getStorageUser();

    if (user !== undefined) {
      sessionService.getCurrentUser(user.location).then(function (response) {
        $scope.username = response.data.username;
        $scope.isMod = response.data.verify;
        $scope.isAdmin = response.data.admin;
        $scope.isEnabled = response.data.enabled;
        $scope.isPresent = true;
      });
    }


    $scope.getTech = function() {
      techsService.getTech($routeParams.id).then(function(response) {
        $scope.tech = response.data;
      });
    };

    $scope.getBibliography = function() {
      techsService.getBibliography($routeParams.id).then(function(response) {
        $scope.bibligraphy = response.data;
        $scope.bibliographyPagingLinks = response.headers('link');
      });
    };

    $scope.getTutorials = function() {
      techsService.getTutorials($routeParams.id).then(function(response) {
        $scope.tutorials = response.data;
        $scope.tutorialsPagingLinks = response.headers('link');
      });
    };

    $scope.getCourses = function() {
      techsService.getCourses($routeParams.id).then(function(response) {
        $scope.courses = response.data;
        $scope.coursesPagingLinks = response.headers('link');
      });
    };

    $scope.getComments = function() {
      techsService.getComments($routeParams.id).then(function(response) {
        $scope.comments = response.data;
        $scope.commentsPagingLinks = response.headers('link');
      });
    };

    $scope.getTech();
    $scope.getBibliography();
    $scope.getCourses();
    $scope.getTutorials();
    $scope.getComments();


    $scope.redirect = function(url) {
      $location.path(url);
    };

    $scope.deleteTech = function() {
      techsService.deleteTech($scope.toDel).then(function() {
        $('#deleteTechModal').modal('hide');
        $location.path('/#/techs');
      });
    };

    $scope.rate = function(stars) {
      techsService.rate($scope.post.location, stars).then($scope.getTech());
    };

    $scope.upVoteComment = function(location) {
      techsService.upVoteComment(location).then($scope.getComments());
    };

    $scope.downVoteComment = function(location) {
      techsService.downVoteComment(location).then($scope.getComments());
    };

    $scope.commentTech = function(answer) {
      techsService.commentTech($routeParams.id, answer).then(function () {
        $scope.getPost();
        $scope.getAnswers();
      });
    };

    $scope.addContentTech = function(title, category, link) {
      techsService.addContentTech($routeParams.id, title, category, link).then(function () {
        $scope.getTech();
        switch (category) {
          case 'bibliography': $scope.getBibliography(); break;
          case 'course': $scope.getCourses(); break;
          case 'tutorial': $scope.getTutorials(); break;
          default: break;
        }
      });
    };

    // $scope.setData = function(response) {
    //   $scope.answers = response.data;
    //   $scope.pagingLinks = response.headers('link');
    // };

    // $('#deleteCommentModal').on('hide.bs.modal',function () {
    //   $scope.cleanDel();
    // });
  });

});
