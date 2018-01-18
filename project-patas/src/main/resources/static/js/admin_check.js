var username = $.session.get("username");

if (username == undefined)
	window.location.replace("/login.html"); 
else {
	$.ajax({
		url: "/user/check_admin",
		type: "POST",
		data: username,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			if (response == false)
				window.location.replace("/warnings/access_denied.html");	
		}
	});
}