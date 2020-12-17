'use strict';
define(['frontend'], function(frontend) {

  frontend.controller('PostsCtrl', function($scope) {
    $scope.isAdmin = true;
    $scope.isEnable = false;
    $scope.isPresent = true;
    $scope.page_size = 7;
    $scope.curr_page = 1;
    $scope.posts = [{title: 'post1',description:'description',votesUp: 2, votesDown: 3, user: {name:'user1',isAuthor: true,vote: 0}, location:'/post/1', tags:[{tag:'OO', type:'category'},{tag:'tag2', type:'tech_name'}],time:'10-feb-2020 19:00'},
      {title: 'post2',description:'description',votesUp: 2, votesDown: 3, user: {name:'user2',isAuthor: true,vote: 1}, location:'/post/1', tags:[{tag:'OO', type:'category'},{tag:'tag2', type:'tech_name'}],time:'10-feb-2020 19:00'},
      {title: 'post3',description:'description',votesUp: 2, votesDown: 3, user: {name:'user3',isAuthor: true,vote: -1}, location:'/post/1', tags:[{tag:'OO', type:'category'},{tag:'tag2', type:'tech_name'}],time:'10-feb-2020 19:00'}];
    $scope.postAmount = 20;
  });

});
