$(document).ready(function() {	
	// Protect fields
	$( "#password" ).keydown(protectUserField);
	$( "#newPassword" ).keydown(protectUserField);
	$( "#passwordConf" ).keydown(protectUserField);

	//Validate fields
	$( "#password" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorPassword" ));
	})
	
	$( "#newPassword" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorNewPassword" ));
	})
	
	$( "#passwordConf" ).focusout(function() {
		if (validateUserField(this) > 0)
			hideAlert($( "#errorPasswordConf" ));
	})
	
	$( "#passwordChangeForm" ).submit(function(event) {
		event.preventDefault();

		if ( validateUserField( $( "#newPassword" )[0] ) <= 0 )
			showAlert($( "#errorNewPassword" ), "Senha inválida.");
		else if ( validateUserField( $( "#passwordConf" )[0] ) <= 0 )
			showAlert($( "#errorPasswordConf" ), "Senha inválida.");
		else if ( $("#newPassword").val() != $("#passwordConf").val() )
			showAlert($( "#errorPasswordConf" ), "Senhas não coincidem.");
		else {	
			var jsonData = {};
			
			$.ajax({
				url: "/user/get_session",
				type: "POST",
				data: "username",
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					// Store the username so we know which user to edit
					jsonData["username"] = response;
					jsonData["password"] = $("#password").val();
					jsonData["new_password"] = $("#newPassword").val();
					
					jsonData = JSON.stringify(jsonData);
					
					$.ajax({
						url: "/user/set_password",
						type: "POST",
						data: jsonData,
						contentType: "application/json; charset=UTF-8",
						success: function(response) {
							alert("Senha alterada com sucesso!");
							window.location.replace("/user/my_account.html");					
						},
						error: function(response) {
							alert(response.responseText);
							window.location.replace("/user/change_password.html");
						}
					});
				},
				error: function(response) {
					window.location.replace("/login.html");
				}	
			});
		}
	});
	
	$( "#cancelBtn" ).click(function() {
		window.location.replace("/user/my_account.html");
	});
});