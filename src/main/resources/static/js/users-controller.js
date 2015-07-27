newsreadrControllers.controller('UsersController', ['$scope', '$http',

    function($scope, $http) {

		$scope.infos = null;
		$scope.users = [];
		$scope.roles = [];
		
		// load infos
		$http.get('api/infos').success(function(infos) {
			$scope.infos = infos;
		});
		
		// load users
		$scope.loadUsers = function() {
			$http.get('api/users').success(function(users) {
				$scope.users = users;
			});
		};
		
		$scope.loadUsers();
		
		// load roles
		$scope.loadRoles = function() {
			$http.get('api/roles').success(function(roles) {
				$scope.roles = roles;
			});
		};
		
		$scope.loadRoles();
		
		// add user
		$scope.addUser = function() {
			$scope.form = {
				method: 'POST',
				url: 'api/users',
				windowTitle: 'Add user',
				role: 'USER'
			};
			$('#userDialog').modal('show');
		};
		
		// edit user
		$scope.editUser = function(user) {
			var copy = angular.copy(user);
			copy.method = 'PUT';
			copy.url = 'api/users/' + copy.userId;
			copy.windowTitle = 'Edit user';
			$scope.form = copy;
			$('#userDialog').modal('show');
		};
		
		// save user
		$scope.saveUser = function() {
			
			var form = $scope.form;
			
			$http({
				method: form.method,
				url: form.url,
				params: {
					username: form.username,
					password: form.password,
					role: form.role
				}
			}).success(function(response) {
				$scope.loadUsers();
				$('#userDialog').modal('hide');
			});
			
			$scope.form = {};
			
		};
		
		$scope.deleteUser = function(user) {
			$http.delete('api/users/' + user.userId).success(function() {
				$scope.loadUsers();
			});
		};
		

	}

]);