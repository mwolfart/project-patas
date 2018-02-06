$.ajax({
	url: "/user/get_session",
	type: "POST",
	data: "username",
	contentType: "application/json; charset=UTF-8",
	success: function(username) {
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
	},
	error: function() {
		window.location.replace("/login.html");	
	}
});