'use strict';
define(['frontend'], function(frontend) {

  frontend.service('userService', function(Restangular) {
    this.getUser = function(id) {
      return Restangular.one('users',id).get();
    };
    this.getPicture = function(id) {
      return Restangular.one('users/' + id + '/image').get();
    };
    this.getData = function(url) {
      return Restangular.one(url).get();
    };
    this.setMod = function(url,set) {
      return Restangular.one(url + set).put();
    };
    this.update = function(file,description,id) {
      var fd = new FormData();
      if (file !== undefined) {
        fd.append('picture', file);
      }
      if (description !== undefined) {
        fd.append('description', description);
      }
      return Restangular.one('users/' + id).withHttpConfig({transformRequest: angular.identity})
        .customPUT(fd, '', undefined, {'Content-Type': undefined});
    };

    this.getCurrentMods = () => {
      return Restangular.one('mod/moderators').get();
    };

    this.getCurrentApplicants = () => {
      return Restangular.one('mod/applicants').get();
    };

    this.getVerified = () => {
      return Restangular.one('mod/verified').get();
    };

    this.getReportedComments = () => {
      return Restangular.one('mod/reported_comments').get();
    };

    this.getReportedContents = () => {
      return Restangular.one('mod/reported_contents').get();
    };

    this.deleteMod = (mod) => {
      return Restangular.one(mod).remove();
    };

    this.acceptMod = (mod) => {
      return Restangular.one(mod).post()
    };

    this.rejectMod = (mod) => {
      return Restangular.one(mod).remove();
    };

    this.applyForTech = (tech) => {
      return Restangular.one(tech).post();
    };

    this.quitTech = (tech) => {
      return Restangular.one(tech).remove();
    };

    this.deleteCommentReport = (reportedComment) => {
      return Restangular.one(reportedComment ).remove();
    };

    this.acceptCommentReport = (reportedComment) => {
      return Restangular.one(reportedComment).post();
    }

    this.deleteContentReport = (reportedContent) => {
      return Restangular.one(reportedContent).remove();
    }

    this.acceptContentReport = (reportedContent) => {
      return Restangular.one(reportedContent).post();
    }
  });
});
