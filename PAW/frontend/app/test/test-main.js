var allTestFiles = []
var TEST_REGEXP = /(spec|test)\.js$/i

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
  if (TEST_REGEXP.test(file)) {
    // Normalize paths to RequireJS module names.
    // If you require sub-dependencies of test files to be loaded as-is (requiring file extension)
    // then do not normalize the paths
    var normalizedTestModule = file.replace(/^\/base\/|\.js$/g, '')
    allTestFiles.push(normalizedTestModule)
  }
})

require.config({
  baseUrl: '/base',

  deps: allTestFiles,
  paths: {
    affix: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/affix',
    alert: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/alert',
    angular: '../../bower_components/angular/angular',
    'angular-route': '../../bower_components/angular-route/angular-route',
    'angular-translate': '../../bower_components/angular-translate/angular-translate',
    button: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/button',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    carousel: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/carousel',
    collapse: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/collapse',
    dropdown: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/dropdown',
    'es5-shim': '../../bower_components/es5-shim/es5-shim',
    jquery: '../../bower_components/jquery/dist/jquery',
    json3: '../../bower_components/json3/lib/json3',
    lodash: '../../bower_components/lodash/lodash',
    moment: '../../bower_components/moment/moment',
    popover: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/popover',
    requirejs: '../../bower_components/requirejs/require',
    restangular: '../../bower_components/restangular/dist/restangular',
    scrollspy: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/scrollspy',
    tab: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tab',
    tooltip: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tooltip',
    transition: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/transition',
    karma: '../../bower_components/karma/lib/karma',
    bower: '../../bower_components/bower/atom-full-compiled',
    install: '../../bower_components/install/detect-zoom',
    ngstorage: '../../bower_components/ngstorage/ngStorage',
    'ng-file-upload': '../../bower_components/ng-file-upload/ng-file-upload',
    'ng-file-upload-shim': '../../bower_components/ng-file-upload-shim/ng-file-upload-shim',
    modal: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/modal',
    angularMocks: '../../node_modules/angular-mocks/angular-mocks',
  },
  shim: {
    angular: {
      deps: [
        'jquery'
      ],
      exports: 'angular'
    },
    'angular-route': {
      deps: [
        'angular'
      ]
    },
    bootstrap: {
      deps: [
        'jquery'
      ]
    },
    tooltip: {
      deps: [
        'jquery'
      ]
    },
    'angular-translate': {
      deps: [
        'angular'
      ]
    },
    'ng-file-upload': {
      deps: [
        'angular'
      ]
    }
  },
  packages: [

  ]
});
