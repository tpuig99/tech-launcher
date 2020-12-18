'use strict';
define(['frontend'], function(frontend) {

	frontend.service('postService', ["Restangular", function(Restangular) {

    this.getPost = function(id) {
      let post = Restangular.one('posts',id);
      return post.get();
    };

    this.getAnswers = function(id) {
      return Restangular.one('posts',id).getList('answers');
    };

    this.deletePost = function(id){
      return Restangular.one('posts',id).remove();
    };

    this.deletePost = function(url){
      return Restangular.one(url).remove();
    };
    this.upVote = function(post){
      post.votesUp += 1;
      post.post();
    }

    this.downVote = function(post){
      post.votesDown += 1;
      post.post();
    }

    this.getPosts = function() {
      return Restangular.one('posts').get();
    };
	}]);
});
