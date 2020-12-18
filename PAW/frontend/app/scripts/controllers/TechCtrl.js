'use strict';
define(['frontend'], function(frontend) {

  frontend.controller('TechCtrl', function($scope) {
    $scope.isAdmin = true;
    $scope.isOwner = true;
    $scope.isEnable = false;
    $scope.isMod = true;
    $scope.isPresent = true;
    $scope.categories = [ {category : 'category1',location : 'location1'},{category : 'category2',location : 'location2'},{category : 'category3',location : 'location3'}];
    $scope.hots = [ {name : 'hot1',location : 'hot1'},{name : 'hot2',location : 'hot2'},{name : 'hot3',location : 'hot3'}];
    $scope.interestsList = [ {name : 'interest1',location : 'interest1'},{name : 'interest2',location : 'interest2'},{name : 'interest3',location : 'interest3'}];
  });

});
