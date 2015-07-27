newsreadrControllers.controller('AccountController', ['$scope', '$http',

    function($scope, $http) {
	
		$scope.userId = null;
		$scope.username = null;
		$scope.newPassword = null;
		$scope.verifyPassword = null;
		
		// load infos
		$http.get('api/infos').success(function(response) {
			$scope.userId = response.userId;
			$scope.username = response.username;
		});
	
		// update accout
		$scope.updateAccount = function() {
			if($scope.newPassword == $scope.verifyPassword) {
				$http.put('api/users/' + $scope.userId, {
					params: {
						username: $scope.username,
						password: $scope.newPassword
					}
				}).success(function(response) {
					window.location.replace('login?newCredentials');
				});
			}
		};
		
	}

]);