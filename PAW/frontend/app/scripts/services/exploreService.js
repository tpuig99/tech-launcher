'use strict';
define(['frontend'], function(frontend) {

	frontend.service('exploreService', function(Restangular) {


    this.getTechs = function() {
      return Restangular.one('explore?techs_page=1').get();
    };

    this.getPosts = function() {
      return Restangular.one('explore?posts_page=1&is_post=true').get();
    };

    this.search = function(toSearch,starsLeft, starsRight,nameFlag, commentAmount, lastComment, lastUpdate, groupBy, orderBy) {

      var toSearchQ = toSearch === undefined ? '' : toSearch;
      var starsLeftQ = starsLeft === undefined ? '0' : starsLeft;
      var starsRightQ = starsRight === undefined ? '5' : starsRight;
      var commentAmountQ = commentAmount === undefined ? '' : commentAmount;
      var lastCommentQ = lastComment === undefined ? '' : lastComment;
      var lastUpdateQ = lastUpdate === undefined ? '' : lastUpdate;
      var order;

      console.log(groupBy);
      console.log(orderBy);

      if (groupBy !== undefined && orderBy === undefined) {
        order = groupBy;
      } else if (groupBy !== undefined && orderBy !== undefined) {
        order = groupBy * orderBy;
      } else {
        order = '0';
      }


      var url = 'explore?to_search=' + toSearchQ + '&stars_left=' + starsLeftQ + '&stars_right=' + starsRightQ + '&name_flag=' + nameFlag + '&oder=' + order + '&comment_amount=' + commentAmountQ + '&last_comment=' + lastCommentQ + '&last_update=' + lastUpdateQ + '&techs_page=1';

      console.log(url);
      return Restangular.one(url).get();
    };



	});
});
