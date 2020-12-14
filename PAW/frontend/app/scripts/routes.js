'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/register': {
                templateUrl: '/views/session/register.html',
                controller: 'RegisterCtrl'
            },
            '/register/forgot_password': {
              templateUrl: '/views/session/recover_password.html',
              controller: 'RecoverPasswordCtrl'
            },
            '/login': {
                templateUrl: '/views/session/login.html',
                controller: 'LoginCtrl'
            },
            '/posts/:id': {
                templateUrl: '/views/posts/post.html',
                controller: 'PostCtrl'
            }

            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
