var controllers = angular.module('controllers', []);

var app = angular.module('app', ['ngRoute', 'ngSanitize', 'ngUpload', 'angular-loading-bar', 'infinite-scroll', 'controllers']);

app.config(function($httpProvider) {

    $httpProvider.interceptors.push(['$q', function($q) {
        return {
            responseError: function(rejection) {
                if (rejection.status == 401) {
                    window.location.replace("login?timeout");
                }
                return $q.reject(rejection);
            }
        };
    }]);

});

app.config(['$sceProvider', function($sceProvider) {
    $sceProvider.enabled(false);
}]);

app.config(['$routeProvider', function($routeProvider) {

    // entries
    $routeProvider.when('/entries', {
        controller: 'EntriesController',
        templateUrl: 'entries/Entries'

    // subscriptions
    }).when('/subscriptions', {
        controller: 'SubscriptionsController',
        templateUrl: 'subscriptions/Subscriptions'

    // users
    }).when('/users', {
        controller: 'UsersController',
        templateUrl: 'users/Users'

    // profile
    }).when('/profile', {
        controller: 'ProfileController',
        templateUrl: 'profile/Profile'

    // fallback
    }).otherwise({
        redirectTo: '/entries'
    });

}]);