<!doctype html>
<html lang="en" ng-app="newsreadrApp">
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>newsreadr</title>
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600" />
		<link rel="stylesheet" href="lib/font-awesome/css/font-awesome.min.css" />
		<link rel="stylesheet" href="lib/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="lib/bootstrap/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="css/non-responsive.css" />
		<link rel="stylesheet" href="css/app.css" />
		<!--[if lt IE 9]>
			<script src="lib/html5shiv/html5shiv.js"></script>
			<script src="lib/respond/respond.min.js"></script>
		<![endif]-->
	</head>
	<body>
		<div ng-view></div>
		<script src="lib/jquery/jquery-1.11.0.min.js"></script>
		<script src="lib/bootstrap/js/bootstrap.min.js"></script>
		<script src="lib/angular/angular.min.js"></script>
		<script src="lib/angular/angular-route.min.js"></script>
		<script src="lib/angular/ng-infinite-scroll.min.js"></script>
		<script src="lib/angular/angular-sanitize.min.js"></script>
		<script src="lib/angular/ng-upload.min.js"></script>
		<script src="js/app.js"></script>
		<script src="js/account-controller.js"></script>
		<script src="js/entries-controller.js"></script>
		<script src="js/subscriptions-controller.js"></script>
		<script src="js/users-controller.js"></script>
	</body>
</html>