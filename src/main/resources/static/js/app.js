var newsreadrControllers = angular.module('newsreadrControllers', []);

var newsreadrApp = angular.module('newsreadrApp', [
	'infinite-scroll',
    'newsreadrControllers',
    'ngUpload',
    'ngRoute',
    'ngSanitize'
]);

newsreadrApp.config(function($httpProvider) {
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

newsreadrApp.config(['$sceProvider', function($sceProvider) {
	$sceProvider.enabled(false);
}]);

newsreadrApp.config(['$routeProvider',
                     
    function($routeProvider) {
	
		$routeProvider.
			when('/entries', {
				controller: 'EntriesController',
				templateUrl: 'partial/entries'
			}).
			when('/subscriptions', {
				controller: 'SubscriptionsController',
				templateUrl: 'partial/subscriptions'
			}).
			when('/users', {
				controller: 'UsersController',
				templateUrl: 'partial/users'
			}).
			when('/profile', {
				controller: 'AccountController',
				templateUrl: 'partial/account'
			}).
			otherwise({
				redirectTo: '/entries'
			});
	}

]);