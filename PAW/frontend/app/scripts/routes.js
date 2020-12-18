'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/explore': {
              templateUrl: '/views/explore.html',
              controller: 'ExploreCtrl'
            },
            '/login': {
                templateUrl: '/views/session/login.html',
                controller: 'LoginCtrl'
            },
            '/login/:id/password': {
              templateUrl: '/views/session/change_password.html',
              controller: 'LoginCtrl'
            },
            '/register': {
              templateUrl: '/views/session/register.html',
              controller: 'RegisterCtrl'
            },
            '/register/forgot_password': {
              templateUrl: '/views/session/recover_password.html',
              controller: 'RecoverPasswordCtrl'
            },
            '/posts/:id': {
                templateUrl: '/views/posts/post.html',
                controller: 'PostCtrl'
            },
            '/techs': {
              templateUrl: '/views/techs/techs.html',
              controller: 'TechsCtrl'
            },
            '/tech/:id': {
              templateUrl: '/views/techs/tech.html',
              controller: 'TechCtrl'
            },
            '/posts': {
              templateUrl: '/views/posts/posts.html',
              controller: 'PostsCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
