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
            '/register/forgot_password/:token': {
              templateUrl: '/views/session/change_password.html',
              controller: 'ChangePasswordCtrl'
            },
            '/register': {
              templateUrl: '/views/session/register.html',
              controller: 'RegisterCtrl'
            },
            '/register/forgot_password': {
              templateUrl: '/views/session/recover_password.html',
              controller: 'RecoverPasswordCtrl'
            },
            '/posts/add': {
              templateUrl: "/views/posts/add_post.html",
              controller: 'PostsCtrl'
            },
            '/posts/:id/edit': {
              templateUrl: '/views/posts/edit_post.html',
              controller: 'PostCtrl'
            },
            '/posts/:id': {
                templateUrl: '/views/posts/post.html',
                controller: 'PostCtrl'
            },
            '/techs': {
              templateUrl: '/views/techs/techs.html',
              controller: 'TechsCtrl'
            },
            '/techs/category/:category': {
              templateUrl: '/views/techs/techs_cats.html',
              controller: 'TechsCatCtrl'
            },
            '/techs/:id': {
              templateUrl: '/views/techs/tech.html',
              controller: 'TechCtrl'
            },
            '/users/:id': {
              templateUrl: '/views/session/userProfile.html',
              controller: 'userCtrl'
            },
            '/posts': {
              templateUrl: '/views/posts/posts.html',
              controller: 'PostsCtrl'
            },
            '/mod': {
              templateUrl: '/views/session/mod_page.html',
              controller: 'ModCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
