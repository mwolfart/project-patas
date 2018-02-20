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
			// Load the data using username (specified in URL)
			var username = getUrlParameter("username");
			
			$.ajax({
				url: "/user/delete",
				type: "POST",
				data: username,
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

		if ( validateUserField( $( "#password" )[0] ) == -1 )
			showAlert($( "#errorPassword" ), "Senha inválida.");
		else if ( validateUserField( $( "#passwordConf" )[0] ) == -1 )
			showAlert($( "#errorPasswordConf" ), "Senha inválida.");
		else if ( $("#password").val() != $("#passwordConf").val() )
			showAlert($( "#errorPasswordConf" ), "Senhas não coincidem.");
		else if ( validateStringField( $( "#fullName" )[0] ) == -1 )
			showAlert($( "#errorFullName" ), "Nome contém caracteres inválidos.");
		else {
			// Convert form to json and fix its format
			var jsonData = formToJson(this);	
			
			// Store the username so we know which user to edit
			jsonData["username"] = $("#username").val();
			jsonData = JSON.stringify(jsonData);
			
			// Post the data
			$.ajax({
				url: "/user/update",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				error: function(response) {
					alert(response.responseText);
				},
				success: function() {
					// Reset the form to previous state	
					$(" #password ").val("");
					$(" #passwordConf ").val("");
					
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
	
	// Load the data using username (specified in URL)
	var username = getUrlParameter("username");
	
	$.ajax({
		url: "/user/view",
		type: "POST",
		data: username,
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