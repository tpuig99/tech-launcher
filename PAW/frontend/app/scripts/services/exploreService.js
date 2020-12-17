'use strict';
define(['frontend'], function(frontend) {

	frontend.service('exploreService', function(Restangular) {


    this.getTechs = function() {
      return Restangular.one('explore?techs_page=1').get();
    };

    this.getPosts = function() {
      return Restangular.one('explore?posts_page=1&is_post=true').get();
    };



	});
});
