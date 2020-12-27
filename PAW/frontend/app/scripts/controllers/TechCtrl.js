'use strict';
define(['frontend', 'services/techsService', 'services/sessionService'], function(frontend) {

  frontend.controller('TechCtrl', function($scope, $location, $window, $routeParams, techsService, $sessionStorage,Restangular, sessionService, $localStorage) {

    $scope.isPresent = false;
    $scope.isVerify = false;
    $scope.isOwner = false;

    var user = sessionService.getStorageUser();

    if (user !== undefined) {
      sessionService.getCurrentUser(user.location).then(function (response) {
        $scope.username = response.data.username;
        $scope.isMod = response.data.verify;
        $scope.isAdmin = response.data.admin;
        $scope.isEnable = response.data.enabled;
        $scope.isPresent = true;
        $scope.userVerifications = response.data.verifications;
      });
    }

    $scope.getTech = function() {

      techsService.getTech($routeParams.id).then(function (tech) {
        $scope.tech = tech.data;

        if ($scope.tech.author === $scope.username) {
          $scope.isOwner = true;
        }

        if ($scope.userVerifications !== undefined && $scope.userVerifications.length > 0) {
          let verification;
          for (verification of $scope.userVerifications) {
            console.log("verification:" + verification);
            if (!verification.pending && verification.frameworkName === $scope.tech.name) {
              $scope.isVerify = true;
            }
          }
        }

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

        techsService.getData($scope.tech.competitors).then(function (competitors) {
          $scope.tech.competitors = competitors.data;
        });

      });
    };

    $scope.getTech();

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
        case 'competitors':
          $scope.tech.competitors = response.data;
          break;
        default:
          break;
      }
    };


    $scope.redirect = function(url) {
      $location.path(url);
    };

    $scope.isReporter = function (data, username) {
      let report;
      for (report of data.reports) {
        if (report.reported === username) {
          return true;
        }
      }
      return false;
    }

    $scope.logAll = function (p1, p2, p3, p4) {
      console.log("p1: " + p1 + "   p2: " + p2 + "     p3: " + p3 + "    p4: "+ p4);
    }

    $scope.setDel = function (url) {
      $scope.toDel = url;
    };

    $scope.cleanDel = function () {
      $scope.toDel = undefined;
    };

    $scope.setReport = function (url) {
      $scope.toReport = url;
    };

    $scope.cleanReport = function () {
      $scope.toReport = undefined;
    };

    $scope.deleteTech = function() {
      $('#deleteTechModal').modal('hide');
      techsService.deleteData($scope.toDel).then(function() {
        $location.path('/#/techs');
      });
    };

    $scope.deleteContent = function() {
      techsService.deleteData($scope.toDel).then(function() {
        $scope.getTech();
        $('#deleteContentModal').modal('hide');
      });
    };

    $scope.deleteComment = function() {
      techsService.deleteData($scope.toDel).then(function() {
        $scope.getTech();
        $('#deleteCommentModal').modal('hide');
      });
    };

    $scope.addContent = function(title, type, link) {
      techsService.addContent($routeParams.id, title, type, link).then(function () {
        $scope.getTech();
        $('#addContentModal').modal('hide');
      });
    }

    $scope.addComment = function(input) {
      techsService.addComment($routeParams.id, input).then(function () {
        $scope.getTech();
      });
    }

    $scope.replyComment = function (location, input) {
      techsService.addReply(location, input).then(function () {
        $scope.getTech();
      });
    }

    $scope.reportContent = function(description) {
      techsService.report($scope.toReport, description).then(function () {
        $scope.getTech();
        $('#reportContentModal').modal('hide');
      })
    }

    $scope.reportComment = function(description) {
      techsService.report($scope.toReport, description).then(function () {
        $scope.getTech();
        $('#reportCommentModal').modal('hide');
      })
    }

    $scope.upVote = function (location) {
      techsService.vote(location, 'up').then(function () {
        $scope.getTech();
      })
    }

    $scope.downVote = function (location) {
      techsService.vote(location, 'down').then(function () {
        $scope.getTech();
      })
    }

    $scope.rateTech = function (stars) {
      techsService.rate($routeParams.id, stars).then(function () {
        $scope.getTech();
      })
    }

    $scope.hasUserVoted = function (comment, username) {
      return false;
    }

    techsService.getCategories().then(function (cats) {
      $scope.categories = cats.data;
    });

    techsService.getTypes().then(function (cats) {
      $scope.types = cats.data;
    });

    $scope.setPic = function(file) {
      $scope.add.picture = file;
    };

    $scope.addTech = function () {
      techsService.addTech($scope.add).then(function (response) {
        if (response.status === 201) {
          $location.path('/#/techs');
        }
      });
    };

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
