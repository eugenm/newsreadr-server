<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>newsreadr</title>
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600" />
		<link rel="stylesheet" href="lib/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="lib/bootstrap/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="css/login.css" />
		<!--[if lt IE 9]>
		    <script src="lib/html5shiv/html5shiv.js"></script>
		    <script src="lib/respond/respond.min.js"></script>
		<![endif]-->
	</head>
	<body>
		<div class="container">
            <form action="login" method="post" class="form-horizontal">
				<input type="text" name="username" class="form-control input-sm" placeholder="Username" />
				<input type="password" name="password" class="form-control input-sm" placeholder="Password" />
				<button type="submit" class="btn btn-primary btn-sm">Login</button>
				<button type="reset" class="btn btn-default btn-sm">Reset</button>
				<c:if test="${param.logout != null}"><div class="alert alert-success">Successfully logged out</div></c:if>
				<c:if test="${param.timeout != null}"><div class="alert alert-danger">You have automatically been logged out because of inactivity</div></c:if>
				<c:if test="${param.error != null}"><div class="alert alert-danger">Access denied</div></c:if>
				<c:if test="${param.newCredentials != null}"><div class="alert alert-success">Credentials successfully changed. Please renew your authentication</div></c:if>
			</form>
		</div>
		<script src="lib/jquery/jquery-1.11.0.min.js"></script>
		<script src="lib/bootstrap/js/bootstrap.min.js"></script>
	</body>
</html>