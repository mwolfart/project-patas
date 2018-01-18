$(document).ready(function() {
	// Protect fields
	$( "#password" ).keydown(protectUserField);
	$( "#username" ).keydown(protectUserField);
	
	// TODO: CHECK VALIDATION? IS IT NEEDED?
	
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
					$.session.set("username", username);
					window.location.replace("/index.html");	
				}
				else {
					alert("Senha incorreta. Cuidado maiúsculas e minúsculas.");
					window.location.replace("/login.html");					
				}
			},
			error: function(response) {
				alert(response.responseText);
				console.log(response);
				window.location.replace("/login.html");
			}
		});
	});
});