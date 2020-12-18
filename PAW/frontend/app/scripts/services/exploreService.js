'use strict';
define(['frontend'], function(frontend) {

	frontend.service('exploreService', function(Restangular) {


    this.getTechs = function() {
      return Restangular.one('explore?techs_page=1').get();
    };

    this.getPosts = function() {
      return Restangular.one('explore?posts_page=1&is_post=true').get();
    };

    this.search = function(toSearch,starsLeft, starsRight,nameFlag, commentAmount, lastComment, lastUpdate){

      var to_search = toSearch === undefined ? '' : toSearch;
      var stars_left = starsLeft === undefined ? '0' : starsLeft;
      var stars_right = starsRight === undefined ? '5' : starsRight;
      var comment_amount = commentAmount === undefined ? '' : commentAmount;
      var last_comment = lastComment === undefined ? '' : lastComment;
      var last_update = lastUpdate === undefined ? '' : lastUpdate;


      var url = 'explore?to_search='+to_search+'&stars_left='+stars_left+'&stars_right='+stars_right+'&name_flag='+nameFlag+'&comment_amount='+comment_amount+'&last_comment='+last_comment+'&last_update='+last_update+'&techs_page=1'

      console.log(url);
      return Restangular.one(url).get();
    }



	});
});
