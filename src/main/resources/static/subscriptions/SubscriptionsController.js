controllers.controller('SubscriptionsController', ['$scope', '$http',

    function($scope, $http) {
		
		$scope.infos = null;
		$scope.subscriptions = [];
		
		// load infos
		$http.get('api/infos').success(function(infos) {
			$scope.infos = infos;
		});
	
		// load subscriptions
		$scope.loadSubscriptions = function() {
			$http.get('api/subscriptions').success(function(nodes) {
				$scope.nodes = [];
				angular.forEach(nodes, function(node) {
					var nodeType = node.type;
					if(nodeType == 'SUBSCRIPTION') {
						$scope.nodes.push(node);
					}
				});
			});
		};
		
		$scope.loadSubscriptions();
		
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