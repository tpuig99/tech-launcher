'use strict';
define(['frontend', 'services/techsService', 'services/sessionService'], function(frontend) {

  frontend.controller('TechCtrl', function($scope, $location, $window, $routeParams, techsService, $sessionStorage,Restangular, sessionService, $localStorage) {

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

    techsService.getTech($routeParams.id).then(function (tech) {
      $scope.tech = tech.data;

      techsService.getData($scope.tech.comments).then(function (comments) {
        $scope.tech.comments = comments.data;
        $scope.commentsPaging = comments.headers('link');
      });

      techsService.getData($scope.tech.books).then(function (books) {
        $scope.tech.books = books.data;
        $scope.booksPaging = books.headers('link');
      });

      techsService.getData($scope.tech.courses).then(function (courses) {
        $scope.tech.courses = courses.data;
        $scope.coursesPaging = courses.headers('link');
      });

      techsService.getData($scope.tech.tutorials).then(function (tutorials) {
        $scope.tech.tutorials = tutorials.data;
        $scope.tutorialsPaging = tutorials.headers('link');
      });

    });


    $scope.setData = function(response,id) {
      switch (id) {
        case 'comments':
          $scope.tech.comments = response.data;
          $scope.commentsPaging = response.headers('link');
          break;
        case 'books':
          $scope.tech.books = response.data;
          $scope.booksPaging = response.headers('link');
          break;
        case 'courses':
          $scope.tech.courses = response.data;
          $scope.coursesPaging = response.headers('link');
          break;
        case 'tutorials':
          $scope.tech.tutorials = response.data;
          $scope.tutorialsPaging = response.headers('link');
          break;
        default:
          break;
      }
    };


    $scope.redirect = function(url) {
      $location.path(url);
    };

    // $scope.deleteTech = function() {
    //   techsService.deleteTech($scope.toDel).then(function() {
    //     $('#deleteTechModal').modal('hide');
    //     $location.path('/#/techs');
    //   });
    // };
    //
    // $scope.rateTech = function(stars) {
    //   techsService.rateTech($scope.post.location, stars).then($scope.getTech());
    // };
    //
    // $scope.upVoteComment = function(location) {
    //   techsService.upVoteComment(location).then($scope.getComments());
    // };
    //
    // $scope.downVoteComment = function(location) {
    //   techsService.downVoteComment(location).then($scope.getComments());
    // };
    //
    // $scope.commentTech = function(answer) {
    //   techsService.commentTech($routeParams.id, answer).then(function () {
    //     $scope.getPost();
    //     $scope.getAnswers();
    //   });
    // };
    //
    // $scope.addContentTech = function(title, category, link) {
    //   techsService.addContentTech($routeParams.id, title, category, link).then(function () {
    //     $scope.getTech();
    //     switch (category) {
    //       case 'bibliography': $scope.getBibliography(); break;
    //       case 'course': $scope.getCourses(); break;
    //       case 'tutorial': $scope.getTutorials(); break;
    //       default: break;
    //     }
    //   });
    // };

    // $scope.setData = function(response) {
    //   $scope.answers = response.data;
    //   $scope.pagingLinks = response.headers('link');
    // };

    // $('#deleteCommentModal').on('hide.bs.modal',function () {
    //   $scope.cleanDel();
    // });
  });

});
