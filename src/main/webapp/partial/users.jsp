<jsp:include page="admin-navbar.jsp" />
<div class="container">
	<button class="btn btn-primary btn-sm" ng-click="addUser()">
	    <i class="fa fa-plus fa-fw"></i>
	    Add user
	</button>
	<div class="spacer"></div>
	<table id="users" class="table table-hover">
	    <thead>
	        <tr>
	            <th>Username</th>
	            <th>Role</th>
	            <th colspan="2">Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	        <tr ng-repeat="user in users">
	            <td class="username">{{user.username}}</td>
	            <td>{{user.role}}</td>
	            <td class="action"><i class="fa fa-pencil fa-fw" ng-click="editUser(user)"></i></td>
	            <td class="action"><i class="fa fa-trash-o fa-fw" ng-click="deleteUser(user)"></i></td>
	        </tr>
	    </tbody>
	</table>
</div>

<div id="userDialog" class="modal fade">
    <div class="modal-dialog">
        <form autocomplete="off" ng-submit="saveUser()">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">{{form.windowTitle}}</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="newUsername">Username</label>
                        <input id="newUsername" class="form-control input-sm" type="text" ng-model="form.username"  />
                    </div>
                    <div class="form-group">
                        <label for="newPassword">Password</label>
                        <input id="newPassword" class="form-control input-sm" type="password" ng-model="form.password" />
                    </div>
                    <div class="form-group">
                        <label for="role">Role</label>
                        <select id="role" class="form-control input-sm" ng-model="form.role" ng-options="role as role for role in roles"></select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default btn-sm" data-dismiss="modal"><i class="fa fa-times fa-fw"></i> Cancel</button>
                    <button class="btn btn-primary btn-sm" type="submit"><i class="fa fa-check fa-fw"></i> Save</button>
                </div>
            </div>
        </form>
    </div>
</div>