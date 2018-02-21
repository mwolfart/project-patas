$.ajax({
	url: "/user/get_session",
	type: "POST",
	data: "username",
	contentType: "application/json; charset=UTF-8",
	error: function() {
		window.location.replace("/login.html");	
	},
	async: false
});