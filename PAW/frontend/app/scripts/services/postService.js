'use strict';
define(['frontend'], function(frontend) {

	frontend.service('postService', function(Restangular) {

	 /* let apiRest = Restangular.withConfig( function(RestangularConfigurer) {
	    RestangularConfigurer.addResponseInterceptor(
	      function(body, request, response){
	        if(request === 'get'){
	          return body;
          }else{
	          return response;
          }

        }
      );
    });

	  this.postUpVote = function(post){

	    let vote = post.votesUp += 1;

	    post.post("up_vote",vote);
    }*/

	});
});
