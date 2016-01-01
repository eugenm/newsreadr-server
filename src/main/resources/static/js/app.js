var newsreadrControllers = angular.module('newsreadrControllers', []);

var newsreadrApp = angular.module('newsreadrApp', ['ngRoute', 'ngSanitize', 'ngUpload', 'angular-loading-bar', 'infinite-scroll', 'newsreadrControllers']);

newsreadrApp.config(function($httpProvider) {

    $httpProvider.interceptors.push(['$q', function($q) {
        return {
            responseError : function(rejection) {
                if (rejection.status == 401) {
                    window.location.replace("login?timeout");
                }
                return $q.reject(rejection);
            }
        };
    }]);

});

newsreadrApp.config(['$sceProvider', function($sceProvider) {
    $sceProvider.enabled(false);
}]);

newsreadrApp.config(['$routeProvider', function($routeProvider) {

    // entries
    $routeProvider.when('/entries', {
        controller : 'EntriesController',
        templateUrl : 'partial/entries'

    // subscriptions
    }).when('/subscriptions', {
        controller : 'SubscriptionsController',
        templateUrl : 'partial/subscriptions'

    // users
    }).when('/users', {
        controller : 'UsersController',
        templateUrl : 'partial/users'

    // profile
    }).when('/profile', {
        controller : 'ProfileController',
        templateUrl : 'partial/profile'

    // fallback
    }).otherwise({
        redirectTo : '/entries'
    });
}

]);