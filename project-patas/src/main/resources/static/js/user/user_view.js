// jsonToForm := Object -> Void
// sends all the obtained values to the form.
function jsonToForm(json) {
	$(" #username ").val(json.username);
	$(" #password ").val("");
	$(" #passwordConf ").val("");
	$(" #userType ").val(json.userType);
	$(" #fullName ").val(json.fullName);
}

$(document).ready(function() {
	// Edit button onClick handler
	$(" #editBtn ").click(function() {		
		$(" #username ").prop('disabled', false);
		$(" #password ").prop('disabled', false);
		$(" #passwordConf ").prop('disabled', false);
		$(" #userType ").prop('disabled', false);
		$(" #fullName ").prop('disabled', false);
		
		$(" #editBtn ").prop('disabled', true);
		$(" #saveBtn ").prop('disabled', false);
		$(" #deleteBtn ").prop('disabled', true);
	});
	
	// Delete button onClick handler
	$(" #deleteBtn ").click(function(event) {
		event.preventDefault();
		
		if (confirm("Tem certeza que deseja excluir este usuário?")) {
			// TODO: TESTAR SE USUÁRIO ESTÁ LOGADO
			var POST = getUrlParameter('id');
			
			$.ajax({
				url: "http://localhost:8080/user/delete",
				type: "POST",
				data: user_id,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Usuário removido com sucesso!");
					window.location.replace("/user/user_search.html");
				},
				error: function(response) {
					alert(response.responseText);
				}
			});
		}
	});
	
	$( "#userEditForm" ).submit(function(event) {
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
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the id so we know which user to edit
			jsonData["id"] = parseInt(getUrlParameter('id'));
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "http://localhost:8080/user/register",
				type: "POST",
				dataType: "json",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response);
				},
				success: function() {
					/* UNIQUE PART */
					// Reset the form to previous state	
					$(" #username ").prop('disabled', true);
					$(" #password ").prop('disabled', true);
					$(" #passwordConf ").prop('disabled', true);
					$(" #userType ").prop('disabled', true);
					$(" #fullName ").prop('disabled', true);
					
					$(" #editBtn ").prop('disabled', false);
					$(" #saveBtn ").prop('disabled', true);
					$(" #deleteBtn ").prop('disabled', false);
				}
			});
		}
	});
	
	// Load the data using dog id (specified in URL)
	var user_id = getUrlParameter('id');
	
	// if the id isn't specified, set it to 1 by default
	if (user_id == 0)
		user_id = "1";
	
	$.ajax({
		url: "http://localhost:8080/user/view",
		type: "POST",
		data: user_id,
		contentType: "application/json; charset=UTF-8",
		success: function(response) {
			jsonToForm(response);
		},
		error: function(response) {
			alert(response.responseText);
			$(" #editBtn ").prop("disabled", true);
			$(" #deleteBtn ").prop("disabled", true);
		}
	});
});