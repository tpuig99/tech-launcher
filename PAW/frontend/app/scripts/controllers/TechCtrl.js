'use strict';
define(['frontend', 'services/techsService', 'services/sessionService'], function(frontend) {

  frontend.controller('TechCtrl', function($scope, $location, $window, $routeParams, techsService, $sessionStorage,Restangular, sessionService, $localStorage) {

    $scope.isPresent = false;
    $scope.isVerify = false;
    $scope.verifyPending = false;
    $scope.isOwner = false;

    $scope.getUser = function () {
      var user = sessionService.getStorageUser();

      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          $scope.username = response.data.username;
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.isEnable = response.data.enabled;
          $scope.allowMod = response.data.allowedModeration;
          $scope.isPresent = true;
          $scope.userVerifications = response.data.applications;
          $scope.userVotes = response.data.techVotes;

          if ($scope.userVerifications !== undefined && $scope.userVerifications.length > 0) {
            let verification;
            for (verification of $scope.userVerifications) {
              if (verification.frameworkName === $scope.tech.name) {
                if (!verification.pending) {
                  $scope.isVerify = true;
                  $scope.verifyPending = false;
                } else {
                  $scope.isVerify = false;
                  $scope.verifyPending = true;
                }
              }

            }
          }
        });
      }
    }

    $scope.getTech = function() {

      $scope.getUser();

      techsService.getTech($routeParams.id).then(function (tech) {
        $scope.tech = tech.data;

        if ($scope.tech.author === $scope.username) {
          $scope.isOwner = true;
        }

        if ($scope.userVerifications !== undefined && $scope.userVerifications.length > 0) {
          let verification;
          for (verification of $scope.userVerifications) {
            if (verification.frameworkName === $scope.tech.name) {
              if (!verification.pending) {
                $scope.isVerify = true;
                $scope.verifyPending = false;
              } else {
                $scope.isVerify = false;
                $scope.verifyPending = true;
              }
            }

          }
        }

        if ($scope.userVotes !== undefined && $scope.userVotes.length > 0) {
          let vote;
          for (vote of $scope.userVotes) {
            if (vote.techName === $scope.tech.name) {
              console.log(vote.vote);
              $scope.star = vote.vote;
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
      if (data.reports !== undefined && data.reports.length > 0) {
        for (report of data.reports) {
          if (report.reported === username) {
            return true;
          }
        }
      }
      return false;
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
        $location.path('/techs');
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

    $scope.addContent = function (title, type, link) {
      $scope.contentNameError = false;
      techsService.checkTitle($routeParams.id, title, type).then(function (response) {
        if (response.status === 200) {
          techsService.addContent($routeParams.id, title, type, link).then(function () {
            $scope.getTech();
            $('#addContentModal').modal('hide');
          });
          $('#addContentTitle').val('')
          $('#addContentType').val('')
          $('#addContentLink').val('')
        }
      }).catch(function () {
        $scope.contentNameError = true;
      })
    };


    $scope.addComment = function(input) {
      techsService.addComment($routeParams.id, input).then(function () {
        $scope.getTech();
        $scope.commentInput = '';
      });
    }

    $scope.replyComment = function (location, input) {
      techsService.addReply(location, input).then(function () {
        $scope.getTech();
        $scope.commentInput = '';
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

    $scope.hasUserVotedUp = function (comment, username) {
      let commentVote;
      if (comment.votes == null) {
        return false;
      }
      for (commentVote of comment.votes) {
        if (commentVote.voter === username && commentVote.value > 0) {
          return true;
        }
      }
      return false;
    }

    $scope.hasUserVotedDown = function (comment, username) {
      let commentVote;
      if (comment.votes == null) {
        return false;
      }

      for (commentVote of comment.votes) {
        if (commentVote.voter === username && commentVote.value < 0) {
          return true;
        }
      }
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
      $scope.techNameError = false;
      techsService.checkName($scope.add.name).then(function (response) {
        if (response.status === 200) {
          techsService.addTech($scope.add).then(function (response) {
            if (response.status === 201) {
              $location.path('/techs');
            }
          });
        }
      }).catch(function () {
          $scope.techNameError = true;
      })
    };

    $scope.applyForMod = function (location) {
      techsService.applyForMod(location).then(function (response) {
        if (response.status === 200) {
          $scope.getUser();
          $scope.getTech();
        }
      })
    };

    $scope.stopBeingAMod = function (location) {
      techsService.stopBeingAMod(location).then(function (response) {
        if (response.status === 200) {
          $scope.getUser();
          $scope.getTech();
        }
      })
    };

  });

});
