newsreadrControllers.controller('AccountController', ['$scope', '$http',

    function($scope, $http) {
	
		$scope.userId = null;
		$scope.username = null;
		$scope.newPassword = null;
		$scope.verifyPassword = null;
		
		// load infos
		$http.get('api', {
			params: {
				method: 'get_infos'
			}
		}).success(function(response) {
			$scope.userId = response.userId;
			$scope.username = response.username;
		});
	
		// update accout
		$scope.updateAccount = function() {
			if($scope.newPassword == $scope.verifyPassword) {
				$http.get('api', {
					params: {
						method: 'update_user',
						userId: $scope.userId,
						username: $scope.username,
						password: $scope.newPassword
					}
				}).success(function(response) {
					if(response.success) {
						window.location.replace('login?newCredentials');
					}
				});
			}
		};
		
	}

]);