// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#userRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateUserField( $( "#username" )[0] ) == 0 )
			showAlert($( "#errorUserName" ), "Nome de usuário deve ser informado.");
		else if ( validateUserField( $( "#password" )[0] ) == 0 )
			showAlert($( "#errorPassword" ), "Senha deve ser informada.");
		else if ( validateUserField( $( "#passwordConf" )[0] ) == 0 )
			showAlert($( "#errorPasswordConf" ), "Senha deve ser informada novamente.");
		else if ( validateUserField( $( "#username" )[0] ) == -1 )
			showAlert($( "#errorUserName" ), "Nome de usuário inválido.");
		else if ( validateUserField( $( "#password" )[0] ) == -1 )
			showAlert($( "#errorPassword" ), "Senha inválida.");
		else if ( validateUserField( $( "#passwordConf" )[0] ) == -1 )
			showAlert($( "#errorPasswordConf" ), "Senha inválida.");
		else if ( $("password")[0] != $("passwordConf")[0] )
			showAlert($( "#errorPasswordConf" ), "Senhas não coincidem.");
		else if ( validateStringField( $( "#fullName" )[0] ) == -1 )
			showAlert($( "#errorFullName" ), "Nome contém caracteres inválidos.");
		else {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/user/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Usuário cadastrado com sucesso!");
					window.location.replace("/user/user_view.html?id=" + response);
				},
				error: function(response) {
					alert(response);
				}
			});
		}
	});
});
