// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#userRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateUserField( $( "#username" )[0] ) == 0 )
			showAlert($( "#errorUserName" ), "Campo obrigat�rio!");
		else if ( validateUserField( $( "#password" )[0] ) == 0 )
			showAlert($( "#errorPassword" ), "Campo obrigat�rio!");
		else if ( validateUserField( $( "#passwordConf" )[0] ) == 0 )
			showAlert($( "#errorPasswordConf" ), "Campo obrigat�rio!");
		else if ( validateUserField( $( "#username" )[0] ) == -1 )
			showAlert($( "#errorUserName" ), "Nome de usu�rio inv�lido!");
		else if ( validateUserField( $( "#password" )[0] ) == -1 )
			showAlert($( "#errorPassword" ), "Senha inv�lida!");
		else if ( validateUserField( $( "#passwordConf" )[0] ) == -1 )
			showAlert($( "#errorPasswordConf" ), "Senha inv�lida!");
		else if ( $("password")[0] != $("passwordConf")[0] )
			showAlert($( "#errorPasswordConf" ), "Senhas n�o coincidem!");
		else if ( validateStringField( $( "#fullName" )[0] ) == -1 )
			showAlert($( "#errorFullName" ), "Nome cont�m caracteres inv�lidos!");
		else {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "http://localhost:8080/user/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Usu�rio cadastrado com sucesso!");
					window.location.replace("/user/user_view.html?id=" + response);
				},
				error: function(response) {
					alert(response);
				}
			});
		}
	});
});
