<nav id="top" class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="#/entries">newsreadr</a>
        <ul class="nav navbar-nav">
            <li><a class="pointer" ng-click="refresh()"><i class="fa fa-refresh fa-fw"></i> Refresh</a></li>
            <li><a class="pointer" ng-click="markEntriesAsRead()"><i class="fa fa-check fa-fw"></i> Mark all as read</a></li>
            <li>
                <a class="pointer" ng-click="toggleUnread()">
                    <i class="fa fa-square-o fa-fw" ng-show="unreadOnly"></i>
                    <i class="fa fa-check-square fa-fw" ng-hide="unreadOnly"></i>
                    Display read entries
                </a>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#/subscriptions"><i class="fa fa-cogs fa-fw"></i> Settings</a></li>
            <li><a href="logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
        </ul>
    </div>
</nav>
<div class="container">
	<div class="row">
		<ul id="sidebar" class="col-xs-2">
		    <li><a id="subscribe" class="btn btn-primary btn-sm" href="#/subscriptions">Subscribe</a></li>
		    <li ng-repeat="node in nodes">
                <i class="fa fa-list-ul fa-fw" ng-show="node.type == 'ALL'"></i>
                <i class="fa fa-bookmark fa-fw" ng-show="node.type == 'BOOKMARKS'"></i>
                <i class="fa fa-angle-right fa-fw pointer" ng-show="node.type == 'FOLDER' && !node.expanded" ng-click="toggleFolder(node)"></i>
                <i class="fa fa-angle-down fa-fw pointer" ng-show="node.type == 'FOLDER' && node.expanded" ng-click="toggleFolder(node)"></i>
                <span class="unread" ng-hide="node.unread == 0">{{node.unread}}</span>
                <span class="pointer" ng-click="changeFeed(node.feed)">{{node.title}}</span>
                <ul ng-show="node.type == 'FOLDER' && node.expanded && node.subscriptions.length != 0">
                    <li ng-repeat="subscription in node.subscriptions">
                        <span class="unread" ng-hide="subscription.unread == 0">{{subscription.unread}}</span>
                        <span class="pointer" ng-click="changeFeed(subscription.feed)">{{subscription.title}}</span>
                    </li>
                </ul>
		    </li>
		</ul>
		<div class="col-xs-10">
            <p ng-show="entries.length == 0"><strong>No unread entries</strong></p>
		    <table id="entries" class="table" ng-hide="entries.length == 0" infinite-scroll="nextPage()" infinite-scroll-disabled="loadingEntries">
		        <tr class="read-{{entry.read}}" ng-repeat-start="entry in entries">
		            <td class="action">
		                <i class="fa fa-bookmark-o fa-fw" ng-hide="entry.bookmarked" ng-click="toggleBookmark(entry)"></i>
		                <i class="fa fa-bookmark fa-fw" ng-show="entry.bookmarked" ng-click="toggleBookmark(entry)"></i>
		            </td>
		            <td class="pointer subscription" ng-click="toggleEntry(entry)">{{entry.subscription}}</td>
		            <td class="pointer title" ng-click="toggleEntry(entry)">{{entry.title}}</td>
		            <td class="pointer date" ng-click="toggleEntry(entry)">{{entry.publishDate | date: 'dd.MM.yyyy / HH:mm'}}</td>
		            <td class="action"><a href="{{entry.url}}" target="_blank"><i class="fa fa-external-link-square fa-fw"></i></a></td>
		        </tr>
		        <tr class="content-row" ng-show="entry.visible" ng-repeat-end>
		            <td colspan="5">
		                <div class="content" ng-bind-html="entry.content"></div>
		            </td>
		        </tr>
		    </table>        
		</div>
	</div>
</div>