<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<a class="navbar-brand" href="#/entries">newsreadr</a>
		<ul class="nav navbar-nav">
			<li><a href="#/subscriptions"><i class="fa fa-rss fa-fw"></i> Subscriptions</a></li>
			<security:authorize ifAnyGranted="ROLE_ADMIN">
                <li><a href="#/users"><i class="fa fa-users fa-fw"></i> Users</a></li>
			</security:authorize>
			<li><a href="#/profile"><i class="fa fa-user fa-fw"></i> Profile</a></li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="#/entries"><i class="fa fa-list-ul fa-fw"></i> Entries</a></li>
			<li><a href="logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
		</ul>
	</div>
</nav>