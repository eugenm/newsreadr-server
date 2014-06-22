newsreadrControllers.controller('SubscriptionsController', ['$scope', '$http',

    function($scope, $http) {
		
		$scope.infos = null;
		$scope.folders = [];
		$scope.subscriptions = [];
		
		// load infos
		$http.get('api', {
			params: {
				method: 'get_infos'
			}
		}).success(function(response) {
			$scope.infos = response.infos;
		});
	
		// load subscriptions
		$scope.loadSubscriptions = function() {
			$http.get('api', {
				params: {
					method: 'get_subscriptions'
				}
			}).success(function(response) {
				var nodes = response.nodes;
				$scope.nodes = [];
				$scope.folders = [];
				angular.forEach(nodes, function(node) {
					var nodeType = node.type;
					if(nodeType == 'FOLDER' || nodeType == 'SUBSCRIPTION') {
						$scope.nodes.push(node);
					}
					if(nodeType == 'FOLDER') {
						$scope.folders.push(node);
					}
				});
			});
		};
		
		$scope.loadSubscriptions();
		
		// add folder
		$scope.addFolder = function() {
			$scope.folderForm = {
				windowTitle: 'Add folder',
				apiMethod: 'add_folder'
			};
			$('#folderDialog').modal('show');
		};
		
		// edit folder
		$scope.editFolder = function(folder) {
			var copy = angular.copy(folder);
			copy.windowTitle = 'Edit folder';
			copy.apiMethod = 'update_folder';
			$scope.folderForm = copy;
			$('#folderDialog').modal('show');
		};
		
		// save folder
		$scope.saveFolder = function() {
			
			var form = $scope.folderForm;
			
			$http.get('api', {
				params: {
					method: form.apiMethod,
					folderId: form.id,
					title: form.title
				}
			}).success(function(response) {
				$scope.loadSubscriptions();
				$('#folderDialog').modal('hide');
			});
			
			$scope.folderForm = {};
			
		};
		
		// delete folder
		$scope.deleteFolder = function(folder) {
			$http.get('api', {
				params: {
					method: 'remove_folder',
					folderId: folder.id
				}
			}).success(function(response) {
				$scope.loadSubscriptions();
			});
		};
		
		// add subscription
		$scope.addSubscription = function() {
			$scope.subscriptionForm = {
				windowTitle: 'Add subscription',
				apiMethod: 'add_subscription'
			};
			$('#subscriptionDialog').modal('show');
		};
		
		// edit subscription
		$scope.editSubscription = function(subscription) {
			var copy = angular.copy(subscription);
			copy.windowTitle = 'Edit subscription';
			copy.apiMethod = 'update_subscription';
			$scope.subscriptionForm = copy;
			$('#subscriptionDialog').modal('show');
		};
		
		// save subscription
		$scope.saveSubscription = function() {
			
			var form = $scope.subscriptionForm;
			
			$http.get('api', {
				params: {
					method: form.apiMethod,
					subscriptionId: form.id,
					folderId: form.folderId,
					url: form.url,
					title: form.title
				}
			}).success(function(response) {
				$scope.loadSubscriptions();
				$('#subscriptionDialog').modal('hide');
			});
			
			$scope.subscriptionForm = {};
		};
		
		// delete subscription
		$scope.deleteSubscription = function(subscription) {
			$http.get('api', {
				params: {
					method: 'remove_subscription',
					subscriptionId: subscription.id
				}
			}).success(function(response) {
				$scope.loadSubscriptions();
			});
		}
		
		// imported subscriptions
		$scope.importedSubscriptions = function() {
			$scope.loadSubscriptions();
			$('#importDialog').modal('hide');
		};
		
		// export subscriptions
		$scope.exportSubscriptions = function() {
			window.location.href = "export";
		};
		
	}

]);