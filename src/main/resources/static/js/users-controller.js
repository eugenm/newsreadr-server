newsreadrControllers.controller('UsersController', ['$scope', '$http',

    function($scope, $http) {

		$scope.infos = null;
		$scope.users = [];
		$scope.roles = [];
		
		// load infos
		$http.get('api', {
			params: {
				method: 'get_infos'
			}
		}).success(function(data) {
			$scope.infos = data;
		});
		
		// load users
		$scope.loadUsers = function() {
			$http.get('api', {
				params: {
					method: 'get_users'
				}
			}).success(function(response) {
				$scope.users = response.users;
			});
		};
		
		$scope.loadUsers();
		
		// load roles
		$scope.loadRoles = function() {
			$http.get('api', {
				params: {
					method: 'get_roles'
				}
			}).success(function(response) {
				$scope.roles = response.roles;
			});
		};
		
		$scope.loadRoles();
		
		// add user
		$scope.addUser = function() {
			$scope.form = {
				windowTitle: 'Add user',
				apiMethod: 'add_user',
				role: 'USER'
			};
			$('#userDialog').modal('show');
		};
		
		// edit user
		$scope.editUser = function(user) {
			var copy = angular.copy(user);
			copy.windowTitle = 'Edit user';
			copy.apiMethod = 'update_user';
			$scope.form = copy;
			$('#userDialog').modal('show');
		};
		
		// save user
		$scope.saveUser = function() {
			
			var form = $scope.form;
			
			$http.get('api', {
				params: {
					method: form.apiMethod,
					userId: form.userId,
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
			$http.get('api', {
				params: {
					method: 'remove_user',
					userId: user.userId
				}
			}).success(function(response) {
				$scope.loadUsers();
			});
		};
		

	}

]);