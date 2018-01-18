var username = $.session.get("username");

if (username == undefined)
	window.location.replace("/login.html"); 