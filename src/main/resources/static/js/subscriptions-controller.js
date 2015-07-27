newsreadrControllers.controller('SubscriptionsController', ['$scope', '$http',

    function($scope, $http) {
		
		$scope.infos = null;
		$scope.folders = [];
		$scope.subscriptions = [];
		
		// load infos
		$http.get('api/infos').success(function(infos) {
			$scope.infos = infos;
		});
	
		// load subscriptions
		$scope.loadSubscriptions = function() {
			$http.get('api/subscriptions').success(function(nodes) {
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
				method: 'POST',
				url: 'api/folders'
			};
			$('#folderDialog').modal('show');
		};
		
		// edit folder
		$scope.editFolder = function(folder) {
			var copy = angular.copy(folder);
			copy.windowTitle = 'Edit folder';
			copy.method = 'PUT';
			copy.url = 'api/folders/' + folder.id;
			$scope.folderForm = copy;
			$('#folderDialog').modal('show');
		};
		
		// save folder
		$scope.saveFolder = function() {
			
			var form = $scope.folderForm;
			
			$http({
				method: form.method,
				url: form.url,
				params: {
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
			$http.delete('api/folders/' + folder.id).success(function(response) {
				$scope.loadSubscriptions();
			});
		};
		
		// add subscription
		$scope.addSubscription = function() {
			$scope.subscriptionForm = {
				windowTitle: 'Add subscription',
				method: 'POST',
				apiUrl: 'api/subscriptions'
			};
			$('#subscriptionDialog').modal('show');
		};
		
		// edit subscription
		$scope.editSubscription = function(subscription) {
			var copy = angular.copy(subscription);
			copy.windowTitle = 'Edit subscription';
			copy.method = 'PUT';
			copy.apiUrl = 'api/subscriptions/' + subscription.id;
			$scope.subscriptionForm = copy;
			$('#subscriptionDialog').modal('show');
		};
		
		// save subscription
		$scope.saveSubscription = function() {
			
			var form = $scope.subscriptionForm;
			
			$http({
				method: form.method,
				url: form.apiUrl,
				params: {
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
			$http.delete('api/subscriptions/' + subscription.id).success(function(response) {
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
			window.location.href = "api/subscriptions/export";
		};
		
	}

]);