'use strict';
define(['frontend'], function(frontend) {

	frontend.service('postService', function(Restangular) {

    this.getPost = function(id) {
      let post = Restangular.one('posts',id);
      return post.get();
    };

    this.getAnswers = function(id) {
      return Restangular.one('posts',id).getList('answers');
    };

    this.deletePost = function(id){
      return Restangular.one('posts',id).remove();
    }

    this.upVote = function(post){
      post.votesUp += 1;
      post.post();
    }

    this.downVote = function(post){
      post.votesDown += 1;
      post.post();
    }

	});
});
