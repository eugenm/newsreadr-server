<jsp:include page="admin-navbar.jsp" />
<div class="container">
	<button class="btn btn-primary btn-sm" ng-click="addFolder()">
	    <i class="fa fa-plus fa-fw"></i>
	    Add folder
	</button>
	<button class="btn btn-primary btn-sm" ng-click="addSubscription()">
	    <i class="fa fa-plus fa-fw"></i>
	    Add subscription
	</button>
	<button class="btn btn-default btn-sm" data-toggle="modal" data-target="#importDialog">
	    <i class="fa fa-download fa-fw"></i>
	    Import subscriptions
	</button>
	<button class="btn btn-default btn-sm" ng-click="exportSubscriptions()">
	    <i class="fa fa-upload fa-fw"></i>
	    Export subscriptions
	</button>
	<div class="spacer"></div>
	<table id="subscriptions" class="table table-hover">
	    <thead>
	        <tr>
	            <th>Folder / Subscription</th>
	            <th>URL</th>
	            <th colspan="2">Actions</th>
	        </tr>
	    </thead>
	    <tbody ng-repeat="node in nodes">
            <tr>
                <td>
                    <i class="fa fa-folder-open-o fa-fw" ng-show="node.type == 'FOLDER'"></i>
                    <i class="fa fa-rss fa-fw" ng-show="node.type == 'SUBSCRIPTION'"></i>
                    {{node.title}}
                </td>
                <td>{{node.url}}</td>
                <td class="action" ng-show="node.type == 'FOLDER'"><i class="fa fa-pencil fa-fw" ng-click="editFolder(node)"></i></td>
                <td class="action" ng-show="node.type == 'FOLDER'"><i class="fa fa-trash-o fa-fw" ng-click="deleteFolder(node)"></i></td>
                <td class="action" ng-show="node.type == 'SUBSCRIPTION'"><i class="fa fa-pencil fa-fw" ng-click="editSubscription(node)"></i></td>
                <td class="action" ng-show="node.type == 'SUBSCRIPTION'"><i class="fa fa-trash-o fa-fw" ng-click="deleteSubscription(node)"></i></td>
            </tr>
            <tr ng-repeat="subscription in node.subscriptions">
                <td class="indent"><i class="fa fa-rss fa-fw"></i> {{subscription.title}}</td>
                <td>{{subscription.url}}</td>
                <td class="action"><i class="fa fa-pencil fa-fw" ng-click="editSubscription(subscription)"></i></td>
                <td class="action"><i class="fa fa-trash-o fa-fw" ng-click="deleteSubscription(subscription)"></i></td>
            </tr>
	    </tbody>
	</table>
</div>

<div class="modal fade" id="folderDialog">
    <div class="modal-dialog">
        <form ng-submit="saveFolder()">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">{{folderForm.windowTitle}}</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="title">Title</label>
                        <input type="text" ng-model="folderForm.title" id="title" class="form-control input-sm" />
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default btn-sm" data-dismiss="modal"><i class="fa fa-times fa-fw"></i> Cancel</button>
                    <button type="submit" class="btn btn-primary btn-sm"><i class="fa fa-check fa-fw"></i> Save</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="subscriptionDialog">
    <div class="modal-dialog">
        <form ng-submit="saveSubscription()">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">{{subscriptionForm.windowTitle}}</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="url">URL</label>
                        <input type="url" ng-model="subscriptionForm.url" ng-disabled="subscriptionForm.id != null" id="url" class="form-control input-sm" />
                    </div>
                    <div class="form-group">
                        <label for="title">Title (optional)</label>
                        <input type="text" ng-model="subscriptionForm.title" id="title" class="form-control input-sm" />
                    </div>
                    <div class="form-group">
                        <label for="folder">Folder (optional)</label>
                        <select ng-model="subscriptionForm.folderId" ng-options="folder.id as folder.title for folder in folders" id="folder" class="form-control input-sm">
                            <option value=""></option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default btn-sm" data-dismiss="modal"><i class="fa fa-times fa-fw"></i> Cancel</button>
                    <button type="submit" class="btn btn-primary btn-sm"><i class="fa fa-check fa-fw"></i> Save</button>
                </div>
            </div>
        </form>
    </div>
</div>

<div id="importDialog" class="modal fade">
    <div class="modal-dialog">
        <form class="form" action="import" ng-upload="loadSubscriptions()">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Import subscriptions</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="file">File</label>
                        <input id="file" name="opmlFile" type="file" class="input-sm" />
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default btn-sm" data-dismiss="modal"><i class="fa fa-times fa-fw"></i> Cancel</button>
                    <button type="submit" class="btn btn-primary btn-sm" upload-submit><i class="fa fa-download fa-fw"></i> Import</button>
                </div>
            </div>
        </form>
    </div>
</div>