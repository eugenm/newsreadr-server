newsreadrControllers.controller('EntriesController', ['$scope', '$http', '$anchorScroll',
                                                   
    function ($scope, $http, $anchorScroll) {
	
		// entry filter
		$scope.feed = 'ALL';
		$scope.latestEntryId = null;
		$scope.unreadOnly = true;
		$scope.page = 0;
		
		// subscriptions
		$scope.nodes = [];
		$scope.allEntry = null;
		$scope.bookmarksEntry = null;
		$scope.folders = {};
		
		// entries
		$scope.entries = [];
		
		// infitite scroll blocker
		$scope.loadingEntries = false;
		
		// load subscriptions
		$scope.loadSubscriptions = function() {
			$http.get('api', {
				params: {
					method: 'get_subscriptions'
				}
			}).success(function(response) {
				var nodes = response.nodes;
				$scope.nodes = nodes;
				angular.forEach(nodes, function(node) {
					var nodeType = node.type;
					if(nodeType == 'FOLDER') {
						$scope.folders[node.id] = node;
					} else if(nodeType == 'ALL') {
						$scope.allEntry = node;
					} else if(nodeType == 'BOOKMARKS') {
						$scope.bookmarksEntry = node;
					}
				});
			});
		};
		
		$scope.loadSubscriptions();
		
		// toggle folder
		$scope.toggleFolder = function(folder) {
			folder.expanded = !folder.expanded;
		};
		
		// load all entries
		$scope.loadEntries = function() {
			$scope.loadingEntries = true;
			if($scope.page == 0) {
			    $anchorScroll();
			}
			$http.get('api', {
				params: {
					method: 'get_entries',
					feed: $scope.feed,
					latestEntryId: $scope.latestEntryId,
					unreadOnly: $scope.unreadOnly,
					page: $scope.page
				}
			}).success(function(response) {
				$scope.loadingEntries = false;
			    if($scope.page == 0) {
			    	$scope.latestEntryId = response.latestEntryId;
			    	$scope.entries = response.entries;
			    } else {
			    	$scope.entries = $scope.entries.concat(response.entries);
			    }
			});
		};
		
		$scope.loadEntries();
		
		// change feed
		$scope.changeFeed = function(feed) {
			$scope.feed = feed;
			$scope.page = 0;
			$scope.loadEntries();
		};
		
		// infinite scroll
		$scope.nextPage = function() {
			$scope.loadEntries($scope.page++);
		};
		
		// refresh
		$scope.refresh = function() {
			$scope.page = 0;
			$scope.loadSubscriptions();
			$scope.loadEntries();
		};
		
		// mark entries as read
		$scope.markEntriesAsRead = function() {
			$http.get('api', {
				params: {
					method: 'mark_entries_as_read',
					feed: $scope.feed,
					latestEntryId: $scope.latestEntryId
				}
			}).success(function(response) {
				$scope.refresh();
			});
		};
		
		// display read entries
		$scope.toggleUnread = function() {
			$scope.unreadOnly = !$scope.unreadOnly;
			$scope.page = 0;
			$scope.loadEntries();
		};
		
		// toggle bookmark
		$scope.toggleBookmark = function(entry) {
			var method = entry.bookmarked ? 'remove_bookmark' : 'add_bookmark';
			$http.get('api', {
				params: {
					method: method,
					userEntryId: entry.id
				}
			}).success(function(response) {
				entry.bookmarked = !entry.bookmarked;
				if(!entry.read) {
					if(entry.bookmarked) {
						$scope.bookmarksEntry.unread++;
					} else {
						$scope.bookmarksEntry.unread--;
					}
				}
			});
		};
		
		// load entry
		$scope.toggleEntry = function(entry) {
			entry.visible = !entry.visible; 
			if(!entry.content) {
				$http.get('api', {
					params: {
						method: 'get_entry',
						userEntryId: entry.id
					}
				}).success(function(response) {
					if(!entry.read) {
						$scope.allEntry.unread--;
						if(entry.bookmarked) {
							$scope.bookmarksEntry.unread--;
						}
					}
					entry.read = true;
					entry.content = response.content;
				});
			}
		};

	}

]);