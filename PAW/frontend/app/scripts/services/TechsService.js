'use strict';
define(['frontend'], function(frontend) {

  frontend.service('techs Service', function() {

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

     $scope.homeInfo = Restangular.all('techs').getList().$object;
     $scope.categories = Restangular.all('techs/categories').getList().$object;
     }*/

  });
});
