'use strict';
define(['frontend','services/userService','services/sessionService'], function(frontend) {

  frontend.controller('ModCtrl', function($scope, $location, sessionService, $window, $routeParams, $sessionStorage,userService, $localStorage) {
    $scope.isPresent = false;

    $scope.$parent.$watch('username',function () {
      let user = sessionService.getStorageUser();
      if (user !== undefined) {
        sessionService.getCurrentUser(user.location).then(function (response) {
          $scope.username = response.data.username;
          $scope.isMod = response.data.verify;
          $scope.isAdmin = response.data.admin;
          $scope.isOwner = response.data.techsAmount > 0;
          $scope.isEnable = response.data.enabled;
          $scope.isPresent = true;
        });
        $scope.getCurrentMods();
        $scope.getCurrentApplicants();
        $scope.getVerified();
        $scope.getReportedComments();
        $scope.getReportedContents();
      }
    });

    $scope.getCurrentMods = () => {
      userService.getCurrentMods().then((response) => {
        $scope.mods = response.data;
        $scope.modsPaging = response.headers('link');
      });
    };

    $scope.getCurrentApplicants = () => {
      userService.getCurrentApplicants().then((response) => {
        $scope.applicants = response.data;
        $scope.applicantsPaging = response.headers('link');
      });
    };

    $scope.getVerified = () => {
      userService.getVerified().then((response) => {
        $scope.verified = response.data;
        $scope.verifiedPaging = response.headers('link');
      });
    };

    $scope.getReportedComments = () => {
      userService.getReportedComments().then((response) => {
        $scope.reportedComments = response.data;
        $scope.repComsPaging = response.headers('link');
      });
    };

    $scope.getReportedContents = () => {
      userService.getReportedContents().then((response) => {
        $scope.reportedContents = response.data;
        $scope.repConsPaging = response.headers('link');
      });
    };

    $scope.acceptMod = (applicant) => {
      userService.acceptMod(applicant.location).then((response) => {
        $scope.getCurrentApplicants();
        $scope.getVerified();
        $scope.getCurrentMods();
      });
    }

    $scope.rejectMod = (applicant) => {
      userService.rejectMod(applicant.location).then((response) => {
        $scope.getCurrentApplicants();
        $scope.getVerified();
      });
    }

    $scope.deleteMod = (mod) => {
      userService.deleteMod(mod.location).then((response) => {
        $scope.getCurrentMods();
      });
    }

    $scope.deleteCommentReport = (reportedComment) => {
      userService.deleteCommentReport(reportedComment.location).then((response) => {
        $scope.getReportedComments();
      });
    };

    $scope.acceptCommentReport = (reportedComment) => {
      userService.acceptCommentReport(reportedComment.location).then((response) => {
        $scope.getReportedComments();
      });
    }

    $scope.deleteContentReport = (reportedContent) => {
      userService.deleteContentReport(reportedContent.location).then((response) => {
        $scope.getReportedContents();
      });
    }

    $scope.acceptContentReport = (reportedContent) => {
      userService.acceptContentReport(reportedContent.location).then((response) => {
        $scope.getReportedContents();
      })
    }

    $scope.arrayIsEmpty = (array) => {
      return _.isEmpty(array);
    }

    $scope.setData = function(response,id) {
      switch (id) {
        case 'mods':
          $scope.mods = response.data;
          $scope.modsPaging = response.headers('link');
          console.log($scope.modsPaging)
          break;
        case 'repCons':
          $scope.reportedContents = response.data;
          $scope.repConsPaging = response.headers('link');
          break;
        case 'repComs':
          $scope.reportedComments = response.data;
          $scope.repComsPaging = response.headers('link');
          break;
        case 'applicants':
          $scope.applicants = response.data;
          $scope.applicantsPaging = response.headers('link');
          break;
        case 'verified':
          $scope.verified = response.data;
          $scope.verifiedPaging = response.headers('link');
          break;
        default:
          break;
      }
    };
  });
});
