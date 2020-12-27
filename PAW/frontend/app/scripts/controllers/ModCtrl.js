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
      }
    });

    $scope.getCurrentMods = () => {
      userService.getCurrentMods().then((response) => {
        $scope.mods = response.data;
      });
    };
    $scope.getCurrentMods();

    $scope.getCurrentApplicants = () => {
      userService.getCurrentApplicants().then((response) => {
        $scope.applicants = response.data;
      });
    };

    $scope.getCurrentApplicants();

    $scope.getVerified = () => {
      userService.getVerified().then((response) => {
        $scope.verified = response.data;
      });
    };

    $scope.getVerified();

    $scope.getReportedComments = () => {
      userService.getReportedComments().then((response) => {
        $scope.reportedComments = response.data;
      });
    };

    $scope.getReportedComments();

    $scope.getReportedContents = () => {
      userService.getReportedContents().then((response) => {
        $scope.reportedContents = response.data;
      });
    };

    $scope.getReportedContents();

    $scope.acceptMod = (applicant) => {
      userService.acceptMod(applicant.location);
    }

    $scope.rejectMod = (applicant) => {
      userService.rejectMod(applicant.location);
    }

    $scope.deleteMod = (mod) => {
      userService.deleteMod(mod.location);
    }

    $scope.deleteCommentReport = (reportedComment) => {
      userService.deleteCommentReport(reportedComment.location);
    };

    $scope.acceptCommentReport = (reportedComment) => {
      userService.acceptCommentReport(reportedComment.location);
    }

    $scope.deleteContentReport = (reportedContent) => {
      userService.deleteContentReport(reportedContent.location);
    }

    $scope.acceptContentReport = (reportedContent) => {
      userService.acceptContentReport(reportedContent.location);
    }

  });
});
