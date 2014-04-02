<jsp:include page="admin-navbar.jsp" />
<div class="container">
    <div class="row">
        <div class="col-xs-4">
            <form class="form" ng-submit="updateAccount()" autocomplete="off">
                <div class="form-group">
                    <label for="newUsername">New username (required)</label>
                    <input id="newUsername" class="form-control input-sm" type="text" ng-model="username" />
                </div>
                <div class="form-group">
                    <label for="newPassword">New password</label>
                    <input id="newPassword" class="form-control input-sm" type="password" ng-model="newPassword" />
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm new password</label>
                    <input id="confirmPassword" class="form-control input-sm" type="password" ng-model="verifyPassword" />
                </div>
                <button class="btn btn-primary btn-sm"><i class="fa fa-check fa-fw"></i> Save</button>
            </form>
        </div>
    </div>
</div>
