// Document load script
$(document).ready(function() {
	// Set the submit configuration for the form
	$( "#vacRegisterForm" ).submit(function(event) {
		event.preventDefault();
		
		if ( validateStringField( $( "#dogName" )[0] ) == 0 )
			showAlert($( "#errorDogName" ), "Nome do cachorro deve ser informado.");
		else if ( validateStringField( $( "#vacName" )[0] ) == 0 )
			showAlert($( "#errorVacName" ), "Nome da vacina deve ser informado.");
		else if ( validateDateField( $( "#appDate" )[0] ) == 0 )
			showAlert($( "#errorAppDate" ), "Data deve ser informada.");
		else if ( validateStringField( $( "#vacName" )[0] ) == -1 )
			showAlert($( "#errorVacName" ), "Nome inválido.");
		else if ( validateDateField( $( "#appDate" )[0] ) == -1 )
			showAlert($( "#errorAppDate" ), "Data inválida.");
		else if ( validateDateField( $( "#nextAppDate" )[0] ) == -1 )
			showAlert($( "#errorNextAppDate" ), "Data inválida.");
		else if ( validateStringField( $( "#obs" )[0] ) == -1 )
			showAlert($( "#errorObs" ), "Observações contém caracteres inválidos.");
		else {
			// Convert form to json and fix
			var jsonData = formToJson(this);
			jsonData = JSON.stringify(jsonData);
	
			// Post the data
			$.ajax({
				url: "/vaccination/register",
				type: "POST",
				data: jsonData,
				contentType: "application/json; charset=UTF-8",
				success: function(response) {
					alert("Registro cadastrado com sucesso!");
					window.location.replace("/vaccination/vaccination_view.html?id=" + response);
				},
				error: function(response) {
					alert(response);
				}
			});
		}
	});
});
