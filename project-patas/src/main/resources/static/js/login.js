$(document).ready(function() {
	// Protect fields
	$( "#password" ).keydown(protectUserField);
	$( "#username" ).keydown(protectUserField);
	
	// TODO: CHECK VALIDATION? IS IT NEEDED?
	
	$.ajax({
		url: "/user/get_session",
		type: "POST",
		data: "username",
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			window.location.replace("/index.html");
		}
	});
	
	$("#loginForm").submit(function(event) {
		event.preventDefault();
		
		var username = $("#username").val();
		var password = $("#password").val();
		
		var userData = {};
		if(username) userData["username"] = username;
		if(password) userData["password"] = password;
		userData = JSON.stringify(userData);
		
		$.ajax({
			url: "/user/check_password",
			type: "POST",
			data: userData,
			contentType: "application/json; charset=UTF-8",
			success: function(response) {
				if (response == true) {
					//$.session.set("username", username);
					var session = {};
					session["key"] = "username";
					session["value"] = username;
					// Duration can be altered
					// current duration = 1h
					session["duration"] = 60 * 60 * 1000;					
					session = JSON.stringify(session);
					
					$.ajax({
						url: "/user/set_session",
						type: "POST",
						data: session,
						contentType: "application/json; charset=UTF-8",
						success: function() {
							window.location.replace("/index.html");
						},
						error: function(response) {
							console.log(response);
						}						
					});
				}
				else {
					alert("Senha incorreta. Cuidado maiúsculas e minúsculas.");
					window.location.replace("/login.html");					
				}
			},
			error: function(response) {
				alert(response.responseText);
				window.location.replace("/login.html");
			}
		});
	});
});